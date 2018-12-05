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
public class DocumentRelationship {
    
    private String targetObject;
    private DocumentRelationshipType type;

    /**
     * Construct a DocumentRelationship containing the targetObject's UUID and
     * an XDS Relationship type.
     * 
     * @param targetObject
     * @param type 
     */
    public DocumentRelationship(String targetObject, DocumentRelationshipType type) {
        this.targetObject = targetObject;
        this.type = type;
    }

    /**
     * Get the targetObject (UUID)
     * 
     * @return 
     */
    public String getTargetObject() {
        return targetObject;
    }

    /**
     * Get the Relationship type
     * 
     * @return 
     */
    public DocumentRelationshipType getType() {
        return type;
    }
}
