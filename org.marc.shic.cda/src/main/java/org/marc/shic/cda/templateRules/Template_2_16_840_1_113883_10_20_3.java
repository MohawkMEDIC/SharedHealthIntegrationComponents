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
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Authorization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Custodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DataEnterer;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LegalAuthenticator;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.vocabulary.BindingRealm;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.exceptions.TemplateViolationException;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * General CDA R2 Header Constraint
 *
 * @author Paul
 */
public class Template_2_16_840_1_113883_10_20_3 extends AbstractTemplateRule {

    protected Template_2_16_840_1_113883_10_20_3() {
    }

    @Deprecated
    protected Template_2_16_840_1_113883_10_20_3(TemplateRuleID templateId) {
        super(templateId);
    }

    @Deprecated
    public static Template_2_16_840_1_113883_10_20_3 newInstance() {
        return new Template_2_16_840_1_113883_10_20_3(TemplateRuleID.CDT_GeneralCDAR2HeaderConstraint);
    }

    @Override
    public void apply(Template template) {
        DocumentTemplate document = (DocumentTemplate) template;
        ClinicalDocument root = document.getRoot();
/*
        if (!root.getTypeId().getExtension().equals("POCD_HD000040")) {
            throw new TemplateViolationException(template, getTemplateID(),
                    "The document typeId field extension must be POCD_HD000040, found " + root.getTypeId().getExtension());
        }
        if (document.getChildren(Component2.class).size() > 1) {
            throw new TemplateViolationException(template, getTemplateID(),
                    "The number of component items in the document must be at most 1.");
        }
*/
        CdaUtils.addTemplateRuleID(root, getTemplateID());

        for (Template<Author> author : document.getChildrenRoot(Author.class)) {
            //TODO: Author rules checking here.
            //root.getAuthor().add(author.getRoot());
        }
        
        for(Template<Authorization> authorization : document.getChildrenRoot(Authorization.class)) {
            //TODO: Authorization rules checking here.
            //root.getAuthorization().add(authorization.getRoot());
        }
        
        for(Template<LegalAuthenticator> authenticator : document.getChildrenRoot(LegalAuthenticator.class)) {
            //TODO: Legal authenticator rules checking here.
            //root.setLegalAuthenticator(authenticator.getRoot());
        }
        
        for(Template<DataEnterer> dataEnterer : document.getChildrenRoot(DataEnterer.class)) {
            //TODO: Data enterer rules checking here.
            //root.setDataEnterer(dataEnterer.getRoot());
        }
        
        for(Template<RecordTarget> recordTarget : document.getChildrenRoot(RecordTarget.class)) {
            //TODO: Record target rules checking here.
            //root.getRecordTarget().add(recordTarget.getRoot());
        }
        
        for(Template<DocumentationOf> documentationOf : document.getChildrenRoot(DocumentationOf.class)) {
            //TODO: Documentation of rules checking here.
        }
        
        for(Template<Custodian> custodian : document.getChildrenRoot(Custodian.class)) {
            //TODO: Custodian rules checking here.
            //root.setCustodian(custodian.getRoot());
        }
    }

    public void populateWithRequiredDefaults(DocumentTemplate document) {
        document.getRoot().getTemplateId().add(new II(this.getTemplateID().getOid()));

        //document.addTemplateRule(this);

        // adds in CONF-HP=15
        document.getRoot().getRealmCode().add(new CS(BindingRealm.UnitedStatesOfAmerica)); // this template requires that the Realm be set to US
    }
}
