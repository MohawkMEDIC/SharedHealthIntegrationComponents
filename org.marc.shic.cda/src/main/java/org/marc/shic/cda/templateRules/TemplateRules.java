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

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.utils.CdaUtils;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Paul
 */
public class TemplateRules {

    public ArrayList<TemplateRules> rules;
    private static final Logger LOGGER = Logger.getLogger(TemplateRules.class);

    public TemplateRules() {
    }

    public boolean runValidation(DocumentTemplate _document) {
        return testSingleConformanceRule(_document, ConformanceRuleParts.NormalDosing);
    }

    public static boolean testSingleConformanceRule(DocumentTemplate document, ConformanceRuleParts conformanceRule) {
        boolean validated = true;

        if (CdaUtils.testXpathValue(document, conformanceRule)) {
            LOGGER.debug("Xpath Validates\n***************************\n");
        } else {
            LOGGER.debug("Xpath FAILED Validation\n***************************\n");
            validated = false;
        }

        return validated;
    }

}
