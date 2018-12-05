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
 * @since 4-Feb-2014
 *
 */
package org.marc.shic.cda.level3;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Product;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Supply;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryAct;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentActMood;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.templates.ManufacturedProductTemplate;
import org.marc.shic.cda.templates.Performer2Template;
import org.marc.shic.cda.templates.clinicalstatements.SupplyTemplate;

/**
 * A part of the medication that defines a pharmaceutical dispensing event. At a
 * minimum, the dispenser/medication performer information must be set.
 *
 * @author Ryan Albert
 */
public final class MedicationDispensary extends SupplyTemplate {

	/**
	 * Constructs a new entry conforming to the specified standard.
	 *
	 * @param standard The standard to conform to.
	 */
	protected MedicationDispensary(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public MedicationDispensary(Supply root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	protected void initRoot() {
		EntryRelationship entry = new EntryRelationship();
		Act specialInstructions = new Act();
		specialInstructions.setClassCode(x_ActClassDocumentEntryAct.Act);
		specialInstructions.setMoodCode(x_DocumentActMood.Intent);

		entry.setClinicalStatement(specialInstructions);
		getRoot().getEntryRelationship().add(entry);

		getRoot().setMoodCode(x_DocumentSubstanceMood.Eventoccurrence);
	}

	/**
	 * Creates and adds a performer to the dispensing event, which should
	 * then have data populated for it.
	 *
	 * @return A created and added Performer2Template object.
	 */
	public Performer2Template addPerformer() {
		Performer2Template result = new Performer2Template(getStandard());
		addChild(result);
		getRoot().getPerformer().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and sets a product to the dispensing event, which should then
	 * have data populated for it.
	 *
	 * @return A created and added ManufacturedProductTemplate
	 */
	public ManufacturedProductTemplate setProduct() {
		ManufacturedProductTemplate result = new ManufacturedProductTemplate(getStandard());
		addChild(result);
		getRoot().setProduct(new Product(result.getRoot()));
		return result;
	}

	/**
	 * Sets the instructions for the patient on how to administer the
	 * medication.
	 *
	 * @param text
	 */
	public void setInstructionText(ED text) {
		((Template<Act>) getChildRoot(Act.class)).getRoot().setText(text);
	}

	/**
	 * Sets the language code of the instruction text.
	 *
	 * @param language
	 */
	public void setLanguageCode(CS<String> language) {
		((Template<Act>) getChildRoot(Act.class)).getRoot().setLanguageCode(language);
	}

}
