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
 * @author Mohamed Ibrahim Date: Aug 22, 2013
 *
 */
package org.marc.shic.cda.level1;

import org.marc.everest.datatypes.BL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DataEnterer;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.NonXMLBody;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.cda.utils.DataTypeHelpers;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.cda.templates.AuthorTemplate;
import org.marc.shic.cda.templates.AuthoringDeviceTemplate;

/**
 * Provides a type of CDA document that provides level 1 functionality.
 *
 * @author Mohamed Ibrahim
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.2.20")
public class Level1Document extends DocumentTemplate {

	/**
	 * Creates a generic Level1 Document.
	 *
	 * @param standard
	 */
	public Level1Document(CDAStandard standard) {
		this(null, CDAStandard.CCD);
	}

	/**
	 * Wraps an existing ClinicalDocument to conform to a specific
	 * CDAStandard.
	 *
	 * @param doc
	 * @param standard
	 */
	public Level1Document(ClinicalDocument doc, CDAStandard standard) {
		super(doc, standard);
	}

	/**
	 * Adds a nonXmlBody tag (for a Scanned document) in the CDA body
	 *
	 * @param content
	 * @param mediaType
	 */
	public void setNonXmlBody(byte[] content, String mediaType) {
		NonXMLBody dataBody = new NonXMLBody();
		Component2 componentBody = new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE, dataBody);
		dataBody.setText(content, mediaType);

		getRoot().setComponent(componentBody);
	}

	/**
	 * Gets the content of the level 1 document, in bytes.
	 *
	 * @return
	 */
	public byte[] getContent() {
		return getRoot().getComponent().getBodyChoiceIfNonXMLBody().getText().getData();
	}

	/**
	 * Gets the media type of the content.
	 *
	 * @return
	 */
	public String getMediaType() {
		return getRoot().getComponent().getBodyChoiceIfNonXMLBody().getText().getMediaType();
	}

	/**
	 * Adds a scanning device to the document.
	 *
	 * @param dicomCode A DICOM code identifying the type of scanner.
	 * @param manufacturerModel A String representing the name of the
	 * manufacturer model.
	 * @param softwareName A String representing the name of the software
	 * running the scanner.
	 * @return An AuthorTemplate that has been created and added.
	 */
	public AuthorTemplate addScanner(Code dicomCode, String manufacturerModel, String softwareName) {
		AuthoringDeviceTemplate result = new AuthoringDeviceTemplate(getStandard());
		result.setDeviceCode(dicomCode.ensureSystem("1.2.840.10008.2.16.4"));
		result.setManufacturerModelName(manufacturerModel);
		result.setSoftwareName(softwareName);
		addChild(result);
		getRoot().getAuthor().add(result.getRoot());
		return result;
	}

	/**
	 * Adds an author (Scanner) to the CDA Document
	 *
	 * @param location
	 * @param manufacturer
	 * @param softwareName
	 */
	@Deprecated
	public void addScanner(LocationDemographic location, String manufacturer, String softwareName) {
		Author scanner = DataTypeHelpers.createScanner(location, getRoot().getEffectiveTime(), manufacturer, softwareName);
		addChild(scanner, getStandard()).addTemplateRule(TemplateRuleID.PCC_ScanningDevice);
		getRoot().getAuthor().add(scanner);
	}

	/**
	 * Sets a Custodian element in the CDA Document
	 *
	 * @param facility
	 */
	@Deprecated
	public void setCustodian(LocationDemographic facility) {
		getRoot().setCustodian(DataTypeHelpers.createCustodian(facility));
	}

	/**
	 * Sets the dataEnterer element in the CDA document
	 *
	 * @param operator
	 */
	@Deprecated
	public void setDataEnterer(PersonDemographic operator) {
		DataEnterer enterer = DataTypeHelpers.createDataEnterer(operator, getRoot().getEffectiveTime());
		addChild(enterer, getStandard()).addTemplateRule(TemplateRuleID.PCC_ScannerOperator);
		getRoot().setDataEnterer(enterer);
	}
}
