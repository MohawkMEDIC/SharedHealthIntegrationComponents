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
package org.marc.shic.svs;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.atna.AtnaCommunicator;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.configuration.*;
import org.marc.shic.core.exceptions.*;
import org.marc.shic.core.utils.ConfigurationUtility;

/**
 *
 * @author tylerg
 */
public class SvsCommunicatorTest
{
    public static String AssigningAuthority = "MiH_OSCAR_A";
    public static String RootId = "1.3.6.1.4.1.33349.3.1.3.201203.2.0.0";
    private static final boolean UseProxy = false;
    private static final String TestAffinityDomainConfig = "../domain.xml";
    private static IheConfiguration config = new IheConfiguration();
    private static SvsCommunicator svs;
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SvsCommunicatorTest.class);

    public SvsCommunicatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        if (UseProxy)
        {
            System.getProperties().put("http.proxyHost", "localhost");
            System.getProperties().put("http.proxyPort", "8888");
        }

//        IheActorConfiguration svsActor = new IheActorConfiguration(IheActorType.SVS, "svs", true, "http://jumbo.irisa.fr:8080/SVSSimulator-SVSSimulator-ejb/SoapRepository?wsdl");
//        config.getAffinityDomain(TestConfigAffinityDomain).addActor(svsActor);
//        config.save(TestConfigLocation);
        try
        {
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("LOCAL_APPLICATION", "LOCAL_FACILITY"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
            svs = new SvsCommunicator(config);
        } catch (ActorNotFoundException ex)
        {
            fail(ex.getMessage());
        } catch (IheConfigurationException ex) {
            LOGGER.error(ex);
        }

    }

    @AfterClass
    public static void tearDownClass()
    {
        AtnaCommunicator.getAuditMessageQueue().awaitShutdown(5);
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /*
     * The following test is dependent upon the target SVS endpoint (for specific data/concepts)
     * It is Uncommented for maven packaging (all tests must pass for the jar to build)
     */
    @Test
    public void retrieveValueSetTest()
    {
        String valueSetId = "1.3.6.1.4.1.21367.101.103";

        String expectedDisplayName = "Marital Status Codes HL7 Table 0002";
        int expectedConceptCount = 2;

        ValueSet valueSet = new ValueSet(valueSetId);

        try
        {
            svs.retrieveValueSet(valueSet);

            assertTrue("Value set is invalid.", valueSet.validate());
            assertTrue("Expected " + expectedDisplayName, valueSet.getDisplayName().equals(expectedDisplayName));
            assertTrue("Expected concept count of " + expectedConceptCount, valueSet.getConceptLists().size() == expectedConceptCount);
        } catch (CommunicationsException ex)
        {
            Logger.getLogger(SvsCommunicatorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    // @Test(expected = SvsCommunicatonException.class)
    public void retrieveValueSetTest_InvalidOid() throws CommunicationsException
    {
        String valueSetId = "12312312312.3123123.131231.2312";

        ValueSet valueSet = new ValueSet(valueSetId);
        svs.retrieveValueSet(valueSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveValueSetTest_NullValueSet() throws CommunicationsException
    {
        svs.retrieveValueSet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveValueSetTest_NullValueSetId() throws CommunicationsException
    {
        ValueSet valueSet = new ValueSet();
        svs.retrieveValueSet(valueSet);
    }
}
