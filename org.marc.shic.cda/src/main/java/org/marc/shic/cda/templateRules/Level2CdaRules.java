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

import org.marc.shic.cda.level1.DocumentTemplate;
import static org.marc.shic.cda.templateRules.Level2CdaRules.*;

/**
 *
 * @author Paul
 */
public class Level2CdaRules extends TemplateRules{
    public Level2CdaRules(){}
    
    public boolean hasLevel2Template(DocumentTemplate _document)
    {
        return testCONF_HP_4(_document);
    }
    
    /**
     *
     * @param _document
     * @param _detailedOutput
     * @return
     */
    @Override
    public boolean runValidation(DocumentTemplate _document)
    {
        // additional level 2 Rules for sections will be run here
        return super.runValidation(_document) && hasLevel2Template(_document) &&
                testCONF_HP_41(_document)  &&
                testCONF_HP_42(_document) &&
                testCONF_HP_43(_document) &&
                testCONF_HP_50(_document) &&
                testCONF_HP_51(_document) &&
                testCONF_HP_52(_document) &&
                testCONF_HP_58(_document);
    }
    
        
    public static boolean testCONF_HP_4(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_4);
    }
    
    public static boolean testCONF_HP_41(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_41);
    }
    
    
    public static boolean testCONF_HP_42(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_42);
    }
    
    
    public static boolean testCONF_HP_43(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_43_1) || 
               testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_43_2);
    }
      
    
    public static boolean testCONF_HP_50(DocumentTemplate document)
    {
        if(testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_1)) 
        {
            return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_2) || 
                   testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_3);
        } else
        {
            return true;
        }

    }
    
    
    public static boolean testCONF_HP_51(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_51);
    }
    
    
    public static boolean testCONF_HP_52(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_52);
    }
    
    
    public static boolean testCONF_HP_58(DocumentTemplate document)
    {
        if(testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_1)) 
        {
            return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_2) || 
                   testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_3);
        } else
        {
            return true;
        }
    }

    
    public static boolean testCONF_HP_63(DocumentTemplate document)
    {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_63);
    }
}
