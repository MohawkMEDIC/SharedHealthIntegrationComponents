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
 * @author Ryan Albert Date: April 4, 2014
 *
 */
package org.marc.shic.cda.level1;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * Wraps a ClinicalDocument and implements the rules required for a XDS-SD.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.2.20")
public class ScannedDocument extends Level1Document {

	/**
	 * Constructs a new ScannedDocument.
	 */
	public ScannedDocument() {
		this(null);
	}

	/**
	 * Wraps an existing ClinicalDocument object.
	 *
	 * @param root The ClinicalDocument object to wrap, or null to create a
	 * new one.
	 */
	public ScannedDocument(ClinicalDocument root) {
		super(root, CDAStandard.PCC);
	}

	/**
	 * Sets the title of the scanned document.
	 *
	 * @param title A String value to set the title to.
	 */
	public void setTitle(String title) {
		getRoot().setTitle(title);
	}

	/**
	 * Gets the title of the scanned document.
	 *
	 * @return A String value containing the title text.
	 */
	public String getTitle() {
		return getRoot().getTitle().getValue();
	}
}
