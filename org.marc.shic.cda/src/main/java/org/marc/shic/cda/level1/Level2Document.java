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
package org.marc.shic.cda.level1;

import org.marc.shic.cda.utils.SectionOrderComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.everest.datatypes.BL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component3;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.StructuredBody;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.level2.ImmunizationsSection;
import org.marc.shic.cda.level2.MedicationSection;
import org.marc.shic.cda.level2.ProblemSection;
import org.marc.shic.cda.level2.PurposeSection;
import org.marc.shic.cda.level2.ResultsSection;
import org.marc.shic.cda.level2.VitalSignsSection;

/**
 * Defines a type of CDA document that provides level 2 functionality
 * (Structured body)
 *
 * @author Ryan Albert
 */
public class Level2Document extends DocumentTemplate {

	private HashMap<SectionTemplate, Integer> sectionOrdering = new HashMap<SectionTemplate, Integer>();
	private ArrayList<SectionTemplate> sections = new ArrayList<SectionTemplate>();

	/**
	 * Instantiates the structured body and adds a purpose section to it.
	 *
	 * @param standard The standard that is to be used within the entire
	 * document.
	 */
	protected Level2Document(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing everest ClinicalDocument object and generates
	 * section Template objects to wrap the existing sections in the
	 * structured body.
	 *
	 * @param root The ClinicalDocument object to wrap.
	 * @param standard The standard that is to be used within the entire
	 * document.
	 */
	protected Level2Document(ClinicalDocument root, CDAStandard standard) {
		super(root, standard);

		if (getRoot().getComponent() != null && getRoot().getComponent().getBodyChoiceIfStructuredBody() != null) {
			for (Component3 comp : getRoot().getComponent().getBodyChoiceIfStructuredBody().getComponent()) {
				Section section = comp.getSection();
				Class sectionTemplateClass = SectionTemplate.getCodeAssociations().get(section.getCode().getCode());
				if (sectionTemplateClass != null) {
					try {
						SectionTemplate sectionTemplate = (SectionTemplate) sectionTemplateClass.getConstructor(Section.class, CDAStandard.class).newInstance(section, getStandard());
						addChild(sectionTemplate);
					} catch (Exception ex) {
						LOGGER.error(String.format("Unable to create section template for a %s", sectionTemplateClass.getName()), ex);
					}
				}
			}
		} else {
			if (getRoot().getComponent() == null) {
				getRoot().setComponent(new Component2());
			}
			if (getRoot().getComponent().getBodyChoiceIfStructuredBody() == null) {
				getRoot().getComponent().setBodyChoice(new StructuredBody());
			}
		}
	}

	/**
	 * Initialization logic for creating a new ClinicalDocument object.
	 */
	@Override
	protected void initRoot() {
		super.initRoot();
		getRoot().setComponent(
			new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE, new StructuredBody()));

		getPurposeSection();
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A ProblemSection Template.
	 */
	public final ProblemSection getProblemSection() {
		ProblemSection section = (ProblemSection) getChild(ProblemSection.class);
		if (section == null) {
			section = new ProblemSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A VitalSignsSection Template.
	 */
	public final VitalSignsSection getVitalSignsSection() {
		VitalSignsSection section = (VitalSignsSection) getChild(VitalSignsSection.class);
		if (section == null) {
			section = new VitalSignsSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A ImmunizationsSection Template.
	 */
	public final ImmunizationsSection getImmunizationsSection() {
		ImmunizationsSection section = (ImmunizationsSection) getChild(ImmunizationsSection.class);
		if (section == null) {
			section = new ImmunizationsSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A MedicationSection Template.
	 */
	public final MedicationSection getMedicationSection() {
		MedicationSection section = (MedicationSection) getChild(MedicationSection.class);
		if (section == null) {
			section = new MedicationSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A PurposeSection Template.
	 */
	public final PurposeSection getPurposeSection() {
		PurposeSection section = (PurposeSection) getChild(PurposeSection.class);
		if (section == null) {
			section = new PurposeSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Gets the existing Template that represents the specific section. If
	 * the Template does not exist, it will be created and added as a child.
	 *
	 * @return A ResultsSection Template.
	 */
	public final ResultsSection getResultsSection() {
		ResultsSection section = (ResultsSection) getChild(ResultsSection.class);
		if (section == null) {
			section = new ResultsSection(getStandard());
			addChild(section);
		}
		return section;
	}

	/**
	 * Adds the sections contained within the child SectionTemplate to the
	 * document, based on their order.
	 */
	@Override
	protected void finalizeRoot() {
		List<SectionTemplate> allSections = getChildrenRoot(Section.class);
		Collections.sort(allSections, SectionOrderComparator.getInstance());

		getRoot().getComponent().getBodyChoiceIfStructuredBody().setComponent(new ArrayList());
		for (SectionTemplate section : allSections) {
			getRoot().getComponent().getBodyChoiceIfStructuredBody().getComponent().add(new Component3(ActRelationshipHasComponent.HasComponent, BL.TRUE, section.getRoot()));
		}
		super.finalizeRoot();
	}

}
