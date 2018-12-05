/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
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

import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.Template;

/**
 * IHE Problem Concern Entry - Problem Observation
 * @author Paul
 */
public class Template_2_16_840_1_113883_10_20_1_28 extends AbstractTemplateRule{
    
    protected Template_2_16_840_1_113883_10_20_1_28(){}
    
    protected Template_2_16_840_1_113883_10_20_1_28(TemplateRuleID templateId)
    {
        super(templateId);
    }
    
    public static Template_2_16_840_1_113883_10_20_1_28 newInstance()
    {
        return new Template_2_16_840_1_113883_10_20_1_28(TemplateRuleID.CCD_ProblemObservation);
    }
    
    @Override
    public void apply(Template template) {
        ((Observation) template.getRoot()).setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
    }
    
    public void populateWithRequiredDefaults(SectionTemplate section, ClinicalStatement clinicalStatement)
    {
        clinicalStatement.getTemplateId().add(new II(this.getTemplateID().getOid()));
        ((Observation) clinicalStatement).setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);

        section.getRoot().getTemplateId().add(getTemplateID().getII());
        //section.addTemplateRule(this);
    }
    
}
