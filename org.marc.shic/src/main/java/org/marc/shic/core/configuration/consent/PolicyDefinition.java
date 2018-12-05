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
 * Date: 19-Sep-2013
 *
 */
package org.marc.shic.core.configuration.consent;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing a single Consent policy.
 */
public class PolicyDefinition {
	private String m_displayName;
	private String m_code;
	private String m_codeSystem;
	private String m_policyDocumentUrl;
	private double m_acknowledgementDuration;
	private List<AclDefinition> m_aclDefinitions;

	/**
	 * A default constructor to instantiate the list
	 */
	public PolicyDefinition() {
		this.m_aclDefinitions = new ArrayList<AclDefinition>();
	}

	public String getDisplayName() {
		return m_displayName;
	}

	public void setDisplayName(String displayName) {
		this.m_displayName = displayName;
	}

	public String getCode() {
		return m_code;
	}

	public void setCode(String code) {
		this.m_code = code;
	}

	public String getCodeSystem() {
		return m_codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.m_codeSystem = codeSystem;
	}

	public double getAcknowledgementDuration() {
		return m_acknowledgementDuration;
	}

	public void setAcknowledgementDuration(double acknowledgementDuration) {
		this.m_acknowledgementDuration = acknowledgementDuration;
	}

	public String getPolicyDocumentUrl() {
		return m_policyDocumentUrl;
	}

	public void setPolicyDocumentUrl(String policyDocumentUrl) {
		this.m_policyDocumentUrl = policyDocumentUrl;
	}

	public List<AclDefinition> getAcl() {
		return m_aclDefinitions;
	}

	public void addAclDefinition(AclDefinition aclDefinition) {
		this.m_aclDefinitions.add(aclDefinition);
	}
}
