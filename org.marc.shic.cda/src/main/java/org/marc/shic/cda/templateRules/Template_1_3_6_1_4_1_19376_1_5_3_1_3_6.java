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
import java.util.HashSet;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.level2.ActiveProblemsSection;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Active Problems Section
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_6 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_6() {
        addDependency(TemplateRuleID.CCD_ProblemsSection);
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_6(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_6 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_3_6(TemplateRuleID.PCC_ActiveProblemsSection);
    }
    
    @Override
    public void apply(Template template) {
        Section section = ((ActiveProblemsSection)template).getRoot();
        section.setTitle("Active Problems Section");
    }

    /**
     *
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document, SectionTemplate section) {
        CdaUtils.addTemplateRuleID(document.getRoot(), this.getTemplateID());
        //document.addTemplateRule(this);
        //section.getSection().setTemplateId(new ArrayList<II>());
        //section.getSection().setTitle("Active Problems Section");

        //Section parent template from CCD 3.5 -> 2.16.840.1.113883.10.20.1.11
        Template_2_16_840_1_113883_10_20_1_11 CCD_3_5 = Template_2_16_840_1_113883_10_20_1_11.newInstance();
        CCD_3_5.populateWithRequiredDefaults(document, section);

        //section.getSection().getTemplateId().add(new II(this.getTemplateID().getOid()));
        //section.getSection().setCode(new CE<String>(LOINC_Codes.Problems.getCode(), LOINC_Codes.Problems.getCodeSystem(), LOINC_Codes.Problems.getCodeSystemName(), null, "Active Problems", null));
    }

    public boolean validate(DocumentTemplate _document, boolean _detailedOutput) {
        return testActiveProblemsTemplateId(_document)
                && testActiveProblemCode(_document)
                && testActiveProblemCodeSystem(_document);
    }

    public boolean testActiveProblemsTemplateId(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.ActiveProblemTemplateId);
    }

    public boolean testActiveProblemCode(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.ActiveProblemCode);
    }

    public boolean testActiveProblemCodeSystem(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.ActiveProblemCodeSystem);
    }

}
