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
import org.marc.shic.cda.level1.Level2Document;
import org.marc.shic.cda.level1.PhrExtractDocument;

import static org.marc.shic.cda.templateRules.TemplateRules.testSingleConformanceRule;

import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.exceptions.TemplateViolationException;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 * Medical Summary Specification
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_1_2 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_2() {
        addDependency(TemplateRuleID.PCC_MedicalDocumentsSpecification);
    }

    @Override
    public void apply(Template template) {
        PhrExtractDocument document = (PhrExtractDocument) template;
        ClinicalDocument root = document.getRoot();

        if (!root.getCode().getCodeSystem().equals(LOINC_Codes.PhrDocumentCode.getCodeSystem())) {
            addViolation(new TemplateViolationException(template, getTemplateID(),
                    String.format("The document code system must be %s, %s. Found %s, %s.",
                            LOINC_Codes.PhrDocumentCode.getCodeSystem(),
                            LOINC_Codes.PhrDocumentCode.getCodeSystemName(),
                            root.getCode().getCodeSystem(),
                            root.getCode().getCodeSystemName())));
        }
        
        CdaUtils.addTemplateRuleID(root, getTemplateID());
    }

    @Deprecated
    public void populateWithRequiredDefaults(Level2Document level2Document) {

        level2Document.getRoot().getTemplateId().add(new II(TemplateRuleID.PCC_MedicalSummarySpecification.getOid()));
//        level2Document.addTemplateRule(this);
//        level2Document.addTemplateRule(this);
    }

    // RULES VALIDATION
    /**
     *
     * @param _document
     * @param _detailedOutput
     * @return
     */
    public boolean runValidation(DocumentTemplate _document) {
        return testMedicalSummarySpecificationTemplateOID(_document);

    }

    public boolean testMedicalSummarySpecificationTemplateOID(DocumentTemplate document) {

        return testSingleConformanceRule(document, ConformanceRuleParts.MedicalSummary);
    }

    public boolean testCONF_HP_4(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_4);
    }

    public boolean testCONF_HP_41(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_41);
    }

    public boolean testCONF_HP_42(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_42);
    }

    public boolean testCONF_HP_43(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_43_1)
                || testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_43_2);
    }

    public boolean testCONF_HP_50(DocumentTemplate document) {
        if (testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_1)) {
            return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_2)
                    || testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_50_3);
        } else {
            return true;
        }

    }

    public boolean testCONF_HP_51(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_51);
    }

    public boolean testCONF_HP_52(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_52);
    }

    public boolean testCONF_HP_58(DocumentTemplate document) {
        if (testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_1)) {
            return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_2)
                    || testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_58_3);
        } else {
            return true;
        }
    }

    public boolean testCONF_HP_63(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_63);
    }
}
