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
 * @author tylerg
 * Date: Nov 28, 2013
 *
 */
package org.marc.shic.xds;

import org.apache.commons.codec.binary.Base64;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;

import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.atna.AuditActor;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.atna.AuditParticipant;
import org.marc.shic.atna.ParticipantObjectDetail;
import org.marc.shic.atna.ParticipantRoleType;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentRelationshipType;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.XdsExtendedQueryParameterType;
import org.marc.shic.core.XdsGuidMetaDataType;
import org.marc.shic.core.XdsGuidType;
import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.InvalidPermissionException;
import org.marc.xds.profiles.IXca;

import static org.marc.shic.xds.XdStarCommunicator.REGISTRY_SUCCESS_STATUS;
import org.marc.shic.xds.parameters.XdsAssociationUuid;
import org.marc.shic.xds.parameters.document.XdsDocumentEntryStatus;

import org.marc.shic.xds.parameters.document.XdsDocumentParameter;
import org.marc.shic.xds.parameters.folder.XdsFolderParameter;
import org.marc.shic.xds.parameters.submissionset.XdsSubmissionSetParameter;
import org.marc.shic.xds.registry.AdhocQueryRequest;
import org.marc.shic.xds.registry.AdhocQueryResponse;
import org.marc.shic.xds.registry.AdhocQueryType;
import org.marc.shic.xds.registry.DocumentRegistryPortType;
import org.marc.shic.xds.registry.ResponseOptionType;
import org.marc.shic.xds.repository.DocumentRepositoryPortType;
import org.marc.shic.xds.repository.RetrieveDocumentSetRequestType;
import org.marc.shic.xds.repository.RetrieveDocumentSetResponseType;
import org.marc.shic.xds.utils.RegistryStoredQueryUtil;

/**
 *
 * @author tylerg
 */
public class XcaCommunicator extends XdStarCommunicator implements IXca
{

    /**
     * The repository actor used for the XCA communicator instance.
     */
    private IheActorConfiguration xcaRepositoryActor;
    /**
     * The registry actor used for the XCA communicator instance.
     */
    private IheActorConfiguration xcaRegistryActor;

    public XcaCommunicator(IheConfiguration configuration) throws ActorNotFoundException
    {
        super(configuration);

        // Reference the repository and registry actors rather than searching for them every time they are needed.
        this.xcaRepositoryActor = this.getConfiguration().getAffinityDomain().getActor(IheActorType.RESPONDING_GATEWAY_REPOSITORY);
        this.xcaRegistryActor = this.getConfiguration().getAffinityDomain().getActor(IheActorType.RESPONDING_GATEWAY_REGISTRY);
    }

    @Override
    public <T extends MetaData> T[] registryStoredQuery(XdsGuidMetaDataType<?> queryType, XdsQuerySpecification querySpec) throws CommunicationsException
    {
        T[] parsedMetaData;
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }
        
        // Sanity check
        if (querySpec == null)
        {
            throw new IllegalArgumentException("querySpec must be passed to this function");
        }

        if (queryType == null)
        {
            throw new IllegalArgumentException("queryType must be passed to this function");
        }

        DocumentRegistryPortType port = (DocumentRegistryPortType) this.getBinding(xcaRegistryActor);

        if (port == null)
        {
            throw new UnsupportedOperationException("No configured actors in DOC_REGISTRY/RESPONDING_GATEWAY_REGISTRY role");
        }
        
        BindingProvider binding = (BindingProvider)this.m_bindings.get(this.xcaRegistryActor);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(querySpec,this.xcaRegistryActor.getEndPointAddress().toASCIIString(),this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);
        
        // Proper?
        RegistryStoredQueryUtil.validateQuerySlots(queryType, querySpec);

        // Everything should be good to so let's create the request
        AdhocQueryRequest request = new AdhocQueryRequest();
        // Setup response option
        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(true);

        request.setResponseOption(responseOption);

        // Setup the ad-hoc query
        AdhocQueryType adhocQuery = new AdhocQueryType();

        if (querySpec.getHomeCommunityId() != null)
        {
            adhocQuery.setHome(querySpec.getHomeCommunityId());
        }

