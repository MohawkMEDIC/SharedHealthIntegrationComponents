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
 * @author brownp Date: Nov 13, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CdaUtils;

/**
 *
 * Alert Observation
 */
public class Template_2_16_840_1_113883_10_20_1_18 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_18() {
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_18(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_18 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_18(TemplateRuleID.CCD_AlertObservation);
    }

    /**
     *
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, ClinicalStatement clinicalStatement) {
        clinicalStatement.getTemplateId().add(new II(this.getTemplateID().getOid()));

        CdaUtils.addTemplateRuleID(section.getRoot(), getTemplateID());
        //section.addTemplateRule(this);
    }

    public boolean validate(DocumentTemplate _document) {
        return testAlertObservationTemplateId(_document);
    }

    public boolean testAlertObservationTemplateId(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.AlertObservation);
    }
}
