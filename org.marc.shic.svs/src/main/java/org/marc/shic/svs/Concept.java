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

/**
 *
 * @author Garrett
 */
public class Concept
{

    private String code;
    private String displayName;
    private String codeSystem;
    private String codeSystemVersion;
    private String codeSystemName;
    
    public String getCodeSystemVersion()
    {
        return codeSystemVersion;
    }

    public void setCodeSystemVersion(String codeSystemVersion)
    {
        this.codeSystemVersion = codeSystemVersion;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getCodeSystem()
    {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem)
    {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystemName()
    {
        return codeSystemName;
    }

    public void setCodeSystemName(String codeSystemName)
    {
        this.codeSystemName = codeSystemName;
    }
    
    

    /**
     * Default constructor.
     */
    public Concept()
    {
    }

    public Concept(String code, String displayName, String codeSystem)
    {
        this.code = code;
        this.displayName = displayName;
        this.codeSystem = codeSystem;
    }

    public Concept(String code, String displayName, String codeSystem, String codeSystemVersion)
    {
        this(code, displayName, codeSystem);
        this.codeSystemVersion = codeSystemVersion;
    }
    
        public Concept(String code, String displayName, String codeSystem, String codeSystemVersion, String codeSystemName)
    {
        this(code, displayName, codeSystem, codeSystemVersion);
        this.codeSystemName = codeSystemName;
    }

    /**
     * Validate the state of the concept.
     *
     * @return True if the concept is valid.
     */
    public boolean validate()
    {
        return this.code != null && this.displayName != null && this.codeSystem != null;
    }
}