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
package org.marc.shic.cda.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.exceptions.DuplicateItemException;

/**
 * Adds a layer of abstraction to the AD class in everest. This class describes
 * and handles a single address with multiple address parts.
 *
 * @author Ryan Albert
 */
public final class Address {

	private AD address;
	private static final Logger LOGGER = Logger.getLogger(Address.class);

	/**
	 * Creates an address with an AD object containing no information, set
	 * to a null flavor.
	 */
	public Address() {
		address = new AD();
		isNull();
	}

	/**
	 * Creates an address, wrapping an existing AD object.
	 *
	 * @param address An existing AD object, may be null.
	 */
	public Address(AD address) {
		this.address = address;
		if (address == null) {
			this.address = new AD();
		}
		isNull();
	}

	/**
	 * Adds a use to the address.
	 *
	 * @param use The PostalAddressUse associated with the address.
	 * @return A value indicating whether or not the use was added to the
	 * address. If false, indicates that the PostalAddressUse was already
	 * added.
	 */
	public boolean addAddressUse(PostalAddressUse use) {
		try {
			SET uses = address.getUse();
			if (uses == null) {
				uses = new SET();
				address.setUse(uses);
			}
			uses.add(new CS(use));
			return true;
		} catch (DuplicateItemException e) {
			LOGGER.debug("Unable to add address use.", e);
			return false;
		}
	}

	/**
	 * Gets all of the uses associated with the address.
	 *
	 * @return A generated set of PostalAddressUse objects associated with
	 * the address.
	 */
	public Set<PostalAddressUse> getAddressUses() {
		HashSet<PostalAddressUse> result = null;
		if (address.getUse() != null) {
			result = new HashSet();
			for (CS use : address.getUse()) {
				result.add((PostalAddressUse) use.getCode());
			}
		}
		return result;
	}

	/**
	 * Adds an address part to the address.
	 *
	 * @param value The value to set for the specified type.
	 * @param type The AddressPartType that defines the part of the address.
	 * @return A value indicating whether or not the part was successfully
	 * added to the address.
	 */
	public boolean addPart(String value, AddressPartType type) {
		try {
			List parts = address.getPart();
			if (parts == null) {
				parts = new ArrayList();
				address.setPart(parts);
			}
			parts.add(new ADXP(value, type));
			return isNull();
		} catch (DuplicateItemException e) {
			LOGGER.error("Unable to add part.", e);
			return false;
		}
	}

	/**
	 * Checks if the address is null (contains any parts). If it does not,
	 * then the null flavor will be set if it has not already been set.
	 *
	 * @return True if a NullFlavor is set upon the address, false if it
	 * isn't, and the address contains information.
	 */
	public boolean isNull() {
		List parts = address.getPart();
		if (parts == null || parts.isEmpty()) {
			if (address.getNullFlavor() == null) {
				address.setNullFlavor(NullFlavor.Other);
			}
			return true;
		}
		address.setNullFlavor((CS) null);
		return false;
	}

	/**
	 * Clears the parts of the address and sets the null flavor to the one
	 * specified.
	 *
	 * @param reason Sets the kind of NullFlavor to set upon the address.
	 */
	public void setNull(NullFlavor reason) {
		List parts = address.getPart();
		if (parts != null && !parts.isEmpty()) {
			parts.clear();
		}
		address.setNullFlavor(reason);
	}

	/**
	 * Adds a time of usability to the address.
	 *
	 * @param useableTime The time that the address may be used.
	 */
	public void setUseablePeriod(Time useableTime) {
		address.setUseablePeriod(useableTime.getGeneralTime());
	}

	/**
	 * Gets a map of all address parts that have been set to this address.
	 *
	 * @return A HashMap containing an association between AddressPartType
	 * objects and String objects.
	 */
	public HashMap<AddressPartType, String> getAddressParts() {
		HashMap result = new HashMap();
		for (ADXP part : address.getPart()) {
			result.put(part.getPartType(), part.getValue());
		}
		return result;
	}

	/**
	 * Gets the everest representation of the address.
	 *
	 * @return The AD object that the address wraps and represents.
	 */
	public AD getAddr() {
		return address;
	}

	/**
	 * Creates a list of Address objects from a set of AD objects.
	 *
	 * @param addresses A set of AD objects
	 * @return A set of Address objects that wrap each AD object provided.
	 */
	public static Set<Address> addressesFrom(Set<AD> addresses) {
		Set<Address> result = new HashSet();
		if (addresses != null) {
			for (AD ad : addresses) {
				result.add(new Address(ad));
			}
		}
		return result;
	}
}
