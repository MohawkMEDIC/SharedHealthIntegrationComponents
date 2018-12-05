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
 * @author tylerg Date: Nov 12, 2013
 *
 */
package org.marc.shic.xds;

import org.marc.shic.xds.parameters.folder.XdsFolderUniqueId;
import org.marc.shic.xds.parameters.submissionset.XdsSubmissionSetUniqueId;
import org.marc.shic.xds.parameters.document.XdsDocumentEntryUniqueId;
import org.marc.shic.xds.parameters.document.XdsDocumentEntryStatus;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentContainerMetaData;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentRelationship;
import org.marc.shic.core.DocumentRelationshipType;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.marc.shic.core.utils.TestUtils;
import org.marc.shic.xds.parameters.*;

/**
 *
 * @author tylerg
 */
public class RyanXdsCommunicatorTest {

    // this is the old global id that required manual entry. 
//    public static final String AssigningAuthority = "IHENA";
//    public static final String RootId = "1.3.6.1.4.1.21367.2010.1.2.300";
    
    // new global AssigningAuthority and RootId to match the current config of PIX/PDQ
    // These ID's are assigned automatically by the PIX/PDQ. 
    public static final String AssigningAuthority = "2.16.840.1.113883.4.357";
    public static final String RootId = "2.16.840.1.113883.4.357";
    
    private static final boolean UseProxy = false;
    private static final String TestAffinityDomainConfig = "../ryan_nist.xml";
    private static final IheConfiguration config = new IheConfiguration();
    private static XdsCommunicator xds;
    private static final Logger LOGGER = Logger.getLogger(RyanXdsCommunicatorTest.class.getName());
    // private static final PatientGenerator generator = new PatientGenerator(AssigningAuthority, RootId);
    private static final PersonDemographic JerrySienfeld;
    private static final PersonDemographic HarrySienfeld;
    private static final PersonDemographic JoesephF;
    private static final PersonDemographic GlobalPatient;
    private static final String TestDocumentUniqueId = "2.25.162522694155806368771250470839907682410";
    private static final String TestFolderUniqueId = "2.25.207683925514453853764270328722139143638";
    private static final String TestFolderUniqueId2 = "1.42.20131104173322.17";
    private static final String HomeCommunityId = "urn:oid:1.3.6.1.4.1.21367.2011.2.6.8";

    static {
        // Load the config used for testing.
//        config = IheConfiguration.load(CONFIG_LOCATION);
//        affDom = config.getAffinityDomain(TEST_AFFINITY_DOMAIN);

        JerrySienfeld = new PersonDemographic();
        JerrySienfeld.addIdentifier(new DomainIdentifier(RootId, "535c52f0-709e-11e3-b884-080027c56667"));

        HarrySienfeld = new PersonDemographic();
        HarrySienfeld.addIdentifier(new DomainIdentifier(RootId, "fad05570-82ac-11e3-9d5d-080027c56667"));

        JoesephF = new PersonDemographic();
        JoesephF.addIdentifier(new DomainIdentifier(RootId, "Z9234RTEDe"));
        
        GlobalPatient = new PersonDemographic();
        GlobalPatient.addIdentifier(new DomainIdentifier(RootId, "639f8372-651a-43fb-80f0-eacb688335d8", AssigningAuthority));
        
        if (UseProxy) {
            System.getProperties().put("http.proxyHost", "localhost");
            System.getProperties().put("http.proxyPort", "8888");
        }

        try {
            // Load the config used for testing.
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("LOCAL_APPLICATION", "LOCAL_FACILITY"));
            config.setKeyStore(new JKSStoreInformation("C:\\keystore.jks", "changeit"));
            config.setTrustStore(new JKSStoreInformation("C:\\truststore.jks", "changeit"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
            xds = new XdsCommunicator(config);
        } catch (ActorNotFoundException ex) {
            LOGGER.error(ex);
            fail(ex.getMessage());
        } catch (IheConfigurationException ex) {
            LOGGER.error(ex);
        }

        // disable CN/hostname verification
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {
                    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });

        // override the default keystore and truststore used by java.
//        System.setProperty("javax.net.ssl.keyStore", config.getKeyStore().getStoreFile());
//        System.setProperty("javax.net.ssl.keyStorePassword", config.getKeyStore().getStorePassword());
//        System.setProperty("javax.net.ssl.trustStore", config.getTrustStore().getStoreFile());
//        System.setProperty("javax.net.ssl.trustStorePassword", config.getTrustStore().getStorePassword());
        AtnaCommunicator.debug = true;

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        AtnaCommunicator.getAuditMessageQueue().awaitShutdown(25);
        //communicator.close();
    }

