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
package org.marc.shic.pix;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.examples.ExampleReceiverApplication;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.QIP;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.QPD;
import ca.uhn.hl7v2.model.v25.segment.RCP;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import java.io.IOException;
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nebri
 */
public class HapiWrapper
{

    private int port;
    private String domain;
    private String sendingApplication;
    private String sendingFacility;

    public HapiWrapper(int port, String domain, String sendingApplication, String sendingFacility)
    {
        this.port = port;
        this.domain = domain;
        this.sendingApplication = sendingApplication;
        this.sendingFacility = sendingFacility;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getSendingApplication()
    {
        return sendingApplication;
    }

    public void setSendingApplication(String sendingApplication)
    {
        this.sendingApplication = sendingApplication;
    }

    public String getSendingFacility()
    {
        return sendingFacility;
    }

    public void setSendingFacility(String sendingFacility)
    {
        this.sendingFacility = sendingFacility;
    }

    /**
     * This method will create a QBP_Q23 message which is used to resolve a
     * patientID
     * in another affinity domain.
     *
     * @param patientId        - patientID that will be used to resolve.
     * @param messageControlID
     * @return
     * @throws HL7Exception
     */
    public String createQBP_Q23(String patientId, String messageControlID) throws HL7Exception
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        //put in qbp message here.

        QBP_Q21 qbp = new QBP_Q21();

        MSH mshSegment = qbp.getMSH();
        QPD qpdSegment = qbp.getQPD();
        RCP rcpSegment = qbp.getRCP();

        Parser parser = new PipeParser();

        Date currentDate = new Date();

        try
        {
            // required MSH segment fields
            mshSegment.getFieldSeparator().setValue("|");
            mshSegment.getEncodingCharacters().setValue("^~\\&");
            mshSegment.getMessageType().getMessageStructure().setValue("QBP_Q21");
            mshSegment.getMessageType().getMessageCode().setValue("QBP");
            mshSegment.getMessageType().getTriggerEvent().setValue("Q23");
            mshSegment.getMessageControlID().setValue(messageControlID);      // can we generate this value?
            mshSegment.getProcessingID().getProcessingID().setValue("P");
            mshSegment.getVersionID().getVersionID().setValue("2.5");

            // Optional MSH segment fields
            mshSegment.getSendingApplication().getNamespaceID().setValue(sendingApplication);
            mshSegment.getSendingFacility().getNamespaceID().setValue(sendingFacility);
            mshSegment.getDateTimeOfMessage().getTime().setValue(currentDate.toString());

            // Required QPD Segment fields
            qpdSegment.getMessageQueryName().getIdentifier().setValue("IHE PIX Query");

            QIP qip = new QIP(qbp);
            qip.getSegmentFieldName().setValue(patientId);
            qpdSegment.getUserParametersInsuccessivefields().setData(qip);

            // Required RCP Segment Fields
            rcpSegment.getQueryPriority().setValue("I");

        } catch (DataTypeException ex)
        {
            // TODO: Rethrow
            ex.printStackTrace();
        }

        String encodedMessage;
        encodedMessage = parser.encode(qbp);
        System.out.println("created message: ");
        System.out.println(encodedMessage);

        return encodedMessage;
    }

    /**
     * This message will create a HL7v2 ADT - A08 message.
     * The ADT_A08 message is used to update patient information in a PIX
     * system.
     */
    public String createADT_A08(String patientFirstName, String patientLastName, String gender, String patientId, String messageControlId, Date patientDateofBirth) throws DataTypeException, HL7Exception
    {
        Date currentDate = new Date();
        Date recordedDate = new Date();
        ADT_A01 adt = new ADT_A01();
        Parser parser = new PipeParser();
        String encodedMessage;

        recordedDate.setTime(1346506441);
        MSH mshSegment = adt.getMSH();
        EVN evnSegment = adt.getEVN();
        PID pidSegment = adt.getPID();

        // required fields
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getMessageType().getMessageCode().setValue("ADT");
        mshSegment.getMessageType().getTriggerEvent().setValue("A08");
        mshSegment.getMessageControlID().setValue(messageControlId);
        mshSegment.getProcessingID().getProcessingID().setValue("P");
        mshSegment.getVersionID().getVersionID().setValue("2.5");

        // optional fields
        mshSegment.getSendingApplication().getNamespaceID().setValue(sendingApplication);
        mshSegment.getSendingFacility().getNamespaceID().setValue(sendingFacility);
        mshSegment.getDateTimeOfMessage().getTime().setValue(currentDate.toString());

        evnSegment.getEvn1_EventTypeCode().setValue("A08");
        evnSegment.getRecordedDateTime().getTime().setValue(recordedDate.toString());

        pidSegment.getPatientIdentifierList(0).getIDNumber().setValue(patientId);
        pidSegment.getPatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(sendingApplication);
        pidSegment.getPatientName(0).getGivenName().setValue(patientFirstName);
        pidSegment.getPatientName(0).getFamilyName().getSurname().setValue(patientLastName);
        pidSegment.getDateTimeOfBirth().getTime().setValue(patientDateofBirth.toString());
        pidSegment.getAdministrativeSex().setValue(gender);

        encodedMessage = parser.encode(adt);
        System.out.println(encodedMessage);

        return encodedMessage;
    }

    /**
     * This method is used to send a completed hl7V2 message to the endpoint
     * configured.
     * This method's return will be the server response to the outgoing message.
     *
     * @param message - message to send to server
     * @return - server response message
     * @throws HL7Exception
     * @throws LLPException
     * @throws IOException
     */
    public Message sendMessage(String message) throws HL7Exception, LLPException, IOException
    {

        Message response;
        LowerLayerProtocol llp = LowerLayerProtocol.makeLLP();
        PipeParser parser = new PipeParser();
        SimpleServer server = new SimpleServer(port, llp, parser);

        ConnectionHub connectionHub = ConnectionHub.getInstance();
        Connection connection = connectionHub.attach(domain, port, new PipeParser(), MinLowerLayerProtocol.class);

        try
        {

            Application handler = new ExampleReceiverApplication();
            server.registerApplication("ADT", "A02", handler);
            server.registerApplication("ADT", "A01", handler);

            server.start();

            Parser genericParser = new GenericParser();
            Message adt = genericParser.parse(message);

            Initiator initiator = connection.getInitiator();
            response = initiator.sendAndReceive(adt);

        } finally
        {
            connectionHub.discard(connection);
            server.stop();//.stopAndWait();
            //ConnectionHub.shutdown();
        }

        return response;
    }
}
