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
package org.marc.shic.cda.templates;

import org.marc.shic.cda.templates.clinicalstatements.ClinicalStatementTemplate;
import org.marc.shic.cda.utils.CoreDataTypeHelpers;
import org.marc.shic.cda.utils.NarrativeBlockUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBElement;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.SD;
import org.marc.everest.datatypes.EncapsulatedDataRepresentation;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.doc.StructDocElementNode;
import org.marc.everest.datatypes.doc.StructDocNode;
import org.marc.everest.datatypes.doc.StructDocTextNode;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component5;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Informant12;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Subject;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActClassDocumentSection;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActMoodEventOccurrence;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.xds.registry.ObjectFactory;
import org.marc.shic.xds.registry.StrucDocItem;
import org.marc.shic.xds.registry.StrucDocList;
import org.marc.shic.xds.registry.StrucDocTable;
import org.marc.shic.xds.registry.StrucDocTbody;
import org.marc.shic.xds.registry.StrucDocText;
import org.marc.shic.xds.registry.StrucDocTd;
import org.marc.shic.xds.registry.StrucDocTh;
import org.marc.shic.xds.registry.StrucDocThead;
import org.marc.shic.xds.registry.StrucDocTr;
import org.reflections.Reflections;

/**
 * Provides the base class for a set of sections that are contained within a CCD
 * document.
 *
 * @author Ryan Albert
 */
public abstract class SectionTemplate extends Template<Section> implements Codeable {

	private static final HashMap<String, Class<? extends SectionTemplate>> codedSections = new HashMap();

	private final ArrayList<String> columnNames;

	/**
	 * Generates cached associations between section classes and their
	 * defined codes with the DefineSection annotation.
	 */
	static {
		Reflections reflections = new Reflections("org.marc.shic.cda.level2");
		for (Class section : reflections.getSubTypesOf(SectionTemplate.class)) {
			DefineSection sectionDefinition = (DefineSection) section.getAnnotation(DefineSection.class);
			if (sectionDefinition != null) {
				codedSections.put(sectionDefinition.code(), section);
			} else {
				Logger.getLogger(SectionTemplate.class).error(String.format("The %s class does not have required %s metadata", section.getSimpleName(), DefineSection.class.getSimpleName()));
			}
		}
	}

	/**
	 * Returns the code associations of all section templates.
	 *
	 * @return
	 */
	public static HashMap<String, Class<? extends SectionTemplate>> getCodeAssociations() {
		return codedSections;
	}

	public static DefineSection getSectionDefinition(SectionTemplate section) {
		return (DefineSection) section.getClass().getAnnotation(DefineSection.class);
	}

	/**
	 * Constructs a SectionTemplate and begins the display table XML.
	 *
	 * @param standard
	 */
	protected SectionTemplate(CDAStandard standard) {
		this(null, standard);
	}

	protected SectionTemplate(Section section, CDAStandard standard) {
		super(section, standard);
		columnNames = new ArrayList<String>();
	}

	@Override
	protected void initRoot() {
		DefineSection sectionInfo = getSectionDefinition(this);
		if (sectionInfo == null) {
			throw new UnsupportedOperationException(
				String.format("Cannot instantiate section %s, which does not have metadata %s set.",
					getClass().getSimpleName(), DefineSection.class.getSimpleName()));
		}
		setCode(new Code(sectionInfo.code(), "2.16.840.1.113883.6.1", sectionInfo.codeName(), "LOINC"));
		setTitle(sectionInfo.title());
	}

	/**
	 * Adds an entry of arbitrary string data to the display table. Only use
	 * for sections that have no functionality. Use only when no Level 3
	 * objects exist. section.
	 *
	 * @param displayData
	 */
	public void addDisplayEntry(String... displayData) {
		//displayEntries.add(Arrays.asList(displayData));
		ClinicalStatementTemplate template = new ClinicalStatementTemplate(new Observation(), getStandard()) {
			@Override
			protected void setReferenceText(String reference) {
				((Observation) getRoot()).setText(CoreDataTypeHelpers.createEDWithReference(reference));
			}
		};
		addChild(template);
		template.setDisplayText(displayData);
	}

