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

import java.util.Set;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.INT;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Supply;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.DefaultTimeable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Timeable;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;

/**
 * Wraps an everest Supply object and provides various methods to assist in
 * populating the data within.
 *
 * @author Ryan Albert
 */
public class SupplyTemplate extends ClinicalStatementTemplate<Supply> implements Identifiable, Codeable, Timeable {

	/**
	 * Constructs a Supply as conforming to a specific standard.
	 *
	 * @param standard
	 */
	public SupplyTemplate(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Constructs a supply around an existing root object.
	 *
	 * @param root
	 * @param standard
	 */
	public SupplyTemplate(Supply root, CDAStandard standard) {
		super(root, standard);
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
		return DefaultTimeable.getTimeable(getRoot(), "EffectiveTime").getTime();
	}

	@Override
	public void setTime(Time value) {
		DefaultTimeable.getTimeable(getRoot(), "EffectiveTime").setTime(value);
	}

	/**
	 * Sets the repeat number of the supply.
	 *
	 * @param value
	 */
	public void setRepeatNumber(int value) {
		getRoot().setRepeatNumber(new INT(value));
	}

	/**
	 * Sets an interval of repeat of the supply.
	 *
	 * @param low
	 * @param high
	 */
	public void setRepeatNumber(int low, int high) {
		getRoot().setRepeatNumber(new INT(low), new INT(high));
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
}
