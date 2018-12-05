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
 * Date: Dec 16, 2013
 *
 */
package org.marc.shic.pix;

/**
 *
 * @author tylerg
 */
public class PixMessageParseException extends RuntimeException
{

    public PixMessageParseException()
    {
        super();
    }

    public PixMessageParseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PixMessageParseException(String message)
    {
        super(message);
    }

    public PixMessageParseException(Throwable cause)
    {
        super(cause);
    }
}
