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
 * @since 6-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer2;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 *
 * @author Ryan Albert
 */
public class Performer2Template extends AssignedEntityTemplate<Performer2> {

	public Performer2Template(CDAStandard standard) {
		this(null, standard);
	}

	public Performer2Template(Performer2 root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	public void setAssignedEntity(AssignedEntity value) {
		getRoot().setAssignedEntity(value);
	}
}
