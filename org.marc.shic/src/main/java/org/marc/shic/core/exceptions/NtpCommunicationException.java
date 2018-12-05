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

import org.marc.shic.core.configuration.IheActorConfiguration;

/**
 * 
 * @author Garrett
 */
public class NtpCommunicationException extends CommunicationsException {

	public NtpCommunicationException(IheActorConfiguration actor) {
		super(actor);
	}

	public NtpCommunicationException(IheActorConfiguration actor,
			String message, Throwable cause) {
		super(actor, message, cause);
	}

	public NtpCommunicationException(IheActorConfiguration actor, String message) {
		super(actor, message);
	}

	public NtpCommunicationException(IheActorConfiguration actor,
			Throwable cause) {
		super(actor, cause);
	}
}
