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
 * Date: Nov 14, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.level2.ImmunizationsSection;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 *
 * CCD 3.11
 */
public class Template_2_16_840_1_113883_10_20_1_6 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_6() {
    }

    @Override
    public void apply(Template template) {
        ImmunizationsSection immunizations = (ImmunizationsSection) template;
        Section root = immunizations.getRoot();

        CdaUtils.addTemplateRuleID(root, getTemplateID());

        root.setCode(LOINC_Codes.HistoryOfImmunizations.getCodedElement());
        
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_6(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_6 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_6(TemplateRuleID.CCD_ImmunizationsSection);
    }

    /**
     *
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section) {
        //section.addTemplateRule(this);
    }

    public boolean validate(DocumentTemplate _document, boolean _detailedOutput) {
        // TODO: add validation rules

        return true;
    }
}
