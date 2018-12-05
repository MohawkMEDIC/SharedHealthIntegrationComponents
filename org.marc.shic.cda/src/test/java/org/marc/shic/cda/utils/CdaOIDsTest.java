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
package org.marc.shic.cda.utils;

import org.marc.shic.cda.templateRules.TemplateRuleID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author brownp
 */
public class CdaOIDsTest {
    
    public CdaOIDsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test that the expected template is found in the enum
     */
    @Test
    public void testValueOf() {
        String expectedTemplateName = "Medications";
        String expectedOID = "1.3.6.1.4.1.19376.1.5.3.1.4.7";

        assertEquals(expectedTemplateName, TemplateRuleID.PCC_Medication.getTemplateName());
        assertEquals(expectedOID, TemplateRuleID.PCC_Medication.getOid());
    }

    /**
    * Test that the expected template is NOT found in the enum when compared to an unexpected value
    */
    @Test
    public void testToFail() {
        String expectedTemplateName = "Medications";
        String expectedOID = "1.3.6.1.4.1.19376.1.5.3.1.4.7";

        assertThat(expectedTemplateName, not(equalTo(TemplateRuleID.PCC_HistoryOfPastIllnessSection.getTemplateName())));
        assertThat(expectedOID, not(equalTo(TemplateRuleID.PCC_HistoryOfPastIllnessSection.toString())));
    }
}