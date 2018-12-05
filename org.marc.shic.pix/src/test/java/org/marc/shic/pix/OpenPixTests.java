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
import java.util.List;
import java.util.UUID;
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
import org.marc.shic.core.AddressPartType;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.PartType;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.marc.shic.core.utils.PatientGenerator;
import static org.marc.shic.core.utils.TestUtils.createAddress;
import static org.marc.shic.pix.NistTest.AssigningAuthority;
import static org.marc.shic.pix.NistTest.RootId;
import static org.marc.shic.pix.PixCommunicatorTest.setUpConfig;

/**
 *
 * @author tylerg
 */
public class OpenPixTests {

    private static final String TestAffinityDomainConfig = "../ryan_nist.xml";
    private static IheConfiguration config = new IheConfiguration();
    private static final Logger LOGGER = Logger.getLogger(OpenPixTests.class);
    private static final PatientGenerator generator;
    public static PixCommunicator communicator;
    
    
    // OLD GLOBAL ID THAT REQUIRED MANUAL ENTRY, DO NOT USE THIS ANYMORE.
    public static final String AssigningAuthority_IHENA = "IHENA";
    public static final String RootId_IHENA = "1.3.6.1.4.1.21367.2010.1.2.300";

    // CURRENT GLOBAL ID + ASSIGNING AUTHORITY THAT IS BEING AUTO-GENERATED.
    public static final String Global_AssigningAuthority = "2.16.840.1.113883.4.357";
    public static final String Global_RootId = "2.16.840.1.113883.4.357";
    
    // data created by Mo + Garret
    public static final String AssigningAuthority = "HIMSS2005";
    public static final String RootId = "1.3.6.1.4.1.21367.2005.1.1";

    // Safe to remove?
    public static final String OutsideLocal_AssigningAuthority = "SIAD";
    public static final String OutsideLocalRootId = "1.3.6.1.4.1.21367.2010.1.2.320";

    // throw away person demographics while Ryan was learning to PIX. safe to remove?
    public static final PersonDemographic Joeyoeyoey;
    public static final PersonDemographic RyanF;
    public static final PersonDemographic JoesephF;
    
    // Person Demographics that are working against auto-generated global ids.s
    public static final PersonDemographic JerrySienfeld;
    public static final PersonDemographic JimSienfeld;
    public static final PersonDemographic HarrySienfeld;
    public static final PersonDemographic Hack1Sienfeld;
    public static final PersonDemographic Hack2Sienfeld;

    private static final PersonDemographic TestPatient;

    static {
        generator = new PatientGenerator(AssigningAuthority, RootId);

        TestPatient = generator.generatePatient();

        // do we still need this persona demographic? 
        Joeyoeyoey = new PersonDemographic() {
            {
                setGender(Gender.M);
                //     addIdentifier(new DomainIdentifier(RootId, "hello", AssigningAuthority));
                addIdentifier(new DomainIdentifier(RootId, "1A", AssigningAuthority));
                addName(new PersonName("JOEEEEEY", "FLETCHER"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 FLETCHER HABOR", "NEW YORK CITY", "NY", "61000"));
                // 0 based for month.
                setDateOfBirth(1955, 8, 10);
            }
        };

        // do we still need this person demographic?
        RyanF = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(RootId_IHENA, "Z9123812391", AssigningAuthority_IHENA));
                addName(new PersonName("RYAN", "FLETCHER"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 FLETCHER HABOR", "NEW YORK CITY", "NY", "61000"));
                // 0 based for month.
                setDateOfBirth(1955, 8, 10);
            }
        };

        // pix-feed to xds only works when we manually seed
        // the global id via a seperate ADT04
        JoesephF = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(Global_RootId, "Z9234RTEDe", Global_AssigningAuthority));
                addName(new PersonName("JOESEPHOEY", "FLETCHER"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 CRAIG HABOR", "NEW YORK CITY", "NY", "61000"));
                // 0 based for month.
                setDateOfBirth(1956, 12, 12);
            }
        };

        // first person demographic to register against auto-generated globalId. Use sienfeld as last name for xds document registeration tests.
        JerrySienfeld = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(RootId, "ZFD2312511", AssigningAuthority));
                addName(new PersonName("JERRY", "SIENFELD"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 TYLER HABOR", "NEW YORK CITY", "NY", "61000"));
                // 0 based for month.
                setDateOfBirth(1967, 12, 12);
            }
        };
        
