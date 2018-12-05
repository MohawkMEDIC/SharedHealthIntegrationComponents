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

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Mohamed
 */
public class JKSStoreInformation implements Serializable {
	private String storeFile;
	private String storePassword;

	/*
	 * Default constructor is needed for serialization
	 */
	public JKSStoreInformation() {
	}

	public JKSStoreInformation(String storeFile, String storePassword) {
		this.storeFile = storeFile;
		this.storePassword = storePassword;
	}

	/**
	 * 
	 * @return
	 */
	public String getStoreFile() {
		return this.storeFile;
	}

	/**
	 * 
	 * @return
	 */
	public String getStorePassword() {
		return this.storePassword;
	}

	// /**
	// *
	// * @return
	// */
	// public List<String> getPrivateKeyAliases() {
	// return new ArrayList<String>();
	// }

	public void setStoreFile(String storeFile) {
		this.storeFile = storeFile;
	}

	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

}
