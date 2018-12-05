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
 * @author Mohamed Ibrahim
 * Date: 06-Nov-2013
 *
 */
package org.marc.shic.core.configuration;

import java.util.ArrayList;
import java.util.List;

import org.marc.shic.core.CodeValue;

/**
 * @author ibrahimm
 *
 */
public class IheCodeMappingConfiguration {
	
	private String attribute;
	private String description;
	private List<CodeValue> codes = new ArrayList<CodeValue>();
	
	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the codes
	 */
	public List<CodeValue> getCodes() {
		return codes;
	}

	/**
	 * @param code the code to add
	 */
	public void addCode(CodeValue code) {
		codes.add(code);
	}
	
}
