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
 * @author Mohamed Ibrahim
 * Date: 02-Dec-2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.DataTypeHelpers;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.PersonDemographic;

/**
 *
 * @author ibrahimm
 */
public class Template_1_3_6_1_4_1_19376_1_2_20_1 extends AbstractTemplateRule {
    
    public Template_1_3_6_1_4_1_19376_1_2_20_1() {
    }

    protected Template_1_3_6_1_4_1_19376_1_2_20_1(TemplateRuleID templateId) {
        super(templateId);
    }
    
    public static Template_1_3_6_1_4_1_19376_1_2_20_1 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_2_20_1(TemplateRuleID.PCC_OriginalAuthor);
    }
    
    @Override
    public void apply(Template template) {
        Author author = ((Template<Author>)template).getRoot();
        author.setContextControlCode(ContextControl.OverridingPropagating);
        CdaUtils.addTemplateRuleID(author.getAssignedAuthor(), getTemplateID().getII());
    }
    
    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document, PersonDemographic author, LocationDemographic location)
    {
//        document.addTemplateRule(this);
        
        Author originalAuthor = DataTypeHelpers.createAuthor(author, location, document.getRoot().getEffectiveTime(), TemplateRuleID.PCC_OriginalAuthor.getOid());
        originalAuthor.setContextControlCode(ContextControl.OverridingPropagating);
        document.getRoot().getAuthor().add(originalAuthor);
    }
    
}
