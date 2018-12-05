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
 * @since 3-Apr-2014
 *
 */
package org.marc.shic.cda.everestfunc;

/**
 * Defines a type that reflects on specific method definitions of a specific
 * class. The target Object that will have getters and setters invoked must
 * contain those method definitions.
 *
 * @author Ryan Albert
 */
public class DefaultImpl extends SimpleGetSetReflector {

	private Object target;

	/**
	 * Constructs the implementation for a specific class with the getX and
	 * setX methods of a specific name.
	 *
	 * @param clazz The Class definition to search for the getter and setter
	 * methods for.
	 * @param modName The name of the getX and setX methods where X is the
	 * String value.
	 */
	public DefaultImpl(Class clazz, String modName) {
		super(clazz, modName);
	}

	/**
	 * Sets the Object that will have the methods invoked on.
	 *
	 * @param target The Object to invoke getters and setters for.
	 */
	public final void setTarget(Object target) {
		this.target = target;
	}

	/**
	 * Gets the Object that will have the methods invoked on.
	 *
	 * @return The working Object of the implementation.
	 */
	public final Object getTarget() {
		return target;
	}

	/**
	 * Invokes the getter method of the set target object and returns the
	 * result.
	 *
	 * @return The result of invoking the getter method of the target
	 * object.
	 */
	protected Object getter() {
		return getter(target);
	}

	/**
	 * Invokes the setter method of the set target object and returns the
	 * result.
	 *
	 * @param param The parameter to provide the setter method of the target
	 * object.
	 */
	protected void setter(Object param) {
		setter(target, param);
	}
}
