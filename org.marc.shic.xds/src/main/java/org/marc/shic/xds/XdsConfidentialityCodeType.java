/* 
 * Copyright 2012 Mohawk College of Applied Arts and Technology
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
 * User: Justin Fyfe
 * Date: 11-08-2012
 */
package org.marc.shic.xds;

/**
 * XDS Document entry confidentiality
 */
public class XdsConfidentialityCodeType {

	/**
	 * Document is submitted
	 */
	public static final XdsConfidentialityCodeType NORMAL = new XdsConfidentialityCodeType("1.3.6.1.4.1.21367.2006.7.107","Connect-a-thon confidentialityCodes");
	/**
	 * Document has been approved for sharing
	 */
	public static final XdsConfidentialityCodeType RESTRICTED = new XdsConfidentialityCodeType("1.3.6.1.4.1.21367.2006.7.109","Connect-a-thon confidentialityCodes");
	/**
	 * Document entry that is deprecated
	 */
	public static final XdsConfidentialityCodeType EMERGENCY_ONLY = new XdsConfidentialityCodeType("1.3.6.1.4.1.21367.2006.7.110","Connect-a-thon confidentialityCodes");
	
	private final String m_code;
	private final String m_scheme;
	
	/**
	 * Create a status type
	 * @param code
	 */
	private XdsConfidentialityCodeType(String code, String scheme)
	{
		this.m_code = code;
		this.m_scheme = scheme;
	}
	
	/**
	 * Get the code for the XDS document entry
	 * @return
	 */
	public String getScheme()
	{
		return this.m_scheme;
	}
	
	/**
	 * Get the code for the XDS document entry
	 * @return
	 */
	public String getCode()
	{
		return this.m_code;
	}
}
