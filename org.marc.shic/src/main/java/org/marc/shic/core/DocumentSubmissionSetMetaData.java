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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core;

import java.util.ArrayList;

/**
 *
 * @author Nebri
 */
public class DocumentSubmissionSetMetaData
{

    private DomainIdentifier id;
    private ArrayList<DocumentMetaData> documents;
    private PersonDemographic patient;
    private PersonDemographic author;

    public DomainIdentifier getId()
    {
        return id;
    }

    public void setId(DomainIdentifier id)
    {
        this.id = id;
    }

    /**
     *
     * @param document
     */
    public void addDocument(DocumentMetaData document)
    {
        documents.add(document);
    }

    public DocumentSubmissionSetMetaData()
    {
        this.documents = new ArrayList<DocumentMetaData>();
    }

    /**
     *
     * @return
     */
    public ArrayList<DocumentMetaData> getDocuments()
    {
        return documents;
    }

    /**
     *
     *
     * /**
     *
     * @return
     */
    public PersonDemographic getPatient()
    {
        return patient;
    }

    /**
     *
     * @param patient
     */
    public void setPatient(PersonDemographic patient)
    {
        this.patient = patient;
    }

    /**
     *
     * @return
     */
    public PersonDemographic getAuthor()
    {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(PersonDemographic author)
    {
        this.author = author;
    }
}
