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
 * @author brownp Date: Sep 4, 2013
 *
 */
package org.marc.shic.cda;

import org.marc.everest.interfaces.IResultDetail;
import org.marc.everest.interfaces.ResultDetailType;

/**
 * Contains information about a part of a CDA document. Used to show
 * information, errors, and warnings.
 *
 * @author brownp
 */
public class ResultDetail implements IResultDetail {

	private String location;
	private Exception exception;
	private ResultDetailType resultDetailType;

	/**
	 * Initializes the ResultDetail with some information about a part of
	 * the CDA document.
	 *
	 * @param _location A String representing the XPath of the location.
	 * @param _exception Optional, but not required, specifies the exception
	 * that was thrown while validating the location.
	 * @param _resultDetailType The type of result.
	 */
	public ResultDetail(String _location, Exception _exception, ResultDetailType _resultDetailType) {
		this.location = _location;
		this.exception = _exception;
		this.resultDetailType = _resultDetailType;
	}

	/**
	 * Gets the type of result the ResultDetail describes.
	 *
	 * @return A ResultDetailType object.
	 */
	@Override
	public ResultDetailType getType() {
		return this.resultDetailType;
	}

	/**
	 * Generates a message from ResultDetail information.
	 *
	 * @return A generated String
	 */
	@Override
	public String getMessage() {
		if (null != this.exception) {
			return String.format("\n%s -> Location: %s\nException: %s\n", this.resultDetailType.toString(), this.location, this.exception.getMessage());
		} else {
			return String.format("\nLocation: %s \nDetailType: %s\n", this.location, this.resultDetailType.toString());
		}
	}

	/**
	 * Returns the XPath location of the ResultDetail
	 *
	 * @return A String representing an XPath location.
	 */
	@Override
	public String getLocation() {
		return this.location;
	}

	/**
	 * Sets the XPath location of the ResultDetail information.
	 *
	 * @param _location A String representing an XPath location.
	 */
	@Override
	public void setLocation(String _location) {
		this.location = _location;
	}

	/**
	 * Gets the exception that was generated while validating the CDA.
	 *
	 * @return If provided, the Exception, else null
	 */
	@Override
	public Exception getException() {
		return this.exception;
	}

}
