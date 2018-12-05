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

import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.core.XdsGuidMetaDataType;
import org.marc.shic.core.XdsGuidType;
import java.io.File;
import java.util.Calendar;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentSubmissionSetMetaData;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.pix.PixCommunicator;
import org.marc.shic.core.utils.TestUtils;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.DocumentContainerMetaData;
import org.marc.shic.core.DocumentRelationship;
import org.marc.shic.core.DocumentRelationshipType;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.marc.shic.pix.PixApplicationException;
import org.marc.shic.xds.parameters.document.XdsDocumentEntryUniqueId;
import org.marc.shic.xds.parameters.folder.XdsFolderUniqueId;

/**
 *
 * @author Craig
 */
public class XdsCommunicatorTest
{

    public static String AssigningAuthority = "MiH_OSCAR_A";
    public static String RootId = "1.3.6.1.4.1.33349.3.1.3.201203.2.0.0";
    private static final boolean UseProxy = false; // set this back to false when done!
    private static final String TestAffinityDomainConfig = "../marc-hi-secure.xml";
    private static IheConfiguration config = new IheConfiguration();
    private static XdsCommunicator xds;
    private static PixCommunicator pix;
    private static PersonDemographic testPatient;
    private static final Logger LOGGER = Logger.getLogger(XdsCommunicatorTest.class);
    private static final String TestFolderUniqueId = "2.25.232984763339613038548800153178925364598";
    private static final String TestDocumentUniqueId = "2.25.251763575938566686814151745283988878776";

    public XdsCommunicatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");

