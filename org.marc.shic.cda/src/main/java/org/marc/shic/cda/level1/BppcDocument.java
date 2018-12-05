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
 * @author Mohamed Ibrahim Date: Aug 22, 2013
 *
 */
package org.marc.shic.cda.level1;

import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.core.CodeValue;
import java.util.Calendar;
import java.util.UUID;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.StructuredBody;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.templates.DocumentationOfTemplate;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.DataTypeHelpers;

/**
 * Defines a Basic Patient Policies and Consents document.
 *
 * @author Mohamed Ibrahim
 */
@TemplateID(standard = CDAStandard.BPPC, oid = "1.3.6.1.4.1.19376.1.5.3.1.1.1")
public class BppcDocument extends Level1Document {

	/**
	 * Creates an empty BppcDocument.
	 *
	 * @param scanned A boolean indicating whether or not the contents are
	 * scanned.
	 */
	public BppcDocument(boolean scanned) {
		this(null);
		if (scanned) {
			CdaUtils.addTemplateRuleID(getRoot(), TemplateRuleID.PCC_BppcWithScannedPart);
		} else {
			CdaUtils.addTemplateRuleID(getRoot(), TemplateRuleID.PCC_BppcWithNoScannedPart);
			getRoot().setComponent(new Component2());
			getRoot().getComponent().setBodyChoice(new StructuredBody());
		}
	}

	/**
	 * Wraps an existing ClinicalDocument defined as a BPPC document.
	 *
	 * @param doc A ClinicalDocument object, or null to create and
	 * initialize one.
	 */
	public BppcDocument(ClinicalDocument doc) {
		super(doc, CDAStandard.PCC);
	}

	/**
	 * Initialization logic for creating a new ClinicalDocument object.
	 */
	@Override
	protected void initRoot() {
		super.initRoot();
		setCode(new Code("57016-8", "2.16.840.1.113883.6.1", "Privacy Policy Acknowledgement Document", "LOINC"));
		getRoot().setTitle("Consent to Share Information");
		getRoot().setConfidentialityCode(x_BasicConfidentialityKind.Normal);
		addId(UUID.randomUUID().toString());
	}

	/**
	 * Adds a consent policy to the document.
	 *
	 * @param consentCode The code that provides policy information.
	 * @return A DocumentationOfTemplate object added to the
	 * ClinicalDocument.
	 */
	public DocumentationOfTemplate addConsentPolicy(Code consentCode) {
		DocumentationOfTemplate result = addDocumentationOf();
		result.setCode(consentCode);
		return result;
	}

	/**
	 * Adds a consent policy to the document.
	 *
	 * @param consentCode The code that provides policy information
	 * @param consentTime The time that the consent has been given.
	 * @return A DocumentationOfTemplate object added to the
	 * ClinicalDocument.
	 */
	public DocumentationOfTemplate addConsentPolicy(Code consentCode, Time consentTime) {
		DocumentationOfTemplate result = addConsentPolicy(consentCode);
		result.setTime(consentTime);
		return result;
	}

	/**
	 * Adds a consent policy to the document.
	 *
	 * @param consent The code that provides policy information.
	 * @param effectiveTimeStart The starting time of the consent.
	 * @param effectiveTimeStop The ending time of the consent.
	 * @Deprecated Use the addConsentPolicy methods that return a
	 * DocumentationOfTemplate for further modifications.
	 */
	@Deprecated
	public void addConsentPolicy(CodeValue consent, Calendar effectiveTimeStart, Calendar effectiveTimeStop) {
		addConsentPolicy(new Code(consent), new Time(effectiveTimeStart, effectiveTimeStop));
	}
}
