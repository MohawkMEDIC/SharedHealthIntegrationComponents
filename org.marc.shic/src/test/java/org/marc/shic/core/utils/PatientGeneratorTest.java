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
 * @author tylerg
 * Date: Nov 4, 2013
 *
 */
package org.marc.shic.core.utils;

import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;

/**
 *
 * @author tylerg
 */
public class PatientGeneratorTest
{
    
    public PatientGeneratorTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of generateId method, of class PatientGenerator.
     */
    @Test
    public void testGenerateId()
    {
        System.out.println("generateId");
        PatientGenerator instance = new PatientGenerator(TestUtils.AssigningAuthority, TestUtils.RootId);
        DomainIdentifier result = instance.generateId();
        
        assertTrue("Generated id must not be null", result != null);
        assertTrue("Generated assigning authority is not set to the provided test utils assigning authority.", result.getAssigningAuthority().equals(TestUtils.AssigningAuthority));
        assertTrue("Generated root id is not set to the provided test utils root id.", result.getRoot().equals(TestUtils.RootId));
        assertTrue("Generated assigning authority is not set to the provided test utils assigning authority.", result.getAssigningAuthority().equals(result.getAssigningAuthority()));
    }

    /**
     * Test of generatePatient method, of class PatientGenerator.
     */
    @Test
    public void testGeneratePatient()
    {
        System.out.println("generatePatient");
        PatientGenerator instance = new PatientGenerator(TestUtils.AssigningAuthority, TestUtils.RootId);
        PersonDemographic result = instance.generatePatient();

        assertTrue("Generated patient must not be null.", result != null);
        assertTrue("Patient must have at least 1 address.", result.getAddresses().size() > 0);
        assertTrue("Patient must have at least 1 name.", result.getNames().size() > 0);
        assertTrue("Date of birth must not be null.", result.getDateOfBirth() != null);
        assertTrue("Patient must have at least 1 id.", result.getIdentifiers().size() > 0);
    }

    /**
     * Test of generateAddress method, of class PatientGenerator.
     */
 //   @Test
    public void testGenerateAddress()
    {
        System.out.println("generateAddress");
        PatientGenerator instance = null;
        PersonAddress expResult = null;
        PersonAddress result = instance.generateCanadianAddress();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateGender method, of class PatientGenerator.
     */
  //  @Test
    public void testGenerateGender()
    {
        System.out.println("generateGender");
        Gender expResult = null;
        Gender result = PatientGenerator.generateGender();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDate method, of class PatientGenerator.
     */
  //  @Test
    public void testGenerateDate()
    {
        System.out.println("generateDate");
        Calendar expResult = null;
        Calendar result = PatientGenerator.generateDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateBasicGender method, of class PatientGenerator.
     */
    @Test
    public void testGenerateBasicGender()
    {
        System.out.println("generateBasicGender");
        Gender result = PatientGenerator.generateBasicGender();
        assertTrue("Gender must be either F or M.", result == Gender.F || result == Gender.M);
    }
}