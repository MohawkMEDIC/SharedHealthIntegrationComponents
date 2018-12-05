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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import java.util.EnumSet;
import org.apache.log4j.Logger;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.exceptions.TemplateViolationException;
import org.marc.shic.cda.utils.CdaUtils;

/**
 *
 * @author Paul
 */
public class AbstractTemplateRule {

	private TemplateRuleID _templateId;
	private EnumSet<TemplateRuleID> dependencies;
	private CDAStandard templateStandard;
	protected final Logger LOGGER = Logger.getLogger(this.getClass());

	protected AbstractTemplateRule() {
		dependencies = EnumSet.noneOf(TemplateRuleID.class);
	}

	public AbstractTemplateRule(TemplateRuleID templateId) {
		this();
		this._templateId = templateId;
	}

	public final TemplateRuleID getTemplateID() {
		return this._templateId;
	}

	protected final void addDependency(TemplateRuleID templateID) {
		dependencies.add(templateID);
	}

	protected final void setTemplateID(TemplateRuleID id) {
		_templateId = id;
	}

	public final Iterable<TemplateRuleID> getDependencies() {
		return dependencies;
	}

	/**
	 * Applies template rules to the template.
	 *
	 * @param temp
	 * @return
	 */
	public void apply(Template temp) {
		
	}

	public void addViolation(TemplateViolationException ex) {

	}

	public boolean testSingleConformanceRule(DocumentTemplate document, ConformanceRuleParts conformanceRule) {
		boolean validated = true;

		if (CdaUtils.testXpathValue(document, conformanceRule)) {
			LOGGER.debug("Xpath Validates***************************");
		} else {
			LOGGER.debug("Xpath FAILED Validation***************************");
			validated = false;
		}

		return validated;
	}

	public boolean validate(DocumentTemplate documentTemplate) {
		return _templateId.getTemplateRules() == null ? true : _templateId.getTemplateRules().runValidation(documentTemplate);
	}

	@Override
	public boolean equals(Object that) {
		return this.hashCode() == that.hashCode();
	}

	@Override
	public int hashCode() {
		return this.getTemplateID().getOid().hashCode();
	}

	@Override
	public String toString() {
		return getTemplateID().toString();
	}
}