        adhocQuery.setId(String.format("urn:uuid:%s", queryType.getGuid()));

        RegistryStoredQueryUtil.populateQueryWithExtendedParameters(adhocQuery, querySpec);

        // Append the ad-hoc query to the request 
        request.setAdhocQuery(adhocQuery);

        try // Communications part
        {

            // Send the request
            AdhocQueryResponse response = port.documentRegistryRegistryStoredQuery(request);

            // First, was there a problem?            
            if (response == null)
            {
                throw new CommunicationsException(xcaRegistryActor, "Registry response was null.");
            } else if (!response.getStatus().equals(REGISTRY_SUCCESS_STATUS))
            {
                throw new CommunicationsException(xcaRegistryActor, String.format("Registry returned : %s", response.getStatus()));
            }

            parsedMetaData = (T[])RegistryStoredQueryUtil.parseMetaDataFromRegistryResponse(response, queryType.getMetaDataClass());

        } catch (XdsMessageParseException e)
        {
            throw new CommunicationsException(xcaRegistryActor, "There was an error parsing the response message.", e);
        } finally
        {
            // Create a stringWriter to hold the XML.
            final StringWriter queryXml = new StringWriter();

            try
            {
                final JAXBContext context = JAXBContext.newInstance(AdhocQueryType.class);
                final Marshaller marshaller = context.createMarshaller();

                marshaller.marshal(adhocQuery, queryXml);

            } catch (JAXBException e)
            {
                LOGGER.error("An error occured marshalling the query.", e);
            }

            final String base64EncodedQuery = Base64.encodeBase64String(queryXml.toString().getBytes());

            // Audit
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentQuery);

            AuditActor sourceActor = new AuditActor(AuditActorType.Source, "http://www.w3.org/2005/08/addressing/anonymous", AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(sourceActor);

            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, xcaRepositoryActor.getEndPointAddress().toString(),
                    null, false, AuditUtility.getIpFromHost(xcaRepositoryActor.getEndPointAddress().toString()), (short) 2);
            auditData.addActor(destinationActor);

