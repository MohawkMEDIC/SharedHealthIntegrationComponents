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
 * Date: January 22, 2014
 *
 */
package org.marc.shic.cda.level2;

import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.SNOMED_CT_Codes;

/**
 * Defines a section that provides information about a patient's general
 * problems.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Problems", order = 4, code = "", codeName = "")
public class ProblemSection extends SectionTemplate {

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public ProblemSection(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public ProblemSection(Section root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	protected void initRoot() {
		setEmptyEntry();
	}

	/**
	 * Sets the Level 3 entry of the section to show no entries being added.
	 */
	private void setEmptyEntry() {
		Entry emptyEntry = new Entry();
		Observation emptyObservation = new Observation();

		CD<String> obsCode = new CD<String>(SNOMED_CT_Codes.DISEASE.getCode(), SNOMED_CT_Codes.DISEASE.getCodeSystem());
		obsCode.setDisplayName(SNOMED_CT_Codes.DISEASE.getDisplayName());
		emptyObservation.setCode(obsCode);

		emptyObservation.setValue(obsCode);
		emptyObservation.getClassCode().setCode("OBS");
		emptyObservation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		emptyObservation.setNegationInd(BL.TRUE);
		emptyEntry.setClinicalStatement(emptyObservation);

		getRoot().getEntry().add(emptyEntry);
	}
}