        // test patient for register xds docs against
        HarrySienfeld = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(RootId, "14521", AssigningAuthority));
                addName(new PersonName("HARRY", "SIENFELD"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 MO HABOR", "HAMILTON", "ON", "62000"));
                // 0 based for month.
                setDateOfBirth(1969, 12, 12);
            }
        };
        
        // test patient for register xds docs against
        JimSienfeld = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(RootId, "89382", AssigningAuthority));
                addName(new PersonName("JIM", "SIENFELD"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 PAUL HABOR", "HAMILTON", "ON", "63000"));
                // 0 based for month.
                setDateOfBirth(1969, 12, 12);
            }
        };
        
        // test patient for register xds docs against
        Hack1Sienfeld = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(RootId, "ZOLRT14315DV1", AssigningAuthority));
                addName(new PersonName("HACK3", "SIENFELD"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 VUK HABOR", "HAMILTON", "ON", "64000"));
                // 0 based for month.
                setDateOfBirth(1968, 12, 12);
            }
        };
        
        Hack2Sienfeld = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(Global_RootId, "123GLOBAL", Global_AssigningAuthority));
                addName(new PersonName("HACK3", "SIENFELD"));
                addAddress(createAddress(AddressUse.Home, "894998956516464665464 VUK HABOR", "HAMILTON", "ON", "64000"));
                // 0 based for month.
                setDateOfBirth(1968, 12, 12);
            }
        };

        AtnaCommunicator.debug = true;
    }
    
        // this user is working against the auto-generated global id
    @Test
    public void testRegister_Morgan() throws CommunicationsException, PixApplicationException {
//        communicator.register(Joeyoeyoey);
//        communicator.register(Hack2Sienfeld);
//        communicator.register(Hack1Sienfeld);
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

    // Tests the hacked approached to global ids.
    // if the global id does not exist, and the patient has no local ids
    // from anywhere else. The global id should be created first via register
    // followed by registering our local id. 
    @Test
    public void testOpenHackCase1() throws PixApplicationException, CommunicationsException {
        
        // paul brown wrote this line to prove a point to the trolls in chicago.
        
        // Create a global id
        PersonDemographic GlobalPatient = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(Global_RootId, "639f8372-651a-43fb-80f0-eacb688335d8", Global_AssigningAuthority));
                addName(new PersonName("PATIENT2", "CASE1"));
                addAddress(createAddress(AddressUse.Home, "123 Fake Street", "Hamilton", "ON", "unit 23"));
                // 0 based for month.
                setDateOfBirth(1968, 12, 12);
            }
        };

        PersonDemographic LocalPatient = new PersonDemographic() {
                {
                    setGender(Gender.M);
                    addIdentifier(new DomainIdentifier(RootId, "44", AssigningAuthority));
                    addName(new PersonName("PATIENT2", "CASE1"));
                    addAddress(createAddress(AddressUse.Home, "123 Fake Street", "Hamilton", "ON", "unit 23"));
                    setDateOfBirth(1968, 12, 12);
                }
        };
        
       Boolean result = communicator.getCorrespondingIdentifiersQAK2(LocalPatient);
       
       if (!result){
           communicator.register(GlobalPatient);
       }
       
       communicator.register(LocalPatient);
       
       LOGGER.info("Message result is: " + result);
       
//       PersonDemographic responsePatient = communicator.getCorrespondingIdentifiers(TestPatient);
       
