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
 * Date: Sept 5, 2013
 *
 */
package org.marc.shic.svs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Garrett
 *
 * Only has support for one list of concepts. This may change in the future if
 * it is needed.
 */
public class ValueSet
{

    private String id;
    private String displayName;
    private String version;
    private List<ConceptList> conceptLists;

    /**
     * Get the id of the value set.
     *
     * @return The id of the value set.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the id of the value set.
     *
     * @param id Id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Get the list of concepts in the value set.
     *
     * @return List of concepts.
     */
    public List<ConceptList> getConceptLists()
    {
        return conceptLists;
    }
    
//    public void addConcept(Concept concept) {
//        conceptList.add(concept);
//    }

    /**
     * Get the display name of the value set.
     *
     * @return The display name of the value set.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }    

    /**
     * Set the display name of the value set.
     *
     * @param displayName Display name to set.
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Default constructor.
     */
    public ValueSet()
    {
        conceptLists = new ArrayList<ConceptList>();
    }

    /**
     * Construct a value set with a requesting id.
     *
     * @param id Id of the requested value set.
     */
    public ValueSet(String id)
    {
        this();
        this.id = id;
    }

    /**
     * Validate the state of the value set.
     *
     * @return True if the value set is valid.
     */
    public boolean validate()
    {
        return this.id != null && this.displayName != null && this.conceptLists.size() > 0;
    }
}