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
package org.marc.shic.core;

import org.marc.shic.core.AddressComponent;
import org.marc.shic.core.AddressPartType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Paul
 */
public class AddressComponentTest
{
    public AddressComponentTest()
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
     * Test that a set value is the same as the get value when set as non null
     *
     */
    @Test
    public void testGetValueGoodValue()
    {
        String expResult = "Test Value";

        AddressComponent instance = new AddressComponent();
        instance.setValue(expResult);

        assertEquals(expResult, instance.getValue());

    }

    /**
     * Test that a set value is the same as the get value when set as null
     *
     */
    @Test
    public void testGetValueNullValue()
    {
        String expResult = null;

        AddressComponent instance = new AddressComponent();
        instance.setValue(expResult);

        assertEquals(expResult, instance.getValue());

    }

    /**
     * Test that two different values are not considered the same
     *
     */
    @Test
    public void testGetValueBadValue()
    {
        String expResult = "Test Value";

        AddressComponent instance = new AddressComponent();
        instance.setValue("Another Value");

        assertThat(expResult, not(equalTo(instance.getValue())));

    }

    /**
     * Test of getType method, of class AddressComponent.
     */
    @Test
    public void testGetType()
    {
        AddressComponent instance = new AddressComponent();
        AddressPartType expResult = null;
        expResult = AddressPartType.AddressLine;
        instance.setType(expResult);


        AddressPartType result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test that a valid AddressComponent is not equal to null
     */
    @Test
    public void testCompareToNullType()
    {
        AddressComponent instance = new AddressComponent();
        AddressPartType expResult = null;
        expResult = AddressPartType.AddressLine;

        AddressPartType result = null;
        assertThat(expResult, not(equalTo(instance.getType())));
    }

    /**
     * Test that two different AddressPartTypes are not equal
     */
    @Test
    public void testNotSameType()
    {
        AddressPartType expResult = null;
        expResult = AddressPartType.AddressLine;

        AddressPartType result = null;
        result = AddressPartType.City;

        assertNotSame(expResult, result);
    }
}
