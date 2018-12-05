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
 * @author Ryan Albert 
 * @since 27-Jan-2014
 *
 */

package org.marc.shic.cda.templateRules;

import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Vital Signs Section
 * 1.3.6.1.4.1.19376.1.5.3.1.3.25
 * @author Ryan Albert
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_25 extends AbstractTemplateRule {

    public Template_1_3_6_1_4_1_19376_1_5_3_1_3_25() {
        addDependency(TemplateRuleID.CCD_VitalSignsSection);
    }
    
    @Override
    public void apply(Template template) {
        CdaUtils.addTemplateRuleID((InfrastructureRoot)template.getRoot(), TemplateRuleID.PCC_VitalSignsSection);
    }
}
