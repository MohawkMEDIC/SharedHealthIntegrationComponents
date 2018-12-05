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
 * @author tylerg Date: Nov 12, 2013
 *
 */
package org.marc.shic.pix;

import ca.uhn.hl7v2.HL7Exception;
import java.io.File;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import static org.marc.shic.core.utils.TestUtils.createAddress;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.marc.shic.core.utils.PatientGenerator;
import static org.marc.shic.pix.PixCommunicatorTest.setUpConfig;

/**
 *
 * @author tylerg
 */
public class NistTest {

    private static final String TestAffinityDomainConfig = "../ryan_nist.xml";
    private static IheConfiguration config = new IheConfiguration();
    private static final Logger LOGGER = Logger.getLogger(PixCommunicatorTest.class);
    private static final PatientGenerator generator;
    public static PixCommunicator communicator;
//    public static final String AssigningAuthority = "NIST2010";
//    public static final String RootId = "2.16.840.1.113883.3.72.5.9.1";

    // Open PIX
    public static final String AssigningAuthority = "IHENA";
    public static final String RootId = "1.3.6.1.4.1.21367.2010.1.2.300";
    // IHE PIX Source/Consumer
    public static final PersonDemographic Patient_Test11317_A08;
    public static final PersonDemographic Patient_Test11317_A40_Prior;
    public static final PersonDemographic Patient_Test11317_A40_New;
    public static final PersonDemographic Patient_Test11317_Q23_ITI9;
    // IHE PDQ Consumer
    public static final PersonDemographic Patient_ITI21_Query_Name_AdministrativeSex;
    public static final PersonDemographic Patient_ITI21_Query_Address;
    public static final PersonDemographic Patient_ITI21_Query_Name_DateOfBirth;
    public static final PersonDemographic Patient_ITI21_Query_PatientId;

    static {
        generator = new PatientGenerator(AssigningAuthority, RootId);

        Patient_ITI21_Query_Name_AdministrativeSex = new PersonDemographic() {

            {
                setGender(Gender.M);
                addName(new PersonName("NEAL"));
            }
        };

        Patient_ITI21_Query_Address = new PersonDemographic() {

            {
            }
        };

        Patient_ITI21_Query_Name_DateOfBirth = new PersonDemographic() {

            {
                addName(new PersonName("NEAL"));
                // 0 based.
                setDateOfBirth(1978, 9, 13);
            }
        };

        Patient_ITI21_Query_PatientId = new PersonDemographic() {

            {
            }
        };

        Patient_Test11317_A08 = new PersonDemographic() {

            {
                setGender(Gender.F);
                addIdentifier(new DomainIdentifier(RootId, "PIX", AssigningAuthority));
                addName(new PersonName("TERI", "TAU"));
                addAddress(createAddress(AddressUse.Home, "202 KEN HABOR", "NEW YORK CITY", "NY", "61000"));
                // 0 based for month.
                setDateOfBirth(1978, 4, 10);
            }
        };

        Patient_Test11317_A40_Prior = new PersonDemographic() {

            {
                setGender(Gender.F);
                addIdentifier(new DomainIdentifier(RootId, "PIXW", AssigningAuthority));
                addName(new PersonName("MARY", "WASHINGTON"));
                addAddress(createAddress(AddressUse.Home, "100 JORIE BLVD", "CHICAGO", "IL", "60523"));
                // 0 based for month.
                setDateOfBirth(1977, 11, 8);
            }
        };

        Patient_Test11317_A40_New = new PersonDemographic() {

            {
                setGender(Gender.F);
                addIdentifier(new DomainIdentifier(RootId, "PIXL", AssigningAuthority));
                addName(new PersonName("MARY", "LINCOLN"));
                addAddress(createAddress(AddressUse.Home, "100 JORIE BLVD", "CHICAGO", "IL", "60523"));
                // 0 based for month.
                setDateOfBirth(1977, 11, 8);
            }
        };

        Patient_Test11317_Q23_ITI9 = new PersonDemographic() {

            {
                setGender(Gender.F);
                addIdentifier(new DomainIdentifier(RootId, "PIXL1", AssigningAuthority));
                addName(new PersonName("MARY", "LINCOLN"));
                addAddress(createAddress(AddressUse.Home, "100 JORIE BLVD", "CHICAGO", "IL", "60523"));
                // 0 based for month.
                setDateOfBirth(1977, 11, 8);
            }
        };

        AtnaCommunicator.debug = true;
    }

    public static void setUpConfig() {
        try {
            // Load the config used for testing.
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("OSCAR", "McMaster"));
            config.setKeyStore(new JKSStoreInformation("C:\\keystore.jks", "changeit"));
            config.setTrustStore(new JKSStoreInformation("C:\\truststore.jks", "changeit"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
        } catch (IheConfigurationException ex) {
            LOGGER.error(ex);
        }
    }

    @BeforeClass
    public static void setUpClass() {
        setUpConfig();
        try {
            communicator = new PixCommunicator(config);
        } catch (ActorNotFoundException ex) {
            LOGGER.info(ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        //  AtnaCommunicator.getAuditMessageQueue().awaitShutdown(115);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test11317_ITI8_A08() throws HL7Exception, CommunicationsException, PixApplicationException {
        communicator.update(Patient_Test11317_A08);
    }

    @Test
    public void testRegister_a04() throws CommunicationsException, PixApplicationException {
        PersonDemographic generatedPatient = generator.generatePatient();

        communicator.register(generatedPatient);
    }

    @Test
    public void test11317_ITI8_A40() throws CommunicationsException, PixApplicationException {
        communicator.merge(Patient_Test11317_A40_New, Patient_Test11317_A40_Prior);
    }

    @Test
    public void test_q23_iti9_query() throws CommunicationsException, PixApplicationException {
        communicator.resolveIdentifiers(Patient_Test11317_Q23_ITI9);
    }

    @Test
    public void iti21_test_q22() throws CommunicationsException, PixApplicationException {
        communicator.findCandidatesQuery(Patient_Test11317_A08);
    }

    @Test
    public void testITI21_PatientName_AdministrativeSex() throws CommunicationsException, PixApplicationException {
        communicator.findCandidatesQuery(Patient_ITI21_Query_Name_AdministrativeSex);
    }

    @Test
    public void testITI21_PatientName_DateOfBirth() throws CommunicationsException, PixApplicationException {
        communicator.findCandidatesQuery(Patient_ITI21_Query_Name_DateOfBirth);
    }
}
