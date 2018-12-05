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
 * @since 18-Mar-2014
 *
 */
package org.marc.shic.cda.everestfunc;

import java.util.Set;
import org.marc.shic.cda.datatypes.Address;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.PostalAddressUse;

/**
 * Provides headers for a Template wrapping an InfrastructureRoot that provides
 * addressing functionality.
 *
 * @author Ryan Albert
 */
public interface Addressable {

	/**
	 * Adds an address value to a specific address with a specific use, on a
	 * specific address part.
	 *
	 * @param use The type of Address that the value will be set in.
	 * @param value A String value providing addressing information.
	 * @param type The AddressPartType to set on in the specified address
	 * use.
	 * @return The modified/created address with the reflected change.
	 */
	Address addAddress(PostalAddressUse use, String value, AddressPartType type);

	/**
	 * Adds an existing Address object to the Template.
	 *
	 * @param address The Address object to add.
	 */
	void addAddress(Address address);

	/**
	 * Generates a set of Address objects that represent all of the set AD
	 * objects on the Template object's root object.
	 *
	 * @return A Set of Address objects generated from all AD objects.
	 */
	Set<Address> getAddresses();
}
