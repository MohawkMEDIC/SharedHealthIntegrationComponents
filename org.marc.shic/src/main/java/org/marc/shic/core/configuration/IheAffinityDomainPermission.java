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
 * Date: March 05, 2014
 * 
 */

package org.marc.shic.core.configuration;

import org.marc.shic.core.exceptions.IheConfigurationException;

/**
 * Affinity domain permissions (ReadOnly, WriteOnly, and ReadWrite)
 * 
 * ReadOnly = Can only query, but not export (PIX, XDS, etc...) WriteOnly = Can
 * only export, but not query (PIX, XDS, etc...) ReadWrite = Can query and
 * export
 * 
 * @author ibrahimm
 */
public enum IheAffinityDomainPermission {

	ReadOnly("R"), WriteOnly("W"), ReadWrite("RW");

	private final String m_permission;

	private IheAffinityDomainPermission(final String permission) {
		this.m_permission = permission;
	}
	
	/**
	 * Returns the IheAffinityDomainPermission enum that matches the specific supplied permission
	 * 
	 * @param permission the permission to match against
	 * @return IheAffinityDomainPermission permission matching
	 * @throws IheConfigurationException An unknown permission was supplied
	 */
	public static IheAffinityDomainPermission fromString(String permission) throws IheConfigurationException {
		if (permission != null) {
			for (IheAffinityDomainPermission b : IheAffinityDomainPermission.values()) {
				if (permission.equalsIgnoreCase(b.m_permission)) {
					return b;
				}
			}
		}
		throw new IheConfigurationException("Unknown affinity domain permission: " + permission);
	}

	@Override
	public String toString() {
		return m_permission;
	}
}
