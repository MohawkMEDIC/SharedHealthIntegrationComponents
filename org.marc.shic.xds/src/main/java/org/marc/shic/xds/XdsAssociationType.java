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
 * Date: Nov 15, 2013
 *
 */
package org.marc.shic.xds;

import org.marc.shic.core.DocumentRelationshipType;

/**
 *
 * @author tylerg
 */
public class XdsAssociationType extends DocumentRelationshipType
{

    /**
     * ebRIM HasMember
     */
    public static final XdsAssociationType HasMember = new XdsAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");

    /**
     * Create a status type
     *
     * @param code
     */
    private XdsAssociationType(String code)
    {
        super(code);
    }
}
