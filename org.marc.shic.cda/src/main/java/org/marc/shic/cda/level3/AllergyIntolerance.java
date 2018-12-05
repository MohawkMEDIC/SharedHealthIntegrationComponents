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
 * Date: January 17, 2014
 *
 */
package org.marc.shic.cda.level3;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * Coherent to the Allergies and Intolerances section. Contains information
 * about an allergy or intolerance. TODO: Modify to reflect allergy data.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.4.5.3")
public class AllergyIntolerance extends Concern {

	/**
	 * Constructs a new entry conforming to the specified standard.
	 *
	 * @param standard The standard to conform to.
	 */
	public AllergyIntolerance(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public AllergyIntolerance(Act root, CDAStandard standard) {
		super(root, standard);
	}
}
