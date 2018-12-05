/*
 * Copyright 2012 Mohawk College of Applied Arts and Technology
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
 * User: Justin Fyfe
 * Date: 11-08-2012
 */
package org.marc.shic.xds;

public class XdsMessageParseException extends Exception
{
    // backing field for the cause of exception
    private final Object objectCause;

    public Object getObjectCause()
    {
        return objectCause;
    }

    /**
     * Message parsing exception
     */
    public XdsMessageParseException(Object cause, String message)
    {
        super(message);
        this.objectCause = cause;
    }

    public XdsMessageParseException(Object objectCause, Throwable cause)
    {
        super(cause);
        this.objectCause = objectCause;
    }

    public XdsMessageParseException(Object objectCause, String message, Throwable cause)
    {
        super(message, cause);
        this.objectCause = objectCause;
    }
}
