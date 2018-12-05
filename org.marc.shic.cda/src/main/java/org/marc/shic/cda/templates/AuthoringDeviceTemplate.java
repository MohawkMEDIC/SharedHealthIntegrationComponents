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
 * @since 3-Apr-2014
 *
 */
package org.marc.shic.cda.templates;

import org.marc.everest.datatypes.SC;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AuthoringDevice;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;

/**
 *
 * @author Ryan Albert
 */
@TemplateID(standard=CDAStandard.PCC, oid="1.3.6.1.4.1.19376.1.2.20.2")
public class AuthoringDeviceTemplate extends AuthorTemplate {
	
	private AuthoringDevice device;

	public AuthoringDeviceTemplate(CDAStandard standard) {
		this(null, standard);
	}
	
	public AuthoringDeviceTemplate(Author root, CDAStandard standard) {
		super(root, standard);
		device = getRoot().getAssignedAuthor().getAssignedAuthorChoiceIfAssignedAuthoringDevice();
		if(device == null) {
			device = new AuthoringDevice();
			getRoot().getAssignedAuthor().setAssignedAuthorChoice(device);
		}
	}
	
	public void setManufacturerModelName(String name) {
		device.setManufacturerModelName(new SC(name));
	}
	
	public String getManufacturerModelName() {
		return device.getManufacturerModelName().getValue();
	}

	public void setSoftwareName(String name) {
		device.setSoftwareName(new SC(name));
	}
	
	public String getSoftwareName() {
		return device.getSoftwareName().getValue();
	}
	
	public void setDeviceCode(Code code) {
		device.setCode((CE<String>) code.getCode(CE.class));
	}
	
	public Code getDeviceCode() {
		return new Code(device.getCode());
	}
}
