/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Garrett
 * Date: Nov 29, 2013
 *
 */
package org.marc.shic.xds;

import java.util.List;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.atna.AuditActor;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.atna.ParticipantRoleType;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.DocumentContainerMetaData;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.InvalidPermissionException;
import org.marc.xds.profiles.IXdr;
import org.marc.shic.xds.repository.DocumentRepositoryPortType;
import org.marc.shic.xds.repository.ProvideAndRegisterDocumentSetRequestType;
import org.marc.shic.xds.repository.RegistryResponseType;

/**
 *
 * @author Garrett
 */
public class XdrCommunicator extends XdStarCommunicator implements IXdr
{

    private final IheActorConfiguration docRecipient;


    public XdrCommunicator(IheConfiguration configuration) throws ActorNotFoundException
    {
        super(configuration);

        this.docRecipient = this.getConfiguration().getAffinityDomain().getActor(IheActorType.DOC_RECIPIENT);
    }

    @Override
    public boolean provideAndRegisterDocumentSetB(MetaData metaData) throws CommunicationsException
    {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.ReadOnly){
            throw new InvalidPermissionException("Invalid Permission: Write or WriteRead required.");
        }
           // Sanity check
        if (metaData == null)
        {
            throw new IllegalArgumentException("a MetaData object must be passed to this method");
        }

        if (metaData.getPatient() == null)
        {
            throw new IllegalArgumentException("Parameter missing from metaData : Patient");
        } else if (metaData.getPatient().getIdentifiers() == null || metaData.getPatient().getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Parameter missing from metaData : Patient.Id");
        }

        // check documents
        if (metaData instanceof DocumentContainerMetaData && ((DocumentContainerMetaData) metaData).getDocuments().isEmpty() && !(metaData instanceof FolderMetaData))
        {
            throw new IllegalArgumentException("No documents were found in the metaData.");
        }

        // check folder
        if (metaData instanceof FolderMetaData && ((FolderMetaData) metaData).getTitle() == null)
        {
            throw new IllegalArgumentException("No folder was found in the metaData.");
        }

        // check document content
        if (metaData instanceof DocumentMetaData && ((DocumentMetaData) metaData).getContent() == null)
        {
            throw new IllegalArgumentException("Document contents were missing from metaData.");
        }

        boolean result = true; // innocent until proven guilty..

        DocumentRepositoryPortType port = (DocumentRepositoryPortType) this.getBinding(docRecipient);
        if (port == null)
        {
            throw new UnsupportedOperationException("No configured actors in DOC_RECIPIENT role");
        }

        // HACK: is it a collection?
        if (metaData instanceof DocumentContainerMetaData)
        {
            for (DocumentMetaData document : ((DocumentContainerMetaData) metaData).getDocuments())
            {
                DomainIdentifier docDomainIdentifier = getPatientAffinityDomainIdOrDefault(document.getPatient());

                if (docDomainIdentifier == null)
                {
                    docDomainIdentifier = document.getPatient().getIdentifiers().get(0);
                }

                document.getPatient().getIdentifiers().clear();
                document.getPatient().addIdentifier(docDomainIdentifier);
            }
        }

        // Use XdsUtility class to create the provide and register request object (containing submission set, and document(s))
        ProvideAndRegisterDocumentSetRequestType request;
        try
        {
            request = XdsUtility.generateProvideAndRegisterRequest(metaData);
        } catch (CommunicationsException ex)
        {
            throw ex;
        }
        
        BindingProvider binding = (BindingProvider)this.m_bindings.get(this.docRecipient);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(metaData,this.docRecipient.getEndPointAddress().toASCIIString(),this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);
        
        // send the request
        RegistryResponseType response;
        try
        {
            // the generated classes can throw multiple exceptions
            response = port.documentRepositoryProvideAndRegisterDocumentSetB(request);

            // for a Provide and Register request, we don't need to return a document, all we return is the status..
            String status = response.getStatus();
            LOGGER.info(status);

            // if the status is a failure, return (result should be) false
            if (status.indexOf("Failure") >= 0)
            {
                // the request has failed, set the result boolean to false
                result = false;
                // log the error returned in the response
                LOGGER.info(response.getRegistryErrorList().getRegistryError().get(0).getCodeContext());
            }

        } catch (WebServiceException e)
        {
            throw new CommunicationsException(docRecipient, e);
        } catch (Exception e)
        {
            throw new CommunicationsException(docRecipient, e);
        } finally
        {
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentRegistration);

            AuditActor sourceActor = new AuditActor(AuditActorType.Source, "http://www.w3.org/2005/08/addressing/anonymous", AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(sourceActor);

            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, docRecipient.getEndPointAddress().toString(), null, false,
                    AuditUtility.getIpFromHost(docRecipient.getEndPointAddress().toString()), (short) 2);
            auditData.addActor(destinationActor);

            //     auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));

            // Patient Data
            String srcPatientId = getFormattedPatientId(metaData.getPatient());
            auditData.addParticipant(ParticipantRoleType.Patient, srcPatientId, null);
            auditData.addParticipant(ParticipantRoleType.SubmissionSet, metaData.getUniqueId(), null);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        return result;
    }
}
