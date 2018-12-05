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

/**
 * @author ibrahimm
 *
 */
public class IheValueSetConfiguration {
	
	private String m_id;
	private String m_description;
	private String m_attribute;
	
	/**
	 * @return the value set id
	 */
	public String getId() {
		return m_id;
	}
	
	/**
	 * @param id the value set id to set
	 */
	public void setId(String id) {
		this.m_id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return m_description;
	}

	/**
	 * @param name the description to set
	 */
	public void setDescription(String description) {
		this.m_description = description;
	}

	/**
	 * @return the classification attribute
	 */
	public String getAttribute() {
		return m_attribute;
	}

	/**
	 * @param attribute the classification attribute to set
	 */
	public void setAttribute(String attribute) {
		this.m_attribute = attribute;
	}

	/**
	 * @param id the value set id
	 * @param description a describing the value set
	 * @param attribute a classification attribute
	 */
	public IheValueSetConfiguration(String id, String description, String attribute) {
		this.m_id = id;
		this.m_description = description;
		this.m_attribute = attribute;
	}
	
	
}