	/**
	 * Generates a string that contains all of the XML data that has
	 * currently been filled into the display table.
	 *
	 * @return A string of table XML data.
	 */
	private String generateTable() {
		ObjectFactory elemFactory = new ObjectFactory();
		if (!getChildren().iterator().hasNext()) {
			JAXBElement<StrucDocList> list = elemFactory.createStrucDocTextList(new StrucDocList());
			StrucDocItem emptyItem = new StrucDocItem();
			emptyItem.getContent().add("There are no items in this section.");
			list.getValue().getItem().add(emptyItem);

			return NarrativeBlockUtil.JAXBToXMLString(list, StrucDocText.class);
		} else {
			JAXBElement<StrucDocTable> table = elemFactory.createStrucDocTextTable(new StrucDocTable());

			// 1. Populate column headers.
			if (!columnNames.isEmpty()) {
				StrucDocThead header = new StrucDocThead();
				StrucDocTr hRow = new StrucDocTr();
				for (String col : columnNames) {
					StrucDocTh colHeader = new StrucDocTh();
					colHeader.getContent().add(col);
					hRow.getThOrTd().add(colHeader);
				}
				header.getTr().add(hRow);
				table.getValue().setThead(header);
			}

			// 2. Fill rows with data
			StrucDocTbody body = new StrucDocTbody();
			for (Template rowData : getChildren()) {
				ClinicalStatementTemplate statement = (ClinicalStatementTemplate) rowData;
				StrucDocTr row = new StrucDocTr();
				if (statement.getReference() != null && !statement.getReference().isEmpty()) {
					row.setID(statement.getReference());
				}
				body.getTr().add(row);
				for (String data : (ArrayList<String>) statement.getDisplayData()) {
					StrucDocTd dataCell = new StrucDocTd();
					dataCell.getContent().add(data);
					row.getThOrTd().add(dataCell);
				}
			}
			table.getValue().getTbody().add(body);
			return NarrativeBlockUtil.JAXBToXMLString(table, StrucDocText.class);
		}
	}
        
        /**
	 * Generates a string that contains all of the XML data that has
	 * currently been filled into the display table.
	 *
	 * @return A string of XML data formatted as such:
         * <items><item title="column">value</item><item title="column">value</item><items>
         * <items><item title="column">value</item><items>
         * ...
	 */
	private List<StructDocNode> generateText() {
            List<StructDocNode> retVal = new ArrayList<StructDocNode>();

            if (!getChildren().iterator().hasNext()) {
                retVal.add(new StructDocTextNode("There are no items in this section."));

            } else {
                for (Template rowData : getChildren()) {
                    ClinicalStatementTemplate statement = (ClinicalStatementTemplate) rowData;

                    StructDocElementNode items = new StructDocElementNode("items");
                    if (statement.getReference() != null && !statement.getReference().isEmpty()) {
                        items.addAttribute("id", statement.getReference());
                    }

                    int index = 0;
                    for (String val : (ArrayList<String>) statement.getDisplayData()) {
                        StructDocElementNode item = new StructDocElementNode("item");
                        if (index < columnNames.size()) {
                            item.addAttribute("title", columnNames.get(index++));
                        }
                        
                        item.addText(val);
                        items.getChildren().add(item);
                    }

                    retVal.add(items);
                }
            }

            return retVal;
	}

	/**
	 * Sets the narrative text block of the section by generating the table
	 * data from child elements.
	 */
	@Override
	protected void finalizeRoot() {
		getRoot().setText(getTextSD());
		super.finalizeRoot();
	}

	/**
	 * Gets the encapsulated text data from the section. Generates the XML
	 * table and sets the encapsulated data representation of the ED to XML.
	 *
	 * @return
	 */
	public ED getText() {
		ED text = new ED(generateTable());
		text.setRepresentation(EncapsulatedDataRepresentation.Xml);
		return text;
	}

	/**
	 * Gets the encapsulated text data from the section. Generates a structured
	 * document table.
	 *
	 * @return
	 */
	public SD getTextSD() {
		SD retVal = new SD();
                retVal.setContent(generateText());
		return retVal;
	}

	/**
	 * Gets a string that provides the title of the section. Should be
	 * overridden by sub-classes for uniqueness.
	 *
	 * @return
	 */
	public final String getTitle() {
		if (getRoot().getTitle() != null) {
			return getRoot().getTitle().getValue();
		}
		return null;
	}

	/**
	 * Adds string data to the display table's column header row. The number
	 * of columns will increase by the number of strings given.
	 *
	 * @param columnNames A variable number of strings that represent each
	 * column to be added.
	 */
	public final void addTableColumns(String... columnNames) {
		this.columnNames.addAll(Arrays.asList(columnNames));
	}

