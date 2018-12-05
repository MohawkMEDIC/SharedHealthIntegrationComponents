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
 * @since 6-Feb-2014
 *
 */
package org.marc.shic.cda.exceptions;

import org.marc.shic.cda.Template;
import org.marc.shic.cda.templateRules.TemplateRuleID;

/**
 *
 * @author Ryan Albert
 */
public class TemplateViolationException extends Exception {

	private final TemplateRuleID templateRuleID;
	private final Template template;

	/**
	 * Constructs an exception that describes a violation that has been made
	 * within the structure of a template.
	 *
	 * @param template The Template that caused the exception.
	 * @param templateRuleID The TemplateRuleID that had been violated.
	 * @param msg Specific detail on why the exception occurred.
	 */
	public TemplateViolationException(Template template, TemplateRuleID templateRuleID, String msg) {
		super(String.format("In %s: %s", template, msg));
		this.templateRuleID = templateRuleID;
		this.template = template;
	}

	/**
	 * Gets the template rule that has been violated.
	 *
	 * @return The template rule that is violated.
	 */
	public TemplateRuleID getTemplateRuleID() {
		return templateRuleID;
	}

	/**
	 * Gets the Template that caused the exception.
	 *
	 * @return A Template object.
	 */
	public Template getTemplate() {
		return template;
	}
}