            auditData.addParticipant(ParticipantRoleType.ITIName, queryType.toString(), base64EncodedQuery.getBytes(), null);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        return parsedMetaData;
    }

    @Override
    public DocumentMetaData retrieveDocumentSet(DocumentMetaData documentMetaData) throws CommunicationsException
    {
        DocumentMetaData[] doc = new DocumentMetaData[]
        {
            documentMetaData
        };
        return retrieveDocumentSet(doc)[0];
    }

    @Override
    public DocumentMetaData[] retrieveDocumentSet(DocumentMetaData[] documentMetaData) throws CommunicationsException
    {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }

        if (documentMetaData == null)
        {
            throw new IllegalArgumentException("documentMetaData must be passed to this function");
        }

        DocumentRepositoryPortType port = (DocumentRepositoryPortType) this.getBinding(xcaRepositoryActor);
        if (port == null)
        {
            throw new UnsupportedOperationException("No configured actors in DOC_REPOSITORY role");
        }

        DocumentMetaData[] retrievedDocuments;
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentRetrieve);

        // Loop through each document request
        for (DocumentMetaData metaData : documentMetaData)
        {
            RetrieveDocumentSetRequestType.DocumentRequest documentRequest = new RetrieveDocumentSetRequestType.DocumentRequest();

            if (metaData.getUniqueId() == null || metaData.getUniqueId().length() == 0)
            {
                throw new IllegalArgumentException("A documentMetaData is missing its unique identifier.");
            }

            if (metaData.getRepositoryUniqueId() == null || metaData.getRepositoryUniqueId().length() == 0)
            {
                throw new IllegalArgumentException("A documentMetaData is missing its repository unique id.");
            }

            // Fill in the retrieve document set request properties
            documentRequest.setDocumentUniqueId(metaData.getUniqueId());

            String repositoryUniqueId = metaData.getRepositoryUniqueId();

            documentRequest.setRepositoryUniqueId(repositoryUniqueId);

            documentRequest.setHomeCommunityId(metaData.getHomeCommunityId());

            request.getDocumentRequest().add(documentRequest);

            // Create an AuditParticipant for each document request.
            AuditParticipant participant = new AuditParticipant(metaData.getUniqueId(), null, ParticipantRoleType.Document);

            String base64RepositoryId = Base64.encodeBase64String(metaData.getRepositoryUniqueId().getBytes());
            ParticipantObjectDetail repositoryId = new ParticipantObjectDetail("Repository Unique Id", base64RepositoryId.getBytes());
            participant.addDetail(repositoryId);

            if (metaData.getHomeCommunityId() != null)
            {
                String base64HomeCommunityId = Base64.encodeBase64String(metaData.getHomeCommunityId().getBytes());
                ParticipantObjectDetail homeCommunityId = new ParticipantObjectDetail("ihe:homeCommunityID", base64HomeCommunityId.getBytes());
                participant.addDetail(homeCommunityId);
            }

            auditData.addParticipant(participant);
        }
        
        BindingProvider binding = (BindingProvider)this.m_bindings.get(this.xcaRegistryActor);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(documentMetaData[0],this.xcaRegistryActor.getEndPointAddress().toASCIIString(),this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);

        RetrieveDocumentSetResponseType response;

        try
        {
            response = port.documentRepositoryRetrieveDocumentSet(request);

            if (response == null)
            {
                throw new CommunicationsException(xcaRepositoryActor, "There was an error retreiving the XDS registry response.");
            }

            String responseStatus = response.getRegistryResponse().getStatus();

            if (!responseStatus.equals(REGISTRY_SUCCESS_STATUS))
            {
                throw new CommunicationsException(xcaRepositoryActor, String.format("Registry returned : %s", responseStatus));
            }
        } catch (WebServiceException e)
        {
            throw new CommunicationsException(xcaRegistryActor, "There was an error communicating with the registry.", e);
        } finally
        {
            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, "http://www.w3.org/2005/08/addressing/anonymous", AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(destinationActor);

            AuditActor sourceActor = new AuditActor(AuditActorType.Destination, xcaRepositoryActor.getEndPointAddress().toString(),
                    getConfiguration().getLocalIdentification().getFacilityName(), false, AuditUtility.getIpFromHost(xcaRepositoryActor.getEndPointAddress().toString()), (short) 2);
            auditData.addActor(sourceActor);

            //auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));
            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        retrievedDocuments = new DocumentMetaData[response.getDocumentResponse().size()];

        for (int i = 0; i < response.getDocumentResponse().size(); i++)
        {
            RetrieveDocumentSetResponseType.DocumentResponse doc = response.getDocumentResponse().get(i);

            DocumentMetaData document = new DocumentMetaData();
            document.setContent(doc.getDocument());
            document.setMimeType(doc.getMimeType());
            document.setUniqueId(doc.getDocumentUniqueId());
            document.setRepositoryUniqueId(doc.getRepositoryUniqueId());
            document.setHomeCommunityId(doc.getHomeCommunityId());

            retrievedDocuments[i] = document;
        }

        return retrievedDocuments;
    }

    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException
    {
        return getAssociations(objectIdentifiers, null);
    }

    /**
     *
     * @param objectIdentifiers The identifier of the associations to retrieve.
     *                          It must be prefixed with "urn:uuid:".
     * @param patient
     * @return
     * @throws CommunicationsException
     */
    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException
    {
        return getAssociations(objectIdentifiers,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId,CodeValue purposeOfUse) throws CommunicationsException
    {
        if (objectIdentifiers == null)
        {
            throw new IllegalArgumentException("objectIdentifiers must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(objectIdentifiers.getParameterName(), objectIdentifiers.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetAssociations, querySpec);
    }

    /**
     * <p>
     * FindFolders stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Find folders (XDSFolder objects) in the registry for a given patientID
     * with matching ‘status’ attribute. The other parameters can be used to
     * restrict the collection of XDSFolder objects returned.</p>
     *
     * @return XDSFolder objects matching the query parameters.
     */
    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        return findFolders(patient,homeCommunityId,null,statusTypes);
    }
    
    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSFolderPatientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSFolderStatus", RegistryStoredQueryUtil.parseStatusTypes(statusTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        
        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindFolders, querySpec);

        return folders;
    }

    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient) throws CommunicationsException
    {
        return findFolders(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, DocumentRelationshipType... associationTypes) throws CommunicationsException
    {
        return getRelatedDocuments(documentId, null, associationTypes);
    }

    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, DocumentRelationshipType... associationTypes) throws CommunicationsException
    {
        return getRelatedDocuments(documentId,homeCommunityId,null,associationTypes);
    }
    
    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse, DocumentRelationshipType... associationTypes) throws CommunicationsException
    {
        if (documentId == null || documentId.getParameterValue() == null || documentId.getParameterValue().length() == 0)
        {
            throw new IllegalArgumentException("documentId must be set.");
        }

        if (associationTypes == null || associationTypes.length == 0)
        {
            throw new IllegalArgumentException("At least one association type must be passed.");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(documentId.getParameterName(), documentId.getParameterValue()));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$AssociationTypes", RegistryStoredQueryUtil.parseStatusTypes(associationTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        MetaData[] metaData = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetRelatedDocuments, querySpec);

        return metaData;
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId, final CodeValue purposeOfUse, final XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSSubmissionSetPatientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSSubmissionSetStatus", RegistryStoredQueryUtil.parseStatusTypes(statusTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        
        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindSubmissionSets, querySpec);

        return submissionSets;
    }
    
    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId, final XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        return findSubmissionSets(patient,homeCommunityId,null,statusTypes);
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient) throws CommunicationsException
    {
        return findSubmissionSets(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId) throws CommunicationsException
    {
        return findSubmissionSets(patient, homeCommunityId, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId) throws CommunicationsException
    {
        return getSubmissionSetAndContents(submissionSetId, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId) throws CommunicationsException
    {
        return getSubmissionSetAndContents(submissionSetId,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (submissionSetId == null || submissionSetId.getParameterValue() == null)
        {
            throw new IllegalArgumentException("submissionSetId must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>(submissionSetId.getParameterName(), submissionSetId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetSubmissionSetAndContents, querySpec);

        return submissionSets;
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException
    {
        return getSubmissionSets(objectIdentifiers, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException
    {
        return getSubmissionSets(objectIdentifiers,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (objectIdentifiers == null)
        {
            throw new IllegalArgumentException("objectIdentifiers must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(objectIdentifiers.getParameterName(), objectIdentifiers.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetSubmissionSets, querySpec);
    }

    /**
     * <p>
     * GetAll stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Get all registry content for a patient given the indicated status, format
     * codes, and confidentiality codes.</p>
     *
     * @return
     *
     * <ul><li>XDSSubmissionSet, XDSDocumentEntry, and XDSFolder objects with
     * patientId attribute matching $patientId parameter</li>
     *
     * <li>Association objects with sourceObject or targetObject attribute
     * matching one of the above objects.</li><ul>
     */
    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        return getAll(patient,homeCommunityId,null,statusTypes);
    }
    
    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();

        String[] parsedStatusTypes = RegistryStoredQueryUtil.parseStatusTypes(statusTypes);

        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$patientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSDocumentEntryStatus", parsedStatusTypes));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSFolderStatus", parsedStatusTypes));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSSubmissionSetStatus", parsedStatusTypes));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetAll, querySpec);

        return submissionSets;
    }

    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient) throws CommunicationsException
    {
        return getAll(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    /**
     * <p>
     * GetDocuments stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSDocumentEntry objects. XDSDocumentEntry
     * objects are selected either by their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSDocumentEntry objects requested.
     */
    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException
    {
        return getDocuments(documentIds, null);
    }

    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException
    {
        return getDocuments(documentIds,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (documentIds == null || documentIds.getParameterValue().length == 0)
        {
            throw new IllegalArgumentException("documentIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(documentIds.getParameterName(), documentIds.getParameterValue()));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetDocuments, querySpec);
    }

    /**
     * <p>
     * GetFolders stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSFolder objects. XDSFolder objects are
     * selected either by their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSFolder objects requested.
     */
    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds) throws CommunicationsException
    {
        return getFolders(folderIds, null);
    }

    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(folderIds.getParameterName(), folderIds.getParameterValue()));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        
        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFolders, querySpec);

        return folders;
    }
    
    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId) throws CommunicationsException
    {
        return getFolders(folderIds,homeCommunityId,null);
    }

    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException
    {
        return getDocumentsAndAssociations(documentIds, null);
    }

    /**
     * <p>
     * GetDocumentsAndAssociations stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSDocumentEntry objects and the Association
     * objects surrounding them. XDSDocumentEntry objects are selected either by
     * their entryUUID or uniqueId attribute. This is the GetDocuments query and
     * GetAssociations query combined into a single query.</p>
     *
     * @return XDSDocumentEntry objects & Association objects whose sourceObject
     *         or targetObject attribute matches one of the above objects.
     */
    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException
    {
        return getDocumentsAndAssociations(documentIds,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (documentIds == null || documentIds.getParameterValue().length == 0)
        {
            throw new IllegalArgumentException("documentIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(documentIds.getParameterName(), documentIds.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        
        MetaData[] documentsAndAssociations = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetDocumentsAndAssociations, querySpec);

        return documentsAndAssociations;
    }

    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId) throws CommunicationsException
    {
        return getFolderAndContents(folderId, null);
    }

    /**
     * <p>
     * GetFoldersAndContents stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve an XDSFolder object and its contents. XDSFolder objects are
     * selected either by their entryUUID or uniqueId attribute. The
     * XDSDocumentEntry objects returned shall match one of the confidentiality
     * codes listed if that parameter is included.</p>
     *
     * @return <ul><li>XDSFolder object specified in the query</li>
     *
     * <li>Association objects of type HasMember that have a sourceObject
     * attribute referencing the XDSFolder object specified in the query</li>
     *
     * <li>XDSDocumentEntry objects referenced by the targetObject attribute of
     * one of the Association objects specified above.</li></ul>
     */
    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId) throws CommunicationsException
    {
        return getFolderAndContents(folderId,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (folderId == null || folderId.getParameterValue() == null || folderId.getParameterValue().length() == 0)
        {
            throw new IllegalArgumentException("folderIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(folderId.getParameterName(), folderId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        
        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFolderAndContents, querySpec);

        return folders;
    }

    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId) throws CommunicationsException
    {
        return getFoldersForDocument(documentId, null);
    }

    /**
     * <p>
     * GetFoldersForDocument stored query.</p>
     *
     * <p>
     * Helper extension for find (registry stored query)</p>
     *
     * <p>
     * Retrieve XDSFolder objects that contain the XDSDocumentEntry object
     * provided with the query. XDSDocumentEntry objects are selected either by
     * their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSFolder objects that contain specified XDSDocumentEntry object.
     *         More specifically, for each Association object of type HasMember that has
     *         a targetObject attribute referencing the target XDSDocumentEntry object,
     *         return the object referenced by its sourceObject if it is of type
     *         XDSFolder.
     */
    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId) throws CommunicationsException
    {
        return getFoldersForDocument(documentId,homeCommunityId,null);
    }
    
    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException
    {
        if (documentId == null || documentId.getParameterValue() == null || documentId.getParameterValue().length() == 0)
        {
            throw new IllegalArgumentException("documentId must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(documentId.getParameterName(), documentId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        
        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFoldersForDocument, querySpec);

        return folders;
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient) throws CommunicationsException
    {
        return findDocuments(patient, null);
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId) throws CommunicationsException
    {
        XdsDocumentParameter param = new XdsDocumentEntryStatus(XdsDocumentEntryStatusType.Approved);
        return findDocuments(patient, homeCommunityId, param);
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, XdsDocumentParameter... parameters) throws CommunicationsException{
        return findDocuments(patient,homeCommunityId,null,parameters);
    }
    
    
    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentParameter... parameters) throws CommunicationsException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSDocumentEntryPatientId", getFormattedPatientId(patient)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        
        for (XdsDocumentParameter param : parameters)
        {
            querySpec.addExtendedParameter(new XdsExtendedQueryParameterType(param.getParameterName(), param.getParameterValue()));
        }
        
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindDocuments, querySpec);
    }
}
