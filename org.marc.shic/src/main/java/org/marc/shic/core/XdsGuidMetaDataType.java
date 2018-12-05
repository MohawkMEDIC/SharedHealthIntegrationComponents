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
 * @author tylerg
 * Date: Aug 21, 2013
 *
 */
package org.marc.shic.core;

public class XdsGuidMetaDataType<T> extends XdsGuidType
{

    private Class<T> metaDataClass;
    private String[] requiredSlots;

    public Class<T> getMetaDataClass()
    {
        return metaDataClass;
    }

    public String[] getRequiredSlots()
    {
        return requiredSlots;
    }

    /**
     * Creates a new instance of the XDS query specification guid along with its
     * associated name
     */
    XdsGuidMetaDataType(String queryGuid, String guidName, Class<T> metaDataClass)
    {
        super(queryGuid, guidName);
        this.metaDataClass = metaDataClass;
    }

    XdsGuidMetaDataType(String queryGuid, String guidName, Class<T> metaDataClass, String... requiredSlots)
    {
        super(queryGuid, guidName);
        this.metaDataClass = metaDataClass;
        this.requiredSlots = requiredSlots;
    }
}
