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

import java.util.HashSet;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;
import org.marc.shic.cda.utils.LOINC_Codes;

public class Template_2_16_840_1_113883_10_20_1_11 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_11() {
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_11(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_11 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_11(TemplateRuleID.CCD_ProblemsSection);
    }
    
    @Override
    public void apply(Template template) {
        SectionTemplate section = (SectionTemplate) template;
        Section root = section.getRoot();
        CdaUtils.addTemplateRuleID(root, getTemplateID());
        root.setCode((CE<String>)CoreDataTypeHelpers.createCodedElement(LOINC_Codes.Problems.getCodeValue()));
        root.setTitle("Problems");
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
        //section.getSection().getTemplateId().add(new II(this.getTemplateID().getOid()));

    }

    public boolean validate(DocumentTemplate _document) {
        return testCCD_3_5_TemplateId(_document);
    }

    public boolean testCCD_3_5_TemplateId(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.CCD_3_5_TemplateId);
    }

}
