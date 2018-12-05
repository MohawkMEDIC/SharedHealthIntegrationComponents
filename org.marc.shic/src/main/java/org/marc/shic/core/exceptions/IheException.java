/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 * 
 * 
 * Date: October 29, 2013
 * 
 */
package org.marc.shic.core.exceptions;

/**
 * 
 * @author Mohamed
 */
public class IheException extends RuntimeException {

	/**
	 * Creates a new IheException instance
	 */
	public IheException(String message) {
		super(message);
	}

	/**
	 * Creates a new IheException instance
	 */
	public IheException(String message, Throwable innerException) {
		super(message, innerException);
	}
}
