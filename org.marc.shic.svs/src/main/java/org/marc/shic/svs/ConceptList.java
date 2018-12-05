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
 * @author
 * @{user}
 * Date:
 * @{date}
 *
 */
package org.marc.shic.svs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tylerg
 */
public class ConceptList
{

    private String lang;
    private List<Concept> conceptList;

    public ConceptList()
    {
        this.conceptList = new ArrayList<Concept>();
    }

    public String getLang()
    {
        return this.lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public List<Concept> getConceptList()
    {
        return this.conceptList;
    }

    public void addConcept(Concept concept)
    {
        this.conceptList.add(concept);
    }
}
