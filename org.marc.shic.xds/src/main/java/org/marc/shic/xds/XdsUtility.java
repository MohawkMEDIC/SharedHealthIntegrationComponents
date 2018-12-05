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
package org.marc.shic.xds;

import org.marc.shic.core.XdsGuidType;
import java.math.BigInteger;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.*;
import static org.marc.shic.core.PartType.Family;
import static org.marc.shic.core.PartType.Given;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.namespace.QName;
import javax.xml.bind.JAXBElement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marc.shic.core.utils.FormatHelpers;

import org.marc.shic.xds.repository.*;

/**
 * A class (package scoped) that takes care of interacting with the XDS proxy
 * classes. It will be used to generate the code for transactions such as: -
 * ProvideAndRegisterDocumentSet-b (ITI-41)
 *
 * @author Mohamed
 */
class XdsUtility {

    /**
     * The new helper method to generate a Provide and Register Document Set-b
     * Request object
     *
     * @param metaData
     * @return
     * @throws CommunicationsException
     */
    static ProvideAndRegisterDocumentSetRequestType generateProvideAndRegisterRequest(MetaData metaData) throws CommunicationsException {

        ProvideAndRegisterDocumentSetRequestType retVal = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest sor = new SubmitObjectsRequest();
        RegistryObjectListType rol = new RegistryObjectListType();
        sor.setRegistryObjectList(rol);
        retVal.setSubmitObjectsRequest(sor);

        // create the submission set and add it to the RegistryObjectList
        RegistryPackageType submissionSet = createSubmissionSet(metaData);
        JAXBElement jaxbRegistryPackage = createJAXBElement(submissionSet, XdsRegistryObjectType.XdsRegistryPackageType);
        sor.getRegistryObjectList().getIdentifiable().add(jaxbRegistryPackage);

        String folderId = null;

        // is this a folder submission?
        if (metaData instanceof FolderMetaData) {
            FolderMetaData folder = (FolderMetaData) metaData;

            // if the folder exists, don't create its registry package (null check for backward compatibility)
            if (folder.getSubmissionType() == null || folder.getSubmissionType() == SubmissionType.NEW) {

                // generate a random OID for the folder UID if not set
                if (folder.getUniqueId() == null) {
                    folder.setUniqueId(generateRandomOid(null));
                }
                
                // generate a random UUID for the folder
                if (folder.getId() == null) {
                    folder.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
                }

                // create the folder and add it to the RegistryObjectList
                RegistryPackageType folderPackage = createXdsFolder(folder);
                JAXBElement jaxbFolderPackage = createJAXBElement(folderPackage, XdsRegistryObjectType.XdsRegistryPackageType);
                sor.getRegistryObjectList().getIdentifiable().add(jaxbFolderPackage);

                // Create the folder-submissionSet association
                JAXBElement folSubAssoc = associate(submissionSet.getId(), folder.getId(), XdsAssociationType.HasMember, null);
                sor.getRegistryObjectList().getIdentifiable().add(folSubAssoc);
            }
            
            // grab the id of the folder (either new or existing)
            folderId = folder.getId();

        }

        // is this a multiple document submission? (also works for documents within a folder
        if (metaData instanceof DocumentContainerMetaData) {
            // Create and add the documents (if new) to the submissionset
            for (DocumentMetaData document : ((DocumentContainerMetaData) metaData).getDocuments()) {

                // if the document exists, don't create the document entry (null check for backward compatibility)
                if (document.getSubmissionType() == null || document.getSubmissionType() == SubmissionType.NEW) {

                    // generate a random OID for the document UID and create it if not set
                    if (document.getUniqueId() == null) {
                        document.setUniqueId(generateRandomOid(null));
                    }
                    
                    // generate a random UUID for the document
                    if (document.getId() == null) {
                        document.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
                    }

                    // create the document entry and add it to the RegistryObjectList
                    ExtrinsicObjectType documentEntry = createDocumentEntry(document);
                    JAXBElement jaxbDocumentEntry = createJAXBElement(documentEntry, XdsRegistryObjectType.XdsExtrinsicObjectType);
                    sor.getRegistryObjectList().getIdentifiable().add(jaxbDocumentEntry);

                    // Create the documentEntry-submissionSet association
                    JAXBElement subDocAssoc = associate(submissionSet.getId(), document.getId(), XdsAssociationType.HasMember, "Original");
                    sor.getRegistryObjectList().getIdentifiable().add(subDocAssoc);

                    // Add the actual document (content)
                    retVal.getDocument().add(createDocument(document));
                }

                // Associations - multiple documents or documents in a folder
                if (metaData instanceof FolderMetaData && folderId != null) {
                    // Create and add the folder-document association
                    JAXBElement folDocAssoc = associate(folderId, document.getId(), XdsAssociationType.HasMember, null);
                    sor.getRegistryObjectList().getIdentifiable().add(folDocAssoc);

                    // Create and add the ((folder-document association)-submissionSet) association
                    JAXBElement folDocSubAssoc = associate(submissionSet.getId(), ((AssociationType1) folDocAssoc.getValue()).getId(), XdsAssociationType.HasMember, null);
                    sor.getRegistryObjectList().getIdentifiable().add(folDocSubAssoc);
                }

                // Check the Document Relationship
                if (document.getRelationship() != null) {
                    // relationship submission
                    JAXBElement docRelationshipAssoc = associate(document.getId(), document.getRelationship().getTargetObject(), document.getRelationship().getType(), null);
                    sor.getRegistryObjectList().getIdentifiable().add(docRelationshipAssoc);
                }

            }
        }

        // is this a single document submission?
        if (metaData instanceof DocumentMetaData) {
            DocumentMetaData document = (DocumentMetaData) metaData;

            // at this point a new submission is implied. Document Relationships are a separate matter,
            // and they get applied further down regardless of the submission type

            // generate a random OID for the document id and create it if not set
            if (document.getUniqueId() == null) {
                document.setUniqueId(generateRandomOid(null));
            }

            // generate a random UUID for the document
            if (document.getId() == null) {
                document.setId(String.format("urn:uuid:%s", UUID.randomUUID().toString()));
            }

            // create the document entry and add it to the RegistryObjectList
            ExtrinsicObjectType documentEntry = createDocumentEntry(document);
            JAXBElement jaxbDocumentEntry = createJAXBElement(documentEntry, XdsRegistryObjectType.XdsExtrinsicObjectType);
            sor.getRegistryObjectList().getIdentifiable().add(jaxbDocumentEntry);

            // Create the documentEntry-submissionSet association (HasMember)
            JAXBElement docSubAssoc = associate(submissionSet.getId(), document.getId(), XdsAssociationType.HasMember, "Original");
            sor.getRegistryObjectList().getIdentifiable().add(docSubAssoc);

            // Check the Document Relationship
            if (document.getRelationship() != null) {
                // relationship submission
                JAXBElement docRelationshipAssoc = associate(document.getId(), document.getRelationship().getTargetObject(), document.getRelationship().getType(), null);
                sor.getRegistryObjectList().getIdentifiable().add(docRelationshipAssoc);
            }

            // Add the actual document (content)
            retVal.getDocument().add(createDocument(document));

        }

        return retVal;
    }

