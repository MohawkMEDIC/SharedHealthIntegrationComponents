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
 * @author Mohamed Ibrahim Date: 29-Nov-2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.everest.datatypes.II;
import org.marc.shic.cda.level1.BppcDocument;

/**
 *
 * @author ibrahimm
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1(TemplateRuleID.PCC_BppcWithScannedPart);
    }

    @Deprecated
    public void populateWithRequiredDefaults(BppcDocument document) {
//        document.addTemplateRule(this);
        document.getRoot().getTemplateId().add(new II(TemplateRuleID.PCC_BppcWithScannedPart.getOid()));
//        document.addTemplateRule(this);
    }

}
