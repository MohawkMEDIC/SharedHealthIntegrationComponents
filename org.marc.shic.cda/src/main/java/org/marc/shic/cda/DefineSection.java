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
 * @since 7-Feb-2014
 *
 */
package org.marc.shic.cda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define the order in which a section is ordered within a generated CDA
 * document.
 *
 * @author Ryan Albert
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefineSection {

	/**
	 * The named title of the section.
	 * @return 
	 */
	String title();

	/**
	 * The order in which the section appears.
	 * @return 
	 */
	int order();

	/**
	 * The LOINC code that identifies the type of section.
	 * @return 
	 */
	String code();
	
	/**
	 * The display name of the code that identifies the type of section.
	 * @return 
	 */
	String codeName();
}
