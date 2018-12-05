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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class DocumentContainerMetaData extends MetaData
{

    private List<DocumentMetaData> documents;
    private String availabilityStatus;
    private List<String> comments;

    public DocumentContainerMetaData()
    {
        super();
        this.comments = new ArrayList<String>();
        this.documents = new ArrayList<DocumentMetaData>();
    }

    public List<DocumentMetaData> getDocuments()
    {
        return documents;
    }

    public void addDocument(DocumentMetaData document)
    {
        this.documents.add(document);
    }

    public String getAvailabilityStatus()
    {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus)
    {
        this.availabilityStatus = availabilityStatus;
    }

    public List<String> getComments()
    {
        return comments;
    }

    public void addComment(String comment)
    {
        this.comments.add(comment);
    }
}
