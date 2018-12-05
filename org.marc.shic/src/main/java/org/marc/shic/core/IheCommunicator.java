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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core;

import org.apache.log4j.Logger;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheConfiguration;

/**
 *
 * @author tylerg
 */
public abstract class IheCommunicator
{

    /**
     * Logger.
     */
    protected static final Logger LOGGER = Logger.getLogger(IheCommunicator.class.getName());
    protected IheConfiguration m_configuration;

    public IheConfiguration getConfiguration()
    {
        return m_configuration;
    }

    public IheCommunicator(IheConfiguration configuration)
    {
        this.m_configuration = configuration;

        Class<? extends IheCommunicator> communicatorClass = this.getClass();
        RequiredActor required = communicatorClass.getAnnotation(RequiredActor.class);

        if (required != null)
        {
            for (IheActorType actorType : required.RequiredActors())
            {
                if (!this.m_configuration.getAffinityDomain().containsActor(actorType))
                {
                    throw new ActorNotFoundException(
                            actorType,
                            String.format("Required actor %s was not found in the affinity domain configuration", actorType.toString()));
                }
            }
        }
    }

    /**
     * Get the affinity domain identifier for the given person demographic
     * record.
     *
     * @throws CommunicationException
     */
    protected DomainIdentifier getPatientAffinityDomainIdOrDefault(PersonDemographic demographic)
    {
        // TODO: Check if affinity domain patient identifier will be stored in this 

        DomainIdentifier affinityDomainId = demographic.getDomainIdentifier(this.getConfiguration().getAffinityDomain().getOid());
        if (affinityDomainId == null)
        {
            // TODO: Log useful information about the patient.
            LOGGER.info("No affinity domain identifier found for patient. Using first identifier.");
            affinityDomainId = demographic.getIdentifiers().get(0);
        }

//        if (affinityDomainId == null) // need to resolve the patient Identifier
//        {
//            //PixCommunicator pixCommunicator = new PixCommunicator(affinityDomain);
//            // PersonDemographic resolvedDemographic = pixCommunicator.resolveIdentifiers(demographic);
//            // Attempt to get the affinity domain again
//            affinityDomainId = demographic.getDomainIdentifer(this.affinityDomain.getIdentification().getUniqueId());
//            if (affinityDomain == null) {
//                throw new UnsupportedOperationException("Could not identify this person in the specified affinity domain");
//            }
//        }
        return affinityDomainId;
    }

    protected String getFormattedPatientId(PersonDemographic patient)
    {
        DomainIdentifier domainIdentifier = getPatientAffinityDomainIdOrDefault(patient);

        return String.format("%s^^^&%s&ISO", domainIdentifier.getExtension(), domainIdentifier.getRoot());
    }
}
