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
package org.marc.shic.pix.messages;

import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.pix.PixUtility;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.segment.PID;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.pix.PatientDomainIdentifier;

/**
 *
 * @author Nebri
 */
public abstract class AdmitDischargeTransfer extends V231Message
{

    protected PV1Class pv1Class;
    protected static final String MESSAGE_TYPE = "ADT";

    public AdmitDischargeTransfer(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient)
    {
        super(actor, identifier, localIdentification, patient);
    }

    protected void fillPidSegment(PID pidSegment) throws DataTypeException, HL7Exception
    {
        PixUtility.populatePidWithNames(pidSegment, patient.getNames());
        PixUtility.populatePidWithAddresses(pidSegment, patient.getAddresses());

        // Gender
        pidSegment.getSex().setValue(patient.getGender().toString());

        // Date of birth
        pidSegment.getDateTimeOfBirth().getTimeOfAnEvent().setValue(PixUtility.formatPatientDateOfBirth(patient.getDateOfBirth()));

        //   pidSegment.getPid6_MotherSMaidenName(0).getXpn1_FamilyLastName().getFn1_FamilyName().setValue("\"\"");
        //pidSegment.getPid6_MotherSMaidenName(0).getXpn7_NameTypeCode().setValue("S");

        // Patient's identifiers.
        for (int i = 0; i < patient.getIdentifiers().size(); i++)
        {
            DomainIdentifier id = patient.getIdentifiers().get(i);

            pidSegment.getPatientIdentifierList(i).getID().setValue(id.getExtension());

            if (id instanceof PatientDomainIdentifier)
            {
                pidSegment.getPatientIdentifierList(i).getIdentifierTypeCode().setValue(((PatientDomainIdentifier) id).getIdentifierTypeCode().toString());
            } else
            {
                // PI
                pidSegment.getPatientIdentifierList(i).getIdentifierTypeCode().setValue("MR");
            }

            pidSegment.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().setValue(id.getAssigningAuthority());

            if (id.getRoot() != null && id.getRoot().length() > 0)
            {
                pidSegment.getPatientIdentifierList(i).getAssigningAuthority().getHd2_UniversalID().setValue(id.getRoot());
                pidSegment.getPatientIdentifierList(i).getAssigningAuthority().getHd3_UniversalIDType().setValue("ISO");
            }
        }
    }
}
