/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.marc.shic.cda.templateRules;

import java.util.ArrayList;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Immunizations Section
 *
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_3_23 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_23() {
        addDependency(TemplateRuleID.CCD_ImmunizationsSection);
    }
    
    @Override
    public void apply(Template template) {
        CdaUtils.addTemplateRuleID((InfrastructureRoot)template.getRoot(), getTemplateID());
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_3_23(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_3_23 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_3_23(TemplateRuleID.PCC_ImmunizationsSection);
    }

    /**
     *
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document, SectionTemplate section) {
//        document.addTemplateRule(this);
        //section.getSection().setTemplateId(new ArrayList<II>());
        //section.getSection().setTitle("Immunizations Section");

        Template_2_16_840_1_113883_10_20_1_6 CCD_3_11 = Template_2_16_840_1_113883_10_20_1_6.newInstance();
        CCD_3_11.populateWithRequiredDefaults(section);

        //section.getSection().getTemplateId().add(new II(this.getTemplateID().getOid()));
    }

    @Deprecated
    public boolean validate(DocumentTemplate _document, boolean _detailedOutput) {
        // TODO: add validation rules

        return true;
    }
}
