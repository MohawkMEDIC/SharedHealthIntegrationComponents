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
package org.marc.shic.core.configuration;

import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Craig
 */
public class IheConfigurationTest
{
    public static IheConfiguration IheConfig;
    public static IheAffinityDomainConfiguration config;
    public static PersonDemographic myPerson;
    public static ArrayList<IheAffinityDomainConfiguration> affinityDomains = new ArrayList<IheAffinityDomainConfiguration>();

    public static void setUpIheConfig()
    {
        IheActorConfiguration myActor = new IheActorConfiguration(IheActorType.PAT_IDENTITY_X_REF_MGR, "name", true, "http://cr.marc-hi.ca", "localIdentification", "remoteIdentification", "clientCertificateInfo", "serverCertificateInfo");

        config = new IheAffinityDomainConfiguration("Test");
        config.setName("Test config");

        // Create a url for Actor endpoint
        URI myEndPoint = URI.create("http://cr.marc-hi.ca:2100");

        // Create Actor Identification
        IheIdentification localIdentification = new IheIdentification("MiH_OSCAR_A", "MOH_CAAT");

        // Create an Actor
        myActor.setEndPointAddress(myEndPoint);
        myActor.setActorType(IheActorType.PAT_IDENTITY_X_REF_MGR);
        myActor.setLocalIdentification(localIdentification);

        // Create Identification
        IheIdentification identification = new IheIdentification("TestId");
        identification.setFacilityName("MARC-HI");

        // Create a Configuration
//        ArrayList<IheActorConfiguration> actors = new ArrayList<IheActorConfiguration>();
//        actors.add(myActor);
        // config.setActors(actors);
        config.getActors().add(myActor);
        config.setIdentification(identification);

        // Create PatientDemographic
        myPerson = new PersonDemographic();
        DomainIdentifier personId = new DomainIdentifier();

        // Populate DomainIdentifier
        personId.setRoot("1234");
        personId.setAssigningAuthority("Mohawk");
        myPerson.addIdentifier(personId);

        // Populate Name component
        PersonName name = new PersonName();
        name.addNamePart("Schrute", PartType.Family);
        name.addNamePart("Dwight", PartType.Given);
        myPerson.addName(name);

        // Populate Date of Birth
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1986, 12, 6);

        myPerson.setDateOfBirth(dateOfBirth);

        // Set Gender
        myPerson.setGender(Gender.M);

//        affinityDomains.add(config);

        IheConfig = new IheConfiguration();
        IheConfig.setAffinityDomain(config);
//        IheConfig.setAffinityDomains(affinityDomains);
    }

    public IheConfigurationTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        setUpIheConfig();

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
     * Test of getAffinityDomain method, of class IheConfiguration.
     */
    @Test
    public void testGetAffinityDomain()
    {
        System.out.println("getAffinityDomain");
        IheAffinityDomainConfiguration expResult = config;
        IheAffinityDomainConfiguration result = IheConfig.getAffinityDomain();
        assert (result.getName().equals(expResult.getName()));
    }

    /**
     * Test of load method, of class IheConfiguration.
     */
//    @Test
//    public void testLoad()
//    {
//        System.out.println("load");
//        String filePath = "../config-v2.xml";
//        IheConfiguration expResult = IheConfig;
//
//        IheConfiguration result = IheConfiguration.load(filePath);
//        assertEquals(expResult.getAffinityDomains().size(), result.getAffinityDomains().size());
//    }

    /**
     * Test of save method, of class IheConfiguration.
     */
//    @Test
//    public void testSave()
//    {
//        System.out.println("save");
//        String filePath = "config.xml";
//
//        Boolean result = IheConfig.save(filePath);
//        assertTrue(result);
//
//    }
}
