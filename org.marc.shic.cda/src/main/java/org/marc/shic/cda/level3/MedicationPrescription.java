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

import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Supply;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.templates.AuthorTemplate;
import org.marc.shic.cda.templates.AuthoringPersonTemplate;
import org.marc.shic.cda.templates.clinicalstatements.SupplyTemplate;

/**
 * A part of the medication that describes a prescription event. The
 * prescription contains at a minimum, an author or prescriber, and the time at
 * which the medication was prescribed.
 *
 * @author Ryan Albert
 */
public final class MedicationPrescription extends SupplyTemplate {

	/**
	 * Constructs a new entry conforming to the specified standard.
	 *
	 * @param standard The standard to conform to.
	 * @param identifier An II object that identifies the prescription.
	 */
	protected MedicationPrescription(CDAStandard standard, II identifier) {
		this(null, standard);
		addId(identifier);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public MedicationPrescription(Supply root, CDAStandard standard) {
		super(root, standard);
		//Populate prescribers (author)
		for (Author author : getRoot().getAuthor()) {
			addChild(AuthorTemplate.wrapAuthor(author, standard));
		}
	}

	@Override
	protected void initRoot() {
		getRoot().setMoodCode(x_DocumentSubstanceMood.Intent);
	}

	/**
	 * Creates and sets a new prescribing author, which should then have
	 * more data populated to it.
	 *
	 * @return An AuthorTemplate object created and added to the
	 * prescription event.
	 */
	public AuthorTemplate addPrescriber() {
		AuthoringPersonTemplate result = new AuthoringPersonTemplate(getStandard());
		addChild(result);
		getRoot().getAuthor().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and sets a new prescribing author initialized with an
	 * identifier, which should then have more data populated to it.
	 *
	 * @param rootId A String containing the root OID or UUID identifier.
	 * @param extId A String containing the identifier extension
	 * @return An AuthorTemplate object created and added to the
	 * prescription event.
	 */
	public AuthorTemplate addPrescriber(String rootId, String extId) {
		AuthorTemplate result = addPrescriber();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and sets a new prescribing author initialized with an
	 * identifier and a first and last name, which should then have more
	 * data populated to it.
	 *
	 * @param rootId A String containing the root OID or UUID identifier.
	 * @param extId A String containing the identifier extension
	 * @param firstName A String containing the first name of the author.
	 * @param lastName A String containing the last name of the author.
	 * @return An AuthorTemplate object created and added to the
	 * prescription event.
	 */
	public AuthorTemplate addPrescriber(String rootId, String extId, String firstName, String lastName) {
		AuthoringPersonTemplate result = (AuthoringPersonTemplate) addPrescriber(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Given);
		return result;
	}

	/**
	 * Creates and sets a new prescribing author initialized with an
	 * identifier, a first and last name, and the time of authoring.
	 *
	 * @param rootId A String containing the root OID or UUID identifier.
	 * @param extId A String containing the identifier extension
	 * @param firstName A String containing the first name of the author.
	 * @param lastName A String containing the last name of the author.
	 * @param prescribeTime The Time that the prescription event took place.
	 * @return An AuthorTemplate object created and added to the
	 * prescription event.
	 */
	public AuthorTemplate addPrescriber(String rootId, String extId, String firstName, String lastName, Time prescribeTime) {
		AuthorTemplate result = addPrescriber(rootId, extId, firstName, lastName);
		result.setTime(prescribeTime);
		return result;
	}
}
