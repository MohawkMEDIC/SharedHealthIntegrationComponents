/* 
 * Copyright 2008-2011 Mohawk College of Applied Arts and Technology
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
 * Identifies additional XDS query parameter types that can be filtered
 */
public class XdsExtendedQueryParameterType<T> {

    /**
     * Represents the XDS-I Accession number query parameter
     */
    public static final String DOCUMENT_TYPE = "$XDSDocumentEntryTypeCode";
    /**
     * Confidentiality code
     */
    public static final String CONFIDENTIALITY_CODE = "$XDSDocumentEntryConfidentialityCode";
    private final String m_parameterName;
    private final T m_value;

    /**
     * Constructs a new query parameter
     *
     * @param parameterName
     */
    public XdsExtendedQueryParameterType(String parameterName, T value) {
        this.m_parameterName = parameterName;
        this.m_value = value;
    }

    /**
     * Get the parameter name
     */
    public String getParameterName() {
        return this.m_parameterName;
    }

    /**
     * Get the value of the extended parameter
     */
    public T getValue() {
        return this.m_value;
    }
}
