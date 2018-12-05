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

import org.marc.shic.cda.datatypes.Time;

/**
 * Provides headers for a Template wrapping an InfrastructureRoot that provides
 * timing functionality.
 *
 * @author Ryan Albert
 */
public interface Timeable {

	/**
	 * Gets the time.
	 *
	 * @return A generated Time object that best represents the time set.
	 */
	Time getTime();

	/**
	 * Sets the time.
	 *
	 * @param value A Time object.
	 */
	void setTime(Time value);
}
