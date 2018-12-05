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

import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.segment.MSH;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheIdentification;

/**
 *
 * @author Nebri
 */
public abstract class V231Message extends PixMessage
{

    protected PersonDemographic patient;

    public PersonDemographic getPatient()
    {
        return patient;
    }

    public void setPatient(PersonDemographic patient)
    {
        this.patient = patient;
    }

    public V231Message(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient)
    {
        super(actor, identifier, localIdentification);
        this.patient = patient;
    }

    protected void fillStandardMsh(MSH msh) throws DataTypeException, HL7Exception
    {
        Date creationDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
        UUID myUUID = UUID.randomUUID();

        msh.getMsh1_FieldSeparator().setValue("|");
        msh.getMsh2_EncodingCharacters().setValue("^~\\&");
        msh.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(this.getLocalIdentification().getApplicationId());
        msh.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(this.getLocalIdentification().getFacilityName());
        msh.getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(pixActor.getRemoteIdentification().getApplicationId());
        msh.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(pixActor.getRemoteIdentification().getFacilityName());

        // Change to ts
        msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(ft.format(creationDate));

        msh.getMsh10_MessageControlID().setValue(myUUID.toString());
        msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("P");
        msh.getMsh12_VersionID().getVid1_VersionID().setValue("2.3.1");
    }
}
