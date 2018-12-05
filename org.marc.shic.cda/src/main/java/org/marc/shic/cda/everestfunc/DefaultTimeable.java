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

import java.util.ArrayList;
import java.util.HashMap;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.GTS;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.datatypes.Time;

/**
 * Specified common default functionality of timeable Templates. Uses optimized
 * reflection on InfrastructureRoot objects to invoke required methods.
 *
 * @author Ryan Albert
 */
public class DefaultTimeable extends DefaultImpl implements Timeable {

	private static HashMap<Class, DefaultTimeable> timeableMap = new HashMap<Class, DefaultTimeable>();

	/**
	 * Creates and/or gets the default implementation for Timeable for the
	 * provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Timeable functionality for.
	 * @param timeName The name of the getX and setX methods where X is the
	 * String value.
	 * @return A DefaultTimeable object.
	 */
	public static Timeable getTimeable(InfrastructureRoot target, String timeName) {
		Class targetClass = target.getClass();
		DefaultTimeable result;
		if (!timeableMap.containsKey(targetClass)) {
			result = new DefaultTimeable(targetClass, timeName);
			timeableMap.put(targetClass, result);
		} else {
			result = timeableMap.get(targetClass);
			if (!result.getGetterMethod().getName().equals(String.format("get%s", timeName))) {
				result = new DefaultTimeable(targetClass, timeName);
				timeableMap.put(targetClass, result);
			}
		}
		result.setTarget(target);
		return result;
	}

	/**
	 * Finds the getX and setX methods for a root object with the specified
	 * method name.
	 *
	 * @param root The InfrastructureRoot object to reflect.
	 * @param timeName A String pointing out the name of the getter and
	 * setter methods.
	 */
	private DefaultTimeable(Class root, String timeName) {
		super(root, timeName);
		Class timeClass = getWorkingType();

		if (timeClass != TS.class && timeClass != IVL.class && timeClass != GTS.class) {
			throw new ClassCastException(String.format("Time type of %s could not be handled.", timeClass.getName()));
		}
	}

	/**
	 * Gets the time.
	 *
	 * @return A generated Time object that best represents the time set.
	 */
	@Override
	public Time getTime() {
		Time result = null;
		if (isCollection()) {
			ArrayList times = (ArrayList) getter();
			if (times != null && !times.isEmpty()) {
				result = new Time((ANY) times.get(0));
			}
		} else {
			result = new Time((ANY) getter());
		}
		return result;
	}

	/**
	 * Sets a time.
	 *
	 * @param value A Time object.
	 */
	@Override
	public void setTime(Time value) {
		if (isCollection()) {
			ArrayList collection = (ArrayList) getter();
			if (collection == null) {
				collection = new ArrayList();
				setter(collection);
			}
			if (value != null) {
				collection.add(value.getTime(getWorkingType()));
			} else {
				collection.clear();
			}
		} else {
			setter(value.getTime(getWorkingType()));
		}
	}
}
