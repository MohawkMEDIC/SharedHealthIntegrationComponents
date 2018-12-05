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

import org.marc.shic.cda.level1.BppcDocument;
import org.marc.shic.cda.utils.CdaTestUtils;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.templateRules.ConformanceRuleParts;
import org.marc.shic.cda.templateRules.Template_1_3_6_1_4_1_19376_1_5_3_1_1_1;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import org.marc.shic.cda.exceptions.TemplateViolationException;

/**
 *
 * @author ibrahimm
 */
public class BppcDocumentTest {

    private static boolean detailedOutput = true;
    private static BppcDocument structuredBppcDocument;
    private static BppcDocument scannedBppcDocument;
    private static Template_1_3_6_1_4_1_19376_1_5_3_1_1_1 MedicalSpecification = Template_1_3_6_1_4_1_19376_1_5_3_1_1_1.newInstance();
    private static final Logger LOGGER = Logger.getLogger(BppcDocumentTest.class.getName());

    public BppcDocumentTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        structuredBppcDocument = CdaTestUtils.createStructuredBppcDocument();
        scannedBppcDocument = CdaTestUtils.createScannedBppcDocument();
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

    /**
     * Test of addConsentPolicy method, of class BppcDocument.
     */
//    @Test
//    public void testAddConsentPolicy() {
//        LOGGER.info("addConsentPolicy");
//        DomainIdentifier eventId = null;
//        CodeValue consent = null;
//        Calendar effectiveTime = null;
//        BppcDocument instance = new BppcDocument();
//        instance.addConsentPolicy(eventId, consent, effectiveTime);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Tests the creation of a structured (non-scanned) BPPC document
     */
    @Test
    public void testCreateStructuredBppcDocument() throws TemplateViolationException {
        LOGGER.info(CdaUtils.toXmlString(structuredBppcDocument.getDocument(), false));

        assertTrue(structuredBppcDocument.validateStandard());
    }

    /**
     * Tests the creation of a non-structured (scanned) BPPC document
     */
    @Test
    public void testCreateScannedBppcDocument() throws TemplateViolationException {
        LOGGER.info(CdaUtils.toXmlString(scannedBppcDocument.getDocument(), false));

        assertTrue(scannedBppcDocument.validateStandard());
    }

    /**
     * *************************************************************************
     * BPPC Conformance Rules (Structured)
     * ************************************************************************
     */
    @Test
    public void testStructuredFullValidation() {
        assertTrue(structuredBppcDocument.validateStandard());
    }

    @Test
    public void validateStructuredCONF_HP_1() {
        assertTrue(MedicalSpecification.testCONF_HP_1(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_2() {
        assertTrue(MedicalSpecification.testCONF_HP_2(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_3() {
        assertTrue(MedicalSpecification.testCONF_HP_3(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_6() {
        assertTrue(MedicalSpecification.testCONF_HP_6(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_7() {
        assertTrue(MedicalSpecification.testCONF_HP_7(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_8() {
        assertTrue(MedicalSpecification.testCONF_HP_8(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_16() {
        assertTrue(MedicalSpecification.testCONF_HP_16(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_17() {
        assertTrue(MedicalSpecification.testCONF_HP_17(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_21() {
        assertTrue(MedicalSpecification.testCONF_HP_21(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_22() {
        assertTrue(MedicalSpecification.testCONF_HP_22(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_23() {
        assertTrue(MedicalSpecification.testCONF_HP_23(structuredBppcDocument));
    }

    @Test
    public void validateStructuredConfidentialityCode() {
        assertTrue(MedicalSpecification.testConfidentialityCode(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_24() {
        assertTrue(MedicalSpecification.testCONF_HP_24(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_30() {
        assertTrue(MedicalSpecification.testCONF_HP_30(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_31() {
        assertTrue(MedicalSpecification.testCONF_HP_31(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_32() {
        assertTrue(MedicalSpecification.testCONF_HP_32(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_33() {
        assertTrue(MedicalSpecification.testCONF_HP_33(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_37() {
        assertTrue(MedicalSpecification.testCONF_HP_37(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_38() {
        assertTrue(MedicalSpecification.testCONF_HP_38(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_39() {
        assertTrue(MedicalSpecification.testCONF_HP_39(structuredBppcDocument));
    }

    @Test
    public void validateStructuredCONF_HP_40() {
        assertTrue(MedicalSpecification.testCONF_HP_40(structuredBppcDocument));
    }

    /**
     * *************************************************************************
     * BPPC Conformance Rules (Scanned)
     * ************************************************************************
     */
    @Test
    public void testScannedFullValidation() {
        assertTrue(scannedBppcDocument.validateStandard());
    }

    @Test
    public void validateScannedCONF_HP_1() {
        assertTrue(MedicalSpecification.testCONF_HP_1(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_2() {
        assertTrue(MedicalSpecification.testCONF_HP_2(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_3() {
        assertTrue(MedicalSpecification.testCONF_HP_3(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_6() {
        assertTrue(MedicalSpecification.testCONF_HP_6(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_7() {
        assertTrue(MedicalSpecification.testCONF_HP_7(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_8() {
        assertTrue(MedicalSpecification.testCONF_HP_8(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_16() {
        assertTrue(MedicalSpecification.testCONF_HP_16(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_17() {
        assertTrue(MedicalSpecification.testCONF_HP_17(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_21() {
        assertTrue(MedicalSpecification.testCONF_HP_21(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_22() {
        assertTrue(MedicalSpecification.testCONF_HP_22(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_23() {
        assertTrue(MedicalSpecification.testCONF_HP_23(scannedBppcDocument));
    }

    @Test
    public void validateScannedConfidentialityCode() {
        assertTrue(MedicalSpecification.testConfidentialityCode(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_24() {
        assertTrue(MedicalSpecification.testCONF_HP_24(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_30() {
        assertTrue(MedicalSpecification.testCONF_HP_30(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_31() {
        assertTrue(MedicalSpecification.testCONF_HP_31(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_32() {
        assertTrue(MedicalSpecification.testCONF_HP_32(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_33() {
        assertTrue(MedicalSpecification.testCONF_HP_33(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_37() {
        assertTrue(MedicalSpecification.testCONF_HP_37(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_38() {
        assertTrue(MedicalSpecification.testCONF_HP_38(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_39() {
        assertTrue(MedicalSpecification.testCONF_HP_39(scannedBppcDocument));
    }

    @Test
    public void validateScannedCONF_HP_40() {
        assertTrue(MedicalSpecification.testCONF_HP_40(scannedBppcDocument));
    }
}
