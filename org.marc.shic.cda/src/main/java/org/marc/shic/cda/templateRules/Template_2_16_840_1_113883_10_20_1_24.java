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

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level3.Medication;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Medication Activity *
 *
 * @author brownp
 */
public class Template_2_16_840_1_113883_10_20_1_24 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_24() {

    }

    @Override
    public void apply(Template template) {
        Medication medication = (Medication) template;
        SubstanceAdministration root = medication.getRoot();
        
        //CdaUtils.addTemplateRuleID(root, TemplateRuleID.CCD_MedicationActivity);
        //TODO: Set proper MOOD code.
        root.setMoodCode(x_DocumentSubstanceMood.Eventoccurrence);
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_24(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_24 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_24(TemplateRuleID.CCD_MedicationActivity);
    }

    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, SubstanceAdministration substance) {
        //section.addTemplateRule(this);
        substance.getTemplateId().add(new II(this.getTemplateID().getOid()));

        // TODO: Change this to Intent
        substance.setMoodCode(x_DocumentSubstanceMood.Intent);

//        substance.setEffectiveTime(new ArrayList<ISetComponent<TS>>());
//        
//        substance.getEffectiveTime().add(CoreDataTypeHelpers.createIVL_TS(lowTime, highTime));
    }

    public boolean validate(DocumentTemplate document) {
        return testMedicationsTemplateID(document);
    }

    public boolean testMedicationsTemplateID(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.MedicationActivity);
    }

}
