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
 * Date: Sept 9 2013
 *
 */
package org.marc.shic.svs;

import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.ParticipantRoleType;
import ihe.iti.svs._2008.*;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOMFeature;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.*;
import org.marc.shic.core.configuration.*;
import org.marc.shic.core.exceptions.*;
import org.marc.shic.svs.exceptions.*;

/**
 *
 * @author Garrett
 */
@RequiredActor(RequiredActors =
{
    IheActorType.SVS, IheActorType.TS, IheActorType.AUDIT_REPOSITORY
})
public class SvsCommunicator extends IheHttpCommunicator
{

    private IheActorConfiguration svsActor;

    public SvsCommunicator(IheConfiguration configuration) throws ActorNotFoundException
    {
        super(configuration);

        svsActor = configuration.getAffinityDomain().getActor(IheActorType.SVS);
    }

    public ValueSet retrieveValueSet(final ValueSet valueSet) throws SvsCommunicatonException
    {
        return retrieveValueSet(valueSet, "");
    }

    /**
     * ITI-48 RetrieveValueSet.
     *
     * @param valueSet Value set to retrieve.
     * @return Requested value set.
     */
    public ValueSet retrieveValueSet(final ValueSet valueSet, final String language) throws SvsCommunicatonException
    {

        // Sanity check.
        if (valueSet == null)
        {
            throw new IllegalArgumentException("valueSet must be passed to this function");
        }

        if (valueSet.getId() == null || valueSet.getId().length() == 0)
        {
            throw new IllegalArgumentException("Missing required parameter: valueSet.id");
        }

        ValueSetRepositoryPortType port = (ValueSetRepositoryPortType) this.getBinding(svsActor);

        if (port == null)
        {
            throw new UnsupportedOperationException("No configured actors in SVS role");
        }

        // Create the request properties.
        final ValueSetRequestType valueSetRequest = new ValueSetRequestType()
        {
            
            {
                setLang(language);
                setVersion(valueSet.getVersion());
                setId(valueSet.getId());
            }
        };

        // Create the request.
        RetrieveValueSetRequestType request = new RetrieveValueSetRequestType()
        {
            
            {
                setValueSet(valueSetRequest);
            }
        };

        RetrieveValueSetResponseType response = null;

        try
        {
            response = port.valueSetRepositoryRetrieveValueSet(request);
        } catch (ExceptionInInitializerError e)
        {
            // Will need to test this with a different SVS repository.
            throw new SvsCommunicatonException(svsActor, "There was an error communicating with the SVS Actor or an invalid value set was requested.", e);
        }

        // Check if the response is valid.
        if (response == null || response.getValueSet() == null || response.getValueSet().getConceptList().isEmpty())
        {
            throw new SvsCommunicatonException(svsActor, "SVS returned an invalid response.");
        }

        ValueSetResponseType responseValueSet = response.getValueSet();

        // Populate the value set with the response.
        valueSet.setDisplayName(responseValueSet.getDisplayName());
        valueSet.setVersion(responseValueSet.getVersion());
        valueSet.getConceptLists().addAll(SvsUtility.parseConceptListsFromValueSetResponseType(responseValueSet));

        AuditMetaData auditData = new AuditMetaData(AuditEventType.RetrieveValueSet)
        {
            
            {
                // Destination - AuditMessage/ActiveParticipant
                addActor(AuditActorType.Destination, svsActor);

                // TODO: Add source
                //addActor(AuditActorType.Source, svsActor);

                // Human Requestor - AuditMessage/ActiveParticipant
                addActor(AuditUtility.generateRequestingUser());
                // Value Set Instance - AuditMessage/ParticipantObjectIdentification
                addParticipant(ParticipantRoleType.ValueSet, valueSet.getId(), null);
            }
        };

        AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());

        return valueSet;
    }

//    /**
//     * Parse all the concepts from a value set response. Each translation of a
//     * concept list is represented as its own list.
//     *
//     * @param valueSetResponse
//     * @return
//     */
//    private ArrayList<ArrayList<Concept>> parseConceptsFromValueSetResponseType(ValueSetResponseType valueSetResponse) {
//        ArrayList<ArrayList<Concept>> translatedConcepts = new ArrayList<>(valueSetResponse.getConceptList().size());
//
//        // Loop through all the translated concepts.
//        for (ConceptListType conceptList : valueSetResponse.getConceptList()) {
//            ArrayList<Concept> currentConceptList = new ArrayList<>(conceptList.getConcept().size());
//
//            // Loop through all the concepts in the current list.
//            for (CD cd : conceptList.getConcept()) {
//                Concept concept = new Concept(cd.code, cd.displayName, cd.codeSystem, cd.codeSystemVersion);
//                currentConceptList.add(concept);
//            }
//
//            translatedConcepts.add(currentConceptList);
//        }
//
//        return translatedConcepts;
//    }
    /**
     * Opens a connection to the actor specified if one is not already open. If
     * a connection is already open then does nothing.
     */
    @Override
    protected void open(IheActorConfiguration system)
    {

        // First, fetch the actor type from the affinity domain configuration
        if (this.m_bindings.containsKey(system)) // Already created an actor in this configuration
        {
            return;
        }

        // Create the service for this system
        Service service = null;
        BindingProvider binding = null;

        // Create the SVS service.
        if (system.getActorType().equals(IheActorType.SVS))
        {
            service = new ValueSetRepositoryService();
            binding = (BindingProvider) service.getPort(new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Port_Soap12"), ValueSetRepositoryPortType.class, new AddressingFeature(true, true), new MTOMFeature(true, 1));
        } else
        {
            throw new IllegalArgumentException("Unsupported actor");
        }

        binding.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, system.getEndPointAddress().toString());

        // Add to the binding collection
        this.m_bindings.put(system, binding);
    }
}
