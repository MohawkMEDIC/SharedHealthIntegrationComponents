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
 * @author Alex Radulescu
 * @since 30-Jan-2014
 *
 */
package org.marc.shic.cda.level2;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 *
 * @author Alex Radulescu
 */
@DefineSection(title = "Social History", order = 6, code = "29762-2", codeName = "Social History")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.15")
public class SocialHistorySection extends SectionTemplate {

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public SocialHistorySection(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public SocialHistorySection(Section root, CDAStandard standard) {
		super(root, standard);
	}
}
