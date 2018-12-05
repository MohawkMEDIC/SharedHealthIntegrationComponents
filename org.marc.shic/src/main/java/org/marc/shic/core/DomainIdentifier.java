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

/**
 *
 * @author Nebri
 */
public class DomainIdentifier
{

    private String root;
    private String extension;
    private String assigningAuthority;

    /**
     * Creates a new domain identifier
     */
    public DomainIdentifier()
    {
    }

//    public DomainIdentifier(String extension)
//    {
//        this();
//        this.extension = extension;
//    }
    
    /**
     * Creates a new domain identifier
     * @param root The Universal ID of the Assigning Authority
     * @param extension 
     */
    public DomainIdentifier(String root, String extension)
    {
        this();
        this.root = root;
        this.extension = extension;
    }

    /**
     * Creates a new domain identifier
     *
     * @param root The Universal ID of the Assigning Authority
     * @param extension
     */
    public DomainIdentifier(String root, String extension,
            String assigningAuthority)
    {
        this(root, extension);
        this.assigningAuthority = assigningAuthority;
    }

    /**
     * Sets the Universal ID component of the assigning authority
     * 
     * @param root  The Universal ID of the Assigning Authority
     */
    public void setRoot(String root)
    {
        this.root = root;
    }

    /**
     * Gets the Universal ID component of the assigning authority
     * 
     * @return 
     */
    public String getRoot()
    {
        return root;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the Namespace ID component of the assigning authority
     * 
     * @param assigningAuthority The Namespace ID of the Assigning Authority
     */
    public void setAssigningAuthority(String assigningAuthority)
    {
        this.assigningAuthority = assigningAuthority;
    }

    /**
     * Gets the Namespace ID component of the assigning authority
     * 
     * @return 
     */
    public String getAssigningAuthority()
    {
        return assigningAuthority;
    }
}
