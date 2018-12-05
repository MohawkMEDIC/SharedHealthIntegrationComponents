/*
 * Copyright 2012 Mohawk College of Applied Arts and Technology
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
 * User: Justin Fyfe
 * Date: 11-08-2012
 */
package org.marc.shic.xds;

import org.marc.shic.core.DocumentMetaData;

/**
 * Xds document metadata class
 * 
 * @author ibrahimm
 * @deprecated use DocumentMetaData instead
 */
public class XdsDocumentMetaData extends DocumentMetaData
{
    // Backing field for hash
    private byte[] hash;
    // Backing field for repository unique id
    private String repositoryUniqueId;

    /**
     * Get repository unique id
     */
    public String getRepositoryUniqueId()
    {
        return this.repositoryUniqueId;
    }

    /**
     * Set the repository unique id
     *
     * @param uniqueId
     */
    public void setRepositoryUniqueId(String uniqueId)
    {
        this.repositoryUniqueId = uniqueId;
    }

    /**
     * Gets the repository unique id the document belongs to.
     *
     * @return Repository id the document belongs to.
     */
    public byte[] getHash()
    {
        return this.hash;
    }

    /**
     * Sets the repository unique id that the document belongs to.
     *
     * @param repositoryUniqueId
     */
    public void setHash(byte[] hash)
    {
        this.hash = hash;
    }
}
