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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core;

import java.util.Calendar;

/**
 *
 * @author Nebri
 */
public class DocumentMetaData extends MetaData
{

    private DomainIdentifier logicalId;
    // TODO: Might have to be an enum.
    private String availability;
    private String title;
    private String mimeType;
    private byte[] content;
    private CodeValue classification;
    private Calendar serviceTimeStart;
    private Calendar serviceTimeEnd;
    private CodeValue classCode;
    private CodeValue type;
    private CodeValue format;
    private CodeValue practiceSetting;
    private CodeValue facilityType;
    private DocumentRelationship relationship;

    public DocumentMetaData()
    {
    }

    public DocumentMetaData(String documentUniqueId, String repositoryUniqueId)
    {
        this();
        this.uniqueId = documentUniqueId;
        this.repositoryUniqueId = repositoryUniqueId;
    }

    public DocumentMetaData(String documentUniqueId, String repositoryUniqueId, String homeCommunityId)
    {
        this(documentUniqueId, repositoryUniqueId);
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Gets the logical id
     *
     * @return
     */
    public DomainIdentifier getLogicalId()
    {
        return logicalId;
    }

    /**
     * Sets the logical id
     *
     * @param logicalId
     */
    public void setLogicalId(DomainIdentifier logicalId)
    {
        this.logicalId = logicalId;
    }

    /**
     * Gets the availability status
     *
     * @return
     */
    public String getAvailability()
    {
        return availability;
    }

    /**
     * Sets the availability status
     *
     * @param availability
     */
    public void setAvailability(String availability)
    {
        this.availability = availability;
    }

    /**
     * gets the title of the document
     *
     * @return
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the title of the document
     *
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Get the mime type of the document.
     *
     * @return Document's mime type.
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Set the mime type of the document.
     *
     * @param mimeType Mime type to set.
     */
    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    /**
     * Document contents.
     *
     * @return Contents of the document.
     */
    public byte[] getContent()
    {
        return content;
    }

    /**
     * Sets the contents of the document.
     *
     * @param content Contents of the document to set.
     */
    public void setContent(byte[] content)
    {
        this.content = content;
    }

    /**
     *
     * @return
     */
    public CodeValue getClassification()
    {
        return classification;
    }

    /**
     *
     * @param classification
     */
    public void setClassification(CodeValue classification)
    {
        this.classification = classification;
    }

    /**
     * Gets the serviceStartTime
     *
     * @return
     */
    public Calendar getServiceTimeStart()
    {
        return serviceTimeStart;
    }

    /**
     * Sets the serviceStartTime
     *
     * @param serviceTimeStart
     */
    public void setServiceTimeStart(Calendar serviceTimeStart)
    {
        this.serviceTimeStart = serviceTimeStart;
    }

    /**
     * Gets the serviceStopTime
     *
     * @return
     */
    public Calendar getServiceTimeEnd()
    {
        return serviceTimeEnd;
    }

    /**
     * Sets the serviceStopTime
     *
     * @param serviceTimeEnd
     */
    public void setServiceTimeEnd(Calendar serviceTimeEnd)
    {
        this.serviceTimeEnd = serviceTimeEnd;
    }

    /**
     * Gets the type code
     *
     * @return
     */
    public CodeValue getType()
    {
        return type;
    }

    /**
     * Sets the type code
     *
     * @param type
     */
    public void setType(CodeValue type)
    {
        this.type = type;
    }

    /**
     * Gets the Format code
     *
     * @return
     */
    public CodeValue getFormat()
    {
        return format;
    }

    /**
     * Sets the Format code
     *
     * @param format
     */
    public void setFormat(CodeValue format)
    {
        this.format = format;
    }

    /**
     * Gets the practice setting
     *
     * @return
     */
    public CodeValue getPracticeSetting()
    {
        return practiceSetting;
    }

    /**
     * Sets the practice setting
     *
     * @param practiceSetting
     */
    public void setPracticeSetting(CodeValue practiceSetting)
    {
        this.practiceSetting = practiceSetting;
    }

    /**
     * Gets the Facility type
     *
     * @return
     */
    public CodeValue getFacilityType()
    {
        return facilityType;
    }

    /**
     * Sets the Facility type
     *
     * @param facilityType
     */
    public void setFacilityType(CodeValue facilityType)
    {
        this.facilityType = facilityType;
    }

    /**
     * Gets the class code
     *
     * @return
     */
    public CodeValue getClassCode()
    {
        return classCode;
    }

    /**
     * Sets the class code
     *
     * @param classCode
     */
    public void setClassCode(CodeValue classCode)
    {
        this.classCode = classCode;
    }

    /**
     * Get the Document Relationship (contains the UUID of a DocumentEntry, and
     * a relationship type)
     *
     * @return
     */
    public DocumentRelationship getRelationship()
    {
        return relationship;
    }

    /**
     * Set a Document Relationship to DocumentEntry's UUID
     *
     * @param relationship
     */
    public void setRelationship(DocumentRelationship relationship)
    {
        this.relationship = relationship;
    }
}
