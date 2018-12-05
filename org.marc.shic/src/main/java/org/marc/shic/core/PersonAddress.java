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
package org.marc.shic.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a person's address.
 * 
 * @author ibrahimm
 */
public class PersonAddress {
	private ArrayList<AddressComponent> parts;
	private AddressUse use;

	/**
	 * Create an address with a specific use.
	 * 
	 * @param use
	 *            The type of address (Work, home, et cetera)
	 */
	public PersonAddress(AddressUse use) {
		this.parts = new ArrayList<AddressComponent>();
		this.use = use;
	}

	/**
	 * Creates an address with a specified use and an initial set of parts.
	 * 
	 * @param use
	 *            The type of address (Work, home, et cetera)
	 * @param parts
	 *            The collection of parts the address is to have
	 */
	public PersonAddress(AddressUse use, List<AddressComponent> parts) {
		this.use = use;

		this.parts = (ArrayList) parts;
	}

	/**
	 * Adds an address part to the address.
	 * 
	 * @param value
	 *            The value of the address part
	 * @param type
	 *            The type of address part to set.
	 */
	public void addAddressPart(String value, AddressPartType type) {
		AddressComponent addressPart = new AddressComponent();
		addressPart.setValue(value);
		addressPart.setType(type);
		parts.add(addressPart);
	}

	/**
	 * Gets the list of all parts of an address.
	 * 
	 * @return ArrayList of all parts of an address.
	 */
	public ArrayList<AddressComponent> getParts() {
		return parts;
	}

	/**
	 * Gets a specific part of an address
	 * 
	 * @param type
	 *            The part of the address to retrieve
	 * @return The part of the address with the given type
	 */
	public AddressComponent getPartByType(AddressPartType type) {
		for (AddressComponent part : parts) {
			if (part.getType() == type)
				return part;
		}
		return null;
	}

	public AddressUse getUse() {
		return use;
	}

	public void setUse(AddressUse use) {
		this.use = use;
	}

}
