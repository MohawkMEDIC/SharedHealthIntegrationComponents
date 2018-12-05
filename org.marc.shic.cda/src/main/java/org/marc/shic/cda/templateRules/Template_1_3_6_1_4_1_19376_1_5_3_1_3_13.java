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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import static org.marc.shic.cda.templateRules.TemplateRules.testSingleConformanceRule;
import org.marc.shic.cda.utils.LOINC_Codes;
import org.marc.everest.datatypes.generic.CE;
import org.marc.shic.cda.Template;

/**
 * Allergies and Drug Sensitivities
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_13 extends AbstractTemplateRule {

    @Deprecated
    private static CE<String> CODE = new CE<String>(LOINC_Codes.Allergies_AdverseReactions_Alerts.getCode(), LOINC_Codes.Allergies_AdverseReactions_Alerts.getCodeSystem());

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_13() {
        addDependency(TemplateRuleID.CCD_AlertsSection);
    }
    
    @Override
    public void apply(Template template) {
        SectionTemplate section = (SectionTemplate) template;
        section.getRoot().setCode(LOINC_Codes.Allergies_AdverseReactions_Alerts.getCodedElement());
        section.getRoot().setTitle("Allergies and Intolerances");
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_13(TemplateRuleID templateId) {
        super(templateId);
        addDependency(TemplateRuleID.CCD_MedicationActivity);
        addDependency(TemplateRuleID.CCD_ProblemsSection);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_13 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_3_13(TemplateRuleID.PCC_AllergiesReactionsSection);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_13 populateWithRequiredDefaults(SectionTemplate section) {
        // 2.16.840.1.113883.10.20.1.2 is the parent of this template
        
        Template_2_16_840_1_113883_10_20_1_2.populateWithRequiredDefaults(section);
        
        Template_1_3_6_1_4_1_19376_1_5_3_1_3_13 template = Template_1_3_6_1_4_1_19376_1_5_3_1_3_13.newInstance();
        section.getRoot().getTemplateId().add(template.getTemplateID().getII());

        //section.getSection().setCode(CODE);
        //section.getSection().setTitle("Allergies and Intolerances");

        return template;
    }

    @Deprecated
    public void populateWithRequiredDefaults() {

    }

    // RULES VALIDATION
    /**
     *
     * @param _document
     * @param _detailedOutput
     * @return
     */
    public boolean runValidation(DocumentTemplate _document ) {
        return testAllergiesSection_OID(_document);
    }

    public boolean testAllergiesSection_OID(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.AllergiesSection_OID);
    }
}
