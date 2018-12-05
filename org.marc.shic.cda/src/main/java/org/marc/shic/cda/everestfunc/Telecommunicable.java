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

import java.util.Set;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;

/**
 * Provides headers for a Template wrapping an InfrastructureRoot that provides
 * telecommunication information functionality.
 *
 * @author Ryan Albert
 */
public interface Telecommunicable {

	/**
	 * Adds an unspecified telecommunication method.
	 *
	 * @param tel A String containing the telecom value.
	 * @return The resulting everest TEL object, added to the set.
	 */
	TEL addTelecom(String tel);

	/**
	 * Adds a telecommunication method with a specific use.
	 *
	 * @param use A specific usage for the method of communication.
	 * @param tel A String containing the telecom value.
	 * @return The created/modified everest TEL object added to the set.
	 */
	TEL addTelecom(TelecommunicationsAddressUse use, String tel);

	/**
	 * Adds an existing telecoms object.
	 *
	 * @param tel The everest TEL object to add.
	 */
	void addTelecom(TEL tel);

	/**
	 * Gets a set of everest TEL objects that are set upon the Template.
	 *
	 * @return A set of everest TEL objects.
	 */
	Set<TEL> getTelecoms();
}
