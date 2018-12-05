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
package org.marc.shic.core.configuration;

import java.io.IOException;

/**
 * Socket connection interface for both Secured and Unsecured connection types.
 * 
 * @author ibrahimm
 */
public abstract class IheSocket {

	public abstract boolean isClosed();

	public abstract void connect(String hostname, int port) throws IOException;

	public abstract void close();

	public abstract void write(String data) throws IOException;

	/**
	 * Reads the next value
	 * 
	 * @return
	 */
	public abstract int read() throws IOException;

}
