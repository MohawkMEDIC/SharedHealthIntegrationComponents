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

import java.util.HashMap;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.datatypes.Code;

/**
 * Specified common default functionality of codeable Templates. Uses optimized
 * reflection to invoke the various InfrastructureRoot methods.
 *
 * @author Ryan Albert
 */
public class DefaultCodeable extends DefaultImpl implements Codeable {

	private static HashMap<Class, DefaultCodeable> codeableMap = new HashMap<Class, DefaultCodeable>();

	/**
	 * Creates and/or gets the default implementation for Codeable for the
	 * provided InfrastructureRoot object.
	 *
	 * @param target An InfrastructureRoot object to provide default
	 * Codeable functionality for.
	 * @return A DefaultCodeable object.
	 */
	public static Codeable getCodeable(InfrastructureRoot target) {
		Class targetClass = target.getClass();
		DefaultCodeable result;
		if (!codeableMap.containsKey(targetClass)) {
			result = new DefaultCodeable(targetClass);
			codeableMap.put(targetClass, result);
		} else {
			result = codeableMap.get(targetClass);
		}
		result.setTarget(target);
		return result;
	}

	/**
	 * Finds the getCode and setCode methods for the specified root object.
	 *
	 * @param root The InfrastructureRoot object to reflect.
	 */
	private DefaultCodeable(Class root) {
		this(root, "Code");
	}

	/**
	 * Finds the getX and setX methods for the specified root object, which
	 * both get and set an everest code, where X is the name of the getter
	 * and setter.
	 *
	 * @param root The InfrastructureRoot object to reflect.
	 * @param codeMethodName The name of the getter and setter to reflect
	 * and provide functionality for.
	 */
	public DefaultCodeable(Class root, String codeMethodName) {
		super(root, codeMethodName);
	}

	/**
	 * Get a Code
	 *
	 * @return A Code object that represents the Template code.
	 */
	@Override
	public Code getCode() {
		return new Code((CS) getter());
	}

	/**
	 * Sets a code
	 *
	 * @param value A Code object to be set as the Template code.
	 */
	@Override
	public void setCode(Code value) {
		setter(value.getCode(getWorkingType()));
	}

}
