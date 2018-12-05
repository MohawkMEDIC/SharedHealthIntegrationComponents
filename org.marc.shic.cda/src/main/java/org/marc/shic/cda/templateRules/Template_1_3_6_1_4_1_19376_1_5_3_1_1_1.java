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
import org.marc.shic.cda.ResultDetail;
import static org.marc.shic.cda.templateRules.TemplateRules.testSingleConformanceRule;
import org.marc.shic.cda.utils.LOINC_Codes;
import java.util.Calendar;
import org.marc.everest.datatypes.II;
import org.marc.everest.interfaces.ResultDetailType;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Medical Document Specification
 *
 * @author Paul
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_1_1 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_1() {
        addDependency(TemplateRuleID.CDT_GeneralCDAR2HeaderConstraint);
    }

    @Override
    public void apply(Template template) {
        CdaUtils.addTemplateRuleID((InfrastructureRoot)template.getRoot(), getTemplateID());
    }

    @Deprecated
    protected Template_1_3_6_1_4_1_19376_1_5_3_1_1_1(TemplateRuleID templateId) {
        super(templateId);
        addDependency(TemplateRuleID.CDT_GeneralCDAR2HeaderConstraint);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_1_1 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_1_1(TemplateRuleID.PCC_MedicalDocumentsSpecification);
    }

    /**
     * Populates the CDA Document with required and default header information
     *
     * @param document
     */
    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document) {
        document.getRoot().getTemplateId().add(new II(this.getTemplateID().getOid()));

//        document.addTemplateRule(this);

        document.getRoot().setCode(LOINC_Codes.GeneralCDA.getCode(), LOINC_Codes.GeneralCDA.getCodeSystem()); // code must be from LOINC

        document.getRoot().setEffectiveTime(Calendar.getInstance());

        document.getRoot().setLanguageCode("en-US");

    }

    // RULES VALIDATION
    public boolean validate(DocumentTemplate _document) {
        // check for Level1Template ... or let the child handle this template

        return testCONF_HP_1(_document)
                && testCONF_HP_2(_document)
                && testCONF_HP_6(_document)
                && testCONF_HP_7(_document)
                && testCONF_HP_8(_document)
                && testCONF_HP_9(_document)
                && testCONF_HP_16(_document)
                && testCONF_HP_17(_document)
                && testCONF_HP_21(_document)
                && testCONF_HP_22(_document)
                && testCONF_HP_23(_document)
                && testCONF_HP_24(_document)
                && testCONF_HP_30(_document)
                && testCONF_HP_31(_document)
                && testCONF_HP_32(_document)
                && testCONF_HP_33(_document)
                && testCONF_HP_37(_document)
                && testCONF_HP_38(_document)
                && testCONF_HP_39(_document)
                && testCONF_HP_40(_document);
    }

    public boolean testCONF_HP_1(DocumentTemplate document) {

        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_1);
    }

    public boolean testCONF_HP_2(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_2);
    }

    public boolean testCONF_HP_3(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_3);
    }

    public boolean testCONF_HP_6(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_6);
    }

    public boolean testCONF_HP_7(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_7_1)
                && testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_7_2);
    }

    public boolean testCONF_HP_8(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_8_1)
                && testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_8_2);
    }

    // CONF-HP-9: All guardianOrganization, providerOrganization, wholeOrganization, representedOrganization, representedCustodianOrganization,
    // receivedOrganization, scopingOrganization and serviceProviderOrganization elements SHALL have
    // name, addr and telecom elements
    public boolean testCONF_HP_9(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_9_1)
                && testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_9_2)
                && testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_9_3);
    }

    public boolean testCONF_HP_16(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_16);
    }

    public boolean testCONF_HP_17(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_17);
    }

    public boolean testCONF_HP_21(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_21);
    }

    public boolean testCONF_HP_22(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_22);
    }

    public boolean testCONF_HP_23(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_23);
    }

    public boolean testConfidentialityCode(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.ConfidentialityCode);
    }

    // The ClinicalDocument/languageCode specifies the language of the History and Physical.
    // History and Physicals must be readable by medical practitioners, caregivers, and patients.  
    public boolean testCONF_HP_24(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_24);
    }

    public boolean testCONF_HP_30(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_30);
    }

    public boolean testCONF_HP_31(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_31);
    }

    public boolean testCONF_HP_32(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_32);
    }

    public boolean testCONF_HP_33(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_33);
    }

    public boolean testCONF_HP_37(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_37);
    }

    public boolean testCONF_HP_38(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_38);
    }

    public boolean testCONF_HP_39(DocumentTemplate document) {
        boolean validated = testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_39_1)
                || testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_39_2);

        if (!validated) {
            document.getResultDetails().add(new ResultDetail(ConformanceRuleParts.CONF_HP_39_1.getXpathExpression(),
                    new Exception(String.format("Either %s or %s must be set.",
                                    ConformanceRuleParts.CONF_HP_39_1.getValue(),
                                    ConformanceRuleParts.CONF_HP_39_2.getValue())),
                    ResultDetailType.ERROR));
        }

        return validated;
    }

    public boolean testCONF_HP_40(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.CONF_HP_40);
    }
}