    /**
     * Creates and returns a JAXBElement containing the Association between a
     * given sourceObject and a targetObjects.
     *
     * @param sourceObject
     * @param targetObject
     * @param submissionSetStatus
     * @return
     */
    private static JAXBElement associate(String sourceObject, String targetObject, DocumentRelationshipType associationType, String submissionSetStatus) {
        JAXBElement retVal = null;
        AssociationType1 association = createAssociation(sourceObject, targetObject, associationType, submissionSetStatus);
        retVal = createJAXBElement(association, XdsRegistryObjectType.XdsAssociationType);

        return retVal;
    }

    /**
     * Create Extrinsic object (DocumentEntry) containing the document's meta
     * data
     *
     * @param document
     * @return
     */
    private static ExtrinsicObjectType createDocumentEntry(DocumentMetaData document) {
        ExtrinsicObjectType retVal = new ExtrinsicObjectType();
//        retVal.setId(String.format("Document_%s", UUID.randomUUID().toString()));
        retVal.setId(document.getId());
        retVal.setMimeType(document.getMimeType());
        retVal.setObjectType(XdsGuidType.XDSDocumentEntry.toString());
        retVal.setName(createName(document.getTitle()));

        // Slots
        DateFormat df = FormatHelpers.SimpleDateFormat_yyyyMMddHHmmss;
        retVal.getSlot().add(createSlot("creationTime", df.format(document.getCreationTime().getTime())));
        retVal.getSlot().add(createSlot("serviceStartTime", df.format(document.getServiceTimeStart().getTime())));
        retVal.getSlot().add(createSlot("serviceStopTime", df.format(document.getServiceTimeEnd().getTime())));
        retVal.getSlot().add(createSlot("languageCode", "en-us"));
        retVal.getSlot().add(createSlot("sourcePatientId", generatePatientId(document.getPatient())));

        if (document.getRepositoryUniqueId() != null) {
            retVal.getSlot().add(createSlot("repositoryUniqueId", document.getRepositoryUniqueId()));
        }

        if (document.getExtendedAttribute("legalAuthenticator") != null) {
            retVal.getSlot().add(createSlot("legalAuthenticator", document.getExtendedAttribute("legalAuthenticator").toString()));
        }

        // Classifications
        if (document.getAuthor() != null) {
            SlotType1 authorInstitution = null;
            if (document.getExtendedAttribute("authorInstitution") != null) {
                authorInstitution = createSlot("authorInstitution", document.getExtendedAttribute("authorInstitution").toString());
            }

            SlotType1 authorTelecommunication = null;
            if (document.getAuthor().getPhone() != null) {
                authorTelecommunication = createSlot("authorTelecommunication", generateXTN(document.getAuthor().getPhone()));
            }

            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_Author, "", null, createSlot("authorPerson", generateXCN(document.getAuthor())), authorInstitution, authorTelecommunication));
        }
        if (document.getClassCode() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_ClassCode, document.getClassCode().getCode(), document.getClassCode().getDisplayName(), createSlot("codingScheme", document.getClassCode().getCodeSystem())));
        }
        if (document.getConfidentiality() != null) {
            for (CodeValue code : document.getConfidentiality()) {
                retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_ConfidentialityCode, code.getCode(), code.getDisplayName(), createSlot("codingScheme", code.getCodeSystem())));
            }
        }
        if (document.getFormat() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_FormatCode, document.getFormat().getCode(), document.getFormat().getDisplayName(), createSlot("codingScheme", document.getFormat().getCodeSystem())));
        }
        if (document.getFacilityType() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_HealthcareFacilityTypeCode, document.getFacilityType().getCode(), document.getFacilityType().getDisplayName(), createSlot("codingScheme", document.getFacilityType().getCodeSystem())));
        }
        if (document.getPracticeSetting() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_PracticeSettingCode, document.getPracticeSetting().getCode(), document.getPracticeSetting().getDisplayName(), createSlot("codingScheme", document.getPracticeSetting().getCodeSystem())));
        }
        if (document.getType() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_TypeCode, document.getType().getCode(), document.getType().getDisplayName(), createSlot("codingScheme", document.getType().getCodeSystem())));
        }

        // External Identifiers
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSDocumentEntry_UniqueId, document.getUniqueId()));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSDocumentEntry_PatientId, generatePatientId(document.getPatient())));

        return retVal;
    }

    /**
     * Creates a Registry package containing an xds folder submission's meta
     * data
     *
     * @param folder
     * @return
     */
    private static RegistryPackageType createXdsFolder(FolderMetaData folder) {
        RegistryPackageType retVal = new RegistryPackageType();
//        retVal.setId(String.format("XdsFolder_%s", UUID.randomUUID().toString()));
        retVal.setId(folder.getId());
        retVal.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        // Name
        retVal.setName(createName(folder.getTitle()));

        // Description
        retVal.setDescription(createName(folder.getDescription()));

        // Classifications
        retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSFolder_CodeList, folder.getCodeList().getCode(), folder.getCodeList().getDisplayName(), createSlot("codingScheme", folder.getCodeList().getCodeSystem())));

        // Classification Node (label the registry package as a folder)
        ClassificationType xdsFolderClassification = createClassificationNode(retVal, XdsGuidType.XDSFolder);
        retVal.getClassification().add(xdsFolderClassification);

        // External Identifiers
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSFolder_UniqueId, folder.getUniqueId()));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSFolder_PatientId, generatePatientId(folder.getPatient())));

        return retVal;
    }

    /**
     * Create a registry package for a submission set
     *
     * @param metaData
     * @return
     */
    private static RegistryPackageType createSubmissionSet(MetaData metaData) {
        RegistryPackageType retVal = new RegistryPackageType();
        retVal.setId(String.format("SubmissionSet_%s", UUID.randomUUID().toString()));
        retVal.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        // Slots
        DateFormat df = FormatHelpers.SimpleDateFormat_yyyyMMddHHmmss;
        retVal.getSlot().add(createSlot("submissionTime", df.format(metaData.getCreationTime().getTime())));

        // Classifications
        if (metaData.getAuthor() != null) {
            SlotType1 authorInstitution = null;
            if (metaData.getExtendedAttribute("authorInstitution") != null) {
                authorInstitution = createSlot("authorInstitution", metaData.getExtendedAttribute("authorInstitution").toString());
            }

            SlotType1 authorTelecommunication = null;
            if (metaData.getAuthor().getPhone() != null) {
                authorTelecommunication = createSlot("authorTelecommunication", generateXTN(metaData.getAuthor().getPhone()));
            }

            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSSubmissionSet_Author, "", null, createSlot("authorPerson", generateXCN(metaData.getAuthor())), authorInstitution, authorTelecommunication));
        }
        SlotType1 contentTypeCode = createSlot("codingScheme", metaData.getContentType().getCodeSystem());
        retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSSubmissionSet_ContentType, metaData.getContentType().getCode(), metaData.getContentType().getDisplayName(), contentTypeCode));

        // Classification Node (label the registry package as a submission set)
        ClassificationType submissionSetClassification = createClassificationNode(retVal, XdsGuidType.XDSSubmissionSet);
        retVal.getClassification().add(submissionSetClassification);

        // External Identifiers
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_UniqueId, generateRandomOid(null)));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_SourceId, metaData.getSourceId()));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_PatientId, generatePatientId(metaData.getPatient())));

        return retVal;
    }

    /**
     * Creates a JAXBElement out of a RegistryObjectType with the supplied
     * XdsRegistryObjectType metadata
     *
     * @param submissionSet
     * @param type
     * @return
     */
    private static JAXBElement<RegistryObjectType> createJAXBElement(RegistryObjectType submissionSet, XdsRegistryObjectType type) {
        JAXBElement<RegistryObjectType> retVal = new JAXBElement<RegistryObjectType>(
                new QName(type.getNamespace(), type.getLocalPart()),
                type.getDeclaredType(),
                submissionSet);

        return retVal;
    }

    /**
     *
     * @param documentSubmission
     * @param affinityDomain
     * @return
     * @throws CommunicationsException
     * @deprecated
     */
    @Deprecated
    public ProvideAndRegisterDocumentSetRequestType generateProvideAndRegisterRequest(DocumentSubmissionSetMetaData documentSubmission, IheAffinityDomainConfiguration affinityDomain) throws CommunicationsException {
        // create the SubmissionSet out of the documentSubmission property and add to the request
        ProvideAndRegisterDocumentSetRequestType retVal = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest sor = new SubmitObjectsRequest();
        RegistryObjectListType rol = new RegistryObjectListType();
        sor.setRegistryObjectList(rol);
        retVal.setSubmitObjectsRequest(sor);

        // add a document entry, and a submission set for every submitted document
        for (DocumentMetaData doc : documentSubmission.getDocuments()) {
            // RegistryObjectList (ExtrinsicObject, RegistryPackage, Classification, and Association)
            RegistryObjectListType registryObjectList;
            try {
                registryObjectList = createRegistryObjectList(documentSubmission, doc, affinityDomain);
            } catch (CommunicationsException ex) {
                Logger.getLogger(XdsUtility.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            sor.getRegistryObjectList().getIdentifiable().addAll(registryObjectList.getIdentifiable());

            // Document (content)
            ProvideAndRegisterDocumentSetRequestType.Document document = new ProvideAndRegisterDocumentSetRequestType.Document();
            document.setId(registryObjectList.getIdentifiable().get(0).getValue().getId()); // gets the document id from ExtrinsicObjectType
            document.setValue(doc.getContent());
            retVal.getDocument().add(document);
        }

        return retVal;
    }

    @Deprecated
    private static RegistryObjectListType createRegistryObjectList(DocumentSubmissionSetMetaData docSub, DocumentMetaData document, IheAffinityDomainConfiguration affinityDomain) throws CommunicationsException {
        RegistryObjectListType retVal = new RegistryObjectListType();
        String documentUniqueId = String.format("%s", UUID.randomUUID().toString());

        ExtrinsicObjectType documentEntry;
        try {
            documentEntry = createExtrinsicObject(docSub, document, documentUniqueId, affinityDomain);
        } catch (CommunicationsException ex) {
            Logger.getLogger(XdsUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        RegistryPackageType submissionSet = createRegistryPackage(docSub, document, documentUniqueId, affinityDomain);
        ClassificationType classification = createClassificationNode(submissionSet, XdsGuidType.XDSSubmissionSet);
        AssociationType1 association = createAssociation(submissionSet.getId(), documentEntry.getId(), XdsAssociationType.HasMember, null);

        // ExtrinsicObject - generate the object as JAXBElement (which will be added to the RegistryObjectList)
        JAXBElement<ExtrinsicObjectType> jaxbDocumentEntry = new JAXBElement<ExtrinsicObjectType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject"),
                ExtrinsicObjectType.class,
                documentEntry);

        // RegistryPackage - generate the object as a JAXBElement (which will be added to the RegistryObjectList)
        JAXBElement<RegistryPackageType> jaxbRegistryPackage = new JAXBElement<RegistryPackageType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"),
                RegistryPackageType.class,
                submissionSet);

        // Classification - generate the object as a JAXBElement (which will be added to the RegistryObjectList)
        JAXBElement<ClassificationType> jaxbClassification = new JAXBElement<ClassificationType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Classification"),
                ClassificationType.class,
                classification);

        // Association - generate the object as a JAXBElement (which will be added to the RegistryObjectList)
        JAXBElement<AssociationType1> jaxbAssociation = new JAXBElement<AssociationType1>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Association"),
                AssociationType1.class,
                association);

        retVal.getIdentifiable().add(jaxbDocumentEntry);
        retVal.getIdentifiable().add(jaxbRegistryPackage);
        retVal.getIdentifiable().add(jaxbClassification);
        retVal.getIdentifiable().add(jaxbAssociation);

        return retVal;
    }

    @Deprecated
    private static ExtrinsicObjectType createExtrinsicObject(DocumentSubmissionSetMetaData docSub, DocumentMetaData doc, String uniqueId, IheAffinityDomainConfiguration affinityDomain) throws CommunicationsException {
        ExtrinsicObjectType retVal = new ExtrinsicObjectType();
        retVal.setId(String.format("Document_%s", UUID.randomUUID().toString()));
        retVal.setMimeType(doc.getMimeType());
        retVal.setObjectType(XdsGuidType.XDSDocumentEntry.toString());
        retVal.setName(createName(doc.getTitle()));

        // Slots
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        Calendar time;
        try {
            time = NTPHelper.getTime(affinityDomain);
        } catch (CommunicationsException ex) {
            Logger.getLogger(XdsUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        retVal.getSlot().add(createSlot("creationTime", df.format(doc.getCreationTime().getTime())));
        retVal.getSlot().add(createSlot("serviceStartTime", df.format(time.getTime())));
        retVal.getSlot().add(createSlot("serviceStopTime", df.format(time.getTime())));
        retVal.getSlot().add(createSlot("languageCode", "en-us"));
        retVal.getSlot().add(createSlot("sourcePatientId", generatePatientId(docSub.getPatient())));

        if (doc.getExtendedAttribute("legalAuthenticator") != null) {
            retVal.getSlot().add(createSlot("legalAuthenticator", doc.getExtendedAttribute("legalAuthenticator").toString()));
        }

        // Classifications
        retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_Author, null, null, createSlot("authorPerson", generateXCN(docSub.getAuthor())), createSlot("authorInstitution", doc.getExtendedAttribute("authorInstitution").toString()), createSlot("authorTelecommunication", generateXTN(docSub.getAuthor().getPhone()))));
        if (doc.getClassCode() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_ClassCode, doc.getClassCode().getCode(), doc.getClassCode().getDisplayName(), createSlot("codingScheme", doc.getClassCode().getCodeSystem())));
        }
        if (doc.getConfidentiality() != null) {
            for (CodeValue code : doc.getConfidentiality()) {
                retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_ConfidentialityCode, code.getCode(), code.getDisplayName(), createSlot("codingScheme", code.getCodeSystem())));
            }
            //  retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_ConfidentialityCode, doc.getConfidentiality().getCode(), doc.getConfidentiality().getDisplayName(), createSlot("codingScheme", doc.getConfidentiality().getCodeSystem())));
        }
        if (doc.getFormat() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_FormatCode, doc.getFormat().getCode(), doc.getFormat().getDisplayName(), createSlot("codingScheme", doc.getFormat().getCodeSystem())));
        }
        if (doc.getFacilityType() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_HealthcareFacilityTypeCode, doc.getFacilityType().getCode(), doc.getFacilityType().getDisplayName(), createSlot("codingScheme", doc.getFacilityType().getCodeSystem())));
        }
        if (doc.getPracticeSetting() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_PracticeSettingCode, doc.getPracticeSetting().getCode(), doc.getPracticeSetting().getDisplayName(), createSlot("codingScheme", doc.getPracticeSetting().getCodeSystem())));
        }
        if (doc.getType() != null) {
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSDocumentEntry_TypeCode, doc.getType().getCode(), doc.getType().getDisplayName(), createSlot("codingScheme", doc.getType().getCodeSystem())));
        }

        // External Identifiers
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSDocumentEntry_UniqueId, uniqueId));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSDocumentEntry_PatientId, generatePatientId(docSub.getPatient())));

        return retVal;
    }

    @Deprecated
    private static RegistryPackageType createRegistryPackage(DocumentSubmissionSetMetaData docSub, DocumentMetaData doc, String uniqueId, IheAffinityDomainConfiguration affinityDomain) throws CommunicationsException {
        RegistryPackageType retVal = new RegistryPackageType();
        retVal.setId(String.format("SubmissionSet_%s", UUID.randomUUID().toString()));
        retVal.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        // Slots
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        Calendar time;
        try {
            time = NTPHelper.getTime(affinityDomain);
        } catch (CommunicationsException ex) {
            Logger.getLogger(XdsUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        retVal.getSlot().add(createSlot("submissionTime", df.format(time.getTime())));

        // Classifications
        SlotType1 authorPerson = createSlot("authorPerson", generateFullName(docSub.getAuthor()));
        SlotType1 authorInstitution = createSlot("authorInstitution", doc.getExtendedAttribute("authorInstitution").toString());
        retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSSubmissionSet_Author, null, null, authorPerson, authorInstitution));
        if (doc.getContentType() != null) {
            SlotType1 contentTypeCode = createSlot("codingScheme", doc.getContentType().getCodeSystem());
            retVal.getClassification().add(createClassification(retVal.getId(), XdsGuidType.XDSSubmissionSet_ContentType, doc.getContentType().getCode(), doc.getContentType().getDisplayName(), contentTypeCode));
        }

        // External Identifiers
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_UniqueId, uniqueId));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_SourceId, generateSourceId(docSub.getId())));
        retVal.getExternalIdentifier().add(createExternalIdentifier(retVal.getId(), XdsGuidType.XDSSubmissionSet_PatientId, generatePatientId(docSub.getPatient())));

        return retVal;
    }

    private static ClassificationType createClassificationNode(RegistryObjectType registryPackage, XdsGuidType scheme) {
        ClassificationType retClassNode = new ClassificationType();
        retClassNode.setId(String.format("Classification_%s", UUID.randomUUID().toString()));
        retClassNode.setClassifiedObject(registryPackage.getId());
        retClassNode.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        retClassNode.setClassificationNode(scheme.toString());

        return retClassNode;
    }

    private static AssociationType1 createAssociation(String source, String target, DocumentRelationshipType associationType, String submissionSetStatus) {
        AssociationType1 retAssoc = new AssociationType1();
        retAssoc.setId(String.format("Association_%s", UUID.randomUUID().toString()));
        retAssoc.setAssociationType(associationType.getCode());
        retAssoc.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
        retAssoc.setSourceObject(source);
        retAssoc.setTargetObject(target);

        // If a submission set status is available, set it
        if (submissionSetStatus != null) {
            SlotType1 slot = createSlot("SubmissionSetStatus", submissionSetStatus);
            retAssoc.getSlot().add(slot);
        }

        return retAssoc;
    }

    private static SlotType1 createSlot(String slotName, String... value) {
        SlotType1 retSlot = new SlotType1();
        retSlot.setName(slotName);
        ValueListType valueListType = new ValueListType();
        valueListType.getValue().addAll(Arrays.asList(value));
        retSlot.setValueList(valueListType);
        return retSlot;
    }

    private static ClassificationType createClassification(String classifiedObject, XdsGuidType scheme, String nodeRepresentation, String name, SlotType1... slots) {
        ClassificationType retClass = new ClassificationType();
        retClass.setId(String.format("Classification_%s", UUID.randomUUID().toString()));
        retClass.setClassificationScheme(scheme.toString());
        retClass.setClassifiedObject(classifiedObject);
        retClass.setNodeRepresentation(nodeRepresentation);
        retClass.getSlot().addAll(Arrays.asList(slots));

        // some Classification types don't contain a Name type.
        if (name != null) {
            retClass.setName(createName(name));
        }

        return retClass;
    }

    private static ExternalIdentifierType createExternalIdentifier(String registryObject, XdsGuidType scheme, String value) {
        ExternalIdentifierType retExtId = new ExternalIdentifierType();
        retExtId.setId(String.format("ExternalIdentifier_%s", UUID.randomUUID().toString()));
        retExtId.setRegistryObject(registryObject);
        retExtId.setIdentificationScheme(scheme.toString());
        retExtId.setValue(value);
        retExtId.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");

        retExtId.setName(createName(scheme.getName()));

        return retExtId;
    }

    private static InternationalStringType createName(String name) {
        InternationalStringType retName = new InternationalStringType();
        LocalizedStringType stringValue = new LocalizedStringType();
        stringValue.setValue(name);
        retName.getLocalizedString().add(stringValue);

        return retName;
    }

    private static String generateSourceId(DomainIdentifier id) {
        return id.getRoot();
    }

    private static String generatePatientId(PersonDemographic patient) {
        // Use the Identifiers in patient, and generate a PID-3 segment
        DomainIdentifier patientInfo = patient.getIdentifiers().get(0);
        String patientId = patientInfo.getExtension();
        //String assigningAuthority = patientInfo.getAssigningAuthority();
        String assigningAuthority = "";
        String root = patientInfo.getRoot();
        String iso = "ISO";

        String pid3 = String.format("%s^^^%s&%s&%s", patientId, assigningAuthority, root, iso);
        return pid3;
    }

    private static String generateFullName(PersonDemographic person) {
        String givenName = "";
        String familyName = "";
        PersonName name = person.getNames().get(0);
        for (NameComponent component : name.getParts()) {
            if (component.getType() == PartType.Given) {
                givenName = component.getValue();
            } else if (component.getType() == PartType.Family) {
                familyName = component.getValue();
            }
        }
        return givenName + " " + familyName;
    }

    /**
     * Generates a unique random ISO OID from a UUID (whether randomly generated
     * or provided).
     *
     * @param uuid The UUID to generate an OID from, if null a random UUID is
     * used
     * @return
     */
    public static String generateRandomOid(String uuid) {
        UUID seed;
        // if a uuid is passed, then use it to generate the OID, otherwise generate a random UUID
        if (uuid == null) {
            seed = UUID.randomUUID();
        } else {
            seed = UUID.fromString(uuid);
        }

        // generate OID
        BigInteger valueOfMostSigBits = createBigInteger(seed.getMostSignificantBits());
        valueOfMostSigBits = valueOfMostSigBits.shiftLeft(64);
        BigInteger valueOfLeastSigBits = createBigInteger(seed.getLeastSignificantBits());
        BigInteger oid = valueOfMostSigBits.or(valueOfLeastSigBits);

        // generate the oid and add it to the extension of the domain identifier
        return String.format("2.25.%s", oid.toString());
    }

    private static BigInteger createBigInteger(long longValue) {
        BigInteger retVal;
        if (longValue < 0) {
            longValue = longValue & Long.MAX_VALUE;
            retVal = BigInteger.valueOf(longValue);
            retVal = retVal.setBit(63);
        } else {
            retVal = BigInteger.valueOf(longValue);
        }

        return retVal;
    }

    /**
     * Creates the Document object that goes in a
     * ProvideAndRegisterDocumentSet-b request.
     *
     * @param document
     * @param documentId
     * @return
     */
    private static ProvideAndRegisterDocumentSetRequestType.Document createDocument(DocumentMetaData document) {
        ProvideAndRegisterDocumentSetRequestType.Document retVal = new ProvideAndRegisterDocumentSetRequestType.Document();
        retVal.setId(document.getId());
        retVal.setValue(document.getContent());

        return retVal;
    }

    /**
     * Generate an XCN formatted author entry
     */
    private static String[] generateXCN(PersonDemographic author) {
        StringBuilder sb = new StringBuilder();

        // Get domain ID of the author
        DomainIdentifier id = author.getIdentifiers().get(0);

        String[] components = new String[9];
        Arrays.fill(components, "");
        // Identifier
        components[0] = id.getExtension();
        components[8] = String.format("&%s&ISO", id.getRoot());

        // Name
        if (author.getNames().size() > 0) // TODO: Make this a little more clear instead of the first
        {
            PersonName name = author.getNames().get(0);
            for (NameComponent nc : name.getParts()) {
                switch (nc.getType()) {
                    case Family:
                        components[1] = nc.getValue();
                        break;
                    case Given:
                        if (components[2] != "") {
                            components[3] += nc.getValue();
                        } else {
                            components[2] = nc.getValue();
                        }
                }
            }
        }

        // Generate the string
        for (int i = 0; i < components.length; i++) {
            sb.append(String.format("%s%s", components[i], i == components.length - 1 ? "" : "^"));
        }
        return new String[]{sb.toString()};
    }

    /**
     * Generate an XTN from the telephone number
     *
     * @param phone
     * @return
     */
    private static String[] generateXTN(String phone) {
        return new String[]{String.format("^^PH^^^^^^^^^%s", phone)};

    }
    
    
}
