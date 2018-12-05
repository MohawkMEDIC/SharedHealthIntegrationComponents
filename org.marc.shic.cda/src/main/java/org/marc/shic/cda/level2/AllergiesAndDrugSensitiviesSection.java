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
package org.marc.shic.cda.level2;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.SNOMED_CT_Codes;

/**
 * Defines a section that provides information about the allergies and drug
 * sensitivities that a patient is susceptible to.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Allergies, adverse reactions, alerts", order = 2, code = "48765-2", codeName = "Allergies, adverse reactions, alerts")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.2")
public class AllergiesAndDrugSensitiviesSection extends SectionTemplate {

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public AllergiesAndDrugSensitiviesSection(CDAStandard standard) {
		this(null, standard);
		setEmptyEntry();
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public AllergiesAndDrugSensitiviesSection(Section root, CDAStandard standard) {
		super(root, standard);
	}

	/**
	 * Creates an entry that specifies no Level 3 information set to the
	 * section.
	 */
	private void setEmptyEntry() {
		Entry emptyEntry = new Entry();
		Observation emptyObservation = new Observation();
		Participant2 emptyParticipant = new Participant2();
		ParticipantRole emptyRole = new ParticipantRole();
		PlayingEntity emptyEntity = new PlayingEntity();

		emptyEntity.setCode(SNOMED_CT_Codes.ALLERGY.getCodedElement());
		emptyEntity.setClassCode(EntityClassRoot.ManufacturedMaterial);
		emptyRole.setPlayingEntityChoice(emptyEntity);
		emptyRole.setClassCode("MANU");
		emptyParticipant.setParticipantRole(emptyRole);
		emptyParticipant.setTypeCode(ParticipationType.Consumable);
		emptyObservation.getParticipant().add(emptyParticipant);

		CD<String> emptyCD = new CD<String>();
		emptyCD.setNullFlavor(NullFlavor.NotApplicable);
		emptyObservation.setValue(emptyCD);
		CD<String> obsCode = new CD<String>("106190000", "2.16.840.1.113883.6.1");
		obsCode.setDisplayName("Allergy");
		emptyObservation.setCode(obsCode);
		emptyObservation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		emptyObservation.setNegationInd(BL.TRUE);
		emptyEntry.setClinicalStatement(emptyObservation);
		getRoot().getEntry().add(emptyEntry);
	}

}
