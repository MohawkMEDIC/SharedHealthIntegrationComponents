/* 
 * Copyright 2012 Mohawk College of Applied Arts and Technology
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
 * User: Justin Fyfe
 * Date: 11-08-2012
 */
package org.marc.shic.core;

/**
 * XDS Document entry status type
 */
public enum XdsDocumentEntryStatusType implements ICodeType
{

    /**
     * Document is submitted
     */
    Submitted("urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted"),
    /**
     * Document has been approved for sharing
     */
    Approved("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved"),
    /**
     * Document entry that is deprecated
     */
    Deprecated("urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");
    private final String m_code;

    /**
     * Create a status type
     *
     * @param code
     */
    private XdsDocumentEntryStatusType(String code)
    {
        this.m_code = code;
    }

    /**
     * Get the code for the XDS document entry
     *
     * @return
     */
    public String getCode()
    {
        return this.m_code;
    }
    
    @Override
    public String toString() {
        return this.m_code;
    }
}
