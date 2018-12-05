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

public enum LOINC_Codes implements ICodeable {

    Allergies_AdverseReactions_Alerts("48765-2", "Allergies, adverse reactions, alerts"),
    PhrDocumentCode("34133-9", "Summarization of episode note"),
    GeneralCDA("", "General CDA"),
    HistoryOfPastIllness("11348-0", "History of Past Illness"),
    Problems("11450-4", "Problem list"),
    HistoryOfMedicationUse("10160-0", "History of Medication Use"),
    SummaryPurpose("48764-5", "Summary Purpose"),
    HistoryOfImmunizations("11369-6", "History of Immunizations"),
    FamilyHistory("10157-6", "Family History"),
    VitalSigns("8716-3", "Vital signs"),
    PlanOfCare("18776-5", "Plan Of Care"),
    SocialHistory("29762-2", "Social History");

    protected String code;
    protected String displayName;
    protected CodeValue codeValue;
    protected CE<String> codedElement;
    protected static final String codeSystem = "2.16.840.1.113883.6.1";
    protected static final String codeSystemName = "LOINC";

    /**
     * Builds the Enum object
     *
     * @param _OID
     */
    private LOINC_Codes(String _code, String _description) {
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
    public CE<String> getCodedElement() {
        return codedElement;
    }

}
