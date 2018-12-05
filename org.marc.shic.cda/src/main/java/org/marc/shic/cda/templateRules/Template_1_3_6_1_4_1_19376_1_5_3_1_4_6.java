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
 * Allergies and Intolerances Entry - for entryRelationship/Observation
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_6 extends AbstractTemplateRule{
        
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_6(){}
    
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_6(TemplateRuleID templateId)
    {
        super(templateId);
    }
    
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_6 newInstance()
    {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_6(TemplateRuleID.PCC_AllergyIntolerance);
    }
    
    @Override
    public void apply(Template template) {
//        template.addTemplateRule(Template_1_3_6_1_4_1_19376_1_5_3_1_4_5.newInstance());
        
        
    }
    
    public void populateWithRequiredDefaults(SectionTemplate section, ClinicalStatement clinicalStatement)
    {
        // Parent Template
        Template_1_3_6_1_4_1_19376_1_5_3_1_4_5 CCD_ProblemConcernEntry = Template_1_3_6_1_4_1_19376_1_5_3_1_4_5.newInstance();
        
//        if(clinicalStatement instanceof Observation)
//            ((Observation)clinicalStatement).setValue(new CD("Some Value in Template 146"));
        
        CCD_ProblemConcernEntry.populateWithRequiredDefaults(section, clinicalStatement);
        
        clinicalStatement.getTemplateId().add(new II(this.getTemplateID().getOid()));
        ((Observation) clinicalStatement).setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);

        section.getRoot().getTemplateId().add(this.getTemplateID().getII());
        //section.addTemplateRule(this);
    }
}