    @Before
    public void setUp() {
        //security logging
//        System.setProperty("javax.net.debug", "all");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRetrieveDocument() {
        boolean testResult = true;

        try {

            for (DocumentMetaData documentMetaData : xds.retrieveDocumentSet(xds.findDocuments(HarrySienfeld))) {
                assertTrue(documentMetaData.getContent() != null && documentMetaData.getContent().length > 0);
            }

        } catch (Exception ex) {
            LOGGER.info(ex);
            testResult = false;
        }

        assertTrue(testResult);
    }

    @Test
    public void testFindFolders() throws CommunicationsException {
        LOGGER.info("testFindFolders");

        FolderMetaData[] folders = xds.findFolders(HarrySienfeld);

        LOGGER.info("Found " + folders.length + " folders.");
    }

    @Test
    public void testGetFolders_HomeCommunityId() throws CommunicationsException {
        LOGGER.info("testGetFolders_HomeCommunityId");

        FolderMetaData[] folders = xds.getFolders(new XdsFolderUniqueId(new String[]{
            TestFolderUniqueId2
        }), HomeCommunityId);

        LOGGER.info(String.format("Found %d folders.", folders.length));
    }

    @Test
    public void testGetDocuments() throws CommunicationsException {
        LOGGER.info("testGetDocuments");

        DocumentMetaData[] documents = xds.getDocuments(new XdsDocumentEntryUniqueId(new String[]{
            TestDocumentUniqueId
        }), HomeCommunityId);

        LOGGER.info("Found " + documents.length + " documents.");
    }

    //@Test
    public void testGetAll() throws CommunicationsException {
        LOGGER.info("testGetAll");

        SubmissionSetMetaData[] submissionSets = xds.getAll(JoesephF);

        LOGGER.info("Found " + submissionSets.length + " submission sets.");
    }

    @Test
    public void testGetAssociations() throws CommunicationsException {
        LOGGER.info("testGetAssociations");

        String[] associationIds = new String[]{
            "urn:uuid:932c1e40-426d-42cd-95d0-b29d847cc990"
        };

        AssociationMetaData[] associations = xds.getAssociations(new XdsAssociationUuid(associationIds));

        LOGGER.info("Found " + associations.length + " associations.");
    }

    @Test
    public void testGetDocumentsAndAssociations() throws CommunicationsException {
        LOGGER.info("testGetDocumentsAndAssociations");

        String[] documentIds = new String[]{
            TestDocumentUniqueId
        };

        MetaData[] metaData = xds.getDocumentsAndAssociations(new XdsDocumentEntryUniqueId(documentIds));

        LOGGER.info(String.format("Found %d documents and associations.", metaData.length));
    }

    @Test
    public void testFindSubmissionSets() throws CommunicationsException {
        LOGGER.info("testFindSubmissionSets");

        SubmissionSetMetaData[] submissionSets = xds.findSubmissionSets(JoesephF);

        LOGGER.info(String.format("Found %d submission sets.", submissionSets.length));
    }

    @Test
    public void testGetSubmissionSetAndContents() throws CommunicationsException {
        LOGGER.info("testGetSubmissionSetAndContents");

        XdsSubmissionSetUniqueId submissionSetId = new XdsSubmissionSetUniqueId("1.42.20131104173322.9");

        SubmissionSetMetaData[] submissionSets = xds.getSubmissionSetAndContents(submissionSetId);

        assertTrue("A submission set should be returned.", submissionSets.length > 0);

        LOGGER.info(String.format("Found %d submission sets.", submissionSets.length));
    }

