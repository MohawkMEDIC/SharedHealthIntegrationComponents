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
 * Date: 13-Sep-2013
 *
 */
package org.marc.shic.core.exceptions;

/**
 * A custom SSL exception thrown when handling keystores, truststores, and
 * certificates.
 */
public class SslException extends Exception {
	/**
	 * Constructs an SslException with a message and a cause/exception
	 * 
	 * @param message
	 * @param cause
	 */
	public SslException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an SslException with a cause/exception
	 * 
	 * @param cause
	 */
	public SslException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an SslException with a message
	 * 
	 * @param cause
	 */
	public SslException(String message) {
		super(message);
	}
}
