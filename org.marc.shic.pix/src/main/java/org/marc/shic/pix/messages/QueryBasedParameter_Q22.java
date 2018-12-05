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
package org.marc.shic.pix.messages;

import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.util.Terser;
import java.util.UUID;
import org.marc.shic.core.AddressComponent;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.pix.PixUtility;

public class QueryBasedParameter_Q22 extends QueryBasedParameter
{

    private int quantity;

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public QueryBasedParameter_Q22(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient) throws HL7Exception
    {
        super(actor, identifier, localIdentification, patient);

        qbp = new QBP_Q21();

        fillMshQ22();
        fillQpdSegment();
        fillRCP();
    }

    public QueryBasedParameter_Q22(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient, int quantity) throws HL7Exception
    {
        this(actor, identifier, localIdentification, patient);
        this.quantity = quantity;
    }

    // Fills the standard MSH with QBP-Q22 information that is static.
    private void fillMshQ22() throws HL7Exception
    {
        qbp.getMSH().getMsh9_MessageType().getMsg1_MessageCode().setValue("QBP");
        qbp.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue("Q22");
        qbp.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("QBP_Q21");
        super.fillStandardMsh(qbp.getMSH());
    }

    // Creates the QPD segment of the message which contains the Query
    // Parameters.
    // QPD-1 is hard coded as these values don't change, they are static.
    // QPD-2 is a unique identifier for the instance of the QPD message. This
    // will need to be generated.
    // QPD-3 Contains the parameters by which we are querying for.
    // PatientID root + extension.
    private void fillQpdSegment() throws HL7Exception
    {
        //  qpdSegment = qbp.getQPD();

        //  qbp.getQPD().getMessageQueryName().getText().setValue("Find Candidates");
//        qbp.getQPD().getMessageQueryName().getIdentifier().setValue("Q22");
//        qbp.getQPD().getMessageQueryName().getNameOfCodingSystem().setValue("HL7");
        Terser terser = new Terser(qbp);

        String uuid = UUID.randomUUID().toString();
        qbp.getQPD().getQueryTag().setValue(uuid);

        terser.set("/QPD-1(0)-1", "IHE PDQ Query");

        // Hapi index.
        int currentIndex = 0;

        for (DomainIdentifier id : patient.getIdentifiers())
        {
            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.3.1");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), id.getExtension());
            currentIndex++;

            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.3.4.1");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), id.getAssigningAuthority());
            currentIndex++;

            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.3.4.2");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), id.getRoot());
            currentIndex++;

            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.3.4.3");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), "ISO");
            currentIndex++;


//            terser.set("/QPD-3(1)-1", "@PID.3.4.1");
//            terser.set("/QPD-3(1)-2", authority);
//
//            terser.set("/QPD-3(2)-1", "@PID.3.4.2");
//            terser.set("/QPD-3(2)-2", root);
//
//            terser.set("/QPD-3(3)-1", "@PID.3.4.3");
//            terser.set("/QPD-3(3)-2", "ISO");
        }

        if (patient.getNames().size() > 0)
        {
            PersonName patientName = patient.getNames().get(0);

            for (NameComponent component : patientName.getParts())
            {
                terser.set(String.format("/QPD-3(%d)-1", currentIndex), component.getType().getPidField());
                terser.set(String.format("/QPD-3(%d)-2", currentIndex), component.getValue());
                currentIndex++;
            }

        }

        if (patient.getGender() != null)
        {
            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.8");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), patient.getGender().toString());
            currentIndex++;
        }

        if (patient.getAddresses().size() > 0)
        {
            PersonAddress patientAddress = patient.getAddresses().get(0);

            for (AddressComponent component : patientAddress.getParts())
            {
                terser.set(String.format("/QPD-3(%d)-1", currentIndex), component.getType().getPidField());
                terser.set(String.format("/QPD-3(%d)-2", currentIndex), component.getValue());
                currentIndex++;
            }
        }

        if (patient.getDateOfBirth() != null)
        {
            terser.set(String.format("/QPD-3(%d)-1", currentIndex), "@PID.7.1");
            terser.set(String.format("/QPD-3(%d)-2", currentIndex), PixUtility.formatPatientDateOfBirth(patient.getDateOfBirth()));
            currentIndex++;
        }

    }

    /**
     * Field RCP-1-Query Priority shall always contain I, signifying that the
     * response to the query is to
     * be returned in Immediate mode.
     *
     * @throws HL7Exception
     */
    private void fillRCP() throws HL7Exception
    {

        qbp.getRCP().getRcp1_QueryPriority().setValue("I");
        qbp.getRCP().getRcp2_QuantityLimitedRequest().getCq1_Quantity().setValue(Integer.toString(quantity));
    }
}
