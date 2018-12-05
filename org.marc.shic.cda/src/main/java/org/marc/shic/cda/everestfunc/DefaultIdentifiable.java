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
import java.util.Set;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;

/**
 * Specifies common, default behavior between Templates that contain
 * InfrastructureRoot objects with arrays of II (identifiers) that can be set.
 * This uses optimized reflection to find the methods within the
 * InfrastructureRoot object.
 *
 * @author Ryan Albert
 */
public class DefaultIdentifiable extends DefaultImpl implements Identifiable {

	private static HashMap<Class, DefaultIdentifiable> identifiableMap = new HashMap<Class, DefaultIdentifiable>();

	/**
	 * Creates and/or gets the default implementation for Identifiable for
	 * the provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Identifiable functionality for.
	 * @return A DefaultIdentifiable object.
	 */
	public static Identifiable getIdentifiable(InfrastructureRoot target) {
		Class targetClass = target.getClass();
		DefaultIdentifiable result;
		if (!identifiableMap.containsKey(targetClass)) {
			result = new DefaultIdentifiable(targetClass);
			identifiableMap.put(targetClass, result);
		} else {
			result = identifiableMap.get(targetClass);
		}
		result.setTarget(target);
		return result;
	}

	/**
	 * Finds the getId and setId methods of the specified root object.
	 *
	 * @param root The InfrastructureRoot object to reflect.
	 */
	private DefaultIdentifiable(Class root) {
		super(root, "Id");
	}

	/**
	 * Adds an ID from a root string and an extension string.
	 *
	 * @param root A String value, formatted as either an OID or a UUID.
	 * @param extension A String value
	 */
	@Override
	public void addId(String root, String extension) {
		addId(new II(root, extension));
	}

	/**
	 * Adds an existing everest II identifier object to the set of
	 * identifiers.
	 *
	 * @param identifier The II object to add.
	 */
	@Override
	public void addId(II identifier) {
		if (isCollection()) {
			Set idSet = (SET) getter();
			if (idSet == null) {
				idSet = new SET();
				setter(idSet);
			}
			idSet.add(identifier);
		} else {
			setter(identifier);
		}
	}

	/**
	 * Adds an ID from a root string.
	 *
	 * @param root A String value, formatted as either an OID or a UUID.
	 */
	@Override
	public void addId(String root) {
		addId(root, null);
	}

	/**
	 * Gets a set of identifiers that have been added.
	 *
	 * @return A set of II objects.
	 */
	@Override
	public Set<II> getIds() {
		Set result = null;
		if (isCollection()) {
			result = (SET) getter();
		} else {
			II id = (II) getter();
			if (id != null) {
				result = new SET();
				result.add(getter());
			}
		}
		return result;
	}
}
