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
 * @author brownp Date: Sep 23, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ManufacturedProduct;

/**
 * Product Entry
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2() {
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2(TemplateRuleID.PCC_ProductEntry);
    }

    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, ManufacturedProduct manufacturedProduct) {
//        section.addTemplateRule(this);
        manufacturedProduct.setTemplateId(new SET<II>());

        Template_2_16_840_1_113883_10_20_1_53 CDA_ProductEntry = Template_2_16_840_1_113883_10_20_1_53.newInstance();
        CDA_ProductEntry.populateWithRequiredDefaults(section, manufacturedProduct);

        manufacturedProduct.getTemplateId().add(new II(this.getTemplateID().getOid()));
    }
}
