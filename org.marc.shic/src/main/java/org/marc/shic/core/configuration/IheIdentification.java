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
package org.marc.shic.core.configuration;

/**
 * 
 * @author Mohamed
 */
public class IheIdentification {

	private String applicationId;
	private String facilityName;

	public IheIdentification() {

	}

	/**
	 * 
	 * @param data
	 */
	public IheIdentification(String data) {
		applicationId = data;
	}

	/**
	 * 
	 * @param applicationId
	 * @param facilityName
	 */
	public IheIdentification(String applicationId, String facilityName) {
		this.applicationId = applicationId;
		this.facilityName = facilityName;
	}

	/**
	 * 
	 * @return
	 */
	public String getApplicationId() {
		return this.applicationId;
	}

	/**
	 * 
	 * @param applicationId
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * 
	 * @return
	 */
	public String getFacilityName() {
		return this.facilityName;
	}

	/**
	 * 
	 * @param facilityName
	 */
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
}