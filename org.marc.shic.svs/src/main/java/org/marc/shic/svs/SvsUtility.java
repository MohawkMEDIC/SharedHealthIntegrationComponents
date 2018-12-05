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

import ihe.iti.svs._2008.CD;
import ihe.iti.svs._2008.ConceptListType;
import ihe.iti.svs._2008.ValueSetResponseType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tylerg
 */
public class SvsUtility
{

    /**
     * Parse the first translation of concepts returned by the value set
     * response.
     *
     * @param valueSetResponse
     * @return
     */
    public static ConceptList parseConceptsFromValueSetResponseType(ValueSetResponseType valueSetResponse)
    {
        ConceptList conceptList = new ConceptList();

        // Loop through the concepts and build a list of them.
        // Only grab the first translation. (0 index)
        for (CD cd : valueSetResponse.getConceptList().get(0).getConcept())
        {
           Concept concept = new Concept(cd.getCode(), cd.getDisplayName(), cd.getCodeSystem(), cd.getCodeSystemVersion(), cd.getCodeSystemName());
            conceptList.addConcept(concept);
        }

        return conceptList;
    }

    /**
     * Parse the concept lists from the value set response.
     * @param valueSetResponse
     * @return Parsed concept lists.
     */
    public static List<ConceptList> parseConceptListsFromValueSetResponseType(ValueSetResponseType valueSetResponse)
    {
        List<ConceptList> conceptLists = new ArrayList<ConceptList>();

        for (ConceptListType conceptListType : valueSetResponse.getConceptList())
        {
            ConceptList conceptList = new ConceptList();
            conceptList.setLang(conceptListType.getLang());

            for (CD cd : conceptListType.getConcept())
            {
                Concept concept = new Concept(cd.getCode(), cd.getDisplayName(), cd.getCodeSystem(), cd.getCodeSystemVersion(), cd.getCodeSystemName());
                conceptList.addConcept(concept);
            }
            
            conceptLists.add(conceptList);
        }

        return conceptLists;
    }
}
