/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.level2.FamilyMedicalHistorySection;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 * CCD Family History
 * @author Paul
 */
public class Template_2_16_840_1_113883_10_20_1_4  extends AbstractTemplateRule{
    
    protected Template_2_16_840_1_113883_10_20_1_4(){}
    
    protected Template_2_16_840_1_113883_10_20_1_4(TemplateRuleID templateId)
    {
        super(templateId);
    }
    
    public static Template_2_16_840_1_113883_10_20_1_4 newInstance()
    {
        return new Template_2_16_840_1_113883_10_20_1_4(TemplateRuleID.CCD_FamilyHistorySection);
    }
    
    public static Template_2_16_840_1_113883_10_20_1_4 populateWithRequiredDefaults(FamilyMedicalHistorySection section)
    {
        Template_2_16_840_1_113883_10_20_1_4 template = Template_2_16_840_1_113883_10_20_1_4.newInstance();
//        section.addTemplateRule(template);
        
        /*section.setCode(Code.fromStrings(LOINC_Codes.FamilyHistory.getCode(),
                                        LOINC_Codes.FamilyHistory.getCodeSystem(),
                                        LOINC_Codes.FamilyHistory.getDisplayName(),
                                        LOINC_Codes.FamilyHistory.getCodeSystemName()));*/
        
        //section.getSection().setTitle("Family History");
        
        return template;
    }
}
