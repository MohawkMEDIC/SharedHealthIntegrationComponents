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
 * @author tylerg Date: Nov 28, 2013
 *
 */
package org.marc.xds.profiles;

import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentRelationshipType;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.XdsGuidMetaDataType;
import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.xds.parameters.XdsAssociationUuid;
import org.marc.shic.xds.parameters.document.XdsDocumentParameter;
import org.marc.shic.xds.parameters.folder.XdsFolderParameter;
import org.marc.shic.xds.parameters.submissionset.XdsSubmissionSetParameter;

/**
 *
 * @author tylerg
 */
public interface IStoredQueryable extends IXdsInteraction {

    public <T extends MetaData> T[] registryStoredQuery(XdsGuidMetaDataType<?> queryType, XdsQuerySpecification querySpec) throws CommunicationsException;

    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException;

    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException;
    
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, XdsDocumentParameter... parameters) throws CommunicationsException;

    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentParameter... parameters) throws CommunicationsException;

    public DocumentMetaData[] findDocuments(PersonDemographic patient) throws CommunicationsException;

    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId) throws CommunicationsException;
    
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public SubmissionSetMetaData[] findSubmissionSets(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;
    
    public SubmissionSetMetaData[] findSubmissionSets(PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;

    public SubmissionSetMetaData[] findSubmissionSets(PersonDemographic patient, String homeCommunityId) throws CommunicationsException;

    public SubmissionSetMetaData[] findSubmissionSets(PersonDemographic patient) throws CommunicationsException;

    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException;

    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException;
    
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId,CodeValue purposeOfUse) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId) throws CommunicationsException;
    
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId) throws CommunicationsException;

    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId) throws CommunicationsException;
    
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public SubmissionSetMetaData[] getAll(PersonDemographic patient) throws CommunicationsException;

    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;

    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException;

    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException;

    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse, DocumentRelationshipType... associationTypes) throws CommunicationsException;

    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, DocumentRelationshipType... associationTypes) throws CommunicationsException;

    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, DocumentRelationshipType... associationTypes) throws CommunicationsException;

    public FolderMetaData[] findFolders(PersonDemographic patient) throws CommunicationsException;

    public FolderMetaData[] findFolders(PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;

    public FolderMetaData[] findFolders(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException;

    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException;

    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException;

    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException;
}
