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
import org.marc.everest.datatypes.II;

/**
 * Provides headers for a Template wrapping an InfrastructureRoot that provides
 * identifying functionality.
 *
 * @author Ryan Albert
 */
public interface Identifiable {

	/**
	 * Adds an ID from a root string and an extension string.
	 *
	 * @param root A String value, formatted as either an OID or a UUID.
	 * @param extension A String value
	 */
	void addId(String root, String extension);

	/**
	 * Adds an ID from a root string.
	 *
	 * @param root A String value, formatted as either an OID or a UUID.
	 */
	void addId(String root);

	/**
	 * Adds an existing everest II identifier object to the set of
	 * identifiers.
	 *
	 * @param identifier The II object to add.
	 */
	void addId(II identifier);

	/**
	 * Gets a set of identifiers that have been added.
	 *
	 * @return A set of II objects.
	 */
	Set<II> getIds();
}
