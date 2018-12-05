/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
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
package org.marc.shic.atna.messages;

import org.marc.shic.atna.exceptions.AtnaMessageParseException;
import org.marc.shic.atna.bindings.AuditMessage;
import org.marc.shic.atna.bindings.AuditMessage.ActiveParticipant;
import org.marc.shic.atna.bindings.AuditSourceIdentificationType;
import org.marc.shic.atna.bindings.CodedValueType;
import org.marc.shic.atna.bindings.EventIdentificationType;
import org.marc.shic.atna.bindings.ParticipantObjectIdentificationType;
import org.marc.shic.atna.AuditActor;
import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.atna.AuditParticipant;
import org.marc.shic.core.NTPHelper;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.exceptions.NtpCommunicationException;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.ParticipantObjectDetail;
import org.marc.shic.atna.bindings.TypeValuePairType;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheConfiguration;

/**
 * A utility class for generating an ATNA (Audit Trail Node Authentication)
 * message
 *
 * @author ibrahimm
 */
public class AuditUtility
{

    private static final Logger LOGGER = Logger.getLogger(AuditUtility.class.getName());

    private AuditUtility()
    {
    }

    public AuditMetaData createActorStartAudit()
    {
        AuditMetaData actorStart = new AuditMetaData(AuditEventType.ApplicationActivityStart);

        // Active Participant: Application Started (1)
        AuditActor appStarted = new AuditActor(AuditActorType.Application, getProcessId());
        actorStart.addActor(appStarted);

        // Active Participant: Persons and or processes that started the Application (0..N)
        AuditActor startingPerson = new AuditActor(AuditActorType.Application, getSystemUser());
        actorStart.addActor(startingPerson);

        return actorStart;
    }

    public AuditMetaData createActorStopAudit()
    {
        AuditMetaData actorStop = new AuditMetaData(AuditEventType.ApplicationActivityStop);

        // Active Participant: Application Stopped (1)
        AuditActor appStopped = new AuditActor(AuditActorType.Application, getProcessId());
        actorStop.addActor(appStopped);

        // Active Participant: Persons and or processes that stopped the Application (0..N)
        AuditActor stoppingPerson = new AuditActor(AuditActorType.Application, getSystemUser());
        actorStop.addActor(stoppingPerson);

        return actorStop;
    }

    public AuditMetaData createUserAuthenticationLoginAudit(String userId, String networkAccessPointID, short networkAccessPointTypeCode)
    {
        AuditMetaData loginAudit = new AuditMetaData(AuditEventType.UserAuthenticationLogin);

        // Active Participant: Person Authenticated or claimed(1)

        AuditActor personAuthenticated = new AuditActor(AuditActorType.UserAuthentication, userId, null, true, networkAccessPointID, networkAccessPointTypeCode);
        loginAudit.addActor(personAuthenticated);

        // Active Participant: Node or System performing authentication(0..1)

        AuditActor nodePerformingAuthentication = new AuditActor(AuditActorType.UserAuthentication, getProcessId());
        nodePerformingAuthentication.setUserIsRequestor(false);

        loginAudit.addActor(nodePerformingAuthentication);

        return loginAudit;
    }

    public AuditMetaData createUserAuthenticationLogoutAudit(String userId, String networkAccessPointID, short networkAccessPointTypeCode)
    {
        AuditMetaData logoutAudit = new AuditMetaData(AuditEventType.UserAuthenticationLogout);

        // Active Participant: Person Authenticated or claimed(1)

        AuditActor personAuthenticated = new AuditActor(AuditActorType.UserAuthentication, userId, null, true, networkAccessPointID, networkAccessPointTypeCode);
        logoutAudit.addActor(personAuthenticated);

        // Active Participant: Node or System performing authentication(0..1)

        AuditActor nodePerformingAuthentication = new AuditActor(AuditActorType.UserAuthentication, getProcessId());
        nodePerformingAuthentication.setUserIsRequestor(false);

        logoutAudit.addActor(nodePerformingAuthentication);

        return logoutAudit;
    }

