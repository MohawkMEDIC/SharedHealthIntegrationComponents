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
package org.marc.shic.cda;

import org.marc.shic.cda.level1.PhrExtractDocument;
//import org.marc.shic.cda.level2.PhrExtractDocumentFactory;

import java.math.BigDecimal;

import org.marc.shic.cda.utils.CdaUtils;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.marc.everest.datatypes.PQ;
import org.marc.everest.interfaces.IResultDetail;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.shic.cda.exceptions.InvalidStringDataException;
import org.marc.shic.cda.exceptions.TemplateViolationException;
import org.marc.shic.cda.level1.CDAFactory;

import org.marc.shic.cda.templateRules.Level2CdaRules;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_1_1;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_1_5;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_2_3;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_3_6;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_3_8;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_4_5;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2;
import org.marc.shic.cda.templateRules.Template_2_16_840_1_113883_10_20_1_11;
import org.marc.shic.cda.utils.CdaTestUtils;

/**
 *
 * @author brownp
 */
public class PhrExtractDocumentFactoryTest {

	private static PhrExtractDocument PhrExtractDocument;
	private static Logger LOGGER = Logger.getLogger(PhrExtractDocumentFactoryTest.class);

	private static boolean detailedOutput = false;
	private static boolean createRequiredElements = false;

	private static Template_1_3_6_1_4_1_19376_1_5_3_1_1_5 XPHR;
	private static Template_1_3_6_1_4_1_19376_1_5_3_1_1_1 MedicalSpecification;
	private static Template_1_3_6_1_4_1_19376_1_5_3_1_3_6 ActiveProblemsSection;
	private static Template_1_3_6_1_4_1_19376_1_5_3_1_3_8 HistoryOfPastIllness;
	private static Template_2_16_840_1_113883_10_20_1_11 CCD_3_5;
	private static Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2 ProblemConcernEntry;

