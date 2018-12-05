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
import org.marc.shic.cda.level1.PhrExtractDocument;

import static org.marc.shic.cda.templateRules.TemplateRules.testSingleConformanceRule;

import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * XPHR Extract Specification
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_1_5 extends AbstractTemplateRule {

    private static String CODE = "34133-9";
    private static String CODESYSTEM = "2.16.840.1.113883.6.1";

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_5() {
        addDependency(TemplateRuleID.PCC_MedicalSummarySpecification);
    }
    
    @Override
    public void apply(Template template) {
        CdaUtils.addTemplateRuleID((InfrastructureRoot)template.getRoot(), getTemplateID());
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_5(TemplateRuleID templateId) {
        super(templateId);
        addDependency(TemplateRuleID.PCC_MedicalSummarySpecification);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_1_5 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_1_5(TemplateRuleID.PCC_PhrExtractSpecification);
    }

//    public static Template_1_3_6_1_4_1_19376_1_5_3_1_1_5 newInstance(PhrExtractDocument phrExtractDocument)
//    {
//        Template_1_3_6_1_4_1_19376_1_5_3_1_1_5 template = new Template_1_3_6_1_4_1_19376_1_5_3_1_1_5(TemplateIDs.PhrExtractSpecification);
//        template.populateWithRequiredDefaults(phrExtractDocument);
//        
//        return template;
//    }
    @Deprecated
    public void populateWithRequiredDefaults(PhrExtractDocument phrExtractDocument) {
        phrExtractDocument.getRoot().getTemplateId().add(new II(TemplateRuleID.PCC_PhrExtractSpecification.getOid()));
        phrExtractDocument.getRoot().setCode(CODE, CODESYSTEM);

//        phrExtractDocument.addTemplateRule(this);
    }

    // RULES VALIDATION
    public boolean runValidation(DocumentTemplate _document) {
        return testPhrExtractSpecificationTemplateOID(_document)
                && testPhrCode(_document)
                && testPhrCodeSystem(_document);
    }

    public boolean testPhrExtractSpecificationTemplateOID(DocumentTemplate document) {

        return testSingleConformanceRule(document, ConformanceRuleParts.PhrExtractSummary);
    }

    public boolean testPhrCode(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.PhrExtractCode);
    }

    public boolean testPhrCodeSystem(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.PhrExtractCodeSystem);
    }
}
