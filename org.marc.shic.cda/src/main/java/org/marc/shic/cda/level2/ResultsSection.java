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
 * @author Ryan Albert
 * @since 21-Feb-2014
 *
 */
package org.marc.shic.cda.level2;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.level3.Result;
import org.marc.shic.cda.level3.ResultObservation;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * This section is responsible for displaying and entering in laboratory tests,
 * diagnostic data, and any measureable results.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Results", order = 12, code = "30954-2", codeName = "Relevant diagnostic tests and/or laboratory data")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.14")
public class ResultsSection extends SectionTemplate {

	/**
	 * Extra text that is appended to the section table.
	 */
	private String narrativeText;

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public ResultsSection(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public ResultsSection(Section root, CDAStandard standard) {
		super(root, standard);
		//Populate results
		for (Entry entry : getRoot().getEntry()) {
			Organizer entryOrganizer = entry.getClinicalStatementIfOrganizer();
			if (entryOrganizer != null) {
				addChild(new Result(entry.getClinicalStatementIfOrganizer(), standard));
			}
		}
		setTableColumns("Measurement", "Value");
	}

	/**
	 * Sets the text/xml that will be appended after the table generation.
	 *
	 * @param text A String containing narrative text.
	 */
	public final void setNarrativeText(String text) {
		narrativeText = text;
	}

	/**
	 * Gets the set narrative text that appears after table generation.
	 *
	 * @return A String containing narrative text.
	 */
	public String getNarrativeText() {
		return narrativeText;
	}

	/**
	 * Creates and adds a new result with required parameters. The returned
	 * result must then have an observation set within it.
	 *
	 * @param timeTaken A Time object
	 * @param code A Code object
	 * @return An empty Result object that shall be populated with more
	 * data.
	 */
	public Result addResult(Time timeTaken, Code code) {
		Result result = new Result(getStandard());
		result.setTime(timeTaken);
		result.setCode(code);
		addChild(result);
		getEntry().add(new Entry(x_ActRelationshipEntry.DRIV, BL.TRUE, result.getRoot()));
		return result;
	}

	/**
	 * Appends narrative text to the CDA narrative block after table
	 * generation.
	 *
	 * @return An everest ED object representing that data.
	 */
	@Override
	public ED getText() {
		int refId = 0;
		for (Template resultTemplate : getChildrenRoot(Organizer.class)) {
			Result resultMeasurement = (Result) resultTemplate;
			ResultObservation resultObservation = (ResultObservation) resultMeasurement.getChild(ResultObservation.class);
			if (resultObservation != null) {
				resultObservation.setReference(String.format("%s%s", getClass().getSimpleName(), refId++));
				resultMeasurement.setDisplayText(resultObservation.getDisplayText(), resultObservation.toString());
			} else {
				LOGGER.error(String.format("No result observation set in %s", resultMeasurement.getCode().getDisplayName()));
			}
		}
		ED result = super.getText();

		if (narrativeText != null && !narrativeText.isEmpty()) {
			result.setData(String.format("%s%s", new String(result.getData()), narrativeText));
		}
		return result;
	}
}
