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
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level3.AllergyIntolerance;

/**
 * Allergies and Intolerances Concern - for ACT
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3 extends AbstractTemplateRule {

    private static final Logger LOGGER = Logger.getLogger(Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3.class);

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3(TemplateRuleID templateId) {
        super(templateId);
    }

    public static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3(TemplateRuleID.PCC_AllergyIntoleranceConcern);
    }

    /**
     * Applied to a single AllergyIntolerance.
     * @param template 
     */
    @Override
    public void apply(Template template) {
//        template.addTemplateRule(Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1.newInstance());

        AllergyIntolerance allergy = (AllergyIntolerance) template;
        Act allergyAct = allergy.getAct();

        CD<String> code = new CD<String>();
        code.setNullFlavor(NullFlavor.NotApplicable);
        allergyAct.setCode(code);

        // statusCode must be active if only a low time is specified
        if (allergy.getStartTime() != null && allergy.getEndTime() == null) {
            allergyAct.setStatusCode(ActStatus.Active);
        }

        // statusCode is either completed or aborted if the low AND high times are set - making it completed for now
        if (allergy.getStartTime() != null && allergy.getEndTime() != null) {
            allergyAct.setStatusCode(ActStatus.Completed);
        }

        allergyAct.setEffectiveTime(CoreDataTypeHelpers.createIVL_TS(allergy.getStartTime(), allergy.getEndTime(), null));
    }

    public void populateWithRequiredDefaults(SectionTemplate section, Act act, Calendar lowTime, Calendar highTime) {
        // apply parent templates
        Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1.newInstance().populateWithRequiredDefaults(section, act, lowTime, highTime);

        section.getRoot().getTemplateId().add(this.getTemplateID().getII());
        //section.addTemplateRule(this);

        CD<String> code = new CD<String>();
        code.setNullFlavor(NullFlavor.NotApplicable);
        act.setCode(code);

        //LOGGER.info(this.getTemplateIDs().getOid());
        act.getTemplateId().add(this.getTemplateID().getII());

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

    public void populateWithRequiredDefaults(SectionTemplate section, Act act) {
        section.getRoot().getTemplateId().add(this.getTemplateID().getII());
        //section.addTemplateRule(this);
    }
}