	/**
	 * Clears the display table's column header row, and adds string data to
	 * it. The number of strings given will reflect the number of columns.
	 *
	 * @param columnNames A variable number of strings that represent each
	 * column.
	 */
	public final void setTableColumns(String... columnNames) {
		getTableColumns().clear();
		addTableColumns(columnNames);
	}

	/**
	 * Gets the list that contains each column item in the table header row.
	 *
	 * @return Returns a list of objects, representing the column header row
	 * data.
	 */
	public ArrayList<String> getTableColumns() {
		return columnNames;
	}

	public int getSectionOrder() {
		return getClass().getAnnotation(DefineSection.class).order();
	}

	/////////////////////////////////////////////////////////////////////////////
	////// CODEABLE INTERFACE METHODS
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Get a Code
	 *
	 * @return
	 */
	@Override
	public final Code getCode() {
		return DefaultCodeable.getCodeable(getRoot()).getCode();
	}

	/**
	 * Sets a code
	 *
	 * @param value
	 */
	@Override
	public final void setCode(Code value) {
		DefaultCodeable.getCodeable(getRoot()).setCode(value);
	}

	/////////////////////////////////////////////////////////////////////////////
	////// AUTO-GENERATED METHODS WRAPPING THE ROOT
	/////////////////////////////////////////////////////////////////////////////
	public CS<ActClassDocumentSection> getClassCode() {
		return getRoot().getClassCode();
	}

	public void overrideClassCode(CS<ActClassDocumentSection> value) {
		getRoot().overrideClassCode(value);
	}

	public void overrideClassCode(ActClassDocumentSection code) {
		getRoot().overrideClassCode(code);
	}

	public CS<ActMoodEventOccurrence> getMoodCode() {
		return getRoot().getMoodCode();
	}

	public void overrideMoodCode(CS<ActMoodEventOccurrence> value) {
		getRoot().overrideMoodCode(value);
	}

	public void overrideMoodCode(ActMoodEventOccurrence code) {
		getRoot().overrideMoodCode(code);
	}

	public II getId() {
		return getRoot().getId();
	}

	public void setId(II value) {
		getRoot().setId(value);
	}

	public void setId(UUID root) {
		getRoot().setId(root);
	}

	public void setId(String root) {
		getRoot().setId(root);
	}

	public void setId(String root, String extension) {
		getRoot().setId(root, extension);
	}

	public void setTitle(ST value) {
		getRoot().setTitle(value);
	}

	public final void setTitle(String value) {
		getRoot().setTitle(value);
	}

	public void setText(SD value) {
		getRoot().setText(value);
	}

//	public void setText(String value) throws UnsupportedEncodingException {
//		getRoot().setText(value);
//	}
//
//	public void setText(byte[] value, String mediaType) {
//		getRoot().setText(value, mediaType);
//	}

	public CE<x_BasicConfidentialityKind> getConfidentialityCode() {
		return getRoot().getConfidentialityCode();
	}

	public void setConfidentialityCode(CE<x_BasicConfidentialityKind> value) {
		getRoot().setConfidentialityCode(value);
	}

	public void setConfidentialityCode(x_BasicConfidentialityKind code) {
		getRoot().setConfidentialityCode(code);
	}

	public void setConfidentialityCode(x_BasicConfidentialityKind code, String codeSystem) {
		getRoot().setConfidentialityCode(code, codeSystem);
	}

	public CS<String> getLanguageCode() {
		return getRoot().getLanguageCode();
	}

	public void setLanguageCode(CS<String> value) {
		getRoot().setLanguageCode(value);
	}

	public void setLanguageCode(String code) {
		getRoot().setLanguageCode(code);
	}

	public Subject getSubject() {
		return getRoot().getSubject();
	}

	public void setSubject(Subject value) {
		getRoot().setSubject(value);
	}

	public ArrayList<Author> getAuthor() {
		return getRoot().getAuthor();
	}

	public void setAuthor(ArrayList<Author> value) {
		getRoot().setAuthor(value);
	}

	public ArrayList<Informant12> getInformant() {
		return getRoot().getInformant();
	}

	public void setInformant(ArrayList<Informant12> value) {
		getRoot().setInformant(value);
	}

	public ArrayList<Entry> getEntry() {
		return getRoot().getEntry();
	}

	public void setEntry(ArrayList<Entry> value) {
		getRoot().setEntry(value);
	}

	public ArrayList<Component5> getComponent() {
		return getRoot().getComponent();
	}

	public void setComponent(ArrayList<Component5> value) {
		getRoot().setComponent(value);
	}
}
