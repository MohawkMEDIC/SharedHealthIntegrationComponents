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
 *
 * Date: Aug 21, 2013
 *
 */
package org.marc.shic.atna;

import org.marc.shic.atna.exceptions.AtnaMessageParseException;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.IheCommunicator;
import org.marc.shic.core.RequiredActor;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheSocketConnection;
import org.marc.shic.core.exceptions.NtpCommunicationException;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.marc.shic.atna.bindings.AuditMessage;
import org.marc.shic.core.configuration.IheConfiguration;

/**
 * Represents a class that can communicate audit meta data to a centralized
 * audit repository using the ATNA specification
 *
 * @author Craig Clark
 */
@RequiredActor(RequiredActors =
{
    IheActorType.AUDIT_REPOSITORY, IheActorType.TS
})
public class AtnaCommunicator extends IheCommunicator
{

    private IheSocketConnection connection;
    private static final Logger LOGGER = Logger.getLogger(AtnaCommunicator.class.getName());
    private static MessageQueue<AuditMessageWrapper> auditMessageQueue;
    /**
     * If in debug mode, all messages will only be logged to the console.
     */
    public static boolean debug = false;
    private IheActorConfiguration atnaActor;

    static
    {
        auditMessageQueue = new MessageQueue<AuditMessageWrapper>(1);
    }

    public static MessageQueue<AuditMessageWrapper> getAuditMessageQueue()
    {
        return auditMessageQueue;
    }

    /**
     * Add an audit message to the messaging queue.
     *
     * @param auditMessageWrapper The audit message to send.
     */
    public static void enqueueMessage(AuditMessageWrapper auditMessageWrapper)
    {
//        LOGGER.info(("Audit message queued."));
        if (auditMessageWrapper == null)
        {
            throw new IllegalArgumentException("auditMessageWrapper must be passed to this function");
        }

        if (auditMessageWrapper.getAuditMetaData() == null)
        {
            throw new IllegalArgumentException("auditMetaData cannot be null.");
        }

        if (auditMessageWrapper.getIheConfiguration() == null)
        {
            throw new IllegalArgumentException("configuration cannot be null.");
        }

        if (auditMessageWrapper.getIheConfiguration().getAffinityDomain() == null)
        {
            throw new IllegalArgumentException("configuration must contain an IheAffinityDomainConfiguration object.");
        }

        if (!debug)
        {
            auditMessageQueue.enqueueMessage(auditMessageWrapper);
        } else
        {
            LOGGER.info(getAuditMessage(auditMessageWrapper.getAuditMetaData(), auditMessageWrapper.getIheConfiguration()));
        }
    }

    /**
     * Add an audit message to the messaging queue.
     *
     * @param auditMetaData               The meta data of the audit message.
     * @param affinityDomainConfiguration The affinity domain of the audit
     *                                    message.
     */
    public static void enqueueMessage(AuditMetaData auditMetaData, IheConfiguration configuration)
    {
        enqueueMessage(new AuditMessageWrapper(auditMetaData, configuration));
    }

    public static String getAuditMessage(AuditMetaData auditMetaData, IheConfiguration configuration)
    {
        StringBuffer buffer = null;

        try
        {
            AuditMessage audit = AuditUtility.generateAuditMessage(auditMetaData, configuration);
            JAXBContext jaxbContext = JAXBContext.newInstance(audit.getClass().getPackage().getName());

            // marshal the JAXB audit message & get the raw message
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(audit, writer);
            buffer = writer.getBuffer();
        } catch (NtpCommunicationException ex)
        {
            LOGGER.error(ex);
        } catch (JAXBException ex)
        {
            LOGGER.error(ex);
        }

        if (buffer != null)
        {
            return buffer.toString();
        } else
        {
            return null;
        }
    }

    /**
     *
     * @param config
     * @throws ActorNotFoundException
     */
    public AtnaCommunicator(IheConfiguration config) throws ActorNotFoundException
    {
        super(config);

        this.atnaActor = this.getConfiguration().getAffinityDomain().getActor(IheActorType.AUDIT_REPOSITORY);
        this.connection = new IheSocketConnection(this.atnaActor.getEndPointAddress(), config.getKeyStore(), config.getTrustStore(), atnaActor.isSecure());
    }

    /**
     * A method that generates the ATNA message payload, and sends it using the
     * connection property
     *
     * @param auditMetaData The Audit meta data (source, destination actors,
     *                      author, patient, etc...)
     * @return boolean indicating the status of the sent Audit message
     * @deprecated Use enqueueMessage instead.
     */
    @Deprecated
    public boolean sendAudit(AuditMetaData auditMetaData) throws CommunicationsException
    {
        enqueueMessage(auditMetaData, this.getConfiguration());
        // Return type is deprecated.
        return true;
    }

    /**
     * Sends an audit message generated from audit meta data to the affinity
     * domain.
     *
     * @param auditMetaData The meta data to generate the message from.
     * @throws CommunicationsException
     */
    void sendAuditMessage(AuditMetaData auditMetaData) throws AtnaCommunicationException, NtpCommunicationException, AtnaMessageParseException
    {
        try
        {
            String atnaMessage = AuditUtility.generateAuditPayload(auditMetaData, this.getConfiguration());
            LOGGER.info("ATNA Message: " + atnaMessage);


            // is the socket connection was closed, re-open it
            if (connection.isClosed())
            {
                open();
            }

            // send audit message

            sendATNAMessage(atnaMessage);
            connection.close();
            

        } catch (AtnaCommunicationException e)
        {
            throw e;
        } catch (NtpCommunicationException e)
        {
            throw e;
        } catch (AtnaMessageParseException e)
        {
            throw e;
        }
    }

    /**
     * A helper method that sends a message through an opened socket stream
     *
     * @param message The message to send
     */
    private void sendATNAMessage(String message) throws AtnaCommunicationException
    {
        if (message == null || message.length() == 0)
        {
            throw new IllegalArgumentException("message cannot be empty.");
        }

        try
        {
            connection.write(message);
        } catch (IOException ex)
        {
            // TODO: Use appropiate communication response enum.
            throw new AtnaCommunicationException(atnaActor, AtnaCommunicationException.AtnaCommunicationResponse.Ok, ex);
        }
    }

    /**
     * Opens the socket defined in the IheAffinityDomainConfiguration field
     * Splits EndPointAddress into port and host address
     */
    private void open() throws AtnaCommunicationException
    {
        try
        {
            // connect (create the socket)
            connection.connect();

        } catch (IOException ex)
        {
            // TODO: Use appropiate communication response enum.
            throw new AtnaCommunicationException(atnaActor, AtnaCommunicationException.AtnaCommunicationResponse.Ok, ex);
        }
    }

    public void close()
    {
        if (connection != null)
        {
            connection.close();
        }
    }
}