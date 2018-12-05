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

import java.math.BigDecimal;

import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.PQ;
import org.marc.everest.datatypes.ST;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.templates.clinicalstatements.ObservationTemplate;

/**
 * Defines an observation of a measurable test/laboratory result.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.31")
public class ResultObservation extends ObservationTemplate {

	/**
	 * Constructs the observation with the required parameters. Once
	 * constructed, the observation must then have a observation value set
	 * using one of the public available "set" methods.
	 *
	 * @param standard The standard to conform to.
	 */
	public ResultObservation(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public ResultObservation(Observation root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	protected void initRoot() {
		getRoot().setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		getRoot().setStatusCode(ActStatus.Completed);
	}

	/**
	 * Sets the value of the observation to a measurable quantity with
	 * units.
	 *
	 * @param value The value of the observation measurement.
	 * @param units The units of the observation measurement.
	 */
	public void setMeasurementValue(double value, String units) {
		getRoot().setValue(new PQ(new BigDecimal(value), units.replaceAll(" ", "")));
	}

	/**
	 * Sets the value of the observation to a true or false value.
	 *
	 * @param value The boolean value of the observation measurement.
	 */
	public void setMeasurementValue(boolean value) {
		getRoot().setValue(new BL(value));
	}

	/**
	 * Sets the value of the observation to a text value that described the
	 * resulting observation.
	 *
	 * @param value A String containing a description of the observation.
	 */
	public void setTextualValue(String value) {
		getRoot().setValue(new ST(value));
	}

	/**
	 * Returns a string of text that provides this result observation with
	 * an identifiable name.
	 *
	 * @return A String containing the name of the observation
	 */
	public String getDisplayText() {
		Code code = getCode();
		if (code != null) {
			if (code.getDisplayName() != null && !code.getDisplayName().isEmpty()) {
				return code.getDisplayName();
			} else if (code.getCode() != null) {
				return code.getCode().toString();
			} else if (code.getOriginalText() != null) {
				return code.getOriginalText();
			}
		}
		return "UNK";
	}

	/**
	 * Creates a string representation of the observation value.
	 *
	 * @return A String representation of the measurement.
	 */
	public String getValueText() {
		ANY value = getRoot().getValue();
		if (value.getClass() == PQ.class) {
			PQ measure = (PQ) value;
			return String.format("%s %s", measure.getValue(), measure.getUnit());
		} else if (value.getClass() == ST.class) {
			ST string = (ST) value;
			return string.toString();
		} else if (value.getClass() == BL.class) {
			BL bool = (BL) value;
			return bool.toString();
		} else {
			return value.toString();
		}
	}

	/**
	 * Creates a string representation of the observation.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("%s: %s", getDisplayText(), getValueText());
	}
}