    @Test
    public void testFindDocuments() throws CommunicationsException {
        LOGGER.info("testFindDocuments");

        DocumentMetaData[] documents = xds.findDocuments(JerrySienfeld);

        LOGGER.info(String.format("Found %d documents.", documents.length));
    }

    @Test
    public void testFindDocuments_StatusTypeParameters() throws CommunicationsException {
        LOGGER.info("testFindDocuments");
        XdsDocumentEntryStatus statusParams = new XdsDocumentEntryStatus(XdsDocumentEntryStatusType.Approved, XdsDocumentEntryStatusType.Deprecated, XdsDocumentEntryStatusType.Submitted);

        DocumentMetaData[] documents = xds.findDocuments(JerrySienfeld, null, statusParams);

        LOGGER.info(String.format("Found %d documents.", documents.length));
    }

    @Test
    public void testGetFolderAndContents() throws CommunicationsException {
        LOGGER.info("testGetFolderAndContents");

        FolderMetaData[] folder = xds.getFolderAndContents(new XdsFolderUniqueId(TestFolderUniqueId));

        LOGGER.info(String.format("Found %s documents in folder.", folder[0].getDocuments().size()));
    }

    @Test
    public void testGetFoldersForDocument() throws CommunicationsException {
        LOGGER.info("testGetFoldersForDocument");

        String documentId = "2.25.116458548968636197910707203482109886799";

        FolderMetaData[] folders = xds.getFoldersForDocument(new XdsDocumentEntryUniqueId(documentId));

        LOGGER.info(String.format("Found %d folders.", folders.length));
    }

    @Test
    public void testGetRelatedDocuments() throws CommunicationsException {
        LOGGER.info("testGetRelatedDocuments");

        XdsDocumentEntryUniqueId documentId = new XdsDocumentEntryUniqueId(TestDocumentUniqueId);

        MetaData[] metaData = xds.getRelatedDocuments(documentId, XdsAssociationType.RPLC);

        LOGGER.info(String.format("Found %d related documents and associations.", metaData.length));
    }

