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
 * @author brownp Date: Sep 17, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.utils.DataTypeHelpers;
import java.util.ArrayList;
import java.util.Calendar;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer1;

/**
 * Healthcare Providers and Pharmacies Used in
 * documentationOf/serviceEvent/performer
 *
 * @author brownp
 */
public class Template_1_3_6_1_4_1_19376_1_5_3_1_2_3 extends AbstractTemplateRule {

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_2_3() {
    }

    protected Template_1_3_6_1_4_1_19376_1_5_3_1_2_3(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_1_3_6_1_4_1_19376_1_5_3_1_2_3 newInstance() {
        return new Template_1_3_6_1_4_1_19376_1_5_3_1_2_3(TemplateRuleID.PCC_HealthcareProvidersAndPharmacies);
    }

    @Deprecated
    public void populateWithRequiredDefaults(DocumentTemplate document, Calendar documentationStartTime, Calendar documentationEndTime, ArrayList<Performer1> performers) {
//        document.addTemplateRule(this);

        DocumentationOf documentationOf = DataTypeHelpers.createDocumentationOf("PCPR", null, documentationStartTime, documentationEndTime, null);

        if (null != performers) {
            for (Performer1 performer : performers) {
                performer.getTemplateId().add(new II(this.getTemplateID().getOid()));
                documentationOf.getServiceEvent().getPerformer().add(performer);
            }

        }

        //document.addDocumentationOf(documentationOf);
    }

    // RULES VALIDATION
    public boolean validate(DocumentTemplate _document) {
        boolean validated = testHealthcareProvidersAndPharmaciesOID(_document);

        return validated;
    }

    public boolean testHealthcareProvidersAndPharmaciesOID(DocumentTemplate document) {
        return testSingleConformanceRule(document, ConformanceRuleParts.HealthcareProvidersAndPharmacy);
    }
}
