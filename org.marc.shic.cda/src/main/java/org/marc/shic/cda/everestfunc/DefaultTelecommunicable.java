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
 * @since 1-Mar-2014
 *
 */
package org.marc.shic.cda.everestfunc;

import java.util.HashMap;
import java.util.Set;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;

/**
 * Provides common default functionality for a telecommunicable template.
 *
 * @author Ryan Albert
 */
public class DefaultTelecommunicable extends DefaultImpl implements Telecommunicable {

	private static HashMap<Class, DefaultTelecommunicable> telecommunicableMap = new HashMap<Class, DefaultTelecommunicable>();

	/**
	 * Creates and/or gets the default implementation for Telecommunicable
	 * for the provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Telecommunicable functionality for.
	 * @return A DefaultTelecommunicable object.
	 */
	public static Telecommunicable getTelecommunicable(InfrastructureRoot target) {
		Class targetClass = target.getClass();
		DefaultTelecommunicable result;
		if (!telecommunicableMap.containsKey(targetClass)) {
			result = new DefaultTelecommunicable(targetClass);
			telecommunicableMap.put(targetClass, result);
		} else {
			result = telecommunicableMap.get(targetClass);
		}
		result.setTarget(target);
		return result;
	}

	private DefaultTelecommunicable(Class root) {
		super(root, "Telecom");
	}

	/**
	 * Adds an unspecified telecommunication method.
	 *
	 * @param tel A String containing the telecom value.
	 * @return The resulting everest TEL object, added to the set.
	 */
	@Override
	public TEL addTelecom(String tel) {
		return addTelecom(null, tel);
	}

	/**
	 * Adds a telecommunication method with a specific use.
	 *
	 * @param use A specific usage for the method of communication.
	 * @param tel A String containing the telecom value.
	 * @return The created/modified everest TEL object added to the set.
	 */
	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		TEL result = null;
		if (tel != null && !tel.isEmpty()) {
			result = new TEL(tel, use);
			addTelecom(result);
		}
		return result;
	}

	/**
	 * Adds an existing telecoms object.
	 *
	 * @param tel The everest TEL object to add.
	 */
	@Override
	public void addTelecom(TEL tel) {
		if (isCollection()) {
			if (tel != null) {
				Set<TEL> telecoms = getTelecoms();
				if (telecoms == null) {
					telecoms = new SET();
					setter(telecoms);
				}
				telecoms.add(tel);
			}
		} else {
			setter(tel);
		}
	}

	/**
	 * Gets a set of everest TEL objects that are set upon the Template.
	 *
	 * @return A set of everest TEL objects.
	 */
	@Override
	public Set<TEL> getTelecoms() {
		Set result = null;
		if (isCollection()) {
			result = (SET) getter();
		} else {
			TEL telecom = (TEL) getter();
			if (telecom != null) {
				result = new SET();
				result.add((TEL) getter());
			}
		}
		return result;
	}

}
