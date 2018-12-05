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
 * @author brownp Date: Sep 18, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;
import java.util.Calendar;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.shic.cda.Template;

/**
 * Problem Concern Entry
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2(TemplateRuleID templateId) {
        super(templateId);
    }

    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2(TemplateRuleID.PCC_ProblemConcernEntry);
    }
    
    @Override
    public void apply(Template template) {
//        template.addTemplateRule(TemplateRuleFactory.getRule("1.3.6.1.4.1.19376.1.5.3.1.4.5.1"));
    }

//    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2 newInstance(SectionTemplate section, Act act, Calendar lowTime, Calendar highTime)
//    {
//        // apply parent templates
//        Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1 ConcernEntry = Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1.populateWithRequiredDefaults(section, act, lowTime, highTime);
//        
//        Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2 ProblemEntry = Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2.newInstance();
//        act.getTemplateId().add(new II(ProblemEntry.getTemplateIDs().getOid()));
//        
//        section.getTemplateRules().add(ProblemEntry);
//        
//        CD<String> code = new CD<String>();
//        code.setNullFlavor(NullFlavor.NotApplicable);
//        act.setCode(code);
//        
//        // statusCode must be active if only a low time is specified
//        if(lowTime != null && highTime == null)
//            act.setStatusCode(ActStatus.Active);
//        
//        // statusCode is either completed or aborted if the low AND high times are set - making it completed for now
//        if(lowTime != null && highTime != null)
//            act.setStatusCode(ActStatus.Completed);
//
//        act.setEffectiveTime(CoreDataTypeHelpers.createIVL_TS(lowTime, highTime));
//        
//        return ProblemEntry;
//    }
    /**
     * Problem Concern Entry
     *
     * @param document
     */
    public void populateWithRequiredDefaults(SectionTemplate section, Act act, Calendar lowTime, Calendar highTime) {
        // add in parent template
        Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1 concernEntryTemplate = Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1.newInstance();
        concernEntryTemplate.populateWithRequiredDefaults(section, act, lowTime, highTime);

        //section.getSection().getTemplateId().add(this.getTemplateID().getII());
        //section.addTemplateRule(this);

        CD<String> code = new CD<String>();
        code.setNullFlavor(NullFlavor.NotApplicable);
        act.setCode(code);

        // statusCode must be active if only a low time is specified
        if (lowTime != null && highTime == null) {
            act.setStatusCode(ActStatus.Active);
        }

        // statusCode is either completed or aborted if the low AND high times are set - making it completed for now
        if (lowTime != null && highTime != null) {
            act.setStatusCode(ActStatus.Completed);
        }

        act.setEffectiveTime(CoreDataTypeHelpers.createIVL_TS(lowTime, highTime, null));
    }

    public boolean validate(DocumentTemplate _document) {
        return testProblemConcernEntryTemplateId(_document);
    }

    public boolean testProblemConcernEntryTemplateId(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.ProblemConcernEntryTemplateId);
    }

}
