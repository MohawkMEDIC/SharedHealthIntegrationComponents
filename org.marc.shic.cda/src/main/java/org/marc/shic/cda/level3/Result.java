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
 * @since 21-Feb-2014
 *
 */
package org.marc.shic.cda.level3;

import java.util.UUID;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryOrganizer;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.templates.clinicalstatements.OrganizerTemplate;

/**
 * Defines a result that has been determined via one or more (not implemented)
 * observations.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.32")
public class Result extends OrganizerTemplate {

	/**
	 * Constructs the result with the required parameters. No observation is
	 * recorded into the result yet.
	 *
	 * @param standard The standard to conform to.
	 */
	public Result(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public Result(Organizer root, CDAStandard standard) {
		super(root, standard);
		//Populate observations
		for (Component4 comp : getRoot().getComponent()) {
			Observation obs = comp.getClinicalStatementIfObservation();
			if (obs != null) {
				addChild(new ResultObservation(comp.getClinicalStatementIfObservation(), standard));
			}
		}
	}

	@Override
	protected void initRoot() {
		getRoot().setClassCode(x_ActClassDocumentEntryOrganizer.BATTERY);
		getRoot().setStatusCode(ActStatus.Completed);

		addId(UUID.randomUUID().toString());
	}

	/**
	 * Creates and returns a result observation with required parameters
	 * that is then set to be this results sole observation. This method
	 * creates an observation with a quantitative value.
	 *
	 * @param resultCode The Code that indicates the kind of observation
	 * that took place.
	 * @param measurement The resulting measurement value
	 * @param measurementUnit The resulting measurement units
	 * @return A ResultObservation Template object added to the result.
	 */
	public ResultObservation setResultObservation(Code resultCode, double measurement, String measurementUnit) {
		ResultObservation result = createResultObservation(resultCode);
		result.setMeasurementValue(measurement, measurementUnit);
		addChild(result);
		Component4 observationComponent = new Component4(ActRelationshipHasComponent.HasComponent, BL.TRUE, result.getRoot());
		getRoot().getComponent().clear();
		getRoot().getComponent().add(observationComponent);
		return result;
	}

	/**
	 * Creates and returns a result observation with required parameters
	 * that is then set to be this results sole observation. This method
	 * creates an observation with a textual value.
	 *
	 * @param resultCode The Code that indicates the kind of observation
	 * that took place.
	 * @param textVal A String of text that describes the observation taking
	 * place.
	 * @return A ResultObservation Template object added to the result.
	 */
	public ResultObservation setResultObservation(Code resultCode, String textVal) {
		ResultObservation result = createResultObservation(resultCode);
		result.setTextualValue(textVal);
		return result;
	}

	/**
	 * Creates and returns a result observation with required parameters
	 * that is then set to be this results sole observation. This method
	 * creates an observation with a boolean value.
	 *
	 * @param resultCode The Code that indicates the kind of observation
	 * that took place.
	 * @param value A boolean indicating a yes/no type result
	 * @return A ResultObservation Template object added to the result.
	 */
	public ResultObservation setResultObservation(Code resultCode, boolean value) {
		ResultObservation result = createResultObservation(resultCode);
		result.setMeasurementValue(value);
		return result;
	}

	/**
	 * Used to create an empty result observation and sets the underlying
	 * everest tree to contain it.
	 *
	 * @param resultCode The Code that indicates the kind of observation
	 * that took place.
	 * @return A new ResultObservation Template object added to the result.
	 */
	protected ResultObservation createResultObservation(Code resultCode) {
		ResultObservation result = new ResultObservation(getStandard());
		result.addId(UUID.randomUUID().toString());
		result.setCode(resultCode);
		addChild(result);
		return result;
	}

	/**
	 * Gets the first observation that is set to the result. The return may
	 * be null if the result has not been set.
	 *
	 * @return A ResultObservation Template object that is a child of the
	 * result.
	 */
	public ResultObservation getResultObservation() {
		return (ResultObservation) getChild(ResultObservation.class);
	}

	/**
	 * Gets the reference value of the observation, as it transfers
	 * reference to this result.
	 *
	 * @return A String that provides the reference number for the result.
	 */
	@Override
	public String getReference() {
		ResultObservation result = getResultObservation();
		if (result != null) {
			return result.getReference();
		}
		return super.getReference();
	}

	/**
	 * Sets the reference value of the observation, as it transfers
	 * reference to this result.
	 *
	 * @param reference A String that provides the reference number for the
	 * result.
	 */
	@Override
	public void setReference(String reference) {
		ResultObservation result = getResultObservation();
		result.setReference(reference);
	}
}
