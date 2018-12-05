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
 * @since 19-Mar-2014
 *
 */
package org.marc.shic.cda.everestfunc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.datatypes.Address;

/**
 * Specified common default functionality of addressing for Templates. Uses
 * optimized reflection to invoke InfrastructureRoot objects.
 *
 * @author Ryan Albert
 */
public class DefaultAddressable extends DefaultImpl implements Addressable {

	private static HashMap<Class, DefaultAddressable> addressableMap = new HashMap<Class, DefaultAddressable>();

	/**
	 * Creates and/or gets the default implementation for Addressable for
	 * the provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Addressable functionality for.
	 * @return A DefaultAddressable object.
	 */
	public static Addressable getAddressable(InfrastructureRoot target) {
		Class targetClass = target.getClass();
		DefaultAddressable result;
		if (!addressableMap.containsKey(targetClass)) {
			result = new DefaultAddressable(targetClass);
			addressableMap.put(targetClass, result);
		} else {
			result = addressableMap.get(targetClass);
		}
		result.setTarget(target);
		return result;
	}

	/**
	 * Finds the getAddr and setAddr methods of the specified root object.
	 *
	 * @param clazz
	 */
	private DefaultAddressable(Class clazz) {
		super(clazz, "Addr");
	}

	/**
	 * Generates a set of Address objects that represent all of the set AD
	 * objects on the Template object's root object.
	 *
	 * @return A Set of Address objects generated from all AD objects.
	 */
	@Override
	public Set<Address> getAddresses() {
		if (isCollection()) {
			return Address.addressesFrom((Set) getter());
		}
		Set result = new HashSet<Address>();
		result.add(getter());
		return result;
	}

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
	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		Address result;
		for (Address ad : getAddresses()) {
			for (PostalAddressUse part : ad.getAddressUses()) {
				if (part == use) {
					result = ad;
					result.addPart(value, type);
					return result;
				}
			}
		}
		result = new Address();
		result.addAddressUse(use);
		result.addPart(value, type);
		addAddress(result);
		return result;
	}

	/**
	 * Adds an existing Address object to the Template.
	 *
	 * @param address The Address object to add.
	 */
	@Override
	public void addAddress(Address address) {
		if (isCollection()) {
			Set<AD> addr = (SET) getter();
			if (addr == null) {
				addr = new SET();
				setter(addr);
			}
			addr.add(address.getAddr());
		} else {
			setter(address.getAddr());
		}
	}
}
