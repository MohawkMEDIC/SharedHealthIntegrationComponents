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
 * @author brownp Date: Aug 21, 2013
 */
package org.marc.shic.cda.level1;

import java.util.UUID;

import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.vocabulary.BindingRealm;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.TemplateIDSet;
import org.marc.shic.cda.templates.ClinicalDocumentTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.utils.CdaTestUtils;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 * Defines a template that represents a clinical document.
 *
 * @author Ryan Albert
 */
@TemplateIDSet(
	{
		@TemplateID(oid = "2.16.840.1.113883.10.20.3"),
		@TemplateID(oid = "2.16.840.1.113883.10.20.22.1.1"),
		@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.1.1")
	}
)
public class DocumentTemplate extends ClinicalDocumentTemplate {

	/**
	 * Creates a new DocumentTemplate with the specified standard.
	 *
	 * @param standard The CDAStandard that the document conforms to.
	 */
	protected DocumentTemplate(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing ClinicalDocument object.
	 *
	 * @param root A ClinicalDocument object, or null to create one.
	 * @param standard The CDAStandard to conform to.
	 */
	protected DocumentTemplate(ClinicalDocument root, CDAStandard standard) {
		super(root, standard);
	}

	/**
	 * Initialization logic for creating a new ClinicalDocument object.
	 */
	@Override
	protected void initRoot() {
		setConfidentialityCode(x_BasicConfidentialityKind.Normal);
		setCode(new Code(LOINC_Codes.GeneralCDA.getCodedElement()));
		setTime(new Time());
		addId(UUID.randomUUID().toString());
		getRoot().setLanguageCode("en-US");
		getRoot().setRealmCode(new SET());
		getRoot().getRealmCode().add(new CS(BindingRealm.Canada));
		getRoot().setTitle(new ST("CDA Document"));
		getRoot().setTypeId("2.16.840.1.113883.1.3", "POCD_HD000040");
	}

	/**
	 * Generates the XML string representation of the document and compares
	 * the output to the schematron for validation.
	 *
	 * @return A boolean indicating whether or not schematron validation
	 * failed.
	 */
	public final boolean validateSchematron() {
		return CdaTestUtils.schemaValidate(CdaUtils.toXmlString(getRoot(), false));
	}

	@Deprecated
	public final boolean validateSchematron(String xmlString) {
		return CdaTestUtils.schemaValidate(xmlString);
	}

	@Deprecated
	public final boolean validateSchematron(ClinicalDocument cdaDocument, boolean createRequiredElements) {
		return CdaTestUtils.schemaValidate(CdaUtils.toXmlString(getRoot(), createRequiredElements));
	}

}
