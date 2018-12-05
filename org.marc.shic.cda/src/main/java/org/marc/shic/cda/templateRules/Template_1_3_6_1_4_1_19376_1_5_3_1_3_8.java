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
 * @author brownp Date: Sep 18, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.LOINC_Codes;
import java.util.ArrayList;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.Template;

/**
 * History of Past Illness
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_8 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_8() {
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_8(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_8 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_3_8(TemplateRuleID.PCC_HistoryOfPastIllnessSection);
    }
    
    @Override
    public void apply(Template template) {
        ((Section)template.getRoot()).setCode(LOINC_Codes.HistoryOfPastIllness.getCodedElement());
    }

    /**
     *
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document, SectionTemplate section) {
//        document.addTemplateRule(this);

        //section.getSection().setTemplateId(new ArrayList<II>());
        //section.getSection().getTemplateId().add(new II(this.getTemplateID().getOid()));
        //section.getSection().setTitle("History of Past Illness Section");
        //section.getSection().setCode(new CE<String>(LOINC_Codes.HistoryOfPastIllness.getCode(), LOINC_Codes.HistoryOfPastIllness.getCodeSystem(), LOINC_Codes.HistoryOfPastIllness.getCodeSystemName(), null, "History of Past Illness Section", null));
    }

    public boolean validate(DocumentTemplate _document, boolean _detailedOutput) {
        return testHistoryOfPastIllnessProblemsTemplateId(_document)
                && testHistoryOfPastIllnessProblemCode(_document)
                && testHistoryOfPastIllnessCodeSystem(_document);
    }

    public boolean testHistoryOfPastIllnessProblemsTemplateId(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.HistoryOfPastIllnessTemplateId);
    }

    public boolean testHistoryOfPastIllnessProblemCode(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.HistoryOfPastIllnessProblemCode);
    }

    public boolean testHistoryOfPastIllnessCodeSystem(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.HistoryOfPastIllnessProblemCodeSystem);
    }

}
