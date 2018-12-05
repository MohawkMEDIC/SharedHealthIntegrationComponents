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
 *
 */
package org.marc.shic.cda.level1;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.level2.ActiveProblemsSection;
import org.marc.shic.cda.level2.AllergiesAndDrugSensitiviesSection;
import org.marc.shic.cda.level2.FamilyMedicalHistorySection;
import org.marc.shic.cda.level2.HistoryOfPastIllnessSection;
import org.marc.shic.cda.level2.SocialHistorySection;
import org.marc.shic.cda.utils.LOINC_Codes;

/**
 * Defines a Patient Health Record Extract document, which applies the PCC
 * standard.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.1.5")
public class PhrExtractDocument extends Level2Document {

	/**
	 * Instantiates the document setting mandatory attributes.
	 */
	public PhrExtractDocument() {
		this(null);

	}

	/**
	 * Wraps an existing everest document.
	 *
	 * @param document A ClinicalDocument to wrap.
	 */
	public PhrExtractDocument(ClinicalDocument document) {
		super(document, CDAStandard.PCC);
	}

	/**
	 * Initialization logic for creating a new ClinicalDocument object.
	 */
	@Override
	protected void initRoot() {
		super.initRoot();
		getRoot().setCode(LOINC_Codes.PhrDocumentCode.getCodedElement());
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A SocialHistorySection Template.
	 */
	public final SocialHistorySection getSocialHistorySection() {
		SocialHistorySection section = (SocialHistorySection) getChild(SocialHistorySection.class);
		if (section == null) {
			section = new SocialHistorySection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A ActiveProblemsSection Template.
	 */
	public final ActiveProblemsSection getActiveProblemsSection() {
		ActiveProblemsSection section = (ActiveProblemsSection) getChild(ActiveProblemsSection.class);
		if (section == null) {
			section = new ActiveProblemsSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A FamilyMedicalHistorySection Template.
	 */
	public final FamilyMedicalHistorySection getFamilyMedicalHistorySection() {
		FamilyMedicalHistorySection section = (FamilyMedicalHistorySection) getChild(FamilyMedicalHistorySection.class);
		if (section == null) {
			section = new FamilyMedicalHistorySection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A HistoryOfPastIllnessSection Template.
	 */
	public final HistoryOfPastIllnessSection getHistoryOfPastIllnessSection() {
		HistoryOfPastIllnessSection section = (HistoryOfPastIllnessSection) getChild(HistoryOfPastIllnessSection.class);
		if (section == null) {
			section = new HistoryOfPastIllnessSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A AllergiesAndDrugSensitiviesSection Template.
	 */
	public final AllergiesAndDrugSensitiviesSection getAllergiesAndDrugSensitiviesSection() {
		AllergiesAndDrugSensitiviesSection section = (AllergiesAndDrugSensitiviesSection) getChild(AllergiesAndDrugSensitiviesSection.class);
		if (section == null) {
			section = new AllergiesAndDrugSensitiviesSection(getStandard());
			addChild(section);
		}
		return section;
	}
}
