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
 * @author Ryan Albert
 * @since 29-Jan-2014
 *
 */
package org.marc.shic.cda.datatypes;

/**
 * Defines a set of standards and their dependencies.
 *
 * @author Ryan Albert
 */
public enum CDAStandard {

	/**
	 * The base Clinical Document Architecture standard that encompasses all
	 * other standards.
	 */
	CDA(),
	/**
	 * Continuity of Care Document standard
	 */
	CCD(CDA),
	/**
	 * NexJ document standard
	 */
	NexJ(CCD),
	/**
	 * Patient Care Coordination framework standard
	 */
	PCC(CCD),
	/**
	 * Basic Patient Privacy Consents document standard.
	 */
	BPPC(PCC);

	private CDAStandard dependency;

	private CDAStandard() {

	}

	private CDAStandard(CDAStandard dependency) {
		this.dependency = dependency;
	}

	/**
	 * Gets the standard that this standard depends on, or is a child of.
	 *
	 * @return A CDAStandard
	 */
	public CDAStandard getParentStandard() {
		return dependency;
	}

	/**
	 * Checks if the specified CDAStandard can be used with the CDAStandard.
	 *
	 * @param standard The CDA standard to check
	 * @return A value indicating whether or not the specified standard is
	 * equal to, or is a dependency of this CDAStandard.
	 */
	public boolean isCompatibleWith(CDAStandard standard) {
		if (this.equals(standard)) {
			return true;
		} else if (dependency != null) {
			return dependency.isCompatibleWith(standard);
		}
		return false;
	}
}
