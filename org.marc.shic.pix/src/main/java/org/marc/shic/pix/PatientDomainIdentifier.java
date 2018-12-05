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
 * Date: Jan 30 2014
 *
 */
package org.marc.shic.pix;

import org.marc.shic.core.DomainIdentifier;

/**
 *
 * @author Garrett
 */
public class PatientDomainIdentifier extends DomainIdentifier
{

    private PatientIdentifierTypeCode identifierTypeCode;

    public PatientIdentifierTypeCode getIdentifierTypeCode()
    {
        return identifierTypeCode;
    }

    public void setIdentifierTypeCode(PatientIdentifierTypeCode identifierTypeCode)
    {
        this.identifierTypeCode = identifierTypeCode;
    }

    public PatientDomainIdentifier(String root, String extension, String assigningAuthority, PatientIdentifierTypeCode identifierTypeCode)
    {
        super(root, extension, assigningAuthority);

        this.identifierTypeCode = identifierTypeCode;
    }
}
