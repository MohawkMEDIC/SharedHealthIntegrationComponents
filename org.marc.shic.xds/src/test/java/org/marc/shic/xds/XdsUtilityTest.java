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
 * @author Mohamed Ibrahim
 * Date: 14-Nov-2013
 *
 */
package org.marc.shic.xds;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ibrahimm
 */
public class XdsUtilityTest {
    
    public XdsUtilityTest() {
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
     * Test of generateProvideAndRegisterRequest method, of class XdsUtility.
     */
    @Test
    public void testGenerateRandomOid() throws Exception {
        System.out.println("generateRandomOid");
        String expResult = "2.25.329800735698586629295641978511506172918";
        String result = XdsUtility.generateRandomOid("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        
        assertEquals(expResult, result);
    }
}