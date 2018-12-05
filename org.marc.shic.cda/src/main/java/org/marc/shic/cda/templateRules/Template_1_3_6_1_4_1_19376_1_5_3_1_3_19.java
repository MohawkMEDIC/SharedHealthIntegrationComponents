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
 * @author brownp Date: Sep 19, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.LOINC_Codes;
import org.marc.everest.datatypes.generic.CE;

/**
 * Medications Section
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_19 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_19() {
        addDependency(TemplateRuleID.CCD_MedicationsSection);
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_19(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_19 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_3_19(TemplateRuleID.PCC_MedicationsSection);
    }

    /**
     * Populates the CDA Document with required and default header information
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section) {
//        section.addTemplateRule(this);

        Template_2_16_840_1_113883_10_20_1_8.newInstance().populateWithRequiredDefaults(section);

        //section.getSection().setTitle("Medication List");

        /*section.getSection().setCode(new CE<String>(LOINC_Codes.HistoryOfMedicationUse.getCode(),
                LOINC_Codes.HistoryOfMedicationUse.getCodeSystem(),
                LOINC_Codes.HistoryOfMedicationUse.getCodeSystemName(),
                null,
                LOINC_Codes.HistoryOfMedicationUse.getDisplayName(),
                null));*/
    }

    public boolean validate(DocumentTemplate document) {
        return testMedicationSectionOID(document);
    }

    public boolean testMedicationSectionOID(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.MedicationSectionTemplateId);
    }

}