	@BeforeClass
	public static void setUpClass() throws TemplateViolationException {
		DOMConfigurator.configure("log4j.xml");
		PhrExtractDocument = CdaTestUtils.createDemoTestDocument();
		MedicalSpecification = Template_1_3_6_1_4_1_19376_1_5_3_1_1_1.newInstance();
		XPHR = Template_1_3_6_1_4_1_19376_1_5_3_1_1_5.newInstance();
		ActiveProblemsSection = Template_1_3_6_1_4_1_19376_1_5_3_1_3_6.newInstance();
		HistoryOfPastIllness = Template_1_3_6_1_4_1_19376_1_5_3_1_3_8.newInstance();
		CCD_3_5 = Template_2_16_840_1_113883_10_20_1_11.newInstance();
		ProblemConcernEntry = Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2.newInstance();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetDocument() {
		LOGGER.debug(String.format("TEST XML DOCUMENT OUTPUT:\n\n%s\n\n", CdaUtils.toXmlString(PhrExtractDocument.getDocument(), false)));
	}

	@Test
	public void testNewInstance() throws TemplateViolationException {
		Class expResult = PhrExtractDocument.class;
		PhrExtractDocument result = CDAFactory.createPHRDocument();

		// Level2Document class is the expected result
		assertEquals(expResult, result.getClass());

		// The Level2Document contains a ClinicalDocument
		assertTrue(result.getDocument() instanceof ClinicalDocument);

		// The ClinicalDocument is not null
		assertTrue(null != result.getDocument());
	}

	@Test
	public void testSchemaValidation() {
		assertTrue(PhrExtractDocument.validateSchematron(PhrExtractDocument.getDocument(), createRequiredElements));
	}

//    @Test
//    public void testFullValidation()
//    {
//        boolean validated = PhrExtractDocument.validate(createRequiredElements);
//        
//        if (!validated && detailedOutput)
//        {
//            for (ResultDetail resultDetail : PhrExtractDocument.getResultDetails())
//            {
//                System.out.println(resultDetail.getMessage());
//            }
//        }
//
//        assertTrue(validated);
//    }
	@Test
	public void testCONF_HP_1() {
		assertTrue(MedicalSpecification.testCONF_HP_1(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_2() {
		assertTrue(MedicalSpecification.testCONF_HP_2(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_4() {
		assertTrue(Level2CdaRules.testCONF_HP_4(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_6() {
		assertTrue(MedicalSpecification.testCONF_HP_6(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_7() {
		assertTrue(MedicalSpecification.testCONF_HP_7(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_8() {
		assertTrue(MedicalSpecification.testCONF_HP_8(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_9() {
		assertTrue(MedicalSpecification.testCONF_HP_9(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_16() {
		assertTrue(MedicalSpecification.testCONF_HP_16(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_17() {
		assertTrue(MedicalSpecification.testCONF_HP_17(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_21() {
		assertTrue(MedicalSpecification.testCONF_HP_21(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_22() {
		assertTrue(MedicalSpecification.testCONF_HP_22(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_23() {
		assertTrue(MedicalSpecification.testCONF_HP_23(PhrExtractDocument));
	}

	@Test
	public void testConfidentialityCode() {
		assertTrue(MedicalSpecification.testConfidentialityCode(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_24() {
		assertTrue(MedicalSpecification.testCONF_HP_24(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_30() {
		assertTrue(MedicalSpecification.testCONF_HP_30(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_31() {
		assertTrue(MedicalSpecification.testCONF_HP_31(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_32() {
		assertTrue(MedicalSpecification.testCONF_HP_32(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_33() {
		assertTrue(MedicalSpecification.testCONF_HP_33(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_37() {
		assertTrue(MedicalSpecification.testCONF_HP_37(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_38() {
		assertTrue(MedicalSpecification.testCONF_HP_38(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_39() {
		assertTrue(MedicalSpecification.testCONF_HP_39(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_40() {
		assertTrue(MedicalSpecification.testCONF_HP_40(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_41() {
		assertTrue(Level2CdaRules.testCONF_HP_41(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_42() {
		assertTrue(Level2CdaRules.testCONF_HP_42(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_43() {
		assertTrue(Level2CdaRules.testCONF_HP_43(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_50() {
		assertTrue(Level2CdaRules.testCONF_HP_50(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_51() {
		assertTrue(Level2CdaRules.testCONF_HP_51(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_52() {
		assertTrue(Level2CdaRules.testCONF_HP_52(PhrExtractDocument));
	}

	@Test
	public void testCONF_HP_58() {
		assertTrue(Level2CdaRules.testCONF_HP_58(PhrExtractDocument));
	}

	@Test
	public void testPhrDocumentOID() {
		assertTrue(XPHR.testPhrExtractSpecificationTemplateOID(PhrExtractDocument));

	}

	@Test
	public void testPhrDocumentCode() {
		assertTrue(XPHR.testPhrCode(PhrExtractDocument));
	}

	@Test
	public void testPhrDocumentCodeSystem() {
		assertTrue(XPHR.testPhrCodeSystem(PhrExtractDocument));
	}

	@Test
	public void testIHE_Problem_ObservationClassCode() {
		assertTrue(Template_1_3_6_1_4_1_19376_1_5_3_1_4_5.newInstance().testObservationClassCode(PhrExtractDocument));
	}

	@Test
	public void testIHE_Problem_ObservationMoodCode() {
		assertTrue(Template_1_3_6_1_4_1_19376_1_5_3_1_4_5.newInstance().testObservationMoodCode(PhrExtractDocument));
	}

	@Test
	public void testHealthcareProvidersAndPharmaciesOID() {
		assertTrue(Template_1_3_6_1_4_1_19376_1_5_3_1_2_3.newInstance().testHealthcareProvidersAndPharmaciesOID(PhrExtractDocument));
	}

	@Test
	public void testActiveProblemsTemplateId() {
		assertTrue(ActiveProblemsSection.testActiveProblemsTemplateId(PhrExtractDocument));
		//testSingleConformanceRule(_document, ConformanceRuleParts.ActiveProblemTemplateId, _detailedOutput);
	}

	@Test
	public void testActiveProblemCode() {
		assertTrue(ActiveProblemsSection.testActiveProblemCode(PhrExtractDocument));
	}

	@Test
	public void testActiveProblemCodeSystem() {
		assertTrue(ActiveProblemsSection.testActiveProblemCodeSystem(PhrExtractDocument));
	}

	@Test
	public void testHistoryOfPastIllnessProblemsTemplateId() {
		assertTrue(HistoryOfPastIllness.testHistoryOfPastIllnessProblemsTemplateId(PhrExtractDocument));
	}

	@Test
	public void testHistoryOfPastIllnessProblemCode() {
		assertTrue(HistoryOfPastIllness.testHistoryOfPastIllnessProblemCode(PhrExtractDocument));
	}

	@Test
	public void testHistoryOfPastIllnessProblemCodeSystem() {
		assertTrue(HistoryOfPastIllness.testHistoryOfPastIllnessCodeSystem(PhrExtractDocument));
	}

	@Test
	public void testProblemConcernEntryTemplateId() {
		assertTrue(ProblemConcernEntry.testProblemConcernEntryTemplateId(PhrExtractDocument));
	}

//    
//    public void exploratoryTesting()
//    {
//        // CREATE THE ACTS
//        Act act = new Act();
//        SubstanceAdministration substanceAdministration = new SubstanceAdministration();
//
//        // CREATE THE EFFECTIVE TIME
//        IVL<TS> ivl = new IVL<TS>();
//        ivl.setLow(TS.now());
//        ivl.setHigh(TS.now());
//        
//        // SET THE SAME EFFECTIVE TIME FOR BOTH
//        act.setEffectiveTime(ivl);
//        substanceAdministration.setEffectiveTime(new ArrayList<ISetComponent<TS>>());
//        substanceAdministration.getEffectiveTime().add(ivl);
//        
//        // PRINT THEM OUT WITH THE R1FormatterCompatibilityMode.ClinicalDocumentArchitecture formatter
//        System.out.println(CdaUtils.toXmlString(act, false));
//        System.out.println(CdaUtils.toXmlString(substanceAdministration, false));
//        
//        
//        Encounter encounter = new Encounter();
//        Observation observation = new Observation();
//        ObservationMedia observationMedia = new ObservationMedia();
//        Organizer organizer = new Organizer();
//        Procedure procedure = new Procedure();
//        RegionOfInterest regionOfInterest = new RegionOfInterest();
//        Supply supply = new Supply();
//        
//        encounter.setEffectiveTime(ivl);
//        observation.setEffectiveTime(ivl);
//        organizer.setEffectiveTime(ivl);
//        procedure.setEffectiveTime(ivl);
//
//
//        
//        supply.setEffectiveTime(new GTS(ivl));
//        
//    }
//    
//    public void exploratoryTestingFromPhrExtractDocument()
//    {
//        
//        
//        ArrayList<Component3> components = PhrExtractDocument.getDocument().getComponent().getBodyChoiceIfStructuredBody().getComponent();
//        
//        for(Component3 component : components)
//        {
//            ArrayList<Entry> entries = component.getSection().getEntry();
//            
//            for(Entry entry : entries)
//            {
//                SubstanceAdministration substance = entry.getClinicalStatementIfSubstanceAdministration();
//                
//                if(substance != null)
//                {
//                    
//                    System.out.println("Found an effectiveTime for a substance. Validated: " + substance.getEffectiveTime().get(0));
//                    System.out.println(CdaUtils.toXmlString(substance, false));
//                    System.out.println("\n");
//                    System.out.println(CdaUtils.toXmlString(substance.getEffectiveTime().get(0), false));
//                    printIResultDetail(substance.getEffectiveTime().get(0).validateEx());
//                }
//            }
//        }
//    }
//
////    @Test
//    public void everestExploratoryTesting2()
//    {
//        CdaTestUtils.findSxcm(SubstanceAdministration.class, 5);
//    }
//    
////    @Test
//    public void everestExploratoryTesting3()
//    {
//        CdaTestUtils.findSxcm(Act.class, 5);
//    }
//    
////    @Test
//    public void everestExploratoryTesting4()
//    {
//        CdaTestUtils.findSxcm(ClinicalDocument.class, 10);
//    }
//    
//    @Test
//    public void everestExploratoryTesting()
//    {
//        // Should print with a translation
//        CE ce = new CodeTranslator().translateCE(new CE<String>("Some Drug Material Code",
//                                "1.2.3.4.5",
//                                "Some Code",
//                                "version 1",
//                                "some Drug Translated Material Code DisplayName",
//                                null),"1.2.3.4.5.6");
//
//          System.out.println(CdaUtils.toXmlString(ce, createRequiredElements));
//          
//          // Should print with a nullflavor of OTH
//         CE ce2 = new CodeTranslator().translateCE(new CE<String>("Some Drug Material Code",
//                                "1.2.3.4.5",
//                                "Some Code",
//                                "version 1",
//                                "some Drug Translated Material Code DisplayName",
//                                null),"1.2.3.4.5.6.8");
//
//          System.out.println(CdaUtils.toXmlString(ce2, createRequiredElements));
//          
//          System.out.println(ce.validate());
//          printIResultDetail(ce.validateEx());
//        TEL tel = new TEL();
//        tel.setValue("http://www.foo.com");
//        CS<TelecommunicationsAddressUse> telUse = new CD<TelecommunicationsAddressUse>();
//        telUse.setCode(TelecommunicationsAddressUse.Home);
//        tel.setUse(new SET<CS<TelecommunicationsAddressUse>>());
//        tel.getUse().add(telUse);
	//tel.setValidTimeHigh(TS.now());
	//tel.setValidTimeLow(TS.now());
	//tel.setUsablePeriod(new GTS(new IVL<TS>(TS.now(), TS.now())));
	//tel.setValidTimeHigh(TS.now());
	//tel.setValidTimeLow(TS.now());
//        System.out.println("\nTEL");
//        System.out.println(CdaUtils.toXmlString(tel, true));
//        //System.out.printf("Validated: %s with a value of: %s %s\n",tel.validate(),tel.getValue(), tel.getValue().startsWith("http://"));
//        printIResultDetail(tel.validateEx());
//        
//             
//        ED ed = new ED(tel);
//        System.out.println("\nED");
//        System.out.println(CdaUtils.toXmlString(ed, false)); 
//        System.out.println("Validated: " + ed.validate());
//        printIResultDetail(ed.validateEx());
//        
//        CD cd = new CD("CD code", "1.2.3.4.5", "CD CodeSystemName", "CD CodeSystemVersion", "CD DisplayName", "CD OriginalText");
//        cd.setOriginalText(ed);
//        System.out.println("\nCD");
//        System.out.println(CdaUtils.toXmlString(cd, false));
//        System.out.println("Validated: " + cd.validate());
//        printIResultDetail(cd.validateEx());
//        
//        CE ce = new CE("CE code", "1.2.3.4.5", "CE CodeSystemName", "CE CodeSystemVersion", "CE DisplayName", "CE OriginalText");
//        ce.setOriginalText(ed);
//        System.out.println("\nCE");
//        System.out.println(CdaUtils.toXmlString(ce, false));
//        System.out.println("Validated: " + ce.validate());
//        printIResultDetail(ce.validateEx());
//        
//        CV cv = new CV("CV code", "1.2.3.4.5", "CV CodeSystemName", "CV CodeSystemVersion", "CV DisplayName", "CV OriginalText");
//        cv.setOriginalText(ed);
//        System.out.println("\nCV");
//        System.out.println(CdaUtils.toXmlString(cv, false));
//        System.out.println("Validated: " + cv.validate());
//        printIResultDetail(cv.validateEx());
//        
//        CS cs = new CS("CS code");
//        System.out.println("\nCS");
//        System.out.println(CdaUtils.toXmlString(cs, false));
//        System.out.println("Validated: " + cs.validate());
//        printIResultDetail(cs.validateEx());
//    }
	public void explorePQ() {
		PQ pq = new PQ();
		pq.setUnit("mg");
		pq.setValue(new BigDecimal(5));

		LOGGER.info(CdaUtils.toXmlString(pq, false));
		LOGGER.info(pq.validate());

	}

	public void printIResultDetail(Collection<IResultDetail> details) {
		if (details == null || details.size() == 0) {
			LOGGER.warn("No details");
		}

		for (IResultDetail detail : details) {
			LOGGER.info(detail.getMessage());
		}
	}
}
