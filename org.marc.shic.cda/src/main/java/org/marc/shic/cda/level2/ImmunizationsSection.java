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

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * Defines a section that provides information about a patient's immunization
 * history.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Immunizations", order = 10, code = "11369-6", codeName = "History of Immunizations")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.6")
public class ImmunizationsSection extends SectionTemplate {

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public ImmunizationsSection(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public ImmunizationsSection(Section root, CDAStandard standard) {
		super(root, standard);
	}
}