    @Test
    public void testRegisterSingleFolder() {
        FolderMetaData meta = new FolderMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setRoot("1.3.6.1.4.1.21367.2005.3.7");
//        meta.setUniqueId(identifier);

        // root ID
        meta.setSourceId(RootId);

        meta.setPatient(GlobalPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
//        author.addPhone("905-111-1111"); // apparently NIST doesn't like understand the authorTelecommunication slot.

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("");
        authInfo.setExtension("999998");
        authInfo.setRoot(RootId);
        author.addIdentifier(authInfo);

        meta.setAuthor(author);

        meta.setTitle("mo's awesome folder");
        meta.setDescription("my awesome folder");
        meta.setCreationTime(new GregorianCalendar(2012, 10, 7));

        CodeValue codeList = new CodeValue();
        codeList.setCode("Referrals");
        codeList.setCodeSystem("Connect-a-thon folderCodeList");
        codeList.setDisplayName("Referrals");
        meta.setCodeList(codeList);

        CodeValue contentTypeCode = new CodeValue();
        contentTypeCode.setCode("History and Physical");
        contentTypeCode.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentTypeCode.setDisplayName("History and Physical");
        meta.setContentType(contentTypeCode);

        boolean result = false;
        try {
            result = xds.register(meta);
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

    @Test
    public void testRegisterSingleDocument() {
        DocumentMetaData meta = new DocumentMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setRoot("1.3.6.1.4.1.21367.2005.3.7");
//        meta.setId(identifier);

//        meta.setRepositoryUniqueId("1.2.3.4.5.6.7.8.9");
        meta.setSourceId(RootId);

        meta.setServiceTimeStart(Calendar.getInstance());
        meta.setServiceTimeEnd(Calendar.getInstance());

        meta.setPatient(GlobalPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
//        author.addPhone("905-111-1111"); // apparently NIST doesn't like understand the authorTelecommunication slot.

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot(RootId);
        author.addIdentifier(authInfo);

        meta.setAuthor(author);

        meta.setMimeType("application/pdf");
        meta.setTitle("First Doc");
        meta.setCreationTime(new GregorianCalendar(2012, 10, 7));
        byte[] content = "doc11".getBytes();
        meta.setContent(content);

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");

        meta.setContentType(contentType);

        CodeValue classCode = new CodeValue();
        classCode.setCode("History and Physical");
        classCode.setCodeSystem("Connect-a-thon classCodes");
        classCode.setDisplayName("History and Physical");
        meta.setClassCode(classCode);

        CodeValue confidentialityCode = new CodeValue();
        confidentialityCode.setCode("N");
        confidentialityCode.setCodeSystem("2.16.840.1.113883.5.25");
        confidentialityCode.setDisplayName("Normal");
        meta.addConfidentiality(confidentialityCode);

        CodeValue formatCode = new CodeValue();
        formatCode.setCode("PDF");
        formatCode.setCodeSystem("Connect-a-thon formatCodes");
        formatCode.setDisplayName("PDF");
        meta.setFormat(formatCode);

        CodeValue healthcareFacilityTypeCode = new CodeValue();
        healthcareFacilityTypeCode.setCode("Outpatient");
        healthcareFacilityTypeCode.setCodeSystem("Connect-a-thon healthcareFacilityTypeCodes");
        healthcareFacilityTypeCode.setDisplayName("Outpatient");
        meta.setFacilityType(healthcareFacilityTypeCode);

        CodeValue practiceSettingCode = new CodeValue();
        practiceSettingCode.setCode("General Medicine");
        practiceSettingCode.setCodeSystem("Connect-a-thon practiceSettingCodes");
        practiceSettingCode.setDisplayName("General Medicine");
        meta.setPracticeSetting(practiceSettingCode);

        CodeValue typeCode = new CodeValue();
        typeCode.setCode("34133-9");
        typeCode.setCodeSystem("LOINC");
        typeCode.setDisplayName("Summarization of Episode Note");
        meta.setType(typeCode);

        meta.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");
        meta.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        boolean result = false;
        try {
            result = xds.register(meta);
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

    @Test
    public void testRegisterMultipleDocuments() {
        DocumentMetaData meta = new DocumentMetaData();
        DocumentMetaData meta2 = new DocumentMetaData();

        meta.setServiceTimeStart(Calendar.getInstance());
        meta2.setServiceTimeStart(Calendar.getInstance());
        meta.setServiceTimeEnd(Calendar.getInstance());
        meta2.setServiceTimeEnd(Calendar.getInstance());

        meta.setPatient(GlobalPatient);
        meta2.setPatient(GlobalPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        //author.addPhone("905-111-1111"); // apparently NIST doesn't like understand the authorTelecommunication slot.

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority(AssigningAuthority);
        authInfo.setExtension("999998");
        authInfo.setRoot(RootId);
        author.addIdentifier(authInfo);

        author.addAddress(TestUtils.createAddress(AddressUse.Workplace, "123 Main St W", "Hamilton", "ON", "Canada", "L8P 1X1"));

        meta.setAuthor(author);

        meta.setMimeType("text/xml");
        meta.setTitle("First Doc");
        meta.setCreationTime(new GregorianCalendar(2012, 10, 7));
        meta.setContent("doc11".getBytes());

        meta2.setAuthor(author);

        meta2.setMimeType("text/xml");
        meta2.setTitle("First Doc");
        meta2.setCreationTime(new GregorianCalendar(2012, 10, 7));
        meta2.setContent("doc22".getBytes());

        CodeValue classCode = new CodeValue();
        classCode.setCode("History and Physical");
        classCode.setCodeSystem("Connect-a-thon classCodes");
        classCode.setDisplayName("History and Physical");
        meta.setClassCode(classCode);
        meta2.setClassCode(classCode);

        CodeValue confidentialityCode = new CodeValue();
        confidentialityCode.setCode("N");
        confidentialityCode.setCodeSystem("2.16.840.1.113883.5.25");
        confidentialityCode.setDisplayName("Normal");
        meta.addConfidentiality(confidentialityCode);
        meta2.addConfidentiality(confidentialityCode);

        CodeValue formatCode = new CodeValue();
        formatCode.setCode("PDF");
        formatCode.setCodeSystem("Connect-a-thon formatCodes");
        formatCode.setDisplayName("PDF");
        meta.setFormat(formatCode);
        meta2.setFormat(formatCode);

        CodeValue healthcareFacilityTypeCode = new CodeValue();
        healthcareFacilityTypeCode.setCode("Outpatient");
        healthcareFacilityTypeCode.setCodeSystem("Connect-a-thon healthcareFacilityTypeCodes");
        healthcareFacilityTypeCode.setDisplayName("Outpatient");
        meta.setFacilityType(healthcareFacilityTypeCode);
        meta2.setFacilityType(healthcareFacilityTypeCode);

        CodeValue practiceSettingCode = new CodeValue();
        practiceSettingCode.setCode("General Medicine");
        practiceSettingCode.setCodeSystem("Connect-a-thon practiceSettingCodes");
        practiceSettingCode.setDisplayName("General Medicine");
        meta.setPracticeSetting(practiceSettingCode);
        meta2.setPracticeSetting(practiceSettingCode);

        CodeValue typeCode = new CodeValue();
        typeCode.setCode("34133-9");
        typeCode.setCodeSystem("LOINC");
        typeCode.setDisplayName("Summarization of Episode Note");
        meta.setType(typeCode);
        meta2.setType(typeCode);

        meta.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");
        meta.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        meta2.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");
        meta2.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        DocumentContainerMetaData container = new DocumentContainerMetaData();
        container.setPatient(GlobalPatient);
        container.setAuthor(author);
        container.setCreationTime(Calendar.getInstance());
        container.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");
        container.setSourceId(RootId);

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");
        container.setContentType(contentType);

        container.addDocument(meta);
        container.addDocument(meta2);

        boolean result = false;
        try {
            result = xds.register(container);
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

    @Test
    public void testRegisterSingleFolderWithMultipleDocuments() {
        FolderMetaData folder = new FolderMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setRoot("1.3.6.1.4.1.21367.2005.3.7");
//        folder.setId(identifier);

        folder.setSourceId(RootId);
        folder.setPatient(JoesephF);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
//        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot(RootId);
        author.addIdentifier(authInfo);

        folder.setAuthor(author);

        folder.setTitle("mo's awesome folder");
        folder.setDescription("my awesome folder");
        folder.setCreationTime(new GregorianCalendar(2012, 10, 7));

        CodeValue codeList = new CodeValue();
        codeList.setCode("Referrals");
        codeList.setCodeSystem("Connect-a-thon folderCodeList");
        codeList.setDisplayName("Referrals");
        folder.setCodeList(codeList);

        CodeValue contentTypeCode = new CodeValue();
        contentTypeCode.setCode("History and Physical");
        contentTypeCode.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentTypeCode.setDisplayName("History and Physical");
        folder.setContentType(contentTypeCode);

        // documents
        DocumentMetaData document1 = TestUtils.getNistDemoDocument();
        document1.setPatient(JoesephF);

        DocumentMetaData document2 = TestUtils.getNistDemoDocument();
        document2.setPatient(JoesephF);

        folder.addDocument(document1);
        folder.addDocument(document2);

        boolean result = false;
        try {
            result = xds.register(folder);
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testReplaceDocument() {

        System.out.println("testReplaceDocument");

        DocumentMetaData[] documents = null;
        try {
            documents = xds.getDocuments(new XdsDocumentEntryUniqueId(new String[]{
                TestDocumentUniqueId
            }));
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        // document to replace
        DocumentMetaData document1 = documents[0];

        // replacement document
        DocumentMetaData document = TestUtils.getNistDemoDocument();

        // replace relationship
        document.setRelationship(new DocumentRelationship(document1.getId(), DocumentRelationshipType.RPLC));

        document.setSourceId(RootId);
        document.setMimeType("text/plain");
        document.setTitle("Replacement Document");
        document.setCreationTime(new GregorianCalendar(2013, 11, 7));
        byte[] content = "replacement document content".getBytes();

        document.setContent(content);
        document.setPatient(JoesephF);

        boolean result = false;
        try {
            result = xds.register(document);
        } catch (CommunicationsException ex) {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }
}
