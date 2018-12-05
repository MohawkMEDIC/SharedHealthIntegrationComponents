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
 * Date: Sept 4, 2013
 *
 */
package org.marc.shic.atna;

import org.apache.log4j.Logger;
import org.marc.shic.atna.exceptions.AtnaMessageParseException;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.exceptions.NtpCommunicationException;

/**
 * A wrapper class for AuditMetaData used to queue audit messages.
 *
 * @author tylerg
 */
public class AuditMessageWrapper implements Runnable
{

    private static final Logger LOGGER = Logger.getLogger(AtnaCommunicator.class.getName());
    /**
     * The audit message to send.
     */
    private AuditMetaData auditMetaData;
    /**
     * The actor to send the audit message to.
     */
    private IheConfiguration configuration;

    /**
     * Default empty constructor.
     */
    public AuditMessageWrapper()
    {
    }

    /**
     * Construct the AuditMessageWrapper with the audit meta data and affinity
     * domain.
     *
     * @param auditMetaData               AuditMetaData to build the message
     *                                    from.
     * @param affinityDomainConfiguration Configuration that contains the ATNA
     *                                    actor.
     */
    public AuditMessageWrapper(AuditMetaData auditMetaData, IheConfiguration configuration)
    {
        this.auditMetaData = auditMetaData;
        this.configuration = configuration;
    }

    /**
     * Get the IheAffinityDomainConfiguration set in this message.
     *
     * @return IheAffinityDomainConfiguration that is set.
     */
    public IheConfiguration getIheConfiguration()
    {
        return configuration;
    }

    /**
     * Set the configuration that contains an ATNA actor.
     *
     * @param affinityDomainConfiguration IheAffinityDomainConfiguration to set.
     */
    public void setIheConfiguration(IheConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Get the audit meta data.
     *
     * @return AuditMetaData that is set.
     */
    public AuditMetaData getAuditMetaData()
    {
        return auditMetaData;
    }

    /**
     * Set the audit meta data that the message will be built from.
     *
     * @param auditMetaData AuditMetaData to set.
     */
    public void setAuditMetaData(AuditMetaData auditMetaData)
    {
        this.auditMetaData = auditMetaData;
    }

    /**
     * Send the message to the affinity domain ATNA endpoint.
     */
    @Override
    public void run()
    {
        try
        {
            AtnaCommunicator atna = new AtnaCommunicator(configuration);
            atna.sendAuditMessage(auditMetaData);
        } catch (ActorNotFoundException ex)
        {
            LOGGER.error(ex);
        } catch (NtpCommunicationException ex)
        {
            LOGGER.error("Ntp communication error, requeueing atna message.", ex);

            // Unable to reach the endpoint, requeue the message.
            AtnaCommunicator.enqueueMessage(this);
        } catch (AtnaCommunicationException ex)
        {
            LOGGER.error("Atna communication error, requeueing atna message.", ex);

            // Unable to reach the endpoint, requeue the message.
            AtnaCommunicator.enqueueMessage(this);
        } catch (AtnaMessageParseException ex)
        {
            LOGGER.error(ex);
        }
    }
}
