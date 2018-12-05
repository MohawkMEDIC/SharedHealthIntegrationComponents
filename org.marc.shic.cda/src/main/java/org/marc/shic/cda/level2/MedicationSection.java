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
 * @author brownp Date: Aug 21, 2013
 *
 */
package org.marc.shic.cda.level2;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.INT;
import org.marc.everest.datatypes.PQ;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LabeledDrug;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ManufacturedProduct;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.TemplateIDSet;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.level3.Medication;
import org.marc.shic.cda.level3.MedicationProduct;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.SNOMED_CT_Codes;

/**
 * Defines a section that provides information about a patient's Medication
 * events.
 *
 * @author Ryan Albert
 */
@DefineSection(title = "Medications Section", order = 8, code = "10160-0", codeName = "History of Medication Use")
@TemplateIDSet(
	{
		@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.8"),
		@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.3.19")
	})
public class MedicationSection extends SectionTemplate {

	/**
	 * Specifies whether or not the set of entries in the section is empty.
	 * If empty, a specific entry used to specify no entries is created.
	 */
	private boolean isEmpty;

	/**
	 * Constructs a new, empty section conforming to the specified standard.
	 *
	 * @param standard
	 */
	public MedicationSection(CDAStandard standard) {
		this(null, standard);
		setEmptyEntry();
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public MedicationSection(Section root, CDAStandard standard) {
		super(root, standard);
		for (Entry entry : getRoot().getEntry()) {
			SubstanceAdministration admin = entry.getClinicalStatementIfSubstanceAdministration();
			if (admin != null) {
				addChild(new Medication(admin, standard));
			}
		}
		setTableColumns("Prescribed Date", "Name", "Dosage", "Quantity", "Repeat", "Route of Administration");
	}

	/**
	 * Creates a medication entry within the section, initialized with the
	 * given attributes.
	 *
	 * @param effectiveTime The Time object providing the time of the
	 * Medication event.
	 * @param route The Code that identifies the kind of Medication event.
	 * @return A Medication object entry set to the section. Data within it
	 * must now be populated.
	 */
	public Medication createMedication(Time effectiveTime, Code route) {
		return createMedication(effectiveTime, route, ActStatus.Completed);
	}

	/**
	 * Creates a medication entry within the section, initialized with the
	 * given attributes.
	 *
	 * @param effectiveTime The Time object providing the time of the
	 * Medication event.
	 * @param route The Code that identifies the kind of Medication event.
	 * @param status A code indicating the status of the medication event.
	 * @return A Medication object entry set to the section. Data within it
	 * must now be populated.
	 */
	public Medication createMedication(Time effectiveTime, Code route, ActStatus status) {
		Medication result = new Medication(getStandard());
		result.setTime(effectiveTime);
		result.setRouteCode(route.ensureSystem("RouteOfAdministration", "RouteOfAdministration"));
		result.setStatusCode(status);
		if (isEmpty) {
			isEmpty = false;
			getRoot().getEntry().clear();
		}
		addChild(result);
		Entry newEntry = new Entry();
		newEntry.setClinicalStatement(result.getRoot());
		getRoot().getEntry().add(newEntry);

		return result;
	}

	/**
	 * Clears all entries in the section and adds an entry indicating that
	 * there are no medications set.
	 */
	private void setEmptyEntry() {
		isEmpty = true;
		Entry emptyEntry = new Entry();
		SubstanceAdministration emptySubstance = new SubstanceAdministration();
		Consumable emptyConsumable = new Consumable();
		ManufacturedProduct emptyProduct = new ManufacturedProduct();
		LabeledDrug emptyDrug = new LabeledDrug();

		emptyDrug.setCode(SNOMED_CT_Codes.DRUG_OR_MEDICAMENT.getCodedElement());
		emptyProduct.setManufacturedDrugOrOtherMaterial(emptyDrug);
		emptyConsumable.setManufacturedProduct(emptyProduct);
		emptySubstance.setConsumable(emptyConsumable);
		emptySubstance.setMoodCode(x_DocumentSubstanceMood.Eventoccurrence);
		emptySubstance.setNegationInd(BL.TRUE);
		emptyEntry.setClinicalStatement(emptySubstance);

		getRoot().getEntry().add(emptyEntry);
	}

	/**
	 * Generates the text that represents the entries in the section.
	 *
	 * @return An everest ED object.
	 */
	@Override
	public ED getText() {
		int refId = 0;
		for (Template medTemplate : getChildrenRoot(SubstanceAdministration.class)) {
			Medication med = (Medication) medTemplate;
			MedicationProduct drug = med.getProduct();
			med.setReference(String.format("%s%s", getClass().getSimpleName(), refId++));

			String doseDisplay = "", rateDisplay = "", repeatDisplay = "", routeDisplay = "", timeDisplay = "";
			IVL<PQ> dose = med.getRoot().getDoseQuantity();
			IVL<PQ> rate = med.getRoot().getRateQuantity();
			IVL<INT> repeat = med.getRoot().getRepeatNumber();
			CE<String> routeCode = med.getRoot().getRouteCode();
			Time medTime = med.getTime();
			if (dose != null) {
				doseDisplay = String.format("%s%s - %s%s", dose.getLow().getValue(), dose.getLow().getUnit(), dose.getHigh().getValue(), dose.getHigh().getUnit());
			}
			if (rate != null) {
				rateDisplay = String.format("%s%s - %s%s", rate.getLow().getValue(), rate.getLow().getUnit(), rate.getHigh().getValue(), rate.getHigh().getUnit());
			}
			if (repeat != null) {
				repeatDisplay = String.format("%s - %s", repeat.getLow().getValue(), repeat.getHigh().getValue());
			}
			if (routeCode != null) {
				routeDisplay = routeCode.getDisplayName();
			}
			if (medTime != null) {
				timeDisplay = medTime.toString();
			}
			med.setDisplayText(timeDisplay, drug.getDisplayName(), doseDisplay, rateDisplay, repeatDisplay, routeDisplay, null);
		}
		return super.getText();
	}
}
