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
 * @since 7-Feb-2014
 *
 */
package org.marc.shic.cda.templates.clinicalstatements;

import java.util.ArrayList;
import java.util.Arrays;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * The base class for a series of everest ClinicalStatement objects. Essentially
 * is contained as a child within SectionTemplate objects.
 *
 * @author Ryan Albert
 * @param <T>
 */
public abstract class ClinicalStatementTemplate<T extends ClinicalStatement> extends Template<T> {

	private ArrayList<String> entryData;
	private String reference;

	/**
	 * Constructs the ClinicalStatement by setting the root object to the
	 * one provided and sets the conforming standard to the one specified.
	 *
	 * @param root
	 * @param standard
	 */
	public ClinicalStatementTemplate(T root, CDAStandard standard) {
		super(root, standard);

		entryData = new ArrayList<String>();
	}

	/**
	 * Gets the narrative reference text that refers to this object.
	 *
	 * @return
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Sets the reference ID to the narrative text block representing this
	 * object.
	 *
	 * @param reference
	 */
	public void setReference(String reference) {
		this.reference = reference;
		setReferenceText(String.format("#%s", reference));
	}

	/**
	 * Sets the reference text of the ClinicalStatement
	 *
	 * @param reference
	 */
	protected abstract void setReferenceText(String reference);

	/**
	 * Adds an entry relationship via a clinical statement.
	 *
	 * @param statement
	 * @return
	 */
	protected final boolean addEntryRelationship(ClinicalStatement statement) {
		if (statement != null) {
			EntryRelationship entry = new EntryRelationship();
			entry.setClinicalStatement(statement);
			entry.setTypeCode(x_ActRelationshipEntryRelationship.HasComponent);
			entry.setContextConductionInd(true);
			return getRoot().getEntryRelationship().add(entry);
		}
		return false;
	}

	/**
	 * Adds an entry relationship via a clinical statement template.
	 *
	 * @param statement
	 * @return
	 */
	protected final boolean addEntryRelationship(ClinicalStatementTemplate statement) {
		return addEntryRelationship(statement.getRoot());
	}

	/**
	 * Removes an entry relationship set to this clinical statement by
	 * clinical statement.
	 *
	 * @param statement
	 * @return
	 */
	protected final boolean removeEntryRelationship(ClinicalStatement statement) {
		EntryRelationship remove = null;
		for (EntryRelationship entry : getRoot().getEntryRelationship()) {
			if (entry.getClinicalStatement().getClass() == statement.getClass()) {
				remove = entry;
				break;
			}
		}
		return getRoot().getEntryRelationship().remove(remove);
	}

	/**
	 * Removes an entry relationship set to this clinical statement by
	 * template.
	 *
	 * @param statement
	 * @return
	 */
	protected final boolean removeEntryRelationship(ClinicalStatementTemplate statement) {
		return removeEntryRelationship(statement.getRoot());
	}

	/**
	 * Sets the display text of a specific column to the given text.
	 *
	 * @param txt
	 * @param colIdx
	 */
	public void setDisplayText(String txt, int colIdx) {
		if (colIdx >= entryData.size()) {
			while (entryData.size() <= colIdx) {
				entryData.add(null);
			}
		}
		entryData.set(colIdx, txt);
	}

	/**
	 * Sets the row of the display to the specified strings starting from
	 * the first element.
	 *
	 * @param data
	 */
	public void setDisplayText(String... data) {
		entryData.clear();
		entryData.addAll(Arrays.asList(data));
	}

	/**
	 * Gets a list of strings that represent the row data displayed in the
	 * table of the section this ClinicalStatement is a part of. This is
	 * only populated after a call to the parent section's finalizeRoot()
	 * method.
	 *
	 * @return
	 */
	public ArrayList<String> getDisplayData() {
		return entryData;
	}

	/**
	 * Returns the clinical statement.
	 *
	 * @return
	 */
	@Override
	public T getRoot() {
		return (T) super.getRoot();
	}
}
