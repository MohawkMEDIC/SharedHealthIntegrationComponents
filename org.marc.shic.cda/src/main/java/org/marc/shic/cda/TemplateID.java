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
 * @since 25-Feb-2014
 *
 */
package org.marc.shic.cda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * Used to specify that a Template applies a specific template rule for
 * validation, on a specific standard.
 *
 * @author Ryan Albert
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TemplateID {

	/**
	 * The OID that represents a set of template rules.
	 *
	 * @return The templateId OID
	 */
	String oid();

	/**
	 * Defines the standard that the template OID applies to.
	 *
	 * @return Defaults to CDA (all standards) if not specified.
	 */
	CDAStandard standard() default CDAStandard.CDA;
}
