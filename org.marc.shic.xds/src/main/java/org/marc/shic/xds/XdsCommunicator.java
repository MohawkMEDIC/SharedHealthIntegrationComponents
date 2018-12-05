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
package org.marc.shic.xds;

import org.marc.shic.xds.parameters.folder.XdsFolderParameter;
import org.marc.shic.xds.parameters.submissionset.XdsSubmissionSetParameter;
import org.marc.shic.xds.parameters.document.XdsDocumentParameter;
import org.marc.shic.core.XdsExtendedQueryParameterType;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.core.XdsGuidMetaDataType;
import org.marc.shic.core.XdsGuidType;
import org.apache.commons.codec.binary.Base64;
import org.marc.shic.atna.AuditEventType;
import org.marc.shic.atna.AuditActor;
import org.marc.shic.atna.AuditActorType;
import org.marc.shic.atna.AuditParticipant;
import org.marc.shic.atna.ParticipantRoleType;
import org.marc.shic.atna.AuditMetaData;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.core.*;
import org.marc.shic.core.configuration.*;

import org.marc.shic.xds.registry.*;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;
import org.marc.shic.atna.ParticipantObjectDetail;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.exceptions.InvalidPermissionException;
import org.marc.xds.profiles.IXds;
import org.marc.shic.xds.parameters.*;
import org.marc.shic.xds.parameters.document.XdsDocumentEntryStatus;

import org.marc.shic.xds.repository.RegistryResponseType;
import org.marc.shic.xds.repository.DocumentRepositoryPortType;
import org.marc.shic.xds.repository.ProvideAndRegisterDocumentSetRequestType;
import org.marc.shic.xds.repository.RetrieveDocumentSetRequestType;
import org.marc.shic.xds.repository.RetrieveDocumentSetRequestType.DocumentRequest;
import org.marc.shic.xds.repository.RetrieveDocumentSetResponseType;
import org.marc.shic.xds.repository.RetrieveDocumentSetResponseType.DocumentResponse;
import org.marc.shic.xds.utils.RegistryStoredQueryUtil;

/**
 * A class for communicating with an IHE XDS registry/repository infrastructure.
 * Register maps to ProvideAndRegisterDocumentSet-b, Find to
 * RegistryStoredQuery, FillInDetails to RetrieveDocument. Like the PIX
 * communications class, all connections on this channel should remain open (if
 * possible) to save on retrieving multiple documents. It will be up to the
 * FillInDetails function to correlate the repositoryUniqueId to the unique
 * identifier of the actor to be queried prior to fetching. Fetch requests shall
 * be batched to save on connection establishment.
 */
//@RequiredActor(RequiredActors =
//{
//    IheActorType.DOC_REGISTRY, IheActorType.DOC_REPOSITORY, IheActorType.TS, IheActorType.AUDIT_REPOSITORY
//})
public class XdsCommunicator extends XdStarCommunicator implements IXds {

    /**
     * The repository actor used for the XDS communicator instance.
     */
    private final IheActorConfiguration docRepositoryActor;
    /**
     * The registry actor used for the XDS communicator instance.
     */
    private final IheActorConfiguration docRegistryActor;

    /**
     * Registry success status response.
     */
    /**
     * Constructs a new instance of the XdsCommunicator. Required actors:
     * DOC_REGISTRY, DOC_REPOSITORY, AUDIT_REPOSITORY, TS
     */
    public XdsCommunicator(IheConfiguration configuration) throws ActorNotFoundException {
        super(configuration);

        // Reference the repository and registry actors rather than searching for them every time they are needed.
        this.docRepositoryActor = this.getConfiguration().getAffinityDomain().getActor(IheActorType.DOC_REPOSITORY);
        this.docRegistryActor = this.getConfiguration().getAffinityDomain().getActor(IheActorType.DOC_REGISTRY);
    }

