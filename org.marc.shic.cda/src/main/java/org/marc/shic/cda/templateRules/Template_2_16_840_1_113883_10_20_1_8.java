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

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.level2.MedicationSection;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 * CCD 3.9
 *
 * @author brownp
 */
public class Template_2_16_840_1_113883_10_20_1_8 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_8() {
    }
    
    @Override
    public void apply(Template template) {
        MedicationSection section = (MedicationSection) template;
        Section root = section.getRoot();
        
        CdaUtils.addTemplateRuleID(root, getTemplateID());
        root.setCode(LOINC_Codes.HistoryOfMedicationUse.getCodedElement());
        
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_8(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_8 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_8(TemplateRuleID.CCD_MedicationsSection);
    }

    /**
     * Populates the CDA Document with required and default header information
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section) {
        //section.addTemplateRule(this);
    }

    public boolean validate(DocumentTemplate document) {
        return testCCD3_9OID(document);
    }

    public boolean testCCD3_9OID(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CCD_3_9_TemplateId);
    }

}
