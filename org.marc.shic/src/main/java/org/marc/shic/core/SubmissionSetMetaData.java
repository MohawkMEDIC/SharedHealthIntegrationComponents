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
 * @author Garrett
 */
public class SubmissionSetMetaData extends DocumentContainerMetaData
{

    private ArrayList<Demographic> intendedRecipients;
    private ArrayList<FolderMetaData> folders;

    public SubmissionSetMetaData()
    {
        super();
        this.intendedRecipients = new ArrayList<Demographic>();
        this.folders = new ArrayList<FolderMetaData>();
    }

    public ArrayList<FolderMetaData> getFolders()
    {
        return folders;
    }

    public void addFolder(FolderMetaData folder)
    {
        this.folders.add(folder);
    }

    public ArrayList<Demographic> getIntendedRecipients()
    {
        return intendedRecipients;
    }

    public void addIntendedRecipient(Demographic recipient)
    {
        this.intendedRecipients.add(recipient);
    }
}