    /**
     * Registers the specified DocumentSubmissionSetMetaData against the
     * repository. Returns true if registration was successful
     *
     * @param documentSubmission
     * @return
     * @throws CommunicationsException
     * @deprecated Use register(MetaData metaData) instead
     */
    @Deprecated
    public boolean register(DocumentSubmissionSetMetaData documentSubmission) throws CommunicationsException {
        // Sanity check
        if (documentSubmission == null) {
            throw new IllegalArgumentException("documentSubmission must be passed to this function");
        }

        if (documentSubmission.getPatient() == null) {
            throw new IllegalArgumentException("Parameter missing from query : Patient");
        } else if (documentSubmission.getPatient().getIdentifiers() == null || documentSubmission.getPatient().getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        if (documentSubmission.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("No documents were found in the submission set.");
        }

        boolean result = true; // innocent until proven guilty..

        DocumentRepositoryPortType port = (DocumentRepositoryPortType) this.getBinding(docRepositoryActor);
        if (port == null) {
            throw new UnsupportedOperationException("No configured actors in DOC_REPOSITORY role");
        }

//        // Next, determine if we need to cross-reference the patient identifier
//        DomainIdentifier domainIdentifier = getPatientAffinityDomainIdOrDefault(documentSubmission.getPatient());
//
//        // Hack
//        if (domainIdentifier == null)
//        {
//            domainIdentifier = documentSubmission.getPatient().getIdentifiers().get(0);
//        }
//
//        if (domainIdentifier == null)
//        {
//            throw new IllegalArgumentException("No affinity domain identifier found in : Patient.Id");
//        }
//
//        // HACK: Only the affintiy domain should be in the patient id
//        documentSubmission.getPatient().getIdentifiers().clear();
//        documentSubmission.getPatient().addIdentifier(domainIdentifier);
        // Use XdsUtility class to create the provide and register request object (containing submission set, and document(s))
        XdsUtility utility = new XdsUtility();
        ProvideAndRegisterDocumentSetRequestType request;
        try {
            request = utility.generateProvideAndRegisterRequest(documentSubmission, this.getConfiguration().getAffinityDomain());
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
            throw ex;
        }

        // send the request
        RegistryResponseType response;
        try {
            // the generated classes can throw multiple exceptions
            response = port.documentRepositoryProvideAndRegisterDocumentSetB(request);

            AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentRegistration);
            auditData.addActor(AuditActorType.Destination, docRepositoryActor);

            auditData.addActor(AuditUtility.generateRequestingUser());

            String srcPatientId = getFormattedPatientId(documentSubmission.getPatient());
            auditData.addParticipant(ParticipantRoleType.Patient, srcPatientId, null);

            auditData.addParticipant(ParticipantRoleType.SubmissionSet, documentSubmission.getId().getExtension(), null);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
            //atnaCommunicator.sendAudit(auditData);

            // for a Provide and Register request, we don't need to return a document, all we return is the status..
            String status = response.getStatus();
            LOGGER.info(status);

            // if the status is a failure, return (result should be) false
            if (status.indexOf("Failure") >= 0) {
                // the request has failed, set the result boolean to false
                result = false;
                // log the error returned in the response
                LOGGER.info(response.getRegistryErrorList().getRegistryError().get(0).getCodeContext());
            }

        } catch (WebServiceException e) {
            result = false;
            LOGGER.info(e.getMessage(), e);
        } catch (Exception e) {
            result = false;
            LOGGER.info(e.getMessage(), e);
        }

        return result;
    }

    public boolean register(MetaData metaData) throws CommunicationsException {
        return provideAndRegisterDocumentSetB(metaData);
    }

    @Override
    public boolean provideAndRegisterDocumentSetB(MetaData metaData) throws CommunicationsException {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.ReadOnly){
            throw new InvalidPermissionException("Invalid Permission: Write or WriteRead required.");
        }
        
        // Sanity check
        if (metaData == null) {
            throw new IllegalArgumentException("a MetaData object must be passed to this method");
        }

        if (metaData.getPatient() == null) {
            throw new IllegalArgumentException("Parameter missing from metaData : Patient");
        } else if (metaData.getPatient().getIdentifiers() == null || metaData.getPatient().getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from metaData : Patient.Id");
        }

        // check documents
        if (metaData instanceof DocumentContainerMetaData && ((DocumentContainerMetaData) metaData).getDocuments().isEmpty() && !(metaData instanceof FolderMetaData)) {
            throw new IllegalArgumentException("No documents were found in the metaData.");
        }

        // check folder
        if (metaData instanceof FolderMetaData && ((FolderMetaData) metaData).getTitle() == null) {
            throw new IllegalArgumentException("No folder was found in the metaData.");
        }

        // check document content
        if (metaData instanceof DocumentMetaData && ((DocumentMetaData) metaData).getContent() == null) {
            throw new IllegalArgumentException("Document contents were missing from metaData.");
        }

        boolean result = true; // innocent until proven guilty..

        DocumentRepositoryPortType port = (DocumentRepositoryPortType) this.getBinding(docRepositoryActor);
        if (port == null) {
            throw new UnsupportedOperationException("No configured actors in DOC_REPOSITORY role");
        }

