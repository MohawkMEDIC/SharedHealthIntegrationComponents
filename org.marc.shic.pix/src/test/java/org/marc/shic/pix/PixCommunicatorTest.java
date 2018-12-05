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
 * Date: Sept 9 2013
 *
 */
package org.marc.shic.pix;

import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.utils.TestUtils;
import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.marc.shic.core.utils.PatientGenerator;

/**
 *
 * @author Craig
 */
public class PixCommunicatorTest
{

    public static String AssigningAuthority = "MiH_OSCAR_A";
    public static String RootId = "1.3.6.1.4.1.33349.3.1.3.201203.2.0.0";
    private static final String TestAffinityDomainConfig = "../demo-secure.xml";
    private static IheConfiguration config = new IheConfiguration();
    public static PixCommunicator communicator;
    private static final Logger LOGGER = Logger.getLogger(PixCommunicatorTest.class);
    private static final PatientGenerator generator = new PatientGenerator(TestUtils.AssigningAuthority, TestUtils.RootId);

    public static void setUpConfig()
    {
        try
        {
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("LOCAL_APPLICATION", "LOCAL_FACILITY"));
            config.setKeyStore(new JKSStoreInformation("C:\\ihe.keystore.jks", "changeit"));
            config.setTrustStore(new JKSStoreInformation("C:\\ihe.truststore.jks", "changeit"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
        } catch (IheConfigurationException ex)
        {
            LOGGER.info(ex);
        }
    }

    public PixCommunicatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        setUpConfig();
        try
        {
            communicator = new PixCommunicator(config);
        } catch (ActorNotFoundException ex)
        {
            LOGGER.info(ex);
        }
    }

    @AfterClass
    public static void tearDownClass()
    {
        //communicator.close();
    }

    @Before
    public void setUp()
    {
        System.setProperty("javax.net.debug", "all");
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testRegisterAndGetCorrespondingIdentifiers() throws PixApplicationException, CommunicationsException
    {
        PersonDemographic patient = generator.generatePatient();

        LOGGER.info("testRegisterAndGetCorrespondingIdentifiers");

        LOGGER.info("Register");
        communicator.register(patient);

        LOGGER.info("GetCorrespondingIdentifiers");
        communicator.getCorrespondingIdentifiers(patient);
    }

    @Test
    public void testRegisterAndFindCandidatesQueryByName() throws PixApplicationException, CommunicationsException
    {
        PersonDemographic patient = generator.generatePatient();

        LOGGER.info("testRegisterAndFindCandidatesQuery");

        LOGGER.info("Register");
        communicator.register(patient);

        LOGGER.info("FindCandidatesQuery");

        PersonName name = new PersonName(patient.getNames().get(0).getNameComponent(PartType.Family).getValue());
        PersonDemographic queryParams = new PersonDemographic();
        queryParams.addName(name);

        List<PersonDemographic> foundPatients = communicator.findCandidatesQuery(queryParams);
        LOGGER.info(String.format("Found %d patients.", foundPatients.size()));
    }

    @Test
    public void testRegisterAndFindCandidatesQueryById() throws PixApplicationException, CommunicationsException
    {
        PersonDemographic patient = generator.generatePatient();

        LOGGER.info("testRegisterAndFindCandidatesQueryById");

        LOGGER.info("Register");
        communicator.register(patient);

        LOGGER.info("FindCandidatesQuery");

        PersonDemographic queryParams = new PersonDemographic();
        queryParams.addIdentifier(patient.getIdentifiers().get(0));

        List<PersonDemographic> foundPatients = communicator.findCandidatesQuery(queryParams);
        LOGGER.info(String.format("Found %d patients.", foundPatients.size()));
    }

    @Test
    public void testFindCandidatesQuery_Gender() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("testFindCandidatesQuery_Gender");

        PersonDemographic queryParams = new PersonDemographic();
        queryParams.setGender(Gender.M);

        List<PersonDemographic> foundPatients = communicator.findCandidatesQuery(queryParams);
        LOGGER.info(String.format("Found %d patients.", foundPatients.size()));
    }

    /**
     * Test of register method, of class PixCommunicator.
     */
    @Test
    public void testRegister() throws Exception
    {
        LOGGER.info("testRegister");
        PersonDemographic patient = generator.generatePatient();
        communicator.register(patient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolveIdentifiers_NullPatient() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("resolveIdentifiers_NullPatient");
        communicator.resolveIdentifiers(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolveIdentifiers_NoPatientIdentifiers() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("resolveIdentifiers_NoPatientIdentifiers");
        communicator.resolveIdentifiers(new PersonDemographic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_NoPatientIdentifiers() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("resolveIdentifiers_NoPatientIdentifiers");
        communicator.update(new PersonDemographic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_NullPatient() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("update_NullPatient");
        communicator.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fillInDetails_NullPatient() throws CommunicationsException, PixApplicationException
    {
        LOGGER.info("fillInDetails_NullPatient");
        communicator.findCandidatesQuery(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_NullOriginalPatient() throws PixApplicationException
    {
        LOGGER.info("merge_NullOriginalPatient");
        PersonDemographic priorPatient = generator.generatePatient();

        try
        {
            communicator.merge(null, priorPatient);

        } catch (CommunicationsException ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_NullPriorPatient() throws PixApplicationException
    {
        LOGGER.info("merge_NullPriorPatient");
        PersonDemographic originalPatient = generator.generatePatient();

        try
        {
            communicator.merge(originalPatient, null);
        } catch (CommunicationsException ex)
        {
            fail(ex.getMessage());
        }
    }

//    @Test
    public void merge_validRequest() throws PixApplicationException
    {
        LOGGER.info("merge_validRequest");

        PersonDemographic originalPatient = generator.generatePatient();
        PersonDemographic priorPatient = generator.generatePatient();

        try
        {
//            communicator.register(originalPatient);
//            communicator.register(priorPatient);

//            originalPatient = communicator.resolveIdentifiers(originalPatient);
//            priorPatient = communicator.resolveIdentifiers(priorPatient);

            communicator.merge(originalPatient, priorPatient);
            //    assertTrue("The merge should succeed.", communicator.merge(originalPatient, priorPatient));
        } catch (CommunicationsException ex)
        {
            //  fail(ex.getMessage());
        }

    }

    // @Test
    public void update_validRequest() throws PixApplicationException
    {
        PersonDemographic person = generator.generatePatient();
        person.getNames().clear();

        person.addName(new PersonName("TERI", "TAU"));

        Calendar dob = Calendar.getInstance();
        dob.set(1987, 5, 10);

        person.setDateOfBirth(dob);

//        person.getIdentifiers().get(0).setExtension("PIX");


        try
        {
            communicator.update(person);
        } catch (CommunicationsException ex)
        {
            //  fail(ex.getMessage());
        }
    }
}
