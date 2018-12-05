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
 * @author brownp Date: Aug 21, 2013
 *
 */
package org.marc.shic.cda.utils;

import static org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationSignature.Signed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.exceptions.InvalidStringDataException;
import org.marc.shic.cda.level1.BppcDocument;
import org.marc.shic.cda.level1.CDAFactory;
import org.marc.shic.cda.level1.ScannedDocument;
import org.marc.shic.cda.level2.ActiveProblemsSection;
import org.marc.shic.cda.level2.AllergiesAndDrugSensitiviesSection;
import org.marc.shic.cda.level2.MedicationSection;
import org.marc.shic.cda.level1.PhrExtractDocument;
import org.marc.shic.cda.level2.ResultsSection;
import org.marc.shic.cda.level2.VitalSignsSection;
import org.marc.shic.cda.level3.Medication;
import org.marc.shic.cda.level3.MedicationPrescription;
import org.marc.shic.cda.level3.MedicationProduct;
import org.marc.shic.cda.level3.ProblemObservation;
import org.marc.shic.cda.level3.Result;
import org.marc.shic.cda.level3.VitalSign;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.cda.templates.AuthorTemplate;
import org.marc.shic.cda.templates.DocumentationOfTemplate;
import org.marc.shic.cda.templates.Performer1Template;
import org.marc.shic.cda.templates.RecordTargetTemplate;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.utils.TestUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CdaTestUtils {

	private static int recurseCounter = 0;
	private static ArrayList<String> returnTypes = new ArrayList<String>();
	private static CodeTranslator codeTranslator = new CodeTranslator();
	private static final Logger LOGGER = Logger.getLogger(CdaTestUtils.class);

	public static PhrExtractDocument createDemoPhrExtractDocument() throws InvalidStringDataException {
		// *** START HEADER ***
		PhrExtractDocument phrExtractDocument = CDAFactory.createPHRDocument();

		PersonAddress address = DataTypeHelpers.createAddress(AddressUse.Workplace, null, "Ancaster", "Ontario", "Canada", "L8N 8N9");
		LocationDemographic location = DataTypeHelpers.createLocationDemographic("1.2.3.4.5.6.7", "Location Demo Extension", "OSCAR_1", "Location1 Demo Name", address, "905 555 9991");
		LocationDemographic location2 = DataTypeHelpers.createLocationDemographic("1.2.3.4.5.6.7", "Location2 Demo Extension", "OSCAR_1", "Location2 Demo Name", address, "tel:905 555 9992");
		LocationDemographic location3 = DataTypeHelpers.createLocationDemographic("1.2.3.4.5.6.7", "Location2 Demo Extension", "OSCAR_1", "Location3 Demo Name", address, null);
		LocationDemographic locationOSCAR = DataTypeHelpers.createLocationDemographic("1.2.3.4.5.6.7", "LocationOSCAR", "Master Hospital", "LocationOSCAR Demo Name", address, "");

		// Add in the document author
		PersonDemographic authorTest = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6", "FakeAuthorID", "Craigy the Author", "Clarky", Calendar.getInstance(), "m", "tel:905 388 6864");

		// Testing Phones
		// need to add more TELs
		// phrExtractDocument.getDocument().getAuthor().get(0).getAssignedAuthor().getTelecom().add(new TEL());
		// END Testing Phones
		//phrExtractDocument.addAuthor(authorTest, location);
		//phrExtractDocument.setCustodian(DataTypeHelpers.createCustodian(location));

		// Add in a legal authenticator
		PersonDemographic legalAuthenticatorTest = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6", "FakeLegalAuthenticatorID", "Craigy the Legal Authenticator", "Clarky", Calendar.getInstance(), "m", "tel:905 388 6864");
		legalAuthenticatorTest.addAddress(DataTypeHelpers.createAddress(AddressUse.Workplace, "135 Fennell Avenue West LEGAL AUTHENTICATOR", "Hamilton", "Ontario", "Canada", "L8N 3T2"));
		//phrExtractDocument.addLegalAuthenticator(legalAuthenticatorTest, Calendar.getInstance());

		// Add in a record target
		PersonDemographic recordTargetTest = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6", "FakePatientID", "Craigy the Patient", "Clarky", Calendar.getInstance(), "m", "tel:905 388 6864");
		recordTargetTest.addAddress(DataTypeHelpers.createAddress(AddressUse.Home, "", "Hamilton", "Ontario", "Canada", null));
		//phrExtractDocument.setRecordTarget(recordTargetTest);

		// Add the HealthCare Providers for a DocumentationOf/Service Event
		PersonDemographic authorTest2 = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6.2", "FakePerformerID2", "Craigy the Performer2", "Clarky", Calendar.getInstance(), "m", "tel:905-555-5555");
		PersonDemographic authorTest3 = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6.3", "FakePerformerID3", "Craigy the Performer3", "Clarky", Calendar.getInstance(), "m", "905-555-5556");
		PersonDemographic authorTest4 = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6.4", "FakePerformerID4", "Craigy the Performer4", "Clarky", Calendar.getInstance(), "m", "");
		PersonDemographic authorTest5 = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6.4", "FakePerformerID4", "Craigy the Performer4", "Clarky", Calendar.getInstance(), "m", "");

		//Old way of adding performers
        /*
		 ArrayList<Performer1> performers = new ArrayList<Performer1>();
		 performers.add(DataTypeHelpers.createPerformer1(authorTest2, location, Calendar.getInstance(), Calendar.getInstance()));
		 performers.add(DataTypeHelpers.createPerformer1(authorTest3, location2, Calendar.getInstance(), Calendar.getInstance()));
		 performers.add(DataTypeHelpers.createPerformer1(authorTest4, location3, Calendar.getInstance(), Calendar.getInstance()));
		 performers.add(DataTypeHelpers.createPerformer1(authorTest5, locationOSCAR, Calendar.getInstance(), Calendar.getInstance()));
		 phrExtractDocument.addDocumentationOf(Calendar.getInstance(), Calendar.getInstance(), performers);*/
		ActiveProblemsSection section = phrExtractDocument.getActiveProblemsSection();
		section.addTableColumns("ID", "Description");
		section.addDisplayEntry("1", "PEANUT OIL");
		section.addDisplayEntry("2", "HOUSE DUST MITES");
		section.addDisplayEntry("3", "FEAR OF OSCAR");

		return phrExtractDocument;
	}

	public static PhrExtractDocument createDemoTestDocument() {
		PhrExtractDocument phrExtractDocument = CDAFactory.createPHRDocument();

		Calendar instantTime = Calendar.getInstance();

		//testLocation.addAddress(address);
		AuthorTemplate author = phrExtractDocument.addAuthor("1.2.3.4.5.6", "The Russian", "Roosky", "Russian", new Time());
		author.addTelecom("+72345678");
		author.addAddress(PostalAddressUse.WorkPlace, "Russia", AddressPartType.Country);

		phrExtractDocument.setCustodian("1.2.3.4.5.6", "RussianOffice", "Russian Hospital", new TEL("tel:72345678", TelecommunicationsAddressUse.Home));
		phrExtractDocument.setLegalAuthenticator("1.2.3.4.5.6", "RussiaKGB", "Nikolai", "Kolowski", Signed);

		RecordTargetTemplate recordTarget = phrExtractDocument.addRecordTarget("1.2.3.4.5.6", "Heisenberg", "Walter", "White", AdministrativeGender.Male, "+71234567");
		recordTarget.addAddress(PostalAddressUse.PrimaryHome, "308 Negra Arroyo Lane", AddressPartType.AddressLine);
		recordTarget.addAddress(PostalAddressUse.PrimaryHome, "Albuquerque", AddressPartType.City);
		recordTarget.addAddress(PostalAddressUse.PrimaryHome, "New Mexico", AddressPartType.State);
		recordTarget.addAddress(PostalAddressUse.PrimaryHome, "USA", AddressPartType.Country);
		recordTarget.addAddress(PostalAddressUse.PrimaryHome, "87104", AddressPartType.PostalCode);
		recordTarget.addName(EntityNameUse.Pseudonym, "Heisenberg", EntityNamePartType.Title);
		
		FuncUtil.copy(recordTarget, phrExtractDocument.setLegalAuthenticator());

		DocumentationOfTemplate docOf = phrExtractDocument.addDocumentationOf();
		Performer1Template performer = docOf.addPerformer("1.2.3.4.5.6", "PerformerRussian", "Zed", "X", new Time());

		//Active Problems Section
		ActiveProblemsSection section = phrExtractDocument.getActiveProblemsSection();
		section.addTableColumns("ID", "Description");
		section.addDisplayEntry("1", "LUNG CANCER");

		//Medications Section
		MedicationSection medsSection = phrExtractDocument.getMedicationSection();

		//Medication 1
		Medication med = medsSection.createMedication(new Time(), Code.search("Oral"));
		MedicationProduct prod1 = med.setProduct("1.2.3.4.5.6", "CAV", Code.search("CYCLOPHOSPAMIDE 100MG"));
		prod1.addName(null, "Cyclophospamide", EntityNamePartType.Given);
		
		MedicationPrescription pres1 = med.setPrescription("1.2.3.4.5.6", "#1");
		pres1.addPrescriber("1.2.3.4.5.6", "DrugLord", "Walter", "White", new Time());

		ProblemObservation ind = med.addIndication(Code.search("Instructions"), new Time(), ActStatus.Normal, Code.search("No conditions."));
		ind.setText("During chemotherapy session.");

		//Medication 2
		Medication med2 = medsSection.createMedication(new Time(), Code.search("IV"));
		MedicationProduct prod2 = med2.setProduct("1.2.3.4.5.6", "CAV", Code.search("DOXORUBICIN 100MG"));
		prod2.addName(null, "Pfizer - Doxorubicin", EntityNamePartType.Given);
		
		MedicationPrescription pres2 = med2.setPrescription("1.2.3.4.5.6", "#1");
		pres2.addPrescriber("1.2.3.4.5.6", "DrugLord", "Walter", "White", new Time());

		ProblemObservation ind2 = med2.addIndication(Code.search("Instructions"), new Time(), ActStatus.Normal, Code.search("No conditions."));
		ind2.setText("During chemotherapy session.");

		//Vital Signs Section
		VitalSignsSection vitals = phrExtractDocument.getVitalSignsSection();
		VitalSign sign = vitals.addVitalSign(new Time());
		sign.setResultObservation(new Code("8867-4", "2.16.840.1.113883.6.1", "HEART BEAT", "LOINC"), 72, "/min");

		//Results Section
		ResultsSection results = phrExtractDocument.getResultsSection();
		Result res1 = results.addResult(new Time(), new Code("43789009", "2.16.840.1.113883.6.96", "CBC WO DIFFERENTIAL", "SNOMED CT"));
		res1.setResultObservation(new Code("30313-1", "2.16.840.1.113883.6.1", "HGB", "LOINC"), 4.5d, "/unit");

		//Result res2 = results.addResult(Time.empty(), Code.fromStrings("43789009", "2.16.840.1.113883.6.96", "CBC WO DIFFERENTIAL", "SNOMED CT"));
		//res2.setResultObservation(Code.fromStrings("30313-1", "2.16.840.1.113883.6.1", "HGB", "LOINC"), "Something else...");
		
		//Allergies Section
		AllergiesAndDrugSensitiviesSection allergies = phrExtractDocument.getAllergiesAndDrugSensitiviesSection();
		allergies.addDisplayEntry("17", "PEANUTS", "DEATH", "SEVERE", "03/03/2013");

		return phrExtractDocument;
	}

	public static PhrExtractDocument createDemoTest2Document() throws InvalidStringDataException {
		// *** START HEADER ***
		PhrExtractDocument phrExtractDocument = CDAFactory.createPHRDocument();

		//PersonAddress address = DataTypeHelpers.createAddress(AddressUse.Workplace, "308 Negra Arroyo Lane", "Albuquerque", "New Mexico", "United States of America", "87104");
		//LocationDemographic testLocation = DataTypeHelpers.createLocationDemographic("1.2.3.4.5.6", "HeadOffice", "OSCAR_1", "Albuquerque Hospital", address, "628 997 4441");
		//PersonDemographic demoPerson = DataTypeHelpers.createPersonDemographic("1.2.3.4.5.6", "Heisenberg", "Walter", "White", Calendar.getInstance(), "m", "tel:628 911 8629");
		Calendar instantTime = Calendar.getInstance();

		//phrExtractDocument.addAuthor((PersonDemographic) null, null);
		//phrExtractDocument.setCustodian((Custodian) null);
		//phrExtractDocument.addLegalAuthenticator(null, null);
		//phrExtractDocument.setRecordTarget((PersonDemographic) null);
		//phrExtractDocument.setRecordTarget((RecordTarget) null);
		//phrExtractDocument.addPerformer((PersonDemographic) null, null, null, null);

		// *** START SECTIONS ***
		ActiveProblemsSection section = phrExtractDocument.getActiveProblemsSection();
		section.addTableColumns(null, null);
		section.addDisplayEntry(null, null);

		MedicationSection medsSection = phrExtractDocument.getMedicationSection();
		medsSection.addTableColumns(null, null, null, null);
		medsSection.addDisplayEntry(null, null, null, null);
		medsSection.addDisplayEntry(null, null, null, null);

		phrExtractDocument.getAllergiesAndDrugSensitiviesSection();
		phrExtractDocument.getProblemSection();

		return phrExtractDocument;
	}

	/**
	 * Creates and populates a sample structured (non-scanned) BPPC document
	 *
	 * @return
	 */
	public static BppcDocument createStructuredBppcDocument() {
		BppcDocument retVal = CDAFactory.createBPPCDocument(false);

		// title
		//retVal.setTitle("Consent to Share Information");

		// language
		//retVal.setLanguageCode("en-US");

		// code
		//retVal.setTypeId(new II("57016-8", "2.16.840.1.113883.6.1"));

		// confidentiality code
		//retVal.setConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal", "Confidentiality"));

		// record target
		PersonDemographic recordTarget = TestUtils.getDemoPatient();
		//retVal.setRecordTarget(recordTarget);

		// location (author, custodian is not used for structured BPPC docs)
		LocationDemographic location = TestUtils.generateCorporateLocation();

		// original author
		PersonDemographic author = TestUtils.generateDemoAuthor();
		//retVal.addAuthor(author, location);

		// legal authenticator (the patient/consent giver is legally responsible for their consent)
		//retVal.addLegalAuthenticator(recordTarget, Calendar.getInstance());

		// custodian
		retVal.setCustodian(location);

		// consent policies
		CodeValue consent = new CodeValue("policyCode", "policyCodeSystem", "policyDisplayName", "policyCodeSystemName");
		retVal.addConsentPolicy(consent, Calendar.getInstance(), Calendar.getInstance());

		// an empty Structured Body
		//retVal.setStructuredBody(new StructuredBody(new Component3(ActRelationshipHasComponent.HasComponent, BL.TRUE, new Section())));

		return retVal;
	}

	/**
	 * Creates a test Scanned (non-structured) BPPC document
	 *
	 * @return
	 */
	public static BppcDocument createScannedBppcDocument() {
		BppcDocument retVal = CDAFactory.createBPPCDocument(true);

		// title
		//retVal.setTitle("Consent to Share Information");

		// language
		//retVal.setLanguageCode("en-US");

		// code
		//retVal.setTypeId(new II("57016-8", "2.16.840.1.113883.6.1"));

		// confidentiality code
		//retVal.setConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal", "Confidentiality"));

		// record target
		PersonDemographic recordTarget = TestUtils.getDemoPatient();
		//retVal.setRecordTarget(recordTarget);

		// location
		LocationDemographic location = TestUtils.generateCorporateLocation();

		// non-structured
		retVal.setNonXmlBody("test pdf".getBytes(), "application/pdf");

		// original author
		PersonDemographic author = TestUtils.generateDemoAuthor();
		//retVal.addAuthor(author, location);

		// scanner author
		retVal.addScanner(location, "SOME SCANNER NAME AND MODEL", "SCAN SOFTWARE NAME v0.0");

		// legal authenticator (the patient/consent giver is legally responsible for their consent)
		//retVal.addLegalAuthenticator(recordTarget, Calendar.getInstance());

		// custodian
		retVal.setCustodian(location);

		// dataEnterer (can be the same original author)
		retVal.setDataEnterer(author);

		// consent policies (multiple)
		CodeValue consent = new CodeValue("policyCode", "policyCodeSystem", "policyDisplayName", "policyCodeSystemName");
		retVal.addConsentPolicy(consent, Calendar.getInstance(), Calendar.getInstance());
		retVal.addConsentPolicy(consent, Calendar.getInstance(), null); // no end time

		return retVal;
	}

	/**
	 * Create a test Scanned document (XDS-SD)
	 *
	 * @return
	 */
	public static ScannedDocument createScannedDocument() {
		ScannedDocument retVal = CDAFactory.createScannedDocument();

		// templateId
		retVal.addTemplateRule(TemplateRuleID.PCC_XDS_SD.getOid());

		// title
		//retVal.setTitle("Test Scanned Document");

		// language
		//retVal.setLanguageCode("en-US");

		// code
//        retVal.getDocument().setCode((CE<String>) CoreDataTypeHelpers.createCodedElement(new CodeValue("57016-8", "2.16.840.1.113883.6.1", "PATIENT PRIVACY ACKNOWLEDGEMENT", "LOINC")));
		// confidentiality code
		//retVal.setConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal", "Confidentiality"));

		// record target
		PersonDemographic recordTarget = TestUtils.getDemoPatient();
		//retVal.setRecordTarget(recordTarget);

		// location
		LocationDemographic location = TestUtils.generateCorporateLocation();

		// non-structured
		retVal.setNonXmlBody("test pdf".getBytes(), "application/pdf");

		// original author
		PersonDemographic author = TestUtils.generateDemoAuthor();
		//retVal.addAuthor(author, location);

		// scanner author
		retVal.addScanner(location, "SOME SCANNER NAME AND MODEL", "SCAN SOFTWARE NAME v0.0");

		// legal authenticator (the patient/consent giver is legally responsible for their consent)
		//retVal.addLegalAuthenticator(recordTarget, Calendar.getInstance());

		// custodian
		retVal.setCustodian(location);

		// dataEnterer (can be the same original author)
		retVal.setDataEnterer(author);

		return retVal;
	}

	/**
	 * Schema validator for Clinical Documents
	 *
	 * @param xmlString
	 * @return
	 */
	public static boolean schemaValidate(String xmlString) {

		// your local directory with the schema files
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		File xsdFile = new File("src/main/resources/infrastructure/cda/CDA.xsd");
		Schema schema = null;
		Validator XsdValidator = null;
		try {
			schema = factory.newSchema(xsdFile);
			XsdValidator = schema.newValidator();

		} catch (SAXException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}

		Source source = new StreamSource(new StringReader(xmlString));

		try {
			XsdValidator.validate(source);
			return true;
		} catch (SAXException saxException) {
			LOGGER.error(saxException.getMessage(), saxException);
		} catch (IOException ioException) {
			//  TODO Auto-generated catch block
			LOGGER.error(ioException.getMessage(), ioException);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return false;
	}

	public static boolean schemaValidate(Document document) {

		// your local directory with the schema files
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		File xsdFile = new File("src/ca/marc/ihe/cda/resources/infrastructure/cda/CDA.xsd");
		Schema schema = null;
		Validator XsdValidator = null;
		try {
			schema = factory.newSchema(xsdFile);
			XsdValidator = schema.newValidator();

		} catch (SAXException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}

		Source source = new DOMSource(document);

		try {
			XsdValidator.validate(source);
			return true;
		} catch (SAXException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return false;
	}
//
//    public static boolean testSingleConformanceRule(DocumentTemplate document, ConformanceRuleParts conformanceRule, boolean detailedOutput)
//    {
//        boolean validated = true;
//        boolean createRequiredElements = false;
//
//        if(CdaUtils.testXpathValue(document, conformanceRule, detailedOutput)){
//            System.out.println("Xpath Validates\n***************************\n");
//        }else{
//            System.out.println("Xpath FAILED Validation\n***************************\n");
//            validated = false;
//        }
//        return validated;
//    }

	public static void findSxcmType(ClinicalDocument cda) {
		File outFile = new File("cdaRecursion.txt");
		FileOutputStream is = null;
		OutputStreamWriter osw = null;
		try {
			is = new FileOutputStream(outFile);
			osw = new OutputStreamWriter(is);
		} catch (FileNotFoundException ex) {
		}

		Writer writer = new BufferedWriter(osw);

		LOGGER.debug("*** STARTING TO SEARCH CDA ***");

		ArrayList<String> rootObjectList = new ArrayList<String>();
		rootObjectList.add("-> " + cda.getClass().toString());

		recurseClass(cda.getClass(), 0, 10, writer, rootObjectList, false);
		LOGGER.debug(String.format("*** DONE SEARCHING CDA %s objects found ***", recurseCounter));
	}

	public static void findSxcm(Class cl, int maxDepth) {
		String fileName = String.format("cdaRecursion_%s.txt", Calendar.getInstance().getTime().toString().replaceAll("[: ]", "_"));
		File outFile = new File(fileName);
		FileOutputStream is = null;
		OutputStreamWriter osw = null;
		try {
			is = new FileOutputStream(outFile);
			osw = new OutputStreamWriter(is);
		} catch (FileNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}

		Writer writer = new BufferedWriter(osw);

		LOGGER.debug("*** STARTING TO SEARCH CDA ***");

		ArrayList<String> rootObjectList = new ArrayList<String>();

		LOGGER.debug(cl.toString());
		rootObjectList.add("-> " + cl.toString());

		recurseClass(cl, 0, maxDepth, writer, rootObjectList, false);
		LOGGER.debug(String.format("*** DONE SEARCHING CDA %s objects found ***", recurseCounter));
	}

	public static void recurseClass(Class cl, int depth, int maxDepth, Writer writer, ArrayList<String> parentList, boolean outputOn) {

		if (depth < maxDepth) {
			depth++;

			for (Method method : cl.getMethods()) {
				ArrayList<String> childList = new ArrayList<String>(parentList);
				childList.add(printDepth(depth) + cl.toString() + " -> " + method.toString());

				if (ArrayList.class.isAssignableFrom(method.getReturnType())) {
					outputOn = true;
					if (outputOn && method.getName().contains("getEffectiveTime")) {
						writeToFile(writer, printDepth(depth) + method.getName() + " -> " + method.getReturnType().getName());
						for (String line : childList) {
							writeToFile(writer, line);
						}

						writeToFile(writer, "\n");

						returnTypes.add(parentList.get(parentList.size() - 1).toLowerCase());
					}
				}

				if (method.getReturnType().getName().contains("org.marc.everest")) {
					recurseClass(method.getReturnType(), depth, maxDepth, writer, childList, outputOn);

				}
			}
		}
	}

	public static void writeToFile(Writer writer, String line) {
		try {
			writer.write(line + "\n");
			writer.flush();
		} catch (IOException ex) {
		}

	}

	public static String printDepth(int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			builder.append(" ");
		}
		return builder.toString();
	}
}
