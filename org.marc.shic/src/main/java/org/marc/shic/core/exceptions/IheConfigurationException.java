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
 * @author Mohamed Ibrahim
 * Date: 02-Nov-2013
 *
 */
package org.marc.shic.core.exceptions;

/**
 * A custom Configuration exception thrown when parsing, and handling affinity domain 
 * config files.
 */
public class IheConfigurationException extends Exception {
	/**
	 * Constructs a IheConfigurationException with a message and a cause/exception
	 * 
	 * @param message
	 * @param cause
	 */
	public IheConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a IheConfigurationException with a cause/exception
	 * 
	 * @param cause
	 */
	public IheConfigurationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a IheConfigurationException with a message
	 * 
	 * @param cause
	 */
	public IheConfigurationException(String message) {
		super(message);
	}
}
