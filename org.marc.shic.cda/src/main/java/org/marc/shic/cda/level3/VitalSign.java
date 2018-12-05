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
 * @since 19-Feb-2014
 *
 */
package org.marc.shic.cda.level3;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;

/**
 * Defines a type of Result that is a vital measurement important to be known.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.35")
public class VitalSign extends Result {

	/**
	 * Defines the result as being "Vital"
	 */
	private static final Code code = new Code("46680005", "2.16.840.1.113883.6.96", "Vital Signs", "SNOMED_CT");

	/**
	 * Constructs the vital sign with the required code and mandatory
	 * attributes.
	 *
	 * @param standard The standard to conform to
	 */
	public VitalSign(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public VitalSign(Organizer root, CDAStandard standard) {
		super(root, standard);
	}

}
