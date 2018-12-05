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
 * Date: 03-Nov-2013
 *
 */
package org.marc.shic.core.utils;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;

/**
 *
 * @author Moe
 */
public class ConfigurationUtilityTest {
    
    public ConfigurationUtilityTest() {
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
     * Test of parseConfiguration method, of class ConfigurationUtility.
     */
    @Test
    public void testParseConfiguration() throws Exception {
        System.out.println("parseConfiguration");
        File file = new File("../oht.xml");
        IheAffinityDomainConfiguration result = ConfigurationUtility.parseConfiguration(file);
        
        assertEquals("1.3.6.1.4.1.33349.3.1.2.2.0.0", result.getOid());
        assertEquals("OpenHealthTools", result.getName());
        assertEquals(6, result.getActors().size());
        assertEquals(IheAffinityDomainPermission.ReadWrite, result.getPermission());
    }
}