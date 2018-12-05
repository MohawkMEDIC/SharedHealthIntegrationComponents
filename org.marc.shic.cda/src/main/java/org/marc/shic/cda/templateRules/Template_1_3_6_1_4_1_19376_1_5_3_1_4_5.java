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
 * Date: October 29, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.level2.AllergiesAndDrugSensitiviesSection;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level3.Concern;

/**
 * CCD Problem Concern Entry - Problem Entry
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_5 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5(TemplateRuleID templateId) {
        super(templateId);
    }

    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_5(TemplateRuleID.PCC_ProblemEntry);
    }
    
    @Override
    public void apply(Template template) {
//        template.addTemplateRule(TemplateRuleFactory.getRule("2.16.840.1.113883.10.20.1.28"));
        
        Observation observation = (Observation) template.getRoot();
        observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
        observation.setStatusCode(ActStatus.Completed);
    }

    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, ClinicalStatement clinicalStatement) {

        // add alert OID
        if (section instanceof AllergiesAndDrugSensitiviesSection) {
            Template_2_16_840_1_113883_10_20_1_18 alertObservation = Template_2_16_840_1_113883_10_20_1_18.newInstance();
            alertObservation.populateWithRequiredDefaults(section, clinicalStatement);
        }

        // Parent Template
        Template_2_16_840_1_113883_10_20_1_28 IHE_ProblemConcern = Template_2_16_840_1_113883_10_20_1_28.newInstance();
        IHE_ProblemConcern.populateWithRequiredDefaults(section, clinicalStatement);

        clinicalStatement.getTemplateId().add(new II(this.getTemplateID().getOid()));
        ((Observation) clinicalStatement).setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
        ((Observation) clinicalStatement).setStatusCode(ActStatus.Completed);

        section.getRoot().getTemplateId().add(this.getTemplateID().getII());
        //section.addTemplateRule(this);
    }

    @Override
    public boolean validate(DocumentTemplate documentTemplate) {
        return testObservationClassCode(documentTemplate)
                && testObservationMoodCode(documentTemplate);
    }

    public boolean testObservationClassCode(DocumentTemplate documentTemplate) {
        return testSingleConformanceRule(documentTemplate, ConformanceRuleParts.IHE_ProblemEntryClassCode);
    }

    public boolean testObservationMoodCode(DocumentTemplate documentTemplate) {
        return testSingleConformanceRule(documentTemplate, ConformanceRuleParts.IHE_ProblemEntryMoodCode);
    }
}