    public static String getProcessId()
    {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    public static String getIpFromHost(String host)
    {
        String hostIp = null;
        try
        {
            // Get ip address
            java.net.URL url = new java.net.URL(host);

            InetAddress destinationIp = InetAddress.getByName(url.getHost());
            hostIp = destinationIp.getHostAddress();

        } catch (UnknownHostException ex)
        {
            LOGGER.info(ex);
        } catch (MalformedURLException ex)
        {
            LOGGER.info(ex);
        }

        return hostIp;
    }

    /**
     * Gets the local network id.
     *
     * @return Local network id. Null if an error occurred.
     */
    public static String getNetworkId()
    {
        String networkId = null;
        try
        {
            networkId = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex)
        {
            LOGGER.info(ex);
        }

        return networkId;
    }

    public static String getSystemUser()
    {
        return System.getProperty("user.name");
    }

    public static AuditActor generateRequestingUser()
    {
        AuditActor actor = new AuditActor();
        actor.setActorType(AuditActorType.RequestingUser);
        actor.setIdentifier(getSystemUser());

        return actor;

    }

    /**
     *
     * @param auditMetaData
     * @param affinityDomain
     * @return
     */
    public static String generateAuditPayload(AuditMetaData auditMetaData, IheConfiguration configuration) throws NtpCommunicationException, AtnaMessageParseException
    {
        String auditPayload = null;

        try
        {
            // create the audit message & JAXB Context
            AuditMessage audit = generateAuditMessage(auditMetaData, configuration);
            JAXBContext jaxbContext = JAXBContext.newInstance(audit.getClass().getPackage().getName());

            // marshal the JAXB audit message & get the raw message
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(audit, writer);
            StringBuffer buffer = writer.getBuffer();

            // create syslog message header & message body
            String header = createAuditMessageHeaders(configuration.getAffinityDomain());
            String payload = new String(buffer);

            if (configuration.getAffinityDomain().getActor(IheActorType.AUDIT_REPOSITORY).getEndPointAddress().getScheme().equalsIgnoreCase("udp"))
            {
                auditPayload = String.format("%s%s", header, payload);
//                int frameLength = header.getBytes().length + payload.getBytes().length;
//                auditPayload = String.format("%d %s%s", frameLength, header, payload);
            } else
            {
                int frameLength = header.getBytes().length + payload.getBytes().length;
                auditPayload = String.format("%d %s%s", frameLength, header, payload);

                //     auditPayload = String.format("%s%s", header, payload);
            }

        } catch (NtpCommunicationException ex)
        {
            throw ex;
        } catch (JAXBException ex)
        {
            throw new AtnaMessageParseException("There was an error marshalling the audit message object.", ex);
        }

        return auditPayload;
    }

    /**
     * Generates the audit message from audit meta data.
     *
     * @param auditMetaData  The meta data to generate the message from.
     * @param affinityDomain Pulls the time from the time server on the affinity
     *                       domain.
     * @return
     */
    public static AuditMessage generateAuditMessage(AuditMetaData auditMetaData, IheConfiguration configuration) throws NtpCommunicationException
    {
        AuditMessage auditMessage = new AuditMessage();

        try
        {
            auditMessage.setEventIdentification(createEventIdentification(auditMetaData, configuration.getAffinityDomain()));
        } catch (NtpCommunicationException ex)
        {
            throw ex;
        }

        auditMessage.getAuditSourceIdentification().add(createAuditSourceIdentification(configuration));

        auditMessage.getActiveParticipant().addAll(generateActiveParticipants(auditMetaData));
        auditMessage.getParticipantObjectIdentification().addAll(generateParticipantObjectIdentifications(auditMetaData));

        return auditMessage;
    }

    /**
     *
     * @param auditMetaData
     * @param affinityDomain
     * @return
     */
    private static EventIdentificationType createEventIdentification(AuditMetaData auditMetaData, IheAffinityDomainConfiguration affinityDomain) throws NtpCommunicationException
    {
        EventIdentificationType eventIdentification = new EventIdentificationType();

        try
        {
            eventIdentification.setEventDateTime(createEventDateTime(affinityDomain));
        } catch (NtpCommunicationException ex)
        {
            throw ex;
        }
        eventIdentification.setEventOutcomeIndicator(BigInteger.ZERO);
        eventIdentification.setEventID(createEventID(auditMetaData));
        eventIdentification.getEventTypeCode().add(createEventTypeCode(auditMetaData));
        eventIdentification.setEventActionCode(createEventActionCode(auditMetaData));

        return eventIdentification;
    }

    /**
     *
     * @param auditMetaData
     * @return
     */
    private static Collection<? extends ParticipantObjectIdentificationType> generateParticipantObjectIdentifications(AuditMetaData auditMetaData)
    {
        List<ParticipantObjectIdentificationType> participantIdentifications = new ArrayList<ParticipantObjectIdentificationType>();

        for (AuditParticipant auditParticipant : auditMetaData.getParticipants())
        {
            ParticipantObjectIdentificationType participant = null;
            switch (auditParticipant.getParticipantRoleType())
            {
                case Document:
                    participant = createParticipantObjectIdentification((short) 2, (short) 3, auditParticipant, createCodedValue("9", "RFC-3881", "Report Number"));

                    for (ParticipantObjectDetail detail : auditParticipant.getDetails())
                    {
                        TypeValuePairType detailType = new TypeValuePairType();
                        detailType.setType(detail.getType());
                        detailType.setValue(detail.getValue());
                        participant.getParticipantObjectDetail().add(detailType);
                    }

                    break;
                case ITIName:
                    participant = createParticipantObjectIdentification((short) 2, (short) 24, auditParticipant, createEventTypeCode(auditMetaData));
                    for (ParticipantObjectDetail detail : auditParticipant.getDetails())
                    {
                        TypeValuePairType detailType = new TypeValuePairType();
                        detailType.setType(detail.getType());
                        detailType.setValue(detail.getValue());
                        participant.getParticipantObjectDetail().add(detailType);
                    }
                    break;
                case Patient:
                    participant = createParticipantObjectIdentification((short) 1, (short) 1, auditParticipant, createCodedValue("2", "RFC-3881", "Patient Number"));

                    for (ParticipantObjectDetail detail : auditParticipant.getDetails())
                    {
                        TypeValuePairType detailType = new TypeValuePairType();
                        detailType.setType(detail.getType());
                        detailType.setValue(detail.getValue());
                        participant.getParticipantObjectDetail().add(detailType);
                    }

                    break;
                case SubmissionSet:
                    participant = createParticipantObjectIdentification((short) 2, (short) 20, auditParticipant, createCodedValue("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "IHE XDS Metadata", "submission set classificationNode"));
                    break;
                case ValueSet:
                    participant = createParticipantObjectIdentification((short) 2, (short) 3, auditParticipant, createCodedValue("9", "RFC-3881", "Report Number"));
                    break;
            }
            participantIdentifications.add(participant);
        }

        return participantIdentifications;
    }

    /**
     * Generate a list of active participants based of off audit meta data.
     *
     * @param auditMetaData The meta data to generate the active participants
     *                      from.
     * @return Collection of active participants.
     */
    private static Collection<? extends ActiveParticipant> generateActiveParticipants(AuditMetaData auditMetaData)
    {
        List<ActiveParticipant> activeParticipants = new ArrayList<ActiveParticipant>();

        for (AuditActor actor : auditMetaData.getActors())
        {
            ActiveParticipant participant = null;
            switch (actor.getActorType())
            {
                case Source:
                    participant = createActiveParticipant(actor.getIdentifier(), actor.isUserIsRequestor(), createCodedValue("110153", "DCM", "Source"));
                    participant.setNetworkAccessPointID(actor.getNetworkEndpoint());
                    participant.setNetworkAccessPointTypeCode(actor.getNetworkAccessPointTypeCode());
                    participant.setAlternativeUserID(actor.getAlternativeUserId());

                    break;
                case Destination:
                    participant = createActiveParticipant(actor, createCodedValue("110152", "DCM", "Destination"));
                    participant.setNetworkAccessPointID(actor.getNetworkEndpoint());
                    participant.setNetworkAccessPointTypeCode(actor.getNetworkAccessPointTypeCode());
                    participant.setAlternativeUserID(actor.getAlternativeUserId());

                    break;
                case RequestingUser:
                    participant = createActiveParticipant(actor.getIdentifier(), null, createCodedValue("110153", "DCM", "Requesting User"));
                    break;
                case Application:
                    participant = createActiveParticipant(actor, createCodedValue("110151", "DCM", "Application Launcher"));
                    break;
            }
            activeParticipants.add(participant);
        }

        return activeParticipants;
    }

    /**
     * Generates an Audit Source Identification object indicating the source
     * system creating the audit event/message
     *
     * @param affinityDomain
     * @return
     */
    private static AuditSourceIdentificationType createAuditSourceIdentification(IheConfiguration configuration)
    {
//        IheActorConfiguration actorConfig;
//
//        actorConfig = configuration.getAffinityDomain().getActor(IheActorType.AUDIT_REPOSITORY);

        AuditSourceIdentificationType auditSourceIdentification = new AuditSourceIdentificationType();
        auditSourceIdentification.setAuditSourceID(configuration.getLocalIdentification().getApplicationId());

        return auditSourceIdentification;
    }

    /**
     * Generates an Active Participant object containing a list of roles
     *
     * @param userID          The Participant object's User ID
     * @param userIsRequester Is the user a requester?
     * @param role            The role of the active participant
     * @return the created active participant object
     */
    private static ActiveParticipant createActiveParticipant(String userID, Boolean userIsRequester, CodedValueType... role)
    {
        ActiveParticipant activeParticipant = new ActiveParticipant();
        activeParticipant.setUserID(userID);
        activeParticipant.setUserIsRequestor(userIsRequester);
        activeParticipant.getRoleIDCode().addAll(Arrays.asList(role));

        return activeParticipant;
    }

    private static ActiveParticipant createActiveParticipant(AuditActor auditActor, CodedValueType... role)
    {
        ActiveParticipant activeParticipant = new ActiveParticipant();
        activeParticipant.setAlternativeUserID(auditActor.getAlternativeUserId());
        activeParticipant.setUserID(auditActor.getIdentifier());
        activeParticipant.setNetworkAccessPointID(auditActor.getNetworkEndpoint());
        activeParticipant.setNetworkAccessPointTypeCode(auditActor.getNetworkAccessPointTypeCode());
        activeParticipant.setUserIsRequestor(auditActor.isUserIsRequestor());
        activeParticipant.getRoleIDCode().addAll(Arrays.asList(role));

        return activeParticipant;
    }

    /**
     * Creates a generic CodedValueType
     *
     * @param code
     * @param codeSystemName
     * @param displayName
     * @return
     */
    public static CodedValueType createCodedValue(String code, String codeSystemName, String displayName)
    {
        CodedValueType codedValue = new CodedValueType();
        codedValue.setCode(code);
        codedValue.setCodeSystemName(codeSystemName);
        codedValue.setDisplayName(displayName);

        return codedValue;
    }

    /**
     *
     * @param typeCode
     * @param typeCodeRole
     * @param participant
     * @param objectIDTypeCode
     * @param query
     * @return
     */
    private static ParticipantObjectIdentificationType createParticipantObjectIdentification(short typeCode, short typeCodeRole, AuditParticipant participant, CodedValueType objectIDTypeCode)
    {
        ParticipantObjectIdentificationType participantObjectIdentification = new ParticipantObjectIdentificationType();
        participantObjectIdentification.setParticipantObjectTypeCode(typeCode);
        participantObjectIdentification.setParticipantObjectTypeCodeRole(typeCodeRole);
        participantObjectIdentification.setParticipantObjectIDTypeCode(objectIDTypeCode);
        participantObjectIdentification.setParticipantObjectID(participant.getIdentifier());
        participantObjectIdentification.setParticipantObjectQuery(participant.getQuery());

        return participantObjectIdentification;
    }

    /**
     * Create the EventTypeCode for an AuditMessage/EventIdentification.
     *
     * @param auditMetaData Meta data to create the event type code from.
     * @return
     */
    private static CodedValueType createEventTypeCode(AuditMetaData auditMetaData)
    {
        CodedValueType eventTypeCode = null;
        switch (auditMetaData.getEvent())
        {
            case DemographicIdentityQuery: // Patient Identitiy Source
                eventTypeCode = createCodedValue("ITI-9", "IHE Transactions", "PIX Query");
                break;
            case DemographicRegistration:
                eventTypeCode = createCodedValue("ITI-8", "IHE Transactions", "Patient Identity Feed");
                break;
            case DemographicUpdate:
                eventTypeCode = createCodedValue("ITI-8", "IHE Transactions", "Patient Identity Feed");
                break;
            case DemographicQuery:
                eventTypeCode = createCodedValue("ITI-21", "IHE Transactions", "Patient Demographics Query");
                break;
            case DemographicDelete:
                eventTypeCode = createCodedValue("ITI-8", "IHE Transactions", "Patient Identity Feed");
                break;
            case DocumentRegistration: // Provide and Register Document Set-b
                eventTypeCode = createCodedValue("ITI-41", "IHE Transactions", "Provide and Register Document Set-b");
                break;
            case DocumentQuery: // Registry Stored Query
                eventTypeCode = createCodedValue("ITI-18", "IHE Transactions", "Registry Stored Query");
                break;
            case DocumentRetrieve: // Retrieve Document Set 
                eventTypeCode = createCodedValue("ITI-43", "IHE Transactions", "Retrieve Document Set");
                break;
            case RetrieveValueSet:
                eventTypeCode = createCodedValue("ITI-48", "IHE Transactions", "Retrieve Value Sets");
                break;
            case UpdateDocumentSet:
                eventTypeCode = createCodedValue("ITI-67", "IHE Transactions", "Update Document Set");
                break;
            case ApplicationActivityStart:
                eventTypeCode = createCodedValue("110120", "DCM", "Application Start");
                break;
            case ApplicationActivityStop:
                eventTypeCode = createCodedValue("110121", "DCM", "Application Stop");
                break;
            case UserAuthenticationLogin:
                eventTypeCode = createCodedValue("110122", "DCM", "Login");
                break;
            case UserAuthenticationLogout:
                eventTypeCode = createCodedValue("110123", "DCM", "Logout");
                break;
        }

        return eventTypeCode;
    }

    /**
     *
     * @param auditMetaData
     * @return
     */
    private static CodedValueType createEventID(AuditMetaData auditMetaData)
    {
        CodedValueType eventId = null;
        switch (auditMetaData.getEvent())
        {
            case DemographicIdentityQuery: // Patient Identitiy Source
                eventId = createCodedValue("110112", "DCM", "Query");
                break;
            case DemographicRegistration:
                eventId = createCodedValue("110110", "DCM", "Patient Record");
                break;
            case DemographicUpdate:
                eventId = createCodedValue("110110", "DCM", "Patient Record");
                break;
            case DemographicQuery:
                eventId = createCodedValue("110112", "DCM", "Query");
                break;
            case DemographicDelete:
                eventId = createCodedValue("110110", "DCM", "Patient Record");
                break;
            case DocumentRegistration: // Provide and Register Document Set-b
                eventId = createCodedValue("110106", "DCM", "Export");
                break;
            case DocumentQuery: // Registry Stored Query
                eventId = createCodedValue("110112", "DCM", "Query");
                break;
            case DocumentRetrieve: // Retrieve Document Set 
                eventId = createCodedValue("110107", "DCM", "Import");
                break;
            case RetrieveValueSet:
                eventId = createCodedValue("110107", "DCM", "Import");
                break;
            case UpdateDocumentSet:
                eventId = createCodedValue("110153", "DCM", "Source");
                break;
            case ApplicationActivityStart:
            case ApplicationActivityStop:
                eventId = createCodedValue("110100", "DCM", "Application Activity");
                break;
            case UserAuthenticationLogin:
            case UserAuthenticationLogout:
                eventId = createCodedValue("110114", "DCM", "User Authentication");
                break;
        }

        return eventId;
    }

    /**
     *
     * @param auditMetaData
     * @return
     */
    private static String createEventActionCode(AuditMetaData auditMetaData)
    {
        String eventActionCode = null;
        switch (auditMetaData.getEvent())
        {
            case DemographicIdentityQuery: // Patient Identitiy Source
                eventActionCode = "E";
                break;
            case DemographicRegistration:
                eventActionCode = "C";
                break;
            case DemographicUpdate:
                eventActionCode = "U";
                break;
            case DemographicDelete:
                eventActionCode = "D";
                break;
            case DemographicQuery:
                eventActionCode = "E";
                break;
            case DocumentRegistration: // Provide and Register Document Set-b
                eventActionCode = "R";
                break;
            case DocumentQuery: // Registry Stored Query
                eventActionCode = "E";
                break;
            case DocumentRetrieve: // Retrieve Document Set 
                eventActionCode = "C";
                break;
            case RetrieveValueSet: // Retrieve Value Set (Consumer)
                // TODO: Check if it is the correct event action code.
                eventActionCode = "C";
                break;
            case UpdateDocumentSet:
                eventActionCode = "U";
                break;
            case ApplicationActivityStart:
            case ApplicationActivityStop:
                eventActionCode = "E";
                break;
            case UserAuthenticationLogin:
            case UserAuthenticationLogout:
                eventActionCode = "E";
                break;
        }

        return eventActionCode;
    }

    /**
     * Creates an EventDateTime for AuditMessage/EventIdentification based off
     * the current time on the affinity domain's time server.
     *
     * @param affinityDomain The affinity domain that contains a time server.
     * @return
     */
    private static XMLGregorianCalendar createEventDateTime(IheAffinityDomainConfiguration affinityDomain) throws NtpCommunicationException
    {
        XMLGregorianCalendar eventDateTime = null;

        try
        {
            eventDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) NTPHelper.getTime(affinityDomain));
        } catch (DatatypeConfigurationException ex)
        {
            LOGGER.error("Problem setting ATNA's EventIdentification's DateTime", ex);
            // throw ex;

        } catch (NtpCommunicationException ex)
        {
            throw ex;
        }

        return eventDateTime;
    }

    /**
     * Create the headers for an audit message.
     *
     * @param affinityDomain The affinity domain that contains a time server.
     * @return
     * @throws NtpCommunicationException
     */
    private static String createAuditMessageHeaders(IheAffinityDomainConfiguration affinityDomain) throws NtpCommunicationException
    {
        String auditMessageHeaders = null;
        String msgId = "IHE+RFC-3881";
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            Calendar cal;

            try
            {
                cal = NTPHelper.getTime(affinityDomain);
            } catch (NtpCommunicationException ex)
            {
                throw ex;
            }

            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            String application = "java.exe";
            String process = ManagementFactory.getRuntimeMXBean().getName();
            String[] pid = process.split("@");

            auditMessageHeaders = String.format("<85>1 " + dateFormat.format(cal.getTime()) + "T" + timeFormat.format(cal.getTime()) + "Z %s %s %s - - ", hostname, application, pid[0]);

        } catch (UnknownHostException ex)
        {
            LOGGER.error(ex);
        }

        return auditMessageHeaders;
    }
}