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
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import org.marc.shic.core.configuration.IheIdentification;

/**
 *
 * @author Nebri
 */
public abstract class QueryBasedParameter extends V25Message {

    protected PersonDemographic patient;
    protected QBP_Q21 qbp;

    public QBP_Q21 getQbp()
    {
        return qbp;
    }

    public QueryBasedParameter(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification,  PersonDemographic patient) {
        super(actor, identifier, localIdentification);
        this.patient = patient;
    }

    public void setPatient(PersonDemographic patient) {
        this.patient = patient;
    }

    public PersonDemographic getPatient() {
        return patient;
    }

    @Override
    public String encodeMessage() throws HL7Exception {
        Parser parser = new PipeParser();
        String encodedMessage = parser.encode(qbp);
        return encodedMessage;
    }
}
