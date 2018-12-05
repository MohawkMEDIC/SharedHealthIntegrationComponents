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

import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;
import java.util.Calendar;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level3.Concern;

/**
 * Concern Entry
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1(TemplateRuleID templateId) {
        super(templateId);
    }

    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1(TemplateRuleID.PCC_ConcernEntry);
    }

    /**
     * Applies the Concern Entry rule to the section template.
     *
     * @param template
     */
    @Override
    public void apply(Template template) {
//        template.addTemplateRule(TemplateRuleFactory.getRule("2.16.840.1.113883.10.20.1.27"));

        Concern concern = (Concern) template;
        Act concernAct = concern.getAct();

        CD<String> code = new CD<String>();
        code.setNullFlavor(NullFlavor.NotApplicable);
        concernAct.setCode(code);

        // statusCode must be active if only a low time is specified
        if (concern.getStartTime() != null && concern.getEndTime() == null) {
            concernAct.setStatusCode(ActStatus.Active);
        }

        // statusCode is either completed or aborted if the low AND high times are set - making it completed for now
        if (concern.getStartTime() != null && concern.getEndTime() != null) {
            concernAct.setStatusCode(ActStatus.Completed);
        }

        //Concern entries have an Act as a root. Add effective time to it.
        concernAct.setEffectiveTime(
                CoreDataTypeHelpers.createIVL_TS(concern.getStartTime(), concern.getEndTime(), null));
    }

    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, Act act, Calendar lowTime, Calendar highTime) {
        // apply parent CCD Template
        Template_2_16_840_1_113883_10_20_1_27.newInstance().populateWithRequiredDefaults(section, act, lowTime, highTime);

        section.getRoot().getTemplateId().add(getTemplateID().getII());
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

//    public void populateWithRequiredDefaults(SectionTemplate section, Act act)
//    {
//        act.getTemplateId().add(new II(this.getTemplateIDs().getOid()));
//        section.getTemplateRules().add(this);
//    }
}