        // HACK: Get affinity domain id for the patient is it a collection?
        if (metaData instanceof DocumentContainerMetaData) {
            for (DocumentMetaData document : ((DocumentContainerMetaData) metaData).getDocuments()) {
                DomainIdentifier docDomainIdentifier = getPatientAffinityDomainIdOrDefault(document.getPatient());
                if (docDomainIdentifier == null) {
                    docDomainIdentifier = document.getPatient().getIdentifiers().get(0);
                }
                document.getPatient().getIdentifiers().clear();
                document.getPatient().addIdentifier(docDomainIdentifier);
            }
        }

        // submissionset/folder
        DomainIdentifier docDomainIdentifier = getPatientAffinityDomainIdOrDefault(metaData.getPatient());
        if (docDomainIdentifier == null) {
            docDomainIdentifier = metaData.getPatient().getIdentifiers().get(0);
        }
        metaData.getPatient().getIdentifiers().clear();
        metaData.getPatient().addIdentifier(docDomainIdentifier);

        // Use XdsUtility class to create the provide and register request object (containing submission set, and document(s))
        ProvideAndRegisterDocumentSetRequestType request;
        try {
            request = XdsUtility.generateProvideAndRegisterRequest(metaData);
        } catch (CommunicationsException ex) {
            throw ex;
        }
        
        BindingProvider binding = (BindingProvider)this.m_bindings.get(this.docRepositoryActor);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(metaData,this.docRepositoryActor.getEndPointAddress().toASCIIString(),this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);

        // send the request
        RegistryResponseType response;
        try {
            // the generated classes can throw multiple exceptions
            response = port.documentRepositoryProvideAndRegisterDocumentSetB(request);

            // for a Provide and Register request, we don't need to return a document, all we return is the status..
            String status = response.getStatus();
            LOGGER.info(status);

            // if the status is a failure, return (result should be) false
            if (status.indexOf("Failure") >= 0) {
                // the request has failed, set the result boolean to false
                result = false;
                // log the error returned in the response
                LOGGER.info(response.getRegistryErrorList().getRegistryError().get(0).getCodeContext());
            }

        } catch (WebServiceException e) {
            throw new CommunicationsException(docRepositoryActor, e);
        } catch (Exception e) {
            throw new CommunicationsException(docRepositoryActor, e);
        } finally {
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentRegistration);

            AuditActor sourceActor = new AuditActor(AuditActorType.Source, "http://www.w3.org/2005/08/addressing/anonymous", AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(sourceActor);

            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, docRepositoryActor.getEndPointAddress().toString(), null, false,
                    AuditUtility.getIpFromHost(docRepositoryActor.getEndPointAddress().toString()), (short) 2);
            auditData.addActor(destinationActor);

