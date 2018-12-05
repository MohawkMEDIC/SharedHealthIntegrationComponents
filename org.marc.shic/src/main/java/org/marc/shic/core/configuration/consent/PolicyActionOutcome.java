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
 * @author Mohamed Ibrahim Date: 19-Sep-2013
 *
 */
package org.marc.shic.core.configuration.consent;

/**
 * An Enum that holds the values for ACL role actions ranked from less strict to most strict.
 */
public enum PolicyActionOutcome {

    Allow, Elevate, Deny;

    /**
     * A static method that returns the enum value from a 'case-insensitive'
     * string value. It is an alternative to the default case-sensitive
     * 'valueOf'.
     *
     * @param name a string value of the enum value
     * @return PolicyActionOutcome value or null if it D.N.E
     */
    public static PolicyActionOutcome fromString(String name) {
        for (PolicyActionOutcome action : values()) {
            if (name.compareToIgnoreCase(action.toString()) == 0) {
                return action;
            }
        }

        return null;
    }
}
