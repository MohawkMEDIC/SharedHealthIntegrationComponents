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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.PersonName;

/**
 *
 * @author tylerg
 */
public class NameGeneratorTest
{

    public NameGeneratorTest()
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

    private void assertValidName(String name)
    {
        assertTrue("Name should not be null.", name != null);
        // A name of length 1 will be assumed valid.
        assertTrue("Name must have at least a length of 1.", name.length() != 0);
    }

    private void assertValidName(PersonName name)
    {
        assertTrue("A full generated name must have 3 parts.", name.getParts().size() == 3);

        for (NameComponent part : name.getParts())
        {
            assertValidName(part.getValue());
        }
    }

    /**
     * Test of generateName method, of class NameGenerator.
     */
    @Test
    public void testGenerateName()
    {
        System.out.println("generateName");
        String result = NameGenerator.generateName();

        assertValidName(result);
    }

    /**
     * Test of generateFullName method, of class NameGenerator.
     */
    @Test
    public void testGenerateFullName()
    {
        System.out.println("generateFullName");
        PersonName result = NameGenerator.generateFullName();

        assertValidName(result);
    }

    /**
     * Test of generatePersonNames method, of class NameGenerator.
     */
    @Test
    public void testGeneratePersonNames()
    {
        System.out.println("generatePersonNames");
        int amount = 3;
        List<PersonName> result = NameGenerator.generatePersonNames(amount);

        assertTrue(amount + " names should be generated.", result.size() == amount);

        for (PersonName name : result)
        {
            assertValidName(name);
        }
    }

    /**
     * Test of generateNames method, of class NameGenerator.
     */
    @Test
    public void testGenerateNames()
    {
        System.out.println("generateNames");
        int amount = 3;
        List<String> result = NameGenerator.generateNames(amount);

        assertTrue(amount + " names should be generated.", result.size() == amount);

        for (String name : result)
        {
            assertValidName(name);
        }
    }

    /**
     * Test of capitalize method, of class NameGenerator.
     */
    @Test
    public void testCapitalize()
    {
        System.out.println("capitalize");
        String name = "name";
        String expResult = "Name";
        String result = NameGenerator.capitalize(name);
        assertEquals(expResult, result);
    }
}