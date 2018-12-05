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

import java.util.Set;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;

/**
 * Provides headers for a Template wrapping an InfrastructureRoot that provides
 * naming functionality.
 *
 * @author Ryan Albert
 */
public interface Nameable {

	/**
	 * Adds a part of a specific type to full name of a specific use.
	 *
	 * @param use The name that the part will be added to.
	 * @param nameVal The String value to add to the name
	 * @param type The part type that the nameVal will be set to.
	 * @return An everest object that represents the name being
	 * created/modified.
	 */
	EN addName(EntityNameUse use, String nameVal, EntityNamePartType type);

	/**
	 * Adds an existing everest name object to the Template.
	 *
	 * @param name An existing everest object deriving from EN.
	 */
	void addName(EN name);

	/**
	 * Gets the set of names that are set upon the Template.
	 *
	 * @return A set of everest name objects.
	 */
	Set<EN> getNames();
}
