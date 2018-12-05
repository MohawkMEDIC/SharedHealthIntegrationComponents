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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.marc.shic.core.configuration.consent.IConfidential;

public class MetaData implements IConfidential {

    protected String sourceId;
    protected String uniqueId;
    protected String id;
    protected PersonDemographic patient;
    protected PersonDemographic author;
    protected String homeCommunityId;
    protected byte[] hash;
    protected String repositoryUniqueId;
    protected Calendar creationTime;
    protected Calendar updateTime;
    protected List<CodeValue> confidentiality;
    protected CodeValue contentType;
    private CodeValue purposeOfUse;
    protected String version;
    protected Map<String, Object> extendedAttributes = new HashMap<String, Object>();
    private SubmissionType submissionType; 

    public MetaData() {
        this.confidentiality = new ArrayList<CodeValue>();
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PersonDemographic getPatient() {
        return patient;
    }

    public void setPatient(PersonDemographic patient) {
        this.patient = patient;
    }

    public PersonDemographic getAuthor() {
        return author;
    }

    public void setAuthor(PersonDemographic author) {
        this.author = author;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Gets the repository unique id the document belongs to.
     *
     * @return Repository id the document belongs to.
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * Sets the repository unique id that the document belongs to.
     *
     * @param repositoryUniqueId
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    /**
     * Get repository unique id
     */
    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }

    /**
     * Set the repository unique id
     *
     * @param uniqueId
     */
    public void setRepositoryUniqueId(String uniqueId) {
        this.repositoryUniqueId = uniqueId;
    }

    /**
     * Gets the creation time
     *
     * @return
     */
    public Calendar getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time
     *
     * @param creationTime
     */
    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public List<CodeValue> getConfidentiality() {
        return confidentiality;
    }

    public void addConfidentiality(CodeValue confidentiality) {
        this.confidentiality.add(confidentiality);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String versionInfo) {
        this.version = versionInfo;
    }

    public CodeValue getContentType() {
        return contentType;
    }

    public void setContentType(CodeValue contentType) {
        this.contentType = contentType;
    }

    /**
     * Adds an extended attribute (ie. SlotType1, etc...)
     *
     * @param attribute
     * @param value
     */
    public void addExtendedAttribute(String attribute, Object value) {
        this.extendedAttributes.put(attribute, value);
    }

    /**
     *
     * @param attribute
     * @return
     */
    public Object getExtendedAttribute(String attribute) {
        return this.extendedAttributes.get(attribute);
    }

    public CodeValue getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(CodeValue purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    /**
     * Gets the XDS Submission type for this document/folder
     * 
     * @return 
     */
    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    /**
     * Sets the XDS Submission type for this document/folder
     * 
     * @param submissionType 
     */
    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }
}
