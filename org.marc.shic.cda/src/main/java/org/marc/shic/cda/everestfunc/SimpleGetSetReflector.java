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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.interfaces.ICollection;

/**
 * Provides the base functionality for a common getter and setter of a specific
 * Class.
 *
 * @author Ryan Albert
 */
public abstract class SimpleGetSetReflector {

	protected final Logger LOGGER = Logger.getLogger(getClass());

	private Method getter;
	private Method setter;
	private Class workingType;
	private Class returnType;
	private boolean isCollection;

	/**
	 * Constructs the function for a specific root object with the
	 * getter/setter name of the methods. The getter must have 0 parameters,
	 * and the setter must have 1 parameter that is not a String.
	 *
	 * @param clazz The InfrastructureRoot object to reflect.
	 * @param modName The name of the getter and setter method, after 'get'
	 * and 'set'
	 */
	public SimpleGetSetReflector(Class clazz, String modName) {
		getGetter(clazz, modName);
		getSetter(clazz, modName);
		if (getter == null) {
			throw new UnsupportedOperationException(String.format("Unable to find getter method for %s in %s", modName, clazz));
		}
		if (setter == null) {
			throw new UnsupportedOperationException(String.format("Unable to find setter method for %s in %s", modName, clazz));
		}
	}

	/**
	 * Finds the getX method of a specific class and determines if it
	 * returns a collection. Stores the actual return value of the getter
	 * and the generic type parameter of the return type if it is a
	 * collection.
	 *
	 * @param clazz The class to find the getter method in.
	 * @param methodName A String that contains the name of the getX method,
	 * where X is the String value.
	 */
	private void getGetter(Class clazz, String methodName) {
		methodName = String.format("get%s", methodName);
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 0 && method.getReturnType() != null) {
				getter = method;
				returnType = getter.getReturnType();
				Class superClass = returnType;
				workingType = returnType;
				while (superClass != null) {
					for (Class superInterface : superClass.getInterfaces()) {
						if (superInterface == Collection.class || superInterface == ICollection.class) {
							workingType = (Class) ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments()[0];
							isCollection = true;
							return;
						}
					}
					superClass = superClass.getSuperclass();
				}
			}
		}
	}

	/**
	 * Finds the setter method of a specific name in a specific class.
	 *
	 * @param clazz The Class to search for the setter method.
	 * @param methodName A String that contains the name of the getX method,
	 * where X is the String value.
	 */
	private void getSetter(Class clazz, String methodName) {
		methodName = String.format("set%s", methodName);
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == returnType) {
				setter = method;
				break;
			}
		}
	}

	/**
	 * Returns the Class definition of the actual return type of the getter
	 * method, which is also the parameter of the setter method.
	 *
	 * @return A Class representing the return type of the getter, or the
	 * parameter type of the setter
	 */
	public final Class getReturnType() {
		return returnType;
	}

	/**
	 * Returns the Class definition of the return type's generic parameter
	 * if it is a collection, otherwise, this is equivalent to the actual
	 * return type.
	 *
	 * @return A Class representing the working return type.
	 */
	public final Class getWorkingType() {
		return workingType;
	}

	/**
	 * Gets the Method definition that represents the getter.
	 *
	 * @return A Method object representing the getter.
	 */
	public final Method getGetterMethod() {
		return getter;
	}

	/**
	 * Gets the Method definition that represents the setter.
	 *
	 * @return A Method object representing the setter.
	 */
	public final Method getSetterMethod() {
		return setter;
	}

	/**
	 * Determines whether or not the return type, or parameter type of the
	 * getter/setter is a type of Collection/ICollection
	 *
	 * @return A boolean value indicating the presence of a collection.
	 * @see ICollection
	 */
	public final boolean isCollection() {
		return isCollection;
	}

	/**
	 * Invokes the getter method of the root object and returns the object
	 * returned.
	 *
	 * @param target The Object that is having the method invoked on.
	 * @return The object that is returned from the getter.
	 */
	protected Object getter(Object target) {
		try {
			return getter.invoke(target);
		} catch (Exception e) {
			LOGGER.error(String.format("Unable to invoke getter method %s for %s", getter.getName(), target.getClass().getSimpleName()), e);
		}
		return null;
	}

	/**
	 * Invokes the setter method with the object provided.
	 *
	 * @param target The object that is having the method invoked on.
	 * @param set The object that is passed as a parameter to the setter.
	 */
	protected void setter(Object target, Object set) {
		try {
			setter.invoke(target, set);
		} catch (Exception e) {
			LOGGER.error(String.format("Unable to invoke setter method %s for %s", setter.getName(), target.getClass().getSimpleName()), e);
		}
	}
}
