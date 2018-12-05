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
 * Date: 21-Nov-2013
 *
 */
package org.marc.shic.core;

/**
 *
 * @author ibrahimm
 */
public class DocumentRelationshipType implements ICodeType {

    
    /**
     * HL7 Document Relationship Replace
     */
    public static final DocumentRelationshipType RPLC = new DocumentRelationshipType("urn:ihe:iti:2007:AssociationType:RPLC");
    /**
     * HL7 Document Relationship Transform
     */
    public static final DocumentRelationshipType XFRM = new DocumentRelationshipType("urn:ihe:iti:2007:AssociationType:XFRM");
    /**
     * HL7 Document Relationship Append
     */
    public static final DocumentRelationshipType APND = new DocumentRelationshipType("urn:ihe:iti:2007:AssociationType:APND");
    /**
     * HL7 Document Relationship Transform and Replace
     */
    public static final DocumentRelationshipType XFRM_RPLC = new DocumentRelationshipType("urn:ihe:iti:2007:AssociationType:XFRM_RPLC");
    /**
     * IHE Signature
     */
    public static final DocumentRelationshipType signs = new DocumentRelationshipType("urn:ihe:iti:2007:AssociationType:signs");
    
    /**
     * relationship code value
     */
    private final String m_code;
    
    /**
     * Construct a Document Relationship of a specific type
     * 
     * @param code 
     */
    public DocumentRelationshipType(String code) {
        this.m_code = code;
    }
    
    /**
     * Get the Document Relationship type
     * 
     * @return 
     */
    public String getCode() {
        return m_code;
    }
    
}
