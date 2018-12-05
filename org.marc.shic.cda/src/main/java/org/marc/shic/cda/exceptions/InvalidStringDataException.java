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
 * @since January 14, 2014
 * 
 */

package org.marc.shic.cda.exceptions;

/**
 * An exception thrown when an attempt to input a malformed string has occurred.
 * 
 * @author Ryan Albert
 */
@Deprecated
public class InvalidStringDataException extends Exception {

    private String invalidString;
    /**
     * Constructs an instance of <code>InvalidDataException</code> with the
     * string that has caused the error, and a message detailing the reason for
     * it being malformed.
     *
     * @param msg 
     *          The reason for the exception being thrown.
     * @param data
     *          The string that had been input into the system that caused the
     *          exception.
     */
    public InvalidStringDataException(String data, String msg) {
        super(msg);
        invalidString = data;
    }
    
    /**
     * Gets the invalid string data that had caused this exception.
     * 
     * @return 
     *          A string containing invalid data, or a null value.
     */
    public final String getInvalidString() {
        return invalidString;
    }
}
