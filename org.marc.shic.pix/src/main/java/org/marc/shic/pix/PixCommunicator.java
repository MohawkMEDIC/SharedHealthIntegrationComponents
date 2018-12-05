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
 * @author tylerg Date: Sept 9 2013
 *
 */
package org.marc.shic.pix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.atna.AuditActor;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.atna.ParticipantObjectDetail;
import org.marc.shic.atna.ParticipantRoleType;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.IheCommunicator;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.RequiredActor;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheSocketConnection;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.pix.messages.AdmitDischargeTransfer_A04;
import org.marc.shic.pix.messages.AdmitDischargeTransfer_A08;
import org.marc.shic.pix.messages.PV1Class;
import org.marc.shic.pix.messages.QueryBasedParameter_Q22;
import org.marc.shic.pix.messages.QueryBasedParameter_Q23;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.message.ACK;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.group.RSP_K23_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.net.SocketException;
import org.marc.shic.pix.messages.AdmitDischargeTransfer_A40;

import java.util.UUID;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;
import org.marc.shic.core.exceptions.InvalidPermissionException;


/**
 *
 * @author Nebri
 */
@RequiredActor(RequiredActors =
{
    IheActorType.PAT_IDENTITY_X_REF_MGR, IheActorType.PDS, IheActorType.TS, IheActorType.AUDIT_REPOSITORY
})
public class PixCommunicator extends IheCommunicator
{

    private final IheSocketConnection pixConnection;
    private final IheSocketConnection pdsConnection;
    // character indicating the termination of an HL7 message
    private static final char END_MESSAGE = '\u001c';
    // character indicating the start of an HL7 message
    private static final char START_MESSAGE = '\u000b';
    // the final character of a message: a carriage return
    private static final char LAST_CHARACTER = 13;
    // TODO: Move these to core?
    private static final String APPLICATION_ACCEPT = "AA";
    private static final String APPLICATION_ERROR = "AE";
    private static final String APPLICATION_REJECT = "AR";
    private static final int FILL_IN_DETAILS_DEFAULT_QUANTITY = 10;
    private static final Logger LOGGER = Logger.getLogger(PixCommunicator.class);
    private final IheActorConfiguration pixActor;
    private final IheActorConfiguration pdsActor;

    /**
     * Construct a new pix communicator.
     *
     * @param configuration The IheConfiguration object containing
     *                      identification and affinity domain info. Must contain an
     *                      AUDIT_REPOSITORY, TS and PAT_IDENTITY_X_REF_MGR actors.
     * @throws ActorNotFoundException
     */
    public PixCommunicator(IheConfiguration configuration) throws ActorNotFoundException
    {
        super(configuration);

        pixActor = configuration.getAffinityDomain().getActor(IheActorType.PAT_IDENTITY_X_REF_MGR);
        // Implement use of pds throughout
        pdsActor = configuration.getAffinityDomain().getActor(IheActorType.PDS);
        pixConnection = new IheSocketConnection(pixActor.getEndPointAddress(), configuration.getKeyStore(), configuration.getTrustStore(), pixActor.isSecure());
        pdsConnection = new IheSocketConnection(pdsActor.getEndPointAddress(), configuration.getKeyStore(), configuration.getTrustStore(), pdsActor.isSecure());
    }

    public PersonDemographic getCorrespondingIdentifiers(PersonDemographic patient, DomainIdentifier... domainsReturned) throws CommunicationsException, PixApplicationException
    {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }
        
        if (patient == null)
        {
            throw new IllegalArgumentException("patient cannot be null.");
        }

        if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Patient is missing a required identifier.");
        }

