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
import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.util.Terser;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.pix.PatientDomainIdentifier;

/**
 *
 * @author Nebri
 */
public class QueryBasedParameter_Q23 extends QueryBasedParameter
{

    DomainIdentifier[] domainsReturned;

//    private String pidDomain;
    public QueryBasedParameter_Q23(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient, DomainIdentifier... domainsReturned) throws DataTypeException, HL7Exception
    {
        super(actor, identifier, localIdentification, patient);
        this.domainsReturned = domainsReturned;
        qbp = new QBP_Q21();

        fillMshQ23();
        fillQpdSegment();
        fillRCP();
    }

    private void fillMshQ23() throws HL7Exception
    {
        qbp.getMSH().getMsh9_MessageType().getMsg1_MessageCode().setValue("QBP");
        qbp.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue("Q23");
        qbp.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("QBP_Q21");

//        qbp.getMSH().getMsh13_SequenceNumber().setValue(" ");
//        qbp.getMSH().getMsh14_ContinuationPointer().setValue(" ");
//        qbp.getMSH().getMsh15_AcceptAcknowledgmentType().setValue(" ");
//        qbp.getMSH().getMsh16_ApplicationAcknowledgmentType().setValue(" ");
//        qbp.getMSH().getMsh17_CountryCode().setValue(" ");
//        qbp.getMSH().getMsh18_CharacterSet(0).setValue(" ");
        super.fillStandardMsh(qbp.getMSH());
    }

    private void fillQpdSegment() throws HL7Exception
    {
        Terser terser = new Terser(qbp);

        DomainIdentifier patientDomain = patient.getIdentifiers().get(0);

        terser.set("QPD-3-1", patientDomain.getExtension());
        terser.set("QPD-3-4-1", patientDomain.getAssigningAuthority());
        terser.set("QPD-3-4-2", patientDomain.getRoot());

        terser.set("QPD-3-4-3", "ISO");

        if (patientDomain instanceof PatientDomainIdentifier)
        {
            terser.set("QPD-3-5", ((PatientDomainIdentifier) patientDomain).getIdentifierTypeCode().toString());
        } else
        {
            // PI
            terser.set("QPD-3-5", "MR");
        }

        if (domainsReturned != null)
        {
            for (int i = 0; i < domainsReturned.length; i++)
            {
                terser.set(String.format("QPD-4(%d)-4-1", i), domainsReturned[i].getAssigningAuthority());
                terser.set(String.format("QPD-4(%d)-4-2", i), domainsReturned[i].getRoot());
                terser.set(String.format("QPD-4(%d)-4-3", i), "ISO");
            }
        }

//        terser.set("QPD-4-4-1", "NIST2010-3");
//        terser.set("QPD-4-4-2", "2.16.840.1.113883.3.72.5.9.3");
//        terser.set("QPD-4-4-3", "ISO");
        qbp.getQPD().getQpd1_MessageQueryName().getCe1_Identifier().setValue("IHE PIX Query");
        qbp.getQPD().getQpd2_QueryTag().setValue("Q231235421946");
    }

    private void fillRCP() throws HL7Exception
    {
        qbp.getRCP().getRcp1_QueryPriority().setValue("I");
    }
}