        if (UseProxy)
        {
            System.getProperties().put("http.proxyHost", "localhost");
            System.getProperties().put("http.proxyPort", "8888");
        }
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {
                    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });

        try
        {
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("LOCAL_APPLICATION", "LOCAL_FACILITY"));
            config.setKeyStore(new JKSStoreInformation("C:\\ihe.keystore.jks", "changeit"));
            config.setTrustStore(new JKSStoreInformation("C:\\ihe.truststore.jks", "changeit"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
            xds = new XdsCommunicator(config);
            pix = new PixCommunicator(config);
            System.setProperty("javax.net.debug", "all");
        } catch (ActorNotFoundException ex)
        {
            LOGGER.info(ex);
            fail(ex.getMessage());
        } catch (IheConfigurationException ex)
        {
            LOGGER.info(ex);
        }

        PersonDemographic resolvedPatient = null;

        try
        {
            resolvedPatient = pix.resolveIdentifiers(TestUtils.getDemoPatient());
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
            fail(ex.getMessage());
        } catch (PixApplicationException ex)
        {
            LOGGER.info(ex);
            fail(ex.getMessage());
        }

        // Check if the patient that will be used for the test cases is registered.
        if (resolvedPatient.getIdentifiers().size() > 1)
        {
            testPatient = resolvedPatient;
        } else
        {
            try
            {
                // Register the patient that is to be used for the test cases.
                pix.register(TestUtils.getDemoPatient());
                testPatient = pix.resolveIdentifiers(TestUtils.getDemoPatient());

            } catch (Exception ex)
            {
                LOGGER.info(ex);
                fail(ex.getMessage());
            }
        }
    }

    @AfterClass
    public static void tearDownClass()
    {
        AtnaCommunicator.getAuditMessageQueue().awaitShutdown(5);
        System.getProperties().remove("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize");
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test(expected = IllegalArgumentException.class)
    public void registryStoredQuery_NullQuerySpec() throws CommunicationsException
    {
        xds.registryStoredQuery(XdsGuidMetaDataType.RegistryStoredQuery_FindDocuments, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registryStoredQuery_NullQueryType() throws CommunicationsException
    {
        xds.registryStoredQuery(null, new XdsQuerySpecification());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAll_NullPatient() throws CommunicationsException
    {
        xds.getAll(null);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void getFoldersAndContents_NullPatient() throws CommunicationsException
//    {
//        xds.getFoldersAndContents("123", null);
//    }
//    @Test(expected = IllegalArgumentException.class)
//    public void getFoldersAndContents_NullPatientIdentifiers() throws CommunicationsException
//    {
//        xds.getFoldersAndContents("123", new PersonDemographic());
//    }
//    @Test(expected = IllegalArgumentException.class)
//    public void getDocumentsAndAssociations_NullPatient() throws CommunicationsException
//    {
//        xds.getDocumentsAndAssociations("123", null);
//    }
    @Test(expected = IllegalArgumentException.class)
    public void findFolders_NullPatient() throws CommunicationsException
    {
        xds.findFolders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findFolders_NullPatientIdentifiers() throws CommunicationsException
    {
        xds.findFolders(new PersonDemographic());
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void getDocumentsAndAssociations_NullPatientIdentifiers() throws CommunicationsException
//    {
//        xds.getDocumentsAndAssociations("123", new PersonDemographic());
//    }
    // @Test
//    public void getFolderAndContents_ValidFolderId() throws CommunicationsException
//    {
//        String validFolderId = "ebd69b14-1750-4445-aebb-4392218b8fd3";
//
//        System.out.println("getFolderAndContents_ValidFolderId");
//
//        FolderMetaData[] folders = xds.getFoldersAndContents(validFolderId, testPatient);
//        System.out.printf("Found %d folders.%n", folders.length);
//
//        assertTrue("At least 1 folder should be found.", folders.length > 0);
//
//        for (FolderMetaData folder : folders)
//        {
//            assertTrue("At least 1 document should be inside each folder.", folder.getDocuments().size() > 0);
//        }
//    }
//
//    // @Test
//    public void getFolderAndContents_InvalidFolderId() throws CommunicationsException
//    {
//        String invalidFolderId = "12031823dfasd123-123124121241---";
//        System.out.println("getFolderAndContents_InvalidFolderId");
//
//        FolderMetaData[] folders = xds.getFoldersAndContents(invalidFolderId, testPatient);
//
//        assertTrue("No folders should be found.", folders.length == 0);
//    }
    //@Test
//    public void getAssociations_ValidIdentifier() throws CommunicationsException
//    {
//        String validIdentifier = "urn:uuid:e43d219c-5458-4371-b551-3ae7bae96e86";
//        System.out.println("getAssociations_ValidIdentifier");
//
//        AssociationMetaData[] associations = xds.getAssociations(validIdentifier, testPatient);
//
//        assertTrue("At least 1 association should be found.", associations.length > 0);
//    }
//
//    public void getAssociations_InvalidIdentifier() throws CommunicationsException
//    {
//        String validIdentifier = "_123";
//        System.out.println("getAssociations_InvalidIdentifier");
//
//        AssociationMetaData[] associations = xds.getAssociations(validIdentifier, testPatient);
//
//        assertTrue("No associations should be found.", associations.length == 0);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void getAssociations_NullIdentifier() throws CommunicationsException
//    {
//        System.out.println("getAssociations_Nulldentifier");
//
//        xds.getAssociations(null, testPatient);
//    }
    //  @Test
//    public void getDocuments() throws CommunicationsException, Exception
//    {
//        System.out.println("findFolders_ValidRequest");
//        xds.getDocuments("1.2.3", "4.5.6", "7.8.9");
//    }
//    @Test
    public void getAll_ValidPatient() throws CommunicationsException
    {
        System.out.println("getAll_ValidPatient");
        SubmissionSetMetaData[] submissionSets = xds.getAll(testPatient);

        System.out.println("Found " + submissionSets.length + " submission sets.");

        for (SubmissionSetMetaData submissionSet : submissionSets)
        {
            System.out.println("Found " + submissionSet.getFolders().size() + " folders in " + submissionSet.getUniqueId());
        }

        assertTrue("At least 1 submission set should be found.", submissionSets.length > 0);
    }

//   @Test
    public void getAll_InvalidPatient() throws CommunicationsException
    {
        System.out.println("getAll_InvalidPatient");

        PersonDemographic invalidPatient = new PersonDemographic();
        invalidPatient.addIdentifier(new DomainIdentifier(xds.getConfiguration().getAffinityDomain().getIdentification().getApplicationId(), "0310asadad"));
        SubmissionSetMetaData[] submissionSets = xds.getAll(invalidPatient);

        assertTrue("No submission sets should be found.", submissionSets.length == 0);
    }

    //  @Test
    public void findFolders_ValidRequest() throws CommunicationsException
    {
        System.out.println("findFolders_ValidRequest");

        FolderMetaData[] folders = xds.findFolders(testPatient);
        System.out.println("Found " + folders.length + " folders.");
        assertTrue("At least 1 folder should be found.", folders.length > 0);
    }

    // @Test
    public void findFolders_InvalidPatient() throws CommunicationsException
    {
        System.out.println("findFolders_InvalidPatient");

        PersonDemographic invalidPatient = new PersonDemographic();
        invalidPatient.addIdentifier(new DomainIdentifier(xds.getConfiguration().getAffinityDomain().getIdentification().getApplicationId(), "0310asadad"));
        FolderMetaData[] folders = xds.findFolders(invalidPatient);
        assertTrue("No folders should be found.", folders.length == 0);
    }

    //  @Test
//    public void getFoldersForDocument_ValidDocument() throws CommunicationsException
//    {
//        System.out.println("getFoldersForDocument_ValidDocument");
//        FolderMetaData[] folders = xds.getFoldersForDocument("1.2.3.4");
//
//        assertTrue("At least 1 folder should be found.", folders.length > 0);
//    }
//
//    // @Test
//    public void getFoldersForDocument_InvalidDocument() throws CommunicationsException
//    {
//        String invalidDocumentId = "1498129412abc";
//
//        System.out.println("getFoldersForDocument_InvalidDocument");
//        FolderMetaData[] folders = xds.getFoldersForDocument(invalidDocumentId);
//
//        assertTrue("No folders should be found.", folders.length == 0);
//    }
//    @Test
//    public void getDocumentsAndAssociations_ValidIdentifier() throws CommunicationsException
//    {
//        System.out.println("getDocumentsAndAssociations_ValidIdentifier");
//
//        String validIdentifier = "929489d3-417e-484a-832d-a50a48137f19";
//        MetaData[] documentsAndAssociations = xds.getDocumentsAndAssociations(validIdentifier, testPatient);
//
//        assertTrue("No documents and associations were found.", documentsAndAssociations.length > 0);
//    }
    //   @Test
    public void testFindPatientDocuments_ValidPatient() throws CommunicationsException
    {
        try
        {
            System.out.println("testFindPatientDocuments_ValidPatient");
            XdsQuerySpecification querySpec = new XdsQuerySpecification(XdsGuidType.RegistryStoredQuery_FindDocuments);

            querySpec.setPatient(testPatient);

            int result = xds.find(querySpec).length;

            System.out.printf("Found %d documents.%n", result);

            // The test patient used will have at least 1 document registered to them.
            assertTrue("Expected documents", result > 0);

        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetFolderAndContents() throws CommunicationsException
    {
        LOGGER.info("testGetFolderAndContents");

        FolderMetaData[] folder = xds.getFolderAndContents(new XdsFolderUniqueId(TestFolderUniqueId));

        LOGGER.info(String.format("Found %s documents in folder.", folder[0].getDocuments().size()));
    }


    @Test
    public void testGetDocuments() throws CommunicationsException
    {
        DocumentMetaData[] documents = xds.getDocuments(new XdsDocumentEntryUniqueId(new String[]
        {
            TestDocumentUniqueId
        }));

        LOGGER.info("Found " + documents.length + " documents.");
    }

    public void findDocuments() throws CommunicationsException
    {
        DocumentMetaData[] documents = xds.findDocuments(testPatient);

        LOGGER.info(String.format("Found %d documents.", documents.length));
    }

    //   @Test
    public void testFindPatientDocuments_InvalidPatient() throws CommunicationsException
    {
        try
        {
            System.out.println("testFindPatientDocuments_InvalidPatient");
            XdsQuerySpecification querySpec = new XdsQuerySpecification(XdsGuidType.RegistryStoredQuery_FindDocuments);

            PersonDemographic invalidPatient = new PersonDemographic();
            invalidPatient.addIdentifier(new DomainIdentifier(xds.getConfiguration().getAffinityDomain().getIdentification().getApplicationId(), "0310asadad"));
            querySpec.setPatient(invalidPatient);

            int result = xds.find(querySpec).length;

            // The test patient used will have at least 1 document registered to them.
            assertTrue("No documents should be found.", result == 0);

        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
            fail(ex.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_NullSubmissionSet() throws CommunicationsException
    {
        xds.register((MetaData) null); // great job java.. method overload at its best
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_NullPatient() throws CommunicationsException
    {
        xds.register(new DocumentSubmissionSetMetaData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_NoAffinityDomainIdentifier() throws CommunicationsException
    {
        PersonDemographic patient = new PersonDemographic();

        // Add a suffix to the affinity domain id.
        patient.addIdentifier(new DomainIdentifier(xds.getConfiguration().getAffinityDomain().getOid() + "Z", "123"));
        DocumentSubmissionSetMetaData submissionSet = new DocumentSubmissionSetMetaData();

        submissionSet.setPatient(patient);

        xds.register(submissionSet);
    }

//    @Test
    public void registerDocumentTest()
    {
        DocumentSubmissionSetMetaData docSubData = new DocumentSubmissionSetMetaData();

        DomainIdentifier identifier = new DomainIdentifier();
        identifier.setExtension("");
        identifier.setAssigningAuthority("MiH_OSCAR_A");
        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        docSubData.setId(identifier);

        docSubData.setPatient(testPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        author.addIdentifier(authInfo);

        docSubData.setAuthor(author);

        DocumentMetaData meta = new DocumentMetaData();
        DocumentMetaData meta2 = new DocumentMetaData();
        //meta.setDocumentUniqueId(identifier);
        meta.setMimeType("text/xml");
        meta.setTitle("First Doc");
        meta.setCreationTime(new GregorianCalendar(2012, 10, 7));
        meta2.setMimeType("text/xml");
        meta2.setTitle("Second Doc");
        meta2.setCreationTime(new GregorianCalendar(2012, 10, 8));
        byte[] content = "doc11".getBytes();
        byte[] content2 = "doc22".getBytes();

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");

        meta.setContentType(contentType);
        meta2.setContentType(contentType);

        CodeValue classCode = new CodeValue();
        classCode.setCode("Class Code Value");
        classCode.setCodeSystem("OSCAR Specific Value");
        classCode.setDisplayName("letter");
        meta.setClassCode(classCode);
        meta2.setClassCode(classCode);

        CodeValue confidentialityCode = new CodeValue();
        confidentialityCode.setCode("2.16.840.1.113883.5.25");
        confidentialityCode.setCodeSystem("Connect-a-thon confidentialityCodes");
        confidentialityCode.setDisplayName("N");
        meta.addConfidentiality(confidentialityCode);
        meta2.addConfidentiality(confidentialityCode);

        CodeValue formatCode = new CodeValue();
        formatCode.setCode("urn:ad:oscar:xhtml-form");
        formatCode.setCodeSystem("OSCAR Specific Value");
        formatCode.setDisplayName("urn:ad:oscar:xhtml-form");
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
        typeCode.setCode("52033-8");
        typeCode.setCodeSystem("LOINC");
        typeCode.setDisplayName("General Correspondence");
        meta.setType(typeCode);
        meta2.setType(typeCode);

        meta.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.33349.3.1.3.201203.2.0.0&ISO");
        meta.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");
        //meta2.addExtendedAttribute("legalAuthenticator", "afjalsfjlsaf");

        meta.setContent(content);
        meta2.setContent(content2);
        docSubData.addDocument(meta);
        //docSubData.addDocument(meta2);

        boolean result = false;
        try
        {
            result = xds.register(docSubData);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }
        assert (result);
    }

//    @Test
    public void testRegisterSingleDocument()
    {
        DocumentMetaData meta = new DocumentMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
//        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        meta.setId(identifier);

        meta.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");

        meta.setServiceTimeStart(Calendar.getInstance());
        meta.setServiceTimeEnd(Calendar.getInstance());

        meta.setPatient(testPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        author.addIdentifier(authInfo);

        meta.setAuthor(author);

        meta.setMimeType("text/plain");
        meta.setTitle("Existing Document for Pre-Connectathon testing");
        meta.setCreationTime(new GregorianCalendar(2013, 10, 7));
        byte[] content = "Existing Document for Pre-Connectathon testing content".getBytes();
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

        meta.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.33349.3.1.3.201203.2.0.0&ISO");
        meta.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        boolean result = false;
        try
        {
            result = xds.register(meta);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testRegisterSingleFolder()
    {
        FolderMetaData meta = new FolderMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
//        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        meta.setId(identifier);

        meta.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");

        meta.setPatient(testPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
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
        try
        {
            result = xds.register(meta);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testRegisterSingleFolderWithMultipleDocuments()
    {
        FolderMetaData folder = new FolderMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
//        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        folder.setId(identifier);
        folder.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        folder.setPatient(testPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
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
        DocumentMetaData document1 = TestUtils.getDemoDocument();
        document1.setPatient(testPatient);

        DocumentMetaData document2 = TestUtils.getDemoDocument();
        document2.setPatient(testPatient);

        folder.addDocument(document1);
        folder.addDocument(document2);

        boolean result = false;
        try
        {
            result = xds.register(folder);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testRegisterMultipleDocuments()
    {
        DocumentMetaData meta = new DocumentMetaData();
        DocumentMetaData meta2 = new DocumentMetaData();

        meta.setServiceTimeStart(Calendar.getInstance());
        meta2.setServiceTimeStart(Calendar.getInstance());
        meta.setServiceTimeEnd(Calendar.getInstance());
        meta2.setServiceTimeEnd(Calendar.getInstance());

        meta.setPatient(testPatient);
        meta2.setPatient(testPatient);

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
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

        meta.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.33349.3.1.3.201203.2.0.0&ISO");
        meta.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        meta2.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.33349.3.1.3.201203.2.0.0&ISO");
        meta2.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        DocumentContainerMetaData container = new DocumentContainerMetaData();
        container.setPatient(testPatient);
        container.setAuthor(author);
        container.setCreationTime(Calendar.getInstance());
        container.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");

        // facility identification
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
        container.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        container.setId(identifier);

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");
        container.setContentType(contentType);

        container.addDocument(meta);
        container.addDocument(meta2);

        boolean result = false;
        try
        {
            result = xds.register(container);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testRegisterSingleDocumentInExistingFolder()
    {

        System.out.println("testRegisterMultipleDocumentsInExistingFolder");

        FolderMetaData[] folders = null;
        try
        {
            folders = xds.getFolders(new XdsFolderUniqueId(TestFolderUniqueId));
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        FolderMetaData folder = folders[0];
        folder.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        author.addIdentifier(authInfo);

        folder.setAuthor(author);

//        folder.setTitle("mo's awesome folder");
//        folder.setDescription("my awesome folder");
        folder.setCreationTime(new GregorianCalendar(2012, 10, 7));

        CodeValue codeList = new CodeValue();
        codeList.setCode("Referrals");
        codeList.setCodeSystem("Connect-a-thon folderCodeList");
        codeList.setDisplayName("Referrals");
//        folder.setCodeList(codeList);

        CodeValue contentTypeCode = new CodeValue();
        contentTypeCode.setCode("History and Physical");
        contentTypeCode.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentTypeCode.setDisplayName("History and Physical");
        folder.setContentType(contentTypeCode);

        // documents
        DocumentMetaData document1 = TestUtils.getDemoDocument();
        document1.setPatient(testPatient);

        DocumentMetaData document2 = TestUtils.getDemoDocument();
        document2.setPatient(testPatient);

        folder.addDocument(document1);
//        folder.addDocument(document2);

        boolean result = false;
        try
        {
            result = xds.register(folder);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testRegisterExistingDocumentInExistingFolder()
    {

        System.out.println("testRegisterExistingDocumentInExistingFolder");

        FolderMetaData[] folders = null;
        DocumentMetaData[] documents = null;
        try
        {
            folders = xds.getFolders(new XdsFolderUniqueId(TestFolderUniqueId));
            documents = xds.getDocuments(new XdsDocumentEntryUniqueId(new String[]
            {
                TestDocumentUniqueId
            }));
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        FolderMetaData folder = folders[0];
        folder.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");

        DocumentMetaData document1 = documents[0];
        document1.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        author.addIdentifier(authInfo);

        folder.setAuthor(author);

//        folder.setTitle("mo's awesome folder");
//        folder.setDescription("my awesome folder");
        folder.setCreationTime(new GregorianCalendar(2012, 10, 7));

        CodeValue contentTypeCode = new CodeValue();
        contentTypeCode.setCode("History and Physical");
        contentTypeCode.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentTypeCode.setDisplayName("History and Physical");
        folder.setContentType(contentTypeCode);

        folder.addDocument(document1);

        boolean result = false;
        try
        {
            result = xds.register(folder);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

//    @Test
    public void testReplaceDocument()
    {

        System.out.println("testReplaceDocument");

        DocumentMetaData[] documents = null;
        try
        {
            documents = xds.getDocuments(new XdsDocumentEntryUniqueId(new String[]
            {
                TestDocumentUniqueId
            }));
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        // document to replace
        DocumentMetaData document1 = documents[0];

        // replacement document
        DocumentMetaData document = TestUtils.getDemoDocument();

        // replace relationship
        document.setRelationship(new DocumentRelationship(document1.getId(), DocumentRelationshipType.RPLC));

        document.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        document.setMimeType("text/plain");
        document.setTitle("Replacement Document");
        document.setCreationTime(new GregorianCalendar(2013, 11, 7));
        byte[] content = "replacement document content".getBytes();

        document.setContent(content);
        document.setPatient(testPatient);

        boolean result = false;
        try
        {
            result = xds.register(document);
        } catch (CommunicationsException ex)
        {
            LOGGER.info(ex);
        }

        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_NullQuerySpec() throws CommunicationsException
    {
        xds.find(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_NullPatient() throws CommunicationsException
    {
        xds.find(new XdsQuerySpecification());
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_NullPatientIdentifiers() throws CommunicationsException
    {
        XdsQuerySpecification querySpec = new XdsQuerySpecification(XdsGuidType.RegistryStoredQuery_FindDocuments);
        PersonDemographic patient = new PersonDemographic();

        querySpec.setPatient(patient);

        xds.find(querySpec);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void find_NoAffinityDomainIdentifier() throws CommunicationsException
//    {
//        XdsQuerySpecification querySpec = new XdsQuerySpecification(XdsGuidMetaDataType.RegistryStoredQuery_FindDocuments);
//        PersonDemographic patient = new PersonDemographic();
//
//        // Add a suffix to the affinity domain id.
////        patient.addIdentifier(new DomainIdentifier(xds.getAffinityDomain().getIdentification().getApplicationId() + "Z", "123"));
//
//        querySpec.setPatient(patient);
//        xds.find(querySpec);
//    }
//    @Test
    public void fillInDetailsTest()
    {
        boolean testResult = true;

        try
        {

            for (DocumentMetaData documentMetaData : xds.fillInDetails(xds.findDocuments(testPatient)))
            {
                assertTrue(documentMetaData.getContent() != null);
            }

        } catch (Exception ex)
        {
            LOGGER.info(ex);
            testResult = false;
        }

        assertTrue(testResult);
    }
}
