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

import org.marc.shic.core.configuration.IheActorType;

/**
 * 
 * @author tylerg
 */
public class ActorNotFoundException extends RuntimeException {

	private IheActorType actorType;

	public IheActorType getActorType() {
		return this.actorType;
	}

	/**
	 * Creates a new communications exception
	 */
	public ActorNotFoundException() {
		super();
	}

	/**
	 * Creates a new ActorNotFoundException
	 */
	public ActorNotFoundException(IheActorType actorType, String message,
			Throwable cause) {
		super(message, cause);
		this.actorType = actorType;
	}

	/**
	 * Creates a new ActorNotFoundException
	 */
	public ActorNotFoundException(IheActorType actorType, String message) {
		super(message);
		this.actorType = actorType;
	}

	/**
	 * Creates a new ActorNotFoundException
	 */
	public ActorNotFoundException(IheActorType actorType, Throwable cause) {
		super(cause);
	}
}