//     PersonDemographic responsePatient = communicator.getCorrespondingIdentifiers(OurLocal); 
//     responsePatient = communicator.resolveIdentifiers(OurLocal);
//     List<PersonDemographic> responsePatientList = communicator.findCandidatesQuery(OurLocal);
        
    }
    
    // Tests the hacked approached to global ids.
    // if the global id exists and the patient has a local id elsewhere
    // just register our local id.
    @Test
    public void testOpenHackCase2() throws PixApplicationException, CommunicationsException {
        
        // Create a global id
        PersonDemographic GlobalPatient = new PersonDemographic() {
            {
                setGender(Gender.M);
                addIdentifier(new DomainIdentifier(Global_RootId, "639f8372-651a-43fb-80f0-eacb688335d8", Global_AssigningAuthority));
                addName(new PersonName("PATIENT3", "CASE2"));
                addAddress(createAddress(AddressUse.Home, "123 Fake Street", "Hamilton", "ON", "unit 23"));
                // 0 based for month.
                setDateOfBirth(1968, 12, 12);
            }
        };

        PersonDemographic OtherLocalPatient = new PersonDemographic() {
                {
                    setGender(Gender.M);
                    addIdentifier(new DomainIdentifier(OutsideLocalRootId, UUID.randomUUID().toString(), OutsideLocal_AssigningAuthority));
                    addName(new PersonName("PATIENT3", "CASE2"));
                    addAddress(createAddress(AddressUse.Home, "123 Fake Street", "Hamilton", "ON", "unit 23"));
                    setDateOfBirth(1968, 12, 12);
                }
        };
        
                PersonDemographic LocalPatient = new PersonDemographic() {
                {
                    setGender(Gender.M);
                    addIdentifier(new DomainIdentifier(RootId, "43", AssigningAuthority));
                    addName(new PersonName("PATIENT3", "CASE2"));
                    addAddress(createAddress(AddressUse.Home, "123 Fake Street", "Hamilton", "ON", "unit 23"));
                    setDateOfBirth(1968, 12, 12);
                }
        };
        
       Boolean result = communicator.getCorrespondingIdentifiersQAK2(OtherLocalPatient, new DomainIdentifier(OutsideLocalRootId, OutsideLocal_AssigningAuthority));
       
       if (!result){
           communicator.register(GlobalPatient);
       }
       
       communicator.register(LocalPatient);
       
       LOGGER.info("Message result is: " + result);
       
//       PersonDemographic responsePatient = communicator.getCorrespondingIdentifiers(TestPatient);
       
//     PersonDemographic responsePatient = communicator.getCorrespondingIdentifiers(OurLocal); 
//     responsePatient = communicator.resolveIdentifiers(OurLocal);
//     List<PersonDemographic> responsePatientList = communicator.findCandidatesQuery(OurLocal);
        
    }
    
    
    //  @Test
    public void test11317_ITI8_A08() throws HL7Exception, CommunicationsException, PixApplicationException {
        Joeyoeyoey.getNames().get(0).addNamePart("Gerry", PartType.Middle);
        Joeyoeyoey.getAddresses().get(0).getPartByType(AddressPartType.Zipcode).setValue("9999");
        communicator.update(Joeyoeyoey);
    }

    @Test
    public void resolveJoey() throws CommunicationsException, PixApplicationException {
//        PersonDemographic resolvePerson = new PersonDemographic() {
//            {
//                addIdentifier(new DomainIdentifier(RootId, "d6b73a99-e626-43fa-aa44-c4257faa521b", AssigningAuthority));
//            }
//        };
        communicator.resolveIdentifiers(JoesephF);

    }
    
    @Test
    public void resolveJerry() throws CommunicationsException, PixApplicationException {
//        PersonDemographic resolvePerson = new PersonDemographic() {
//            {
//                addIdentifier(new DomainIdentifier(RootId, "d6b73a99-e626-43fa-aa44-c4257faa521b", AssigningAuthority));
//            }
//        };
        communicator.resolveIdentifiers(JerrySienfeld);

    }

    @Test
    public void testRegister_Joey() throws CommunicationsException, PixApplicationException {
//        communicator.register(Joeyoeyoey);
        communicator.register(JoesephF);
    }

    // this user is working against the auto-generated global id
    @Test
    public void testRegister_Jerry() throws CommunicationsException, PixApplicationException {
//        communicator.register(Joeyoeyoey);
        communicator.register(JerrySienfeld);
    }
    
    // this user is working against the auto-generated global id
    @Test
    public void testRegister_Harry() throws CommunicationsException, PixApplicationException {
//        communicator.register(Joeyoeyoey);
        communicator.register(HarrySienfeld);
    }
    
    // this user is working against the auto-generated global id
    @Test
    public void testRegister_Jim() throws CommunicationsException, PixApplicationException {
//        communicator.register(Joeyoeyoey);
        communicator.register(JimSienfeld);
    }
    
    @Test
    public void testRegister_a04() throws CommunicationsException, PixApplicationException {
        communicator.register(TestPatient);
    }

    @Test
    public void test11317_ITI8_A40() throws CommunicationsException, PixApplicationException {
        communicator.merge(RyanF, Joeyoeyoey);
    }

    @Test
    public void test_q23_iti9_query() throws CommunicationsException, PixApplicationException {
        communicator.resolveIdentifiers(TestPatient);
    }

    @Test
    public void iti21_test_q22() throws CommunicationsException, PixApplicationException {
        communicator.findCandidatesQuery(TestPatient);
    }

    //  @Test
    public void testITI21_PatientName_AdministrativeSex() throws CommunicationsException, PixApplicationException {
        //     communicator.findCandidatesQuery(Patient_ITI21_Query_Name_AdministrativeSex);
    }

    @Test
    public void testITI21_FindSIENFELD() throws CommunicationsException, PixApplicationException {
        List<PersonDemographic> foundPeople = communicator.findCandidatesQuery(new PersonDemographic() {

            {
                addName(new PersonName("fjdkls"));
            }

        });

        System.out.println("test");
    }
}
