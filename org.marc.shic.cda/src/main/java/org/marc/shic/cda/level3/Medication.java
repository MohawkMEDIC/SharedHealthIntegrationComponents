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
 * @since 27-Jan-2014
 *
 */
package org.marc.shic.cda.level3;

import java.util.List;
import java.util.UUID;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Supply;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.templates.clinicalstatements.SubstanceAdministrationTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * Contains various amounts of data to pertains to a specific patient's
 * medication.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.24")
public final class Medication extends SubstanceAdministrationTemplate {

	/**
	 * Constructs the medication with required parameters and required
	 * internal attributes. Sets a random identifier. There will be no
	 * relevant medication data set; use the methods provided to populate
	 * the data.
	 *
	 * @param standard The standard to conform to.
	 */
	public Medication(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public Medication(SubstanceAdministration root, CDAStandard standard) {
		super(root, standard);
		//Populate ManufacturedProduct
		if (getRoot().getConsumable() != null) {
			addChild(new MedicationProduct(getRoot().getConsumable().getManufacturedProduct(), standard));
		}
		//Populate Entry Relationships
		for (EntryRelationship relationship : getRoot().getEntryRelationship()) {
			ClinicalStatement statement = relationship.getClinicalStatement();
			if (statement != null) {
				if (statement.getClass() == Observation.class) {
					addChild(new ProblemObservation((Observation) statement, standard));
				} else if (statement.getClass() == Supply.class) {
					Supply supply = (Supply) statement;
					if (supply.getMoodCode().getCode() == x_DocumentSubstanceMood.Intent) {
						addChild(new MedicationPrescription(supply, standard));
					} else if (supply.getMoodCode().getCode() == x_DocumentSubstanceMood.Eventoccurrence) {
						addChild(new MedicationDispensary(supply, standard));
					}
				}
			}
		}
	}

	@Override
	protected void initRoot() {
		getRoot().setMoodCode(x_DocumentSubstanceMood.Intent);
		getRoot().setStatusCode(ActStatus.Completed);
		addId(UUID.randomUUID().toString());
		setTime(new Time());
	}

	/**
	 * Creates a consumable product with required parameters, and sets it as
	 * the physical product representing the medication event.
	 *
	 * @param rootId A String containing the root OID or UUID identifier.
	 * @param extensionId A String containing the extension to the
	 * identifier.
	 * @param code A Code that identifies the type of medication product.
	 * @return A MedicationProduct Template object created and added to the
	 * medication event that is to have more data populated to it.
	 */
	public MedicationProduct setProduct(String rootId, String extensionId, Code code) {
		removeAllChildren(MedicationProduct.class);
		MedicationProduct result = new MedicationProduct(getStandard(), code);
		result.addId(new II(rootId, extensionId));
		addChild(result);
		getRoot().setConsumable(new Consumable(result.getRoot()));
		return result;
	}

	/**
	 * Gets the product that represents the medication.
	 *
	 * @return A MedicationProduct Template object that is set as a child of
	 * the medication event.
	 */
	public MedicationProduct getProduct() {
		return (MedicationProduct) getChild(MedicationProduct.class);
	}

	/**
	 * Creates a prescription event within the medication event.
	 *
	 * @param rootId A String containing the root OID or UUID of the
	 * identifier.
	 * @param extensionId A String containing the extension of the
	 * identifier.
	 * @return A MedicationPrescription object that is set upon the
	 * medication event which is to have more data populated to it.
	 */
	public MedicationPrescription setPrescription(String rootId, String extensionId) {
		removeAllChildren(MedicationPrescription.class);
		MedicationPrescription result = new MedicationPrescription(getStandard(), new II(rootId, extensionId));
		addChild(result);
		addEntryRelationship(result);
		return result;
	}

	/**
	 * Gets the prescription data attached to the medication.
	 *
	 * @return A MedicationPrescription Template object that has been set to
	 * the medication event as a child.
	 */
	public MedicationPrescription getPrescription() {
		return (MedicationPrescription) getChild(MedicationPrescription.class);
	}

	/**
	 * Creates and adds a dispensary event.
	 *
	 * @return A MedicationDispensary Template object that has been added to
	 * the medication event that should have more data populated to it.
	 */
	public MedicationDispensary addDispensary() {
		MedicationDispensary result = new MedicationDispensary(getStandard());
		addChild(result);
		addEntryRelationship(result);
		return result;
	}

	/**
	 * Gets a list of all of the dispensing events that are associated with
	 * the medication.
	 *
	 * @return A list of MedicationDispensary Template objects.
	 */
	public List<MedicationDispensary> getDispensaries() {
		return (List) getChildren(MedicationDispensary.class);
	}

	/**
	 * Creates and adds an indication with no problem value set.
	 *
	 * @return A ProblemObservation Template object that is to be populated
	 * with more data.
	 */
	public ProblemObservation addIndication() {
		ProblemObservation result = new ProblemObservation(getStandard());
		addChild(result);
		addEntryRelationship(result);
		return result;
	}

	/**
	 * Creates and adds an indication with required parameters for NexJ.
	 *
	 * @param code The code that indicates the type of indication event.
	 * @param takeTime A Time object representing the time elapsed during
	 * the indication event.
	 * @param status A status code indicating the status of the indication.
	 * @param conditionCode The code that indicates the type of condition
	 * observed.
	 * @return A ProblemObservation Template object that is to be populated
	 * with more data.
	 */
	public ProblemObservation addIndication(Code code, Time takeTime, ActStatus status, Code conditionCode) {
		ProblemObservation result = addIndication();
		result.setCode(code);
		result.setTime(takeTime);
		result.setStatusCode(status);
		result.setCode(conditionCode);
		return result;
	}

	/**
	 * Gets a list of medication indications assigned.
	 *
	 * @return A List of ProblemObservation Template objects.
	 */
	public List<ProblemObservation> getIndications() {
		return (List) getChildren(ProblemObservation.class);
	}

	public void setRouteCode(Code routeCode) {
		getRoot().setRouteCode((CE<String>) routeCode.getCode(CE.class));
	}

	public void setStatusCode(ActStatus status) {
		getRoot().setStatusCode(status);
	}

}
