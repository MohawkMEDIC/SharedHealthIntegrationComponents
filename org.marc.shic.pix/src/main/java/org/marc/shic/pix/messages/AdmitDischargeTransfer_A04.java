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

import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v231.message.ADT_A04;
import ca.uhn.hl7v2.model.v231.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.configuration.IheIdentification;

/**
 *
 * @author clarkc
 */
public class AdmitDischargeTransfer_A04 extends AdmitDischargeTransfer {

    private final ADT_A04 adt;
    private static final String TRIGGER_EVENT = "A04";
    private static final String MESSAGE_STRUCTURE = "ADT_A01";

    
    public AdmitDischargeTransfer_A04(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification, PersonDemographic patient, PV1Class pv1Class) throws HL7Exception {
        super(actor, identifier, localIdentification, patient);

        this.adt = new ADT_A04();
        this.pv1Class = pv1Class;
        
        fillMshA04();
        fillEvnSegment();        
        super.fillPidSegment(adt.getPID());
        fillPV1Segment();
    }

    private void fillMshA04() throws HL7Exception {
        adt.getMSH().getMsh9_MessageType().getMsg1_MessageType().setValue(MESSAGE_TYPE);
        adt.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue(TRIGGER_EVENT);
        adt.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue(MESSAGE_STRUCTURE);
        super.fillStandardMsh(adt.getMSH());
    }

    public ADT_A04 getAdt() {
        return adt;
    }

    private void fillEvnSegment() throws HL7Exception {
        Calendar tempRecordedTime = Calendar.getInstance();
        Date recordedTime;
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");

        tempRecordedTime.set(2013, 1, 18);
        recordedTime = tempRecordedTime.getTime();

        Terser terser = new Terser(adt);
        terser.set("EVN-2", ft.format(recordedTime));
        terser.set("EVN-6", ft.format(recordedTime));
    }

    private void fillPV1Segment() throws HL7Exception {
        Terser terser = new Terser(adt);
        //terser.set("PV1-1", "1");
        terser.set("PV1-2", pv1Class.toString());
    }

    @Override
    public String encodeMessage() throws HL7Exception {
        Parser parser = new PipeParser();
        String encodedMessage = parser.encode(adt);
        return encodedMessage;
    }
}
