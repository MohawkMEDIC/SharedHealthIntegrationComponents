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
 * @since 5-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer1;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.DefaultTimeable;
import org.marc.shic.cda.everestfunc.Timeable;

/**
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.2.3")
public class Performer1Template extends AssignedEntityTemplate<Performer1> implements Timeable {
	
	private Timeable timeable;
	
	public Performer1Template(CDAStandard standard) {
		this(null, standard);
	}
	
	public Performer1Template(Performer1 root, CDAStandard standard) {
		super(root, standard);
	}
	
	@Override
	protected void setAssignedEntity(AssignedEntity value) {
		getRoot().setAssignedEntity(value);
	}
	
	@Override
	public Time getTime() {
		return DefaultTimeable.getTimeable(getRoot(), "Time").getTime();
	}
	
	@Override
	public void setTime(Time value) {
		DefaultTimeable.getTimeable(getRoot(), "Time").setTime(value);
	}
}