//        String assigningAuthority = patient.getIdentifiers().get(0).getAssigningAuthority();
        // Create & serialize the QBP message object
        QueryBasedParameter_Q23 qbp;
        String encodedMessage;
        RSP_K23 responseMessage;

        try
        {
            qbp = new QueryBasedParameter_Q23(
                    pixActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    patient,
                    domainsReturned);

            // Serialize/encode
            encodedMessage = qbp.encodeMessage();

        } catch (HL7Exception e)
        {
            throw new PixMessageParseException("Problem creating the HL7 message", e);
        }

        Message message;

        try
        {
            // Send the message and parse the response
            message = sendMessage(pixConnection, encodedMessage);

            if (message instanceof ca.uhn.hl7v2.model.v25.message.ACK)
            {
                ca.uhn.hl7v2.model.v25.message.ACK ack = (ca.uhn.hl7v2.model.v25.message.ACK) message;
                if (!ack.getMSA().getAcknowledgmentCode().getValue().equals(APPLICATION_ACCEPT))
                {
                    // Pass in ERR
                    throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgmentCode().getValue());
                }
            }

        } catch (CommunicationsException e)
        {
            throw e;
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException("Problem creating the HL7 message", e);
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, "LLP error.", e);
        } finally
        {
            // Patient Data
            DomainIdentifier domainIdentifier = patient.getIdentifiers().get(0);
            String srcPatientId = String.format("%s^^^&%s&ISO", domainIdentifier.getExtension(), domainIdentifier.getRoot());

            AuditMetaData auditData = new AuditMetaData(AuditEventType.DemographicIdentityQuery);

            /* The identity of the Patient Identifier Cross-reference Manager or 
             Document Registry facility and receiving application from the HL7 
             message; concatenated together, separated by the | character. 
             */
            String destinationIdentifier = String.format("%s|%s", pixActor.getRemoteIdentification().getApplicationId(), pixActor.getRemoteIdentification().getFacilityName());

            /*
             The identity of the Patient Identity Source Actor facility and sending 
             application from the HL7 message; concatenated together, separated by 
             the | character. 
             */
            String sourceIdentifier = String.format("%s|%s", getConfiguration().getLocalIdentification().getApplicationId(), getConfiguration().getLocalIdentification().getFacilityName());

            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, destinationIdentifier, pixActor.getOid(), false, pixActor.getEndPointAddress().toString(), (short) 2);
            auditData.addActor(destinationActor);

            AuditActor sourceActor = new AuditActor(AuditActorType.Source, sourceIdentifier, AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(sourceActor);

            //  auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));
            final String base64EncodedMsh10 = Base64.encode(qbp.getQbp().getMSH().getMsh10_MessageControlID().getValue().getBytes());
            final String base64EncodedMessage = Base64.encode(encodedMessage.getBytes());

            auditData.addParticipant(ParticipantRoleType.Patient, srcPatientId, null, null);
            auditData.addParticipant(ParticipantRoleType.ITIName, UUID.randomUUID().toString(), base64EncodedMessage.getBytes(), new ParticipantObjectDetail("MSH-10", base64EncodedMsh10.getBytes()));

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        responseMessage = (RSP_K23) message;

        RSP_K23_QUERY_RESPONSE queryResponse = responseMessage.getQUERY_RESPONSE();
        PID responsePid = queryResponse.getPID();
        CX[] responseIdentifierList = responsePid.getPatientIdentifierList();

        // Keep the local patient identifiers & add new identifiers on top
		// patient.getIdentifiers().clear();

        for (int x = 0; x < responseIdentifierList.length; x++)
        {
            // Change from Cx
            DomainIdentifier resolvedId = new DomainIdentifier();
            resolvedId.setAssigningAuthority(responseIdentifierList[x].getCx4_AssigningAuthority().getHd1_NamespaceID().toString());
            resolvedId.setExtension(responseIdentifierList[x].getCx1_IDNumber().toString());
            resolvedId.setRoot(responseIdentifierList[x].getCx4_AssigningAuthority().getHd2_UniversalID().toString());
            patient.addIdentifier(resolvedId);
        }

        return patient;
    }

        // need to break out the response ack. This piece of information will allow 
        // us to determine when the pix server needs to have the global id seeded.
        // changing logic to return true or false.
        // if the method returns false, a global id must be sent.
        // if the method returns true, only the local id must be sent.
        public Boolean getCorrespondingIdentifiersQAK2(PersonDemographic patient, DomainIdentifier... domainsReturned) throws CommunicationsException, PixApplicationException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient cannot be null.");
        }

        if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Patient is missing a required identifier.");
        }

