/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
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
 * 
 * Date: October 29, 2013
 * 
 */
package org.marc.shic.cda;

import org.marc.shic.cda.level1.ScannedDocument;
import org.apache.log4j.Logger;
import org.marc.shic.cda.utils.CdaTestUtils;
import org.marc.shic.cda.utils.CdaUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.cda.exceptions.TemplateViolationException;

/**
 *
 * @author Moe
 */
public class ScannedDocumentTest {
    
    private static boolean detailedOutput = true;
    private static ScannedDocument scannedDocument;
    private static final Logger LOGGER = Logger.getLogger(ScannedDocumentTest.class);
    
    public ScannedDocumentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        scannedDocument = CdaTestUtils.createScannedDocument();
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

//    /**
//     * Test of addConfidentiality method, of class ScannedDocument.
//     */
//    @Test
//    public void testAddConfidentiality() {
//        System.out.println("addConfidentiality");
//        CodeValue confidentiality = null;
//        ScannedDocument instance = new ScannedDocument();
//        instance.addConfidentiality(confidentiality);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setTypeCode method, of class ScannedDocument.
//     */
//    @Test
//    public void testSetTypeCode() {
//        System.out.println("setTypeCode");
//        CodeValue type = null;
//        ScannedDocument instance = new ScannedDocument();
//        instance.setTypeCode(type);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
    /**
     * Tests the creation of a scanned document XDS-SD
     */
    @Test
    public void testCreateScannedDocument() throws TemplateViolationException {
        LOGGER.info(CdaUtils.toXmlString(scannedDocument.getDocument(), false));

        assertTrue(scannedDocument.validateStandard());
    }
    
    /**
     * *************************************************************************
     * XDS-SD Conformance Rules
     * ************************************************************************
     */
    @Test
    public void testValidation() {
        assertTrue(scannedDocument.validateStandard());
    }
}