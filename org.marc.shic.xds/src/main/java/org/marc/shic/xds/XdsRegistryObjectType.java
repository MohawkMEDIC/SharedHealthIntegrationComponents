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
 * @author Mohamed Ibrahim
 * Date: 15-Nov-2013
 *
 */
package org.marc.shic.xds;

import org.marc.shic.xds.repository.*;

/**
 * A class that helps in the creation of a JAXBElement
 * 
 * @author ibrahimm
 */
public class XdsRegistryObjectType {
    
    public static final XdsRegistryObjectType XdsExtrinsicObjectType = new XdsRegistryObjectType("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject", ExtrinsicObjectType.class);
    public static final XdsRegistryObjectType XdsRegistryPackageType = new XdsRegistryObjectType("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage", RegistryPackageType.class);
    public static final XdsRegistryObjectType XdsClassificationType = new XdsRegistryObjectType("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Classification", ClassificationType.class);
    public static final XdsRegistryObjectType XdsAssociationType = new XdsRegistryObjectType("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Association", AssociationType1.class);
    
    // The namespace of the QName
    private final String m_namespace;
    
    // The localPart of the QName
    private final String m_localPart;
    
    // The declaredType of the JAXBElement
    private Class m_declaredType;
    
    /**
     * Constructs an XdsRegistryObjectType
     * 
     * @param namespace
     * @param localPart
     * @param declareType 
     */
    protected XdsRegistryObjectType(String namespace, String localPart, Class declareType) {
        m_namespace = namespace;
        m_localPart = localPart;
        m_declaredType = declareType;
    }

    /**
     * Gets the namespace value
     * 
     * @return 
     */
    public String getNamespace() {
        return m_namespace;
    }

    /**
     * Gets the localPart value
     * 
     * @return 
     */
    public String getLocalPart() {
        return m_localPart;
    }

    /**
     * Gets the declaredType value
     * 
     * @return 
     */
    public Class getDeclaredType() {
        return m_declaredType;
    }
}
