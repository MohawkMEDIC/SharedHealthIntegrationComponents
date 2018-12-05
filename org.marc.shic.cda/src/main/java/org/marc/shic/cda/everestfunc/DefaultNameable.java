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
 * @since 21-Mar-2014
 *
 */
package org.marc.shic.cda.everestfunc;

import java.util.HashMap;
import java.util.Set;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;

/**
 * Provides common naming functionality to a Nameable Template.
 *
 * @author Ryan Albert
 */
public class DefaultNameable extends DefaultImpl implements Nameable {

	private static HashMap<Class, DefaultNameable> nameableMap = new HashMap<Class, DefaultNameable>();

	/**
	 * Creates and/or gets the default implementation for Nameable for the
	 * provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Nameable functionality for.
	 * @return A DefaultNameable object.
	 */
	public static Nameable getNameable(InfrastructureRoot target) {
		Class targetClass = target.getClass();
		DefaultNameable result;
		if (!nameableMap.containsKey(targetClass)) {
			result = new DefaultNameable(targetClass);
			nameableMap.put(targetClass, result);
		} else {
			result = nameableMap.get(targetClass);
		}
		result.setTarget(target);
		return result;
	}

	/**
	 * Finds the getName and setName methods in the specified root object.
	 *
	 * @param root The InfrastructureRoot object to reflect.
	 */
	private DefaultNameable(Class root) {
		super(root, "Name");
	}

	/**
	 * Adds a part of a specific type to full name of a specific use.
	 *
	 * @param use The name that the part will be added to.
	 * @param nameVal The String value to add to the name
	 * @param type The part type that the nameVal will be set to.
	 * @return An everest object that represents the name being
	 * created/modified.
	 */
	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		if (nameVal == null || nameVal.isEmpty()) {
			return null;
		}
		if (getNames() == null) {
			setter(new SET());
		}
		ENXP nameAdd = new ENXP(nameVal, type);

		//First look through existing names.
		for (EN name : getNames()) {
			for (CS<EntityNameUse> nameUse : name.getUse()) {
				if (nameUse.getCode() == use) {
					name.getParts().add(nameAdd);
					return name;
				}
			}
		}

		//Create new full name with new use.
		EN newName = null;
		try {
			newName = (EN) getWorkingType().newInstance();
			newName.setUse(new SET());
			newName.getUse().add(new CS(use));
			newName.getParts().add(nameAdd);
			addName(newName);
		} catch (Exception e) {
			LOGGER.error("Unable to create and add a name.", e);
			newName = null;
		}
		return newName;
	}

	/**
	 * Adds an existing everest name object to the Template.
	 *
	 * @param name An existing everest object deriving from EN.
	 */
	@Override
	public void addName(EN name) {
		if (isCollection()) {
			Set names = (SET) getter();
			if (names == null) {
				names = new SET();
				setter(names);
			}
			names.add(name);
		} else {
			setter(name);
		}
	}

	/**
	 * Gets the set of names that are set upon the Template.
	 *
	 * @return A set of everest name objects.
	 */
	@Override
	public Set<EN> getNames() {
		Set result = null;
		if (isCollection()) {
			result = (SET) getter();
		} else {
			EN name = (EN) getter();
			if (name != null) {
				result = new SET();
				result.add(name);
			}
		}
		return result;
	}

}
