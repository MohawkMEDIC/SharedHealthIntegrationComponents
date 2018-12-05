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
 * Date: Sept 5, 2013
 *
 */
package org.marc.shic.svs.exceptions;

import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.exceptions.CommunicationsException;


/**
 *
 * @author Garrett
 */
public class SvsCommunicatonException extends CommunicationsException
{

    public SvsCommunicatonException(IheActorConfiguration actor)
    {
        super(actor);
    }

    public SvsCommunicatonException(IheActorConfiguration actor, String message, Throwable cause)
    {
        super(actor, message, cause);
    }

    public SvsCommunicatonException(IheActorConfiguration actor, String message)
    {
        super(actor, message);
    }

    public SvsCommunicatonException(IheActorConfiguration actor, Throwable cause)
    {
        super(actor, cause);
    }
}