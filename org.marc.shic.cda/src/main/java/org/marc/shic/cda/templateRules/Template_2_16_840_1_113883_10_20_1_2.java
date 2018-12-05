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
import org.marc.shic.cda.Template;

/**
 * CCD 3.8
 * @author Paul
 */
public class Template_2_16_840_1_113883_10_20_1_2  extends AbstractTemplateRule{
    
    protected Template_2_16_840_1_113883_10_20_1_2(){}
    
    protected Template_2_16_840_1_113883_10_20_1_2(TemplateRuleID templateId)
    {
        super(templateId);
    }
    
    public static Template_2_16_840_1_113883_10_20_1_2 newInstance()
    {
        return new Template_2_16_840_1_113883_10_20_1_2(TemplateRuleID.CCD_AlertsSection);
    }
    
    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_2 populateWithRequiredDefaults(SectionTemplate section)
    {
        // 2.16.840.1.113883.10.20.1.2 is the parent of this template        
        Template_2_16_840_1_113883_10_20_1_2 template = Template_2_16_840_1_113883_10_20_1_2.newInstance();
        section.getRoot().getTemplateId().add(Template_2_16_840_1_113883_10_20_1_2.newInstance().getTemplateID().getII());
        //section.addTemplateRule(Template_2_16_840_1_113883_10_20_1_2.newInstance());
        
        return template;
    }

}
