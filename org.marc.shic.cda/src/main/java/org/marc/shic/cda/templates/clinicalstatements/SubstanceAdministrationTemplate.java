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
 * @since 6-Feb-2014
 *
 */
package org.marc.shic.cda.templates.clinicalstatements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Timeable;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;

/**
 * Wraps an everest SubstanceAdministration object and provides various method
 * to assist in populating the data within.
 *
 * @author Ryan Albert
 */
public class SubstanceAdministrationTemplate extends ClinicalStatementTemplate<SubstanceAdministration> implements Identifiable, Codeable, Timeable {

	/**
	 * Constructs a SubstanceAdministration as conforming to a specific
	 * standard.
	 *
	 * @param standard
	 */
	public SubstanceAdministrationTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public SubstanceAdministrationTemplate(SubstanceAdministration root, CDAStandard standard) {
		super(root, standard);
	}

	/**
	 * Sets the reference ID text.
	 *
	 * @param reference
	 */
	@Override
	protected void setReferenceText(String reference) {
		getRoot().setText(CoreDataTypeHelpers.createEDWithReference(reference));
	}

	@Override
	public Code getCode() {
		return DefaultCodeable.getCodeable(getRoot()).getCode();
	}

	@Override
	public void setCode(Code value) {
		DefaultCodeable.getCodeable(getRoot()).setCode(value);
	}

	@Override
	public void addId(II id) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(id);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root);
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root, extension);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(getRoot()).getIds();
	}

	@Override
	public Time getTime() {
		List times = getRoot().getEffectiveTime();
		if (times != null && times.size() > 0) {
			return new Time((ANY) getRoot().getEffectiveTime().get(0));
		}
		return null;
	}

	@Override
	public void setTime(Time value) {
		ArrayList times = getRoot().getEffectiveTime();
		if (times == null) {
			times = new ArrayList();
			getRoot().setEffectiveTime(times);
		}
		times.clear();
		times.add(value.getTimestamp());
	}
}
