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
 * @author tylerg
 * Date: Feb 18, 2014
 *
 */
package org.marc.shic.core.exceptions;

/**
 *
 * @author tylerg
 */
public class UnknownPolicyException extends RuntimeException
{

    public UnknownPolicyException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UnknownPolicyException(String message)
    {
        super(message);
    }

    public UnknownPolicyException(Throwable cause)
    {
        super(cause);
    }
}
