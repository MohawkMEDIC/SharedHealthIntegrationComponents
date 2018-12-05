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

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;

/**
 *
 * @author Paul
 */
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;
import java.util.Calendar;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * CCD Template
 *
 * @author brownp
 */
public class Template_2_16_840_1_113883_10_20_1_27 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_1_27() {
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_1_27(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_1_27 newInstance() {
        return new Template_2_16_840_1_113883_10_20_1_27(TemplateRuleID.CCD_ProblemAct);
    }

    @Override
    public void apply(Template temp) {
        //TODO: Fill in 2_16_840_1_113883_10_20_1_27 rules here.
    }

    /**
     * This does not apply to this template rule.
     *
     * @param section
     * @param act
     * @param lowTime
     * @param highTime
     * @deprecated
     */
    @Deprecated
    public void populateWithRequiredDefaults(SectionTemplate section, Act act, Calendar lowTime, Calendar highTime) {
        CdaUtils.addTemplateRuleID(section.getRoot(), this.getTemplateID());
        //section.getRoot().getTemplateId().add(this.getTemplateIDs().getII());

        act.getTemplateId().add(new II(this.getTemplateID().getOid()));

//        template.populateWithRequiredDefaults(section, act);
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
