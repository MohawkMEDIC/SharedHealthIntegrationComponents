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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.INT;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ReferenceRange;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActPriority;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ObservationInterpretation;
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
 * Wraps an everest SubstanceAdministration object and provides various method
 * to assist in populating the data within.
 *
 * @author Ryan Albert
 */
public class ObservationTemplate extends ClinicalStatementTemplate<Observation> implements Identifiable, Codeable, Timeable {

	/**
	 * Constructs the Observation conforming to a specific standard.
	 *
	 * @param standard
	 */
	public ObservationTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public ObservationTemplate(Observation root, CDAStandard standard) {
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
	 * Sets the content of text by string.
	 *
	 * @param text
	 * @param mediaType
	 */
	public void setText(String text, String mediaType) {
		getRoot().setText(text.getBytes(), mediaType);
	}

	/**
	 * Sets the reference text of the observation
	 *
	 * @param reference
	 */
	@Override
	protected void setReferenceText(String reference) {
		getRoot().setText(CoreDataTypeHelpers.createEDWithReference(reference));
	}
}
