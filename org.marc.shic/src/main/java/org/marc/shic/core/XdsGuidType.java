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
package org.marc.shic.core;

import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.XdsGuidMetaDataType;

/**
 * Identifies the type of query that is occurring
 */
public class XdsGuidType
{

    /**
     * Submission set classification id
     */
    public static final XdsGuidType XDSSubmissionSet = new XdsGuidType("a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "XDSSubmissionSet");
    /**
     * XDSFolder classification id
     */
    public static final XdsGuidType XDSFolder = new XdsGuidType("d9d542f3-6cc4-48b6-8870-ea235fbc94c2", "XDSFolder");
    /**
     * Submission set author classification id
     */
    public static final XdsGuidType XDSSubmissionSet_Author = new XdsGuidType("a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d");
    /**
     * Submission set content/type
     */
    public static final XdsGuidType XDSSubmissionSet_ContentType = new XdsGuidType("aa543740-bdda-424e-8c96-df4873be8500");
    /**
     * Submission set patient id
     */
    public static final XdsGuidType XDSSubmissionSet_PatientId = new XdsGuidType("6b5aea1a-874d-4603-a4bc-96a0a7b38446", "XDSSubmissionSet.patientId");
    /**
     * Submission set source id
     */
    public static final XdsGuidType XDSSubmissionSet_SourceId = new XdsGuidType("554ac39e-e3fe-47fe-b233-965d2a147832", "XDSSubmissionSet.sourceId");
    /**
     * Submission set unique id
     */
    public static final XdsGuidType XDSSubmissionSet_UniqueId = new XdsGuidType("96fdda7c-d067-4183-912e-bf5ee74998a8", "XDSSubmissionSet.uniqueId");
    /**
     * Submission set coding scheme
     */
    public static final XdsGuidType XDSSubmissionSet_CodingScheme = new XdsGuidType("aa543740-bdda-424e-8c96-df4873be8500", "XDSSubmissionSet.codingScheme");
    /**
     * Folder unique id
     */
    public static final XdsGuidType XDSFolder_UniqueId = new XdsGuidType("75df8f67-9973-4fbe-a900-df66cefecc5a", "XDSFolder.uniqueId");
    /**
     * Folder code list. ClassificationType.
     */
    public static final XdsGuidType XDSFolder_CodeList = new XdsGuidType("1ba97051-7806-41a8-a48b-8fce7af683c5", "XDSFolder.codeList");
    /**
     * Folder patient id
     */
    public static final XdsGuidType XDSFolder_PatientId = new XdsGuidType("f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a", "XDSFolder.patientId");
    /**
     * Limited meta data classification node
     */
    public static final XdsGuidType XDSSubmissionSet_LimitedMetaData = new XdsGuidType("5003a9db-8d8d-49e6-bf0c-990e34ac7707");
    /**
     * XDS document entry type
     */
    public static final XdsGuidType XDSDocumentEntry = new XdsGuidType("7edca82f-054d-47f2-a032-9b2a5b5186c1");
    /**
     * XDS Document author
     */
    public static final XdsGuidType XDSDocumentEntry_Author = new XdsGuidType("93606bcf-9494-43ec-9b4e-a7748d1a838d");
    /**
     * XDS document entry class code
     */
    public static final XdsGuidType XDSDocumentEntry_ClassCode = new XdsGuidType("41a5887f-8865-4c09-adf7-e362475b143a");
    /**
     * XDS document confidentiality code
     */
    public static final XdsGuidType XDSDocumentEntry_ConfidentialityCode = new XdsGuidType("f4f85eac-e6cb-4883-b524-f2705394840f");
    /**
     * XDS document entry event code list
     */
    public static final XdsGuidType XDSDocumentEntry_EventCodeList = new XdsGuidType("2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4");
    /**
     * XDS docuent entry format code
     */
    public static final XdsGuidType XDSDocumentEntry_FormatCode = new XdsGuidType("a09d5840-386c-46f2-b5ad-9c3699a4309d");
    /**
     * XDS document entry patient id
     */
    public static final XdsGuidType XDSDocumentEntry_PatientId = new XdsGuidType("58a6f841-87b3-4a3e-92fd-a8ffeff98427", "XDSDocumentEntry.patientId");
    /**
     * XDS document entry practice setting
     */
    public static final XdsGuidType XDSDocumentEntry_PracticeSettingCode = new XdsGuidType("cccf5598-8b07-4b77-a05e-ae952c785ead");
    /**
     * XDS document entry practice setting
     */
    public static final XdsGuidType XDSDocumentEntry_HealthcareFacilityTypeCode = new XdsGuidType("f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1");
    /**
     * XDS document entry type code
     */
    public static final XdsGuidType XDSDocumentEntry_TypeCode = new XdsGuidType("f0306f51-975f-434e-a61c-c59651d33983");
    /**
     * XDS document entry unique identifier
     */
    public static final XdsGuidType XDSDocumentEntry_UniqueId = new XdsGuidType("2e82c1f6-a085-4c72-9da3-8640a32e42ab", "XDSDocumentEntry.uniqueId");
    /**
     * XDS limited meta data classification
     */
    public static final XdsGuidType XDSDocumentEntry_LimitedMetaData = new XdsGuidType("ab9b591b-83ab-4d03-8f5d-f93b1fb92e85");
    /**
     * XDS FindDocuments query
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_FindDocuments = new XdsGuidMetaDataType("14d4debf-8f97-4251-9a74-a90016b0af0d", "FindDocuments", DocumentMetaData.class, "$XDSDocumentEntryPatientId", "$XDSDocumentEntryStatus");
    /**
     * XDS FindSubmissionSets query
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_FindSubmissionSets = new XdsGuidMetaDataType("f26abbcb-ac74-4422-8a30-edb644bbc1a9", "FindSubmissionSets", SubmissionSetMetaData.class, "$XDSSubmissionSetPatientId", "$XDSSubmissionSetStatus");
    /**
     * XDS GetAll query
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetAll = new XdsGuidMetaDataType("10b545ea-725c-446d-9b95-8aeb444eddf3", "GetAll", SubmissionSetMetaData.class, "$patientId", "$XDSDocumentEntryStatus", "$XDSFolderStatus", "$XDSSubmissionSetStatus");
    /**
     * XDS FindFolders
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_FindFolders = new XdsGuidMetaDataType("958f3006-baad-4929-a4de-ff1114824431", "FindFolders", FolderMetaData.class, "$XDSFolderPatientId", "$XDSFolderStatus");
    /**
     * XDS GetDocuments
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetDocuments = new XdsGuidMetaDataType("5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4", "GetDocuments", DocumentMetaData.class);
    /**
     * XDS GetFolders
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetFolders = new XdsGuidMetaDataType("5737b14c-8a1a-4539-b659-e03a34a5e1e4", "GetFolders", FolderMetaData.class);
    /**
     * XDS GetDocumentsAndAssociations
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetDocumentsAndAssociations = new XdsGuidMetaDataType("bab9529a-4a10-40b3-a01f-f68a615d247a", "GetDocumentsAndAssociations", MetaData.class);
    /**
     * XDS GetSubmissionSets
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetSubmissionSets = new XdsGuidMetaDataType("51224314-5390-4169-9b91-b1980040715a", "GetSubmissionSets", SubmissionSetMetaData.class, "$uuid");
    /**
     * XDS GetSubmissionSetAndContents
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetSubmissionSetAndContents = new XdsGuidMetaDataType("e8e3cb2c-e39c-46b9-99e4-c12f57260b83", "GetSubmissionSetAndContents", SubmissionSetMetaData.class);
    /**
     * XDS GetFolderAndContents
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetFolderAndContents = new XdsGuidMetaDataType("b909a503-523d-4517-8acf-8e5834dfc4c7", "GetFolderAndContents", FolderMetaData.class, "$XDSFolderUniqueId");
    /**
     * XDS GetFoldersForDocument
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetFoldersForDocument = new XdsGuidMetaDataType("10cae35a-c7f9-4cf5-b61e-fc3278ffb578", "GetFoldersForDocument", FolderMetaData.class);
    /**
     * XDS GetRelatedDocuments
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetRelatedDocuments = new XdsGuidMetaDataType("d90e5407-b356-4d91-a89f-873917b4b0e6", "GetRelatedDocuments", MetaData.class, "$AssociationTypes");
    /**
     * XDS GetAssociations
     */
    public static final XdsGuidMetaDataType RegistryStoredQuery_GetAssociations = new XdsGuidMetaDataType("a7ae438b-4bc2-4642-93e9-be891f7bb155", "GetAssociations", AssociationMetaData.class, "$uuid");
    // The guid of the spec type
    protected final String m_queryGuid;
    // The name value of the guid (if supplied)
    protected String m_guidName;

    /**
     * Creates a new instance of the XDS query specification guid
     */
    public XdsGuidType(String queryGuid)
    {
        this.m_queryGuid = queryGuid;
    }

    /**
     * Creates a new instance of the XDS query specification guid along with its
     * associated name
     */
    protected XdsGuidType(String queryGuid, String guidName)
    {
        this(queryGuid);
        this.m_guidName = guidName;
    }

    /**
     * Gets the query specification guid
     *
     * @return
     */
    public String getGuid()
    {
        return this.m_queryGuid;
    }

    /**
     * Gets the name of the guid
     *
     * @return
     */
    public String getName()
    {
        return this.m_guidName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof String)
        {
            return String.format("urn:uuid:%s", this.m_queryGuid).equals(obj);
        }
        return super.equals(obj);
    }

    /**
     * Returns the guid with "urn:uuid:" prepended.
     *
     * @return
     */
    @Override
    public String toString()
    {
        return String.format("urn:uuid:%s", this.m_queryGuid);
    }
}