            //     auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));
            // Patient Data
            String srcPatientId = getFormattedPatientId(metaData.getPatient());
            auditData.addParticipant(ParticipantRoleType.Patient, srcPatientId, null);
            auditData.addParticipant(ParticipantRoleType.SubmissionSet, metaData.getUniqueId(), null);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        return result;
    }

    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException {
        return getAssociations(objectIdentifiers, null);
    }

    /**
     *
     * @param objectIdentifiers The identifier of the associations to retrieve.
     * It must be prefixed with "urn:uuid:".
     * @return
     * @throws CommunicationsException
     */
    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException {
        return getAssociations(objectIdentifiers, homeCommunityId, null);
    }

    @Override
    public <T extends String> AssociationMetaData[] getAssociations(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (objectIdentifiers == null) {
            throw new IllegalArgumentException("objectIdentifiers must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(objectIdentifiers.getParameterName(), objectIdentifiers.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetAssociations, querySpec);
    }

    /**
     * <p>
     * FindFolders stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Find folders (XDSFolder objects) in the registry for a given patientID
     * with matching ‘status’ attribute. The other parameters can be used to
     * restrict the collection of XDSFolder objects returned.</p>
     *
     * @return XDSFolder objects matching the query parameters.
     */
    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        return findFolders(patient, homeCommunityId, null, statusTypes);
    }

    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        if (patient == null) {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSFolderPatientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSFolderStatus", RegistryStoredQueryUtil.parseStatusTypes(statusTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);

        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindFolders, querySpec);

        return folders;
    }

    @Override
    public FolderMetaData[] findFolders(final PersonDemographic patient) throws CommunicationsException {
        return findFolders(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, DocumentRelationshipType... associationTypes) throws CommunicationsException {
        return getRelatedDocuments(documentId, null, associationTypes);
    }

    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, DocumentRelationshipType... associationTypes) throws CommunicationsException {
        return getRelatedDocuments(documentId, homeCommunityId, null, associationTypes);
    }

    @Override
    public <T extends String> MetaData[] getRelatedDocuments(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse, DocumentRelationshipType... associationTypes) throws CommunicationsException {
        if (documentId == null || documentId.getParameterValue() == null || documentId.getParameterValue().length() == 0) {
            throw new IllegalArgumentException("documentId must be set.");
        }

        if (associationTypes == null || associationTypes.length == 0) {
            throw new IllegalArgumentException("At least one association type must be passed.");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(documentId.getParameterName(), documentId.getParameterValue()));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$AssociationTypes", RegistryStoredQueryUtil.parseStatusTypes(associationTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        MetaData[] metaData = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetRelatedDocuments, querySpec);

        return metaData;
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId, final CodeValue purposeOfUse, final XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        if (patient == null) {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSSubmissionSetPatientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSSubmissionSetStatus", RegistryStoredQueryUtil.parseStatusTypes(statusTypes)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);

        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindSubmissionSets, querySpec);

        return submissionSets;
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId, final XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        return findSubmissionSets(patient, homeCommunityId, null, statusTypes);
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient) throws CommunicationsException {
        return findSubmissionSets(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public SubmissionSetMetaData[] findSubmissionSets(final PersonDemographic patient, final String homeCommunityId) throws CommunicationsException {
        return findSubmissionSets(patient, homeCommunityId, XdsDocumentEntryStatusType.Approved);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId) throws CommunicationsException {
        return getSubmissionSetAndContents(submissionSetId, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId) throws CommunicationsException {
        return getSubmissionSetAndContents(submissionSetId, homeCommunityId, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSetAndContents(XdsSubmissionSetParameter<T> submissionSetId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (submissionSetId == null || submissionSetId.getParameterValue() == null) {
            throw new IllegalArgumentException("submissionSetId must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>(submissionSetId.getParameterName(), submissionSetId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetSubmissionSetAndContents, querySpec);

        return submissionSets;
    }

    /**
     * <p>
     * GetAll stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Get all registry content for a patient given the indicated status, format
     * codes, and confidentiality codes.</p>
     *
     * @return
     *
     * <ul><li>XDSSubmissionSet, XDSDocumentEntry, and XDSFolder objects with
     * patientId attribute matching $patientId parameter</li>
     *
     * <li>Association objects with sourceObject or targetObject attribute
     * matching one of the above objects.</li><ul>
     */
    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        return getAll(patient, homeCommunityId, null, statusTypes);
    }

    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentEntryStatusType... statusTypes) throws CommunicationsException {
        if (patient == null) {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();

        String[] parsedStatusTypes = RegistryStoredQueryUtil.parseStatusTypes(statusTypes);

        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$patientId", getFormattedPatientId(patient)));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSDocumentEntryStatus", parsedStatusTypes));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSFolderStatus", parsedStatusTypes));
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String[]>("$XDSSubmissionSetStatus", parsedStatusTypes));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        SubmissionSetMetaData[] submissionSets = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetAll, querySpec);

        return submissionSets;
    }

    @Override
    public SubmissionSetMetaData[] getAll(PersonDemographic patient) throws CommunicationsException {
        return getAll(patient, null, XdsDocumentEntryStatusType.Approved);
    }

    /**
     * <p>
     * GetDocuments stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSDocumentEntry objects. XDSDocumentEntry
     * objects are selected either by their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSDocumentEntry objects requested.
     */
    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException {
        return getDocuments(documentIds, null);
    }

    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException {
        return getDocuments(documentIds, homeCommunityId, null);
    }

    @Override
    public <T extends String> DocumentMetaData[] getDocuments(XdsDocumentParameter<T[]> documentIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (documentIds == null || documentIds.getParameterValue().length == 0) {
            throw new IllegalArgumentException("documentIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(documentIds.getParameterName(), documentIds.getParameterValue()));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetDocuments, querySpec);
    }

    /**
     * <p>
     * GetFolders stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSFolder objects. XDSFolder objects are
     * selected either by their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSFolder objects requested.
     */
    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds) throws CommunicationsException {
        return getFolders(folderIds, null);
    }

    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(folderIds.getParameterName(), folderIds.getParameterValue()));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);

        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFolders, querySpec);

        return folders;
    }

    @Override
    public <T extends String> FolderMetaData[] getFolders(XdsFolderParameter<T[]> folderIds, String homeCommunityId) throws CommunicationsException {
        return getFolders(folderIds, homeCommunityId, null);
    }

    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds) throws CommunicationsException {
        return getDocumentsAndAssociations(documentIds, null);
    }

    /**
     * <p>
     * GetDocumentsAndAssociations stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve a collection of XDSDocumentEntry objects and the Association
     * objects surrounding them. XDSDocumentEntry objects are selected either by
     * their entryUUID or uniqueId attribute. This is the GetDocuments query and
     * GetAssociations query combined into a single query.</p>
     *
     * @return XDSDocumentEntry objects & Association objects whose sourceObject
     * or targetObject attribute matches one of the above objects.
     */
    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId) throws CommunicationsException {
        return getDocumentsAndAssociations(documentIds, homeCommunityId, null);
    }

    @Override
    public <T extends String> MetaData[] getDocumentsAndAssociations(XdsDocumentParameter<T[]> documentIds, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (documentIds == null || documentIds.getParameterValue().length == 0) {
            throw new IllegalArgumentException("documentIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(documentIds.getParameterName(), documentIds.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);

        MetaData[] documentsAndAssociations = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetDocumentsAndAssociations, querySpec);

        return documentsAndAssociations;
    }

    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId) throws CommunicationsException {
        return getFolderAndContents(folderId, null);
    }

    /**
     * <p>
     * GetFoldersAndContents stored query.</p>
     *
     * <p>
     * Helper method for find (registry stored query).</p>
     *
     * <p>
     * Retrieve an XDSFolder object and its contents. XDSFolder objects are
     * selected either by their entryUUID or uniqueId attribute. The
     * XDSDocumentEntry objects returned shall match one of the confidentiality
     * codes listed if that parameter is included.</p>
     *
     * @return <ul><li>XDSFolder object specified in the query</li>
     *
     * <li>Association objects of type HasMember that have a sourceObject
     * attribute referencing the XDSFolder object specified in the query</li>
     *
     * <li>XDSDocumentEntry objects referenced by the targetObject attribute of
     * one of the Association objects specified above.</li></ul>
     */
    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId) throws CommunicationsException {
        return getFolderAndContents(folderId, homeCommunityId, null);
    }

    @Override
    public <T extends String> FolderMetaData[] getFolderAndContents(XdsFolderParameter<T> folderId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (folderId == null || folderId.getParameterValue() == null || folderId.getParameterValue().length() == 0) {
            throw new IllegalArgumentException("folderIds must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(folderId.getParameterName(), folderId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);

        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFolderAndContents, querySpec);

        return folders;
    }

    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId) throws CommunicationsException {
        return getFoldersForDocument(documentId, null);
    }

    /**
     * <p>
     * GetFoldersForDocument stored query.</p>
     *
     * <p>
     * Helper extension for find (registry stored query)</p>
     *
     * <p>
     * Retrieve XDSFolder objects that contain the XDSDocumentEntry object
     * provided with the query. XDSDocumentEntry objects are selected either by
     * their entryUUID or uniqueId attribute.</p>
     *
     * @return XDSFolder objects that contain specified XDSDocumentEntry object.
     * More specifically, for each Association object of type HasMember that has
     * a targetObject attribute referencing the target XDSDocumentEntry object,
     * return the object referenced by its sourceObject if it is of type
     * XDSFolder.
     */
    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId) throws CommunicationsException {
        return getFoldersForDocument(documentId, homeCommunityId, null);
    }

    @Override
    public <T extends String> FolderMetaData[] getFoldersForDocument(XdsDocumentParameter<T> documentId, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (documentId == null || documentId.getParameterValue() == null || documentId.getParameterValue().length() == 0) {
            throw new IllegalArgumentException("documentId must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T>(documentId.getParameterName(), documentId.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);

        FolderMetaData[] folders = registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetFoldersForDocument, querySpec);

        return folders;
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient) throws CommunicationsException {
        return findDocuments(patient, null);
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId) throws CommunicationsException {
        XdsDocumentParameter param = new XdsDocumentEntryStatus(XdsDocumentEntryStatusType.Approved);
        return findDocuments(patient, homeCommunityId, param);
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, XdsDocumentParameter... parameters) throws CommunicationsException {
        return findDocuments(patient, homeCommunityId, null, parameters);
    }

    @Override
    public DocumentMetaData[] findDocuments(PersonDemographic patient, String homeCommunityId, CodeValue purposeOfUse, XdsDocumentParameter... parameters) throws CommunicationsException {
        if (patient == null) {
            throw new IllegalArgumentException("patient must be passed to this function");
        } else if (patient.getIdentifiers() == null || patient.getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<String>("$XDSDocumentEntryPatientId", getFormattedPatientId(patient)));
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.setPurposeOfUse(purposeOfUse);

        for (XdsDocumentParameter param : parameters) {
            querySpec.addExtendedParameter(new XdsExtendedQueryParameterType(param.getParameterName(), param.getParameterValue()));
        }

        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_FindDocuments, querySpec);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers) throws CommunicationsException {
        return getSubmissionSets(objectIdentifiers, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId) throws CommunicationsException {
        return getSubmissionSets(objectIdentifiers, homeCommunityId, null);
    }

    @Override
    public <T extends String> SubmissionSetMetaData[] getSubmissionSets(XdsAssociationUuid<T[]> objectIdentifiers, String homeCommunityId, CodeValue purposeOfUse) throws CommunicationsException {
        if (objectIdentifiers == null) {
            throw new IllegalArgumentException("objectIdentifiers must be passed to this function");
        }

        XdsQuerySpecification querySpec = new XdsQuerySpecification();
        querySpec.setHomeCommunityId(homeCommunityId);
        querySpec.addExtendedParameter(new XdsExtendedQueryParameterType<T[]>(objectIdentifiers.getParameterName(), objectIdentifiers.getParameterValue()));
        querySpec.setPurposeOfUse(purposeOfUse);
        return registryStoredQuery(XdsGuidType.RegistryStoredQuery_GetSubmissionSets, querySpec);
    }

    /**
     * Executes a registry stored query against the configured XdsRegistry for
     * the current affinity domain returning an array of DocumentMetaData
     * instances which match the query parameters
     *
     * @throws CommunicationsException
     * @deprecated Use findDocuments instead.
     */
    @Deprecated
    public DocumentMetaData[] find(XdsQuerySpecification querySpec) throws CommunicationsException {
        if (querySpec == null) {
            throw new IllegalArgumentException("querySpec must be passed to this function");
        }

        if (querySpec.getQueryType() == null) {
            throw new IllegalArgumentException("Parameter missing from query : querySpec.queryType");
        }

        if (querySpec.getPatient() == null) {
            throw new IllegalArgumentException("Parameter missing from query : Patient");
        } else if (querySpec.getPatient().getIdentifiers() == null || querySpec.getPatient().getIdentifiers().isEmpty()) {
            throw new IllegalArgumentException("Parameter missing from query : Patient.Id");
        }

        return registryStoredQuery((XdsGuidMetaDataType<?>) querySpec.getQueryType(), querySpec);
    }

    @Override
    public DocumentMetaData[] retrieveDocumentSet(DocumentMetaData[] documentMetaData) throws CommunicationsException {
        return fillInDetails(documentMetaData);
    }

    @Override
    public DocumentMetaData retrieveDocumentSet(DocumentMetaData documentMetaData) throws CommunicationsException {

        DocumentMetaData[] doc = new DocumentMetaData[]{
            documentMetaData
        };
        return fillInDetails(doc)[0];
    }

    /**
     * Fills in the details of the supplied DocumentMetaData object by fetching
     * the document contents via a RetrieveDocumentSet operation on the XDS
     * repository. The XDS repository should be driven from the
     * repositoryUniqueId from the meta data.
     *
     * The operation should group together requests from the passed array. For
     * example, if the parameter array has the following document repository
     * unique ids: { 2, 3, 4, 2, 3, 3, 2 } this will result in three retrieve
     * document requests being sent (one to system 2, 3, and 4). This will
     * reduce the connections to the XDS repository.
     */
    @Deprecated
    public DocumentMetaData[] fillInDetails(DocumentMetaData[] documentMetaData) throws CommunicationsException {
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }
        
        if (documentMetaData == null || documentMetaData.length < 1) {
            throw new IllegalArgumentException("documentMetaData must be passed to this function, and must not be empty");
        }

        DocumentRepositoryPortType port = (DocumentRepositoryPortType) this.getBinding(docRepositoryActor);
        if (port == null) {
            throw new UnsupportedOperationException("No configured actors in DOC_REPOSITORY role");
        }

        DocumentMetaData[] retrievedDocuments;
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentRetrieve);

        // Loop through each document request
        for (DocumentMetaData metaData : documentMetaData) {
            DocumentRequest documentRequest = new RetrieveDocumentSetRequestType.DocumentRequest();

            if (metaData.getUniqueId() == null || metaData.getUniqueId().length() == 0) {
                throw new IllegalArgumentException("A documentMetaData is missing its unique identifier.");
            }

            if (metaData.getRepositoryUniqueId() == null || metaData.getRepositoryUniqueId().length() == 0) {
                throw new IllegalArgumentException("A documentMetaData is missing its repository unique id.");
            }

            // Fill in the retrieve document set request properties
            documentRequest.setDocumentUniqueId(metaData.getUniqueId());

            String repositoryUniqueId = metaData.getRepositoryUniqueId();

            documentRequest.setRepositoryUniqueId(repositoryUniqueId);

            documentRequest.setHomeCommunityId(metaData.getHomeCommunityId());

            request.getDocumentRequest().add(documentRequest);

            // Create an AuditParticipant for each document request.
            AuditParticipant participant = new AuditParticipant(metaData.getUniqueId(), null, ParticipantRoleType.Document);

            String base64RepositoryId = Base64.encodeBase64String(metaData.getRepositoryUniqueId().getBytes());
            ParticipantObjectDetail repositoryId = new ParticipantObjectDetail("Repository Unique Id", base64RepositoryId.getBytes());
            participant.addDetail(repositoryId);

            if (metaData.getHomeCommunityId() != null) {
                String base64HomeCommunityId = Base64.encodeBase64String(metaData.getHomeCommunityId().getBytes());
                ParticipantObjectDetail homeCommunityId = new ParticipantObjectDetail("ihe:homeCommunityID", base64HomeCommunityId.getBytes());
                participant.addDetail(homeCommunityId);
            }

            auditData.addParticipant(participant);
        }

        BindingProvider binding = (BindingProvider) this.m_bindings.get(this.docRepositoryActor);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(documentMetaData[0], this.docRepositoryActor.getEndPointAddress().toASCIIString(), this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);

        RetrieveDocumentSetResponseType response;

        try {
            response = port.documentRepositoryRetrieveDocumentSet(request);

            if (response == null) {
                throw new CommunicationsException(docRepositoryActor, "There was an error retreiving the XDS registry response.");
            }

            String responseStatus = response.getRegistryResponse().getStatus();

            if (!responseStatus.equals(REGISTRY_SUCCESS_STATUS)) {
                throw new CommunicationsException(docRepositoryActor, String.format("Registry returned : %s", responseStatus));
            }
        } catch (WebServiceException e) {
            throw new CommunicationsException(docRegistryActor, "There was an error communicating with the registry.", e);
        } finally {
            AuditActor destinationActor = new AuditActor(AuditActorType.Destination,
                    "http://www.w3.org/2005/08/addressing/anonymous",
                    AuditUtility.getProcessId(),
                    true,
                    AuditUtility.getNetworkId(),
                    (short) 1);

            auditData.addActor(destinationActor);

            AuditActor sourceActor = new AuditActor(AuditActorType.Destination,
                    docRepositoryActor.getEndPointAddress().toString(),
                    getConfiguration().getLocalIdentification().getFacilityName(),
                    false,
                    AuditUtility.getIpFromHost(docRepositoryActor.getEndPointAddress().toString()),
                    (short) 2);

            auditData.addActor(sourceActor);

            //auditData.addActor(AuditActorType.RequestingUser, System.getProperty("user.name"));
            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        retrievedDocuments = new DocumentMetaData[response.getDocumentResponse().size()];

        for (int i = 0; i < response.getDocumentResponse().size(); i++) {
            DocumentResponse doc = response.getDocumentResponse().get(i);

            DocumentMetaData document = new DocumentMetaData();
            document.setContent(doc.getDocument());
            document.setMimeType(doc.getMimeType());
            document.setUniqueId(doc.getDocumentUniqueId());
            document.setRepositoryUniqueId(doc.getRepositoryUniqueId());
            document.setHomeCommunityId(doc.getHomeCommunityId());

            retrievedDocuments[i] = document;
        }

        return retrievedDocuments;
    }

    /**
     * Executes a registry stored query against the configured XdsRegistry for
     * the current affinity domain returning an array of the requested meta data
     * type instances which match the query parameters.
     *
     * @param <>> The type of meta data returned.
     * @param querySpec
     * @return
     * @throws CommunicationsException
     */
    @Override
    public <T extends MetaData> T[] registryStoredQuery(final XdsGuidMetaDataType<?> queryType, XdsQuerySpecification querySpec) throws CommunicationsException {
        T[] parsedMetaData;
        
        if(this.m_configuration.getAffinityDomain().getPermission() == IheAffinityDomainPermission.WriteOnly){
            throw new InvalidPermissionException("Invalid Permission: Read or WriteRead required.");
        }
        
        // Sanity check
        if (querySpec == null) {
            throw new IllegalArgumentException("querySpec must be passed to this function");
        }

        if (queryType == null) {
            throw new IllegalArgumentException("queryType must be passed to this function");
        }

        DocumentRegistryPortType port = (DocumentRegistryPortType) this.getBinding(docRegistryActor);

        if (port == null) {
            throw new UnsupportedOperationException("No configured actors in DOC_REGISTRY role");
        }

        BindingProvider binding = (BindingProvider) this.m_bindings.get(this.docRegistryActor);
        List<Handler> handlerList = binding.getBinding().getHandlerChain();
        handlerList.add(new XdsSamlHandler(querySpec, this.docRegistryActor.getEndPointAddress().toASCIIString(), this.m_configuration.getKeyStore()));
        binding.getBinding().setHandlerChain(handlerList);

        // Proper?
        RegistryStoredQueryUtil.validateQuerySlots(queryType, querySpec);

        // Everything should be good to so let's create the request
        AdhocQueryRequest request = new AdhocQueryRequest();
        // Setup response option
        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(true);

        request.setResponseOption(responseOption);

        // Setup the ad-hoc query
        AdhocQueryType adhocQuery = new AdhocQueryType();

        if (querySpec.getHomeCommunityId() != null) {
            adhocQuery.setHome(querySpec.getHomeCommunityId());
        }

        adhocQuery.setId(String.format("urn:uuid:%s", queryType.getGuid()));

        RegistryStoredQueryUtil.populateQueryWithExtendedParameters(adhocQuery, querySpec);

        // Append the ad-hoc query to the request 
        request.setAdhocQuery(adhocQuery);

        try // Communications part
        {

            // Send the request
            AdhocQueryResponse response = port.documentRegistryRegistryStoredQuery(request);

            // First, was there a problem?            
            if (response == null) {
                throw new CommunicationsException(docRegistryActor, "Registry response was null.");
            } else if (!response.getStatus().equals(REGISTRY_SUCCESS_STATUS)) {
                throw new CommunicationsException(docRegistryActor, String.format("Registry returned : %s", response.getStatus()));
            }

            parsedMetaData = (T[]) RegistryStoredQueryUtil.parseMetaDataFromRegistryResponse(response, queryType.getMetaDataClass());

        } catch (XdsMessageParseException e) {
            throw new CommunicationsException(docRegistryActor, "There was an error parsing the response message.", e);
        } catch (SOAPFaultException e) {
            throw new CommunicationsException(docRegistryActor, "A SOAP fault was returned.", e);

        } catch (Exception e) {
            throw new CommunicationsException(docRegistryActor, "Error while getting data from the Registry.", e);

        } finally {
            // Create a stringWriter to hold the XML.
            final StringWriter queryXml = new StringWriter();

            try {
                final JAXBContext context = JAXBContext.newInstance(AdhocQueryType.class);
                final Marshaller marshaller = context.createMarshaller();

                marshaller.marshal(adhocQuery, queryXml);

            } catch (JAXBException e) {
            }

            final String base64EncodedQuery = Base64.encodeBase64String(queryXml.toString().getBytes());

            // Audit
            AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentQuery);

            AuditActor sourceActor = new AuditActor(AuditActorType.Source, "http://www.w3.org/2005/08/addressing/anonymous", AuditUtility.getProcessId(), true, AuditUtility.getNetworkId(), (short) 1);
            auditData.addActor(sourceActor);

            AuditActor destinationActor = new AuditActor(AuditActorType.Destination, docRepositoryActor.getEndPointAddress().toString(),
                    null, false, AuditUtility.getIpFromHost(docRepositoryActor.getEndPointAddress().toString()), (short) 2);
            auditData.addActor(destinationActor);

            auditData.addParticipant(ParticipantRoleType.ITIName, queryType.toString(), base64EncodedQuery.getBytes(), null);

            AtnaCommunicator.enqueueMessage(auditData, this.getConfiguration());
        }

        return parsedMetaData;
    }
}
