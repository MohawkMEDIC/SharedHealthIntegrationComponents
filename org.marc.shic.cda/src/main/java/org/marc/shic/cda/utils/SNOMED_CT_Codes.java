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
 * @author brownp Date: Sep 10, 2013
 *
 */
package org.marc.shic.cda.utils;

import org.marc.everest.datatypes.generic.CE;
import org.marc.shic.core.CodeValue;

public enum SNOMED_CT_Codes implements ICodeable {

    // Used for Observations in Problem Entry
    DISEASE("64572001", "Disease"),
    SYMPTOM("418799008", "Symptom"),
    FINDING("404684003", "Finding"),
    COMPLAINT("409586006", "Complaint"),
    FUNCTIONAL_LIMITATION("248536006", "Functional limitation"),
    PROBLEM("55607006", "Problem"),
    DIAGNOSIS("282291009", "Diagnosis"),
    DRUG_OR_MEDICAMENT("410942007", "Drug or Medicament"),
    ALLERGY("413477004", "Allergen or Pseudoallergen"),
    CONSENT_GIVEN_FOR_EHR_SHARING("425691002", "Consent Given for Electronic Record Sharing");

    protected String code;
    protected String displayName;
    protected CodeValue codeValue;
    protected CE<String> codedElement;
    public static final String codeSystem = "2.16.840.1.113883.6.96";
    public static final String codeSystemName = "SNOMED_CT";

    /**
     * Builds the Enum object
     *
     * @param _OID
     */
    private SNOMED_CT_Codes(String _code, String _description) {
        this.code = _code;
        this.displayName = _description;
        this.codeValue = new CodeValue(code, codeSystem, displayName, codeSystemName);
        this.codedElement = (CE<String>) CoreDataTypeHelpers.createCodedElement(codeValue);
    }

    @Override
    public CodeValue getCodeValue() {
        return codeValue;
    }

    @Override
    public String getCode() {
        return this.code; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDisplayName() {
        return this.displayName; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCodeSystem() {
        return codeSystem; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    @Override
    public CE getCodedElement() {
        return codedElement;
    }

}