//        String assigningAuthority = patien.tgetIdentifiers().get(0).getAssigningAuthority();
        // Create & serialize the QBP message object
        QueryBasedParameter_Q23 qbp;
        String encodedMessage;

        String qak2 = "";
        Boolean result = true;
        
        try
        {
            qbp = new QueryBasedParameter_Q23(
                    pixActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    patient,
                    domainsReturned);

            // Serialize/encode
            encodedMessage = qbp.encodeMessage();

        } catch (HL7Exception e)
        {
            throw new PixMessageParseException("Problem creating the HL7 message", e);
        }

        Message message;

        try
        {
            // Send the message and parse the response
            message = sendMessage(pixConnection, encodedMessage);

            if (message instanceof ca.uhn.hl7v2.model.v25.message.ACK)
            {
                ca.uhn.hl7v2.model.v25.message.ACK ack = (ca.uhn.hl7v2.model.v25.message.ACK) message;                
                
                qak2 = ack.getMSA().getAcknowledgmentCode().getValue();
            }

            if (message instanceof ca.uhn.hl7v2.model.v25.message.RSP_K23) {
                RSP_K23 responseMessage = (RSP_K23) message;
                qak2 = responseMessage.getQAK().getQak2_QueryResponseStatus().getValue();
            }
            
        } catch (CommunicationsException e)
        {
            throw e;
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException("Problem creating the HL7 message", e);
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, "LLP error.", e);
        }  finally {
            
        }
        
        if ("AE".equals(qak2))
        {
            result = false;
        }
            
        
        return result;
    }
    
    /**
     * Resolve patient identifiers.
     *
     * @param patient The patient to resolve identifiers for.
     * @return Patient with resolved identifiers.
     * @throws CommunicationsException
     * @deprecated Use getCorrespondingIdentifiers instead.
     */
    @Deprecated
    public PersonDemographic resolveIdentifiers(PersonDemographic patient, DomainIdentifier... domainsReturned) throws CommunicationsException, PixApplicationException
    {
        return getCorrespondingIdentifiers(patient, domainsReturned);
    }

    /**
     * ITI-21 Patient Demographics Query. QBP^Q22.
     * Find patients matching a set of demographic data.
     *
     * @param demographicQuery The demographic data to find matching patients.
     * @return List of patients whose demographic data matches the requested
     *         data.
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public List<PersonDemographic> findCandidatesQuery(PersonDemographic demographicQuery) throws CommunicationsException, PixApplicationException
    {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }
        
        List<PersonDemographic> foundPatients = new ArrayList<PersonDemographic>();
        QueryBasedParameter_Q22 qbp_q22;
        String encodedMessage;
        Message message;
        RSP_K21 responseMessage = null;

        try
        {
            // Create message and fill it with needed values from configuration.
            qbp_q22 = new QueryBasedParameter_Q22(
                    pdsActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    demographicQuery);

            encodedMessage = qbp_q22.encodeMessage();
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        }

        try
        {
            // Captures the response Message
            message = sendMessage(pdsConnection, encodedMessage);

            if (message instanceof ca.uhn.hl7v2.model.v25.message.ACK)
            {
                ca.uhn.hl7v2.model.v25.message.ACK ack = (ca.uhn.hl7v2.model.v25.message.ACK) message;
                if (!ack.getMSA().getAcknowledgmentCode().getValue().equals(APPLICATION_ACCEPT))
                {
                    // Pass in ERR
                    throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgmentCode().getValue());
                }
            }

            responseMessage = (RSP_K21) message;

        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw new CommunicationsException(pdsActor, e.getMessage(), e);
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        } finally
        {
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DemographicIdentityQuery);
            auditData.addActor(AuditActorType.Destination, pdsActor);
            auditData.addActor(AuditUtility.generateRequestingUser());
            auditData.addParticipant(ParticipantRoleType.ITIName, "ITI-18", DatatypeConverter.printBase64Binary(encodedMessage.getBytes()));

            // Do not fill in the patient participant if there was an error with the request/response.
            if (responseMessage != null)
            {
                foundPatients = parsePatientsFromResponse(responseMessage);

                for (PersonDemographic patient : foundPatients)
                {
                    auditData.addParticipant(ParticipantRoleType.Patient, getFormattedPatientId(patient), null);
                }
            }

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        return foundPatients;
    }

    private List<PersonDemographic> parsePatientsFromResponse(RSP_K21 responseMessage)
    {
        List<PersonDemographic> parsedPatients = new ArrayList<PersonDemographic>();

        for (int i = 0; i < responseMessage.getQUERY_RESPONSEReps(); i++)
        {
            PersonDemographic foundPatient = new PersonDemographic();
            PID responsePid;

	    responsePid = responseMessage.getQUERY_RESPONSE(i).getPID();
	    
//            try
//            {
//                responsePid = responseMessage.getQUERY_RESPONSE(i).getPID();
//            }
//	    catch (HL7Exception e)
//            {
//                throw new PixMessageParseException(e);
//            }

            foundPatient.getIdentifiers().addAll(PixUtility.parseIdentifiersFromCX(responsePid.getPatientIdentifierList()));
            foundPatient.getAddresses().addAll(PixUtility.parseAddressesFromXAD(responsePid.getPatientAddress()));
            foundPatient.getNames().addAll(PixUtility.parseNamesFromXPN(responsePid.getPatientName()));

            // Capture Date of Birth with simple date formatter
            String dateString = responsePid.getDateTimeOfBirth().getTime().getValue();

            Pattern datePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$");// 3 groups
            Pattern dateTimePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{2})([0-9]{2})([0-9]{2})$");// 6 groups
            Pattern dateTimeZonePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{2})([0-9]{2})([0-9]{2})-([0-9]{4})$");// 7 groups

            Matcher date = datePattern.matcher(dateString);
            Matcher dateTime = dateTimePattern.matcher(dateString);
            Matcher dateTimeZone = dateTimeZonePattern.matcher(dateString);

            Calendar dateOfBirth = Calendar.getInstance();

            if (dateTimeZone.find())
            {
                dateOfBirth.set(Integer.parseInt(dateTimeZone.group(1)), Integer.parseInt(dateTimeZone.group(2)), Integer.parseInt(dateTimeZone.group(3)),
                        Integer.parseInt(dateTimeZone.group(4)), Integer.parseInt(dateTimeZone.group(5)), Integer.parseInt(dateTimeZone.group(6)));
                dateOfBirth.setTimeZone(TimeZone.getTimeZone("GMT-" + dateTimeZone.group(7)));
            } else if (dateTime.find())
            {
                dateOfBirth.set(Integer.parseInt(dateTime.group(1)), Integer.parseInt(dateTime.group(2)), Integer.parseInt(dateTime.group(3)),
                        Integer.parseInt(dateTime.group(4)), Integer.parseInt(dateTime.group(5)), Integer.parseInt(dateTime.group(6)));
            } else if (date.find())
            {
                dateOfBirth.set(Integer.parseInt(date.group(1)), Integer.parseInt(date.group(2)), Integer.parseInt(date.group(3)));
            }

            foundPatient.setDateOfBirth(dateOfBirth);

            //TODO: Add rest of gender cases.         
            if (responsePid.getAdministrativeSex().getValue().equals("M"))
            {
                foundPatient.setGender(Gender.M);
            } else if (responsePid.getAdministrativeSex().getValue().equals("F"))
            {
                foundPatient.setGender(Gender.F);
            } else
            {
                foundPatient.setGender(Gender.O);
            }

            parsedPatients.add(foundPatient);

        }

        return parsedPatients;
    }

    @Deprecated
    public PersonDemographic fillInDetails(PersonDemographic patient) throws PixApplicationException, CommunicationsException
    {
        return fillInDetails(patient, FILL_IN_DETAILS_DEFAULT_QUANTITY);
    }

    /**
     * Fill in details from a partially populated person demographic.
     *
     * @param patient Patient to perform the fill in details on.
     * @return Patient with filled in details.
     * @throws PixApplicationException
     * @throws CommunicationsException
     * @deprecated Use findCandidatesQuery instead.
     */
    @Deprecated
    public PersonDemographic fillInDetails(PersonDemographic patient, int quantity) throws PixApplicationException, CommunicationsException
    {
        if (patient == null)
        {
            throw new IllegalArgumentException("patient cannot be null.");
        }

        // Create message and fill it with needed values from configuration.
        QueryBasedParameter_Q22 qbp_q22 = null;

        // Serialize/encode
        String encodedMessage = null;

        Message message;

        try
        {
            qbp_q22 = new QueryBasedParameter_Q22(
                    pixActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    patient,
                    quantity);

            encodedMessage = qbp_q22.encodeMessage(); // encodes the message as a PIXV2 String

            // Captures the response Message
            message = sendMessage(pixConnection, encodedMessage);

            if (message instanceof ACK)
            {
                ACK ack = (ACK) message;
                if (!ack.getMSA().getAcknowledgementCode().getValue().equals(APPLICATION_ACCEPT))
                {
                    // Pass in ERR
                    throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgementCode().getValue());
                }
            }

        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, e.getMessage(), e);
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        } finally
        {
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DemographicIdentityQuery);
            auditData.addActor(AuditActorType.Destination, pixActor);
            auditData.addActor(AuditUtility.generateRequestingUser());

            // Patient Data
            DomainIdentifier domainIdentifier = patient.getIdentifiers().get(0);
            String srcPatientId = String.format("%s^^^&%s&ISO", domainIdentifier.getExtension(), domainIdentifier.getRoot());
            auditData.addParticipant(ParticipantRoleType.Patient, srcPatientId, null);
            auditData.addParticipant(ParticipantRoleType.ITIName, "ITI-18", DatatypeConverter.printBase64Binary(encodedMessage.getBytes()));

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        RSP_K21 responseMessage = (RSP_K21) message;

        PID responsePid = responseMessage.getQUERY_RESPONSE().getPID();

        patient.getIdentifiers().addAll(PixUtility.parseIdentifiersFromCX(responsePid.getPatientIdentifierList()));
        patient.getAddresses().addAll(PixUtility.parseAddressesFromXAD(responsePid.getPatientAddress()));
        patient.getNames().addAll(PixUtility.parseNamesFromXPN(responsePid.getPatientName()));

        // Capture Date of Birth with simple date formatter
        String dateString = responsePid.getDateTimeOfBirth().getTime().getValue();

        Pattern datePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$");// 3 groups
        Pattern dateTimePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{2})([0-9]{2})([0-9]{2})$");// 6 groups
        Pattern dateTimeZonePattern = Pattern.compile("^([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{2})([0-9]{2})([0-9]{2})-([0-9]{4})$");// 7 groups

        Matcher date = datePattern.matcher(dateString);
        Matcher dateTime = dateTimePattern.matcher(dateString);
        Matcher dateTimeZone = dateTimeZonePattern.matcher(dateString);

        Calendar dateOfBirth = Calendar.getInstance();

        if (dateTimeZone.find())
        {
            dateOfBirth.set(Integer.parseInt(dateTimeZone.group(1)), Integer.parseInt(dateTimeZone.group(2)), Integer.parseInt(dateTimeZone.group(3)),
                    Integer.parseInt(dateTimeZone.group(4)), Integer.parseInt(dateTimeZone.group(5)), Integer.parseInt(dateTimeZone.group(6)));
            dateOfBirth.setTimeZone(TimeZone.getTimeZone("GMT-" + dateTimeZone.group(7)));
        } else if (dateTime.find())
        {
            dateOfBirth.set(Integer.parseInt(dateTime.group(1)), Integer.parseInt(dateTime.group(2)), Integer.parseInt(dateTime.group(3)),
                    Integer.parseInt(dateTime.group(4)), Integer.parseInt(dateTime.group(5)), Integer.parseInt(dateTime.group(6)));
        } else if (date.find())
        {
            dateOfBirth.set(Integer.parseInt(date.group(1)), Integer.parseInt(date.group(2)), Integer.parseInt(date.group(3)));
        }

        patient.setDateOfBirth(dateOfBirth);

        //TODO: Add rest of gender cases.         
        if (responsePid.getAdministrativeSex().getValue().equals("M"))
        {
            patient.setGender(Gender.M);
        } else if (responsePid.getAdministrativeSex().getValue().equals("F"))
        {
            patient.setGender(Gender.F);
        } else
        {
            patient.setGender(Gender.O);
        }

        return patient;
    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A04 Register Patient.
     * Register a patient on the pix.
     *
     * @param patient
     * @param pv1
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public void register(PersonDemographic patient, PV1Class pv1) throws PixApplicationException, CommunicationsException
    {
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.ReadOnly){
            throw new InvalidPermissionException("Invalid Permission: Write or WriteRead required.");
        }
        // Sanity checks for all required patient information.
        if (patient == null)
        {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Patient has no identifier.");
        }

        if (patient.getDateOfBirth() == null)
        {
            // Reword?
            throw new IllegalArgumentException("Required property missing from patient: Date of birth");
        }

        String encodedMessage;
        ACK ack;
        AdmitDischargeTransfer_A04 adt_a04 = null;

        try
        {
            adt_a04 = new AdmitDischargeTransfer_A04(
                    pixActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    patient,
                    pv1);

            encodedMessage = adt_a04.encodeMessage();

            ack = (ACK) sendMessage(pixConnection, encodedMessage);

        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, e);
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        } finally
        {
            // Patient Data
            DomainIdentifier domainIdentifier = patient.getIdentifiers().get(0);

            String srcPatientId = String.format("%s^^^&%s&ISO", domainIdentifier.getExtension(), domainIdentifier.getRoot());

            AuditMetaData auditData = generatePixFeedAudit(srcPatientId, adt_a04.getAdt().getMSH().getMsh10_MessageControlID().getValue(), AuditEventType.DemographicRegistration);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        if (!ack.getMSA().getAcknowledgementCode().getValue().equals(APPLICATION_ACCEPT))
        {
            throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgementCode().getValue());
        }

    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A04 Register Patient.
     * Register a patient on the pix.
     *
     * @param patient The patient to register.
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public void register(PersonDemographic patient) throws PixApplicationException, CommunicationsException
    {
        register(patient, PV1Class.O);
    }

    private AuditMetaData generatePixFeedAudit(String patientId, String msh10, AuditEventType eventType)
    {
        AuditMetaData auditData = new AuditMetaData(eventType);

        /* The identity of the Patient Identifier Cross-reference Manager or 
         Document Registry facility and receiving application from the HL7 
         message; concatenated together, separated by the | character. 
         */
        String destinationIdentifier = String.format("%s|%s", pixActor.getRemoteIdentification().getApplicationId(), pixActor.getRemoteIdentification().getFacilityName());

        /*
         The identity of the Patient Identity Source Actor facility and sending 
         application from the HL7 message; concatenated together, separated by 
         the | character. 
         */
        String sourceIdentifier = String.format("%s|%s", getConfiguration().getLocalIdentification().getApplicationId(), getConfiguration().getLocalIdentification().getFacilityName());

        AuditActor destinationActor = new AuditActor();
        destinationActor.setActorType(AuditActorType.Destination);
        destinationActor.setIdentifier(destinationIdentifier);
        destinationActor.setUserIsRequestor(false);
        destinationActor.setAlternativeUserId(pixActor.getOid());
        destinationActor.setNetworkEndpoint(pixActor.getEndPointAddress().toString());
        destinationActor.setNetworkAccessPointTypeCode((short) 2);
//            
//        try
//        {
//            java.net.URL url = new java.net.URL(pixActor.getEndPointAddress().toString());
//
//            InetAddress destinationIp = InetAddress.getByName(url.getHost());
//            destinationActor.setNetworkEndpoint(destinationIp.getHostAddress());
//            destinationActor.setNetworkAccessPointTypeCode((short) 2);
//        } catch (UnknownHostException ex)
//        {
//            LOGGER.info(ex);
//        } catch (MalformedURLException ex)
//        {
//            LOGGER.info(ex);
//        }
        //  auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));

        auditData.addActor(destinationActor);

        AuditActor sourceActor = new AuditActor();
        sourceActor.setActorType(AuditActorType.Source);
        sourceActor.setIdentifier(sourceIdentifier);
        sourceActor.setUserIsRequestor(true);

        // Process id
        sourceActor.setAlternativeUserId(AuditUtility.getProcessId());

        sourceActor.setNetworkAccessPointTypeCode((short) 1);
        sourceActor.setNetworkEndpoint(AuditUtility.getNetworkId());

        auditData.addActor(sourceActor);

        final String base64EncodedMsh10 = Base64.encode(msh10.getBytes());

        auditData.addParticipant(ParticipantRoleType.Patient, patientId, null, new ParticipantObjectDetail("MSH-10", base64EncodedMsh10.getBytes()));
        return auditData;
    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A40 Merge Patient.
     * Merge two patients together.
     * Uses Outpatient as the patient visit.
     *
     * @param patient
     * @param priorPatient
     * @throws CommunicationsException
     * @throws PixApplicationException
     */
    public void merge(PersonDemographic patient, PersonDemographic priorPatient) throws CommunicationsException, PixApplicationException
    {
        merge(patient, priorPatient, PV1Class.O);
    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A40 Merge Patient.
     * Merge two patients together.
     *
     * @param patient
     * @param priorPatient
     * @param pv1
     * @throws CommunicationsException
     * @throws PixApplicationException
     */
    public void merge(PersonDemographic patient, PersonDemographic priorPatient, PV1Class pv1) throws CommunicationsException, PixApplicationException
    {
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.ReadOnly){
            throw new InvalidPermissionException("Invalid Permission: Write or WriteRead required.");
        }
        // TODO: Check if patient/priorPatient are reveresed.

        if (patient == null)
        {
            throw new IllegalArgumentException("patient cannot be null.");
        }

        if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("patient has no identifier.");
        }

        if (priorPatient == null)
        {
            throw new IllegalArgumentException("priorPatient cannot be null.");
        }

        if (priorPatient.getIdentifiers() == null || priorPatient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("priorPatient has no identifier.");
        }

        AdmitDischargeTransfer_A40 adt_a40 = null;
        String encodedMessage;
        ACK ack;

        try
        {
            adt_a40 = new AdmitDischargeTransfer_A40(pixActor, this.getConfiguration().getIdentifier(), this.getConfiguration().getLocalIdentification(), patient, priorPatient, PV1Class.O);

            encodedMessage = adt_a40.encodeMessage();

            ack = (ACK) sendMessage(pixConnection, encodedMessage);

        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, e);
        } finally
        {
            // Patient Data
            DomainIdentifier patientIdentifier = patient.getIdentifiers().get(0);
            String patientId = String.format("%s^^^&%s&ISO", patientIdentifier.getExtension(), patientIdentifier.getRoot());

            AuditMetaData updateAudit = generatePixFeedAudit(patientId, adt_a40.getAdt().getMSH().getMsh10_MessageControlID().getValue(), AuditEventType.DemographicUpdate);

            AtnaCommunicator.enqueueMessage(updateAudit, getConfiguration());

            // Prior Patient Data
            DomainIdentifier priorPatientIdentifier = priorPatient.getIdentifiers().get(0);
            String priorPatientId = String.format("%s^^^&%s&ISO", priorPatientIdentifier.getExtension(), priorPatientIdentifier.getRoot());

            AuditMetaData deleteAudit = generatePixFeedAudit(priorPatientId, adt_a40.getAdt().getMSH().getMsh10_MessageControlID().getValue(), AuditEventType.DemographicDelete);

            AtnaCommunicator.enqueueMessage(deleteAudit, getConfiguration());
        }

        if (!ack.getMSA().getAcknowledgementCode().getValue().equals(APPLICATION_ACCEPT))
        {
            throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgementCode().getValue());
        }
    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A08 Update Patient.
     * Update an existing patient demographic.
     * Uses Outpatient as the visit type.
     *
     * @param patient
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public void update(PersonDemographic patient) throws PixApplicationException, CommunicationsException
    {
        update(patient, PV1Class.O);
    }

    /**
     * ITI-8 Patient Identity Feed. ADT^A08 Update Patient.
     * Update an existing patient demographic.
     *
     * @param patient The information that is updated.
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public void update(PersonDemographic patient, PV1Class pv1) throws PixApplicationException, CommunicationsException
    {
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.ReadOnly){
            throw new InvalidPermissionException("Invalid Permission: Write or WriteRead required.");
        }
        
        if (patient == null)
        {
            throw new IllegalArgumentException("patient cannot be null.");
        }

        if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty())
        {
            throw new IllegalArgumentException("Patient is missing identifiers.");
        }

        String encodedMessage;
        ACK ack;
        AdmitDischargeTransfer_A08 adt_a08 = null;

        // Patient Data            
        DomainIdentifier domainIdentifier = patient.getIdentifiers().get(0);
        String srcPatientId = String.format("%s^^^&%s&ISO", domainIdentifier.getExtension(), domainIdentifier.getRoot());

        try
        {
            adt_a08 = new AdmitDischargeTransfer_A08(
                    pixActor,
                    this.getConfiguration().getIdentifier(),
                    this.getConfiguration().getLocalIdentification(),
                    patient,
                    PV1Class.O);

            encodedMessage = adt_a08.encodeMessage();

            ack = (ACK) sendMessage(pixConnection, encodedMessage);
        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw new CommunicationsException(pixActor, e.getMessage(), e);
        } catch (HL7Exception e)
        {
            throw new PixMessageParseException(e);
        } finally
        {
            AuditMetaData auditData = generatePixFeedAudit(srcPatientId, adt_a08.getAdt().getMSH().getMsh10_MessageControlID().getValue(), AuditEventType.DemographicUpdate);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        if (!ack.getMSA().getAcknowledgementCode().getValue().equals(APPLICATION_ACCEPT))
        {
            throw new PixApplicationException(pixActor, "AcknowledgementCode was: " + ack.getMSA().getAcknowledgementCode().getValue());
        }
    }

    /**
     * Open the connection.
     *
     * @throws CommunicationsException
     */
    private void open(IheSocketConnection connection) throws CommunicationsException
    {
        try
        {
            // connect (create the socket)
            connection.connect();

        } catch (Exception ex)
        {
            throw new CommunicationsException(pixActor, "There was an error establishing a connection.", ex);
        }
    }

    /**
     * Close the connection.
     */
    private void close(IheSocketConnection connection)
    {
        connection.close();
    }

    /**
     * This method is used to send a completed HL7V2 message to the endpoint
     * configured. This method's return will be the server response to the
     * outgoing message.
     *
     * @param message - message to send to server
     * @return - server response message
     * @throws HL7Exception
     * @throws LLPException
     * @throws IOException
     */
    private Message sendMessage(IheSocketConnection connection, String encodedMessage) throws HL7Exception, LLPException, CommunicationsException
    {
        if (encodedMessage == null || encodedMessage.length() == 0)
        {
            throw new IllegalArgumentException("encodedMessage cannot be null or empty.");
        }

        LOGGER.info(String.format("Request:\n%s\n", encodedMessage));

        // If the socket connection was closed, re-open it
        if (connection.isClosed())
        {
            try
            {
                open(connection);
            } catch (CommunicationsException e)
            {
                throw e;
            }
        }

        try
        {
            // Send the message to the socket.
            sendHL7Message(connection, encodedMessage);
        } catch (CommunicationsException ex)
        {
            throw ex;
        }

        // Receive a response from the socket.
        String result;

        try
        {
            result = receiveHL7Message(connection);
        } catch (CommunicationsException e)
        {
            throw e;
        } catch (LLPException e)
        {
            throw e;
        }

        close(connection);

        LOGGER.info(String.format("Response:\n%s\n", result));

        Parser parser = new PipeParser();
        Message responseMessage;

        // Parse the response into a Message object.
        responseMessage = parser.parse(result);

        return responseMessage;
    }

    /**
     * Sends the message to the pix actor.
     *
     * @param message The message to send.
     * @throws CommunicationsException
     */
    private void sendHL7Message(IheSocketConnection connection, String message) throws CommunicationsException
    {
        if (message == null || message.length() == 0)
        {
            throw new IllegalArgumentException("message cannot be null or empty.");
        }

        String fullMessage = START_MESSAGE + message + END_MESSAGE + LAST_CHARACTER;

        try
        {
            connection.write(fullMessage);
        } catch (SocketException e) {
            throw new CommunicationsException(pixActor, e.getMessage(), e);
        } catch (IOException e)
        {
            throw new CommunicationsException(pixActor, "There was an error writing to the connection stream.", e);
        }
    }

    /**
     * Receives a message from the pix actor.
     *
     * @return The received message.
     * @throws LLPException
     * @throws CommunicationsException
     */
    private String receiveHL7Message(IheSocketConnection connection) throws LLPException, CommunicationsException
    {
        StringBuilder message = new StringBuilder();
        boolean endOfMessage = false;

        int character = 0;

        try
        {
            character = connection.read();
        } catch (SocketException e) {
            throw new CommunicationsException(pixActor, e.getMessage(), e);
        } catch (IOException e)
        {
            throw new CommunicationsException(pixActor, "There was an error reading from the connection stream.", e);
        }

        // trying to read when there is no data (stream may have been closed at other end)
        if (character == -1)
        {
            throw new CommunicationsException(pixActor, "End of input stream reached. Stream may have been closed at the other end.");
        }

        if (character != START_MESSAGE)
        {
            throw new LLPException("Message violates the minimal lower layer protocol: no start of message indicator received.");
        }

        // Continue reading until the entire message is received.
        while (!endOfMessage)
        {
            try
            {
                character = connection.read();
            } catch (IOException e)
            {
                throw new CommunicationsException(pixActor, "There was an error reading from the connection stream.", e);
            }

            if (character == -1)
            {
                throw new LLPException("Message violates the minimal lower protocol: message terminated without a terminating character.");
            }

            if (character == END_MESSAGE)
            {
                // Subsequent character should be a carriage return.
                try
                {
                    character = connection.read();
                } catch (IOException e)
                {
                    throw new CommunicationsException(pixActor, "There was an error reading from the connection stream.", e);
                }

                if (character != LAST_CHARACTER)
                {
                    throw new LLPException("Message violates the minimal lower layer protocol: message terminator not followed by a return character.");
                }

                endOfMessage = true;
            } else
            {
                // The character wasn't the end of message, append it to the message.
                message.append((char) character);
            }
        }

        return message.toString();
    }
}
