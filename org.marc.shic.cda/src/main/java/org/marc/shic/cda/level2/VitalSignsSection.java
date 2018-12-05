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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.level3.Result;
import org.marc.shic.cda.level3.VitalSign;

/**
 * A section that defines specific kinds of measurements that are considered to
 * be 'vital' to be known. This section is the equivalent to the results
 * section, with a specific set of results.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Vital Signs", order = 11, code = "8716-3", codeName = "Vital Signs")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.16")
public class VitalSignsSection extends ResultsSection {

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public VitalSignsSection(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public VitalSignsSection(Section root, CDAStandard standard) {
		super(root, standard);
		removeAllChildren(Result.class);
		for (Entry entry : getRoot().getEntry()) {
			Organizer organizer = entry.getClinicalStatementIfOrganizer();
			if (organizer != null) {
				addChild(new VitalSign(entry.getClinicalStatementIfOrganizer(), standard));
			}
		}
	}

	/**
	 * Creates and adds a vital sign to the section with the required
	 * parameters. The returned vital sign must then be modified to contain
	 * a resulting observation.
	 *
	 * @param timeTaken The time it took for the vital sign observation.
	 * @return A VitalSign Template that should have more data populated
	 * with.
	 */
	public VitalSign addVitalSign(Time timeTaken) {
		VitalSign result = new VitalSign(getStandard());
		result.setTime(timeTaken);
		addChild(result);
		getEntry().add(new Entry(x_ActRelationshipEntry.DRIV, BL.TRUE, result.getRoot()));
		return result;
	}
}
