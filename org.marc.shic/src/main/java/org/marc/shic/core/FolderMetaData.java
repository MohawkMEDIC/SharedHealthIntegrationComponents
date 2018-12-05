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
 * @author tylerg Date: Aug 21, 2013
 *
 */
package org.marc.shic.core;

import java.util.ArrayList;

public class FolderMetaData extends DocumentContainerMetaData {

    private CodeValue codeList;
    private String title;
    private String description;

    public FolderMetaData() {
        super();
    }

    public FolderMetaData(CodeValue codeList, String title, String description) {
        this();
        this.codeList = codeList;
        this.title = title;
        this.description = description;
    }
    
  

    public CodeValue getCodeList() {
        return codeList;
    }

    public void setCodeList(CodeValue codeList) {
        this.codeList = codeList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
