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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core.configuration;

import java.io.Serializable;

/**
 * Represents the type of actor involved in an interaction
 *
 * @author Nebri
 */
public enum IheActorType implements Serializable {

    PAT_IDENTITY_X_REF_MGR, PDS, DOC_REPOSITORY, DOC_REGISTRY, DOC_RECIPIENT, RESPONDING_GATEWAY_REGISTRY, RESPONDING_GATEWAY_REPOSITORY, AUDIT_REPOSITORY, TS, SVS;

    /**
     * Attempts to parse an actor type from a string
     *
     * @param name The name to parse
     * @return The actor type
     */
    public static IheActorType fromString(String name) {
        for (IheActorType iheActorType : values()) {
            if (name.compareToIgnoreCase(iheActorType.toString()) == 0) {
                return iheActorType;
            }
        }

        return null;
    }
}
