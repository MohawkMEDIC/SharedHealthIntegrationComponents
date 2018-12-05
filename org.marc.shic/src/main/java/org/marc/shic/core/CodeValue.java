/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
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
package org.marc.shic.core;

/**
 * 
 * @author Nebri
 */
public class CodeValue {

	private String codeSystem;
	private String code;
	private String displayName;
	private String codeSystemName;

	/**
	 * Default constructor
	 */
	public CodeValue() {
	}

	/**
	 * Constructor accepting the specified code, code system, and display name
	 */
	public CodeValue(String code, String codeSystem, String displayName) {
		this.code = code;
		this.codeSystem = codeSystem;
		this.displayName = displayName;
	}

	/**
	 * Constructor accepting the specified code, code system, display name, and
	 * code system name
	 */
	public CodeValue(String code, String codeSystem, String displayName,
			String codeSystemName) {
		this(code, codeSystem, displayName);
		this.codeSystemName = codeSystemName;
	}

	/**
	 * 
	 * @param codeSystem
	 */
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodeSystem() {
		return codeSystem;
	}

	/**
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodeSystemName() {
		return codeSystemName;
	}

	/**
	 * 
	 * @param codeSystemName
	 */
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

}
