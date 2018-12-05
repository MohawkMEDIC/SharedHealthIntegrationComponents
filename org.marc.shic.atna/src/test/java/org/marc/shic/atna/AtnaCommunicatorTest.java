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
package org.marc.shic.atna;

import java.io.File;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.shic.atna.messages.AuditUtility;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;

/**
 *
 * @author clarkc
 */
public class AtnaCommunicatorTest
{
    public static String AssigningAuthority = "MiH_OSCAR_A";
    public static String RootId = "1.3.6.1.4.1.33349.3.1.3.201203.2.0.0";
    private static IheConfiguration config = new IheConfiguration();
    private static final String TestAffinityDomainConfig = "../domain.xml";
    private static AuditMetaData audit = new AuditMetaData();

    private static void setUpConfig()
    {
        try {
            config.setIdentifier(new DomainIdentifier(RootId, "", AssigningAuthority));
            config.setLocalIdentification(new IheIdentification("LOCAL_APPLICATION", "LOCAL_FACILITY"));
            config.setAffinityDomain(ConfigurationUtility.parseConfiguration(new File(TestAffinityDomainConfig)));
        } catch (IheConfigurationException ex) {
            Logger.getLogger(AtnaCommunicatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void setUpAudit()
    {
        AuditMetaData auditData = new AuditMetaData(AuditEventType.DocumentQuery);
        auditData.addActor(AuditActorType.Destination, config.getAffinityDomain().getActor(IheActorType.DOC_REGISTRY));
        auditData.addActor(AuditUtility.generateRequestingUser());
        auditData.addParticipant(ParticipantRoleType.Patient, "123", null);
    }

    @BeforeClass
    public static void setUpClass()
    {
        setUpConfig();
        setUpAudit();
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
     * Test of sendAudit method, of class AtnaCommunicator.
     */
    @Test
    public void testSendAuditOverTCP() throws ActorNotFoundException, CommunicationsException
    {
        try {
            System.out.println("Sending Audit message over TCP");
            AtnaCommunicator instance = new AtnaCommunicator(config);
            boolean expResult = true;
            boolean result = instance.sendAudit(audit);
            assertEquals(expResult, result);
        } catch (ActorNotFoundException ex) {
            Logger.getLogger(AtnaCommunicatorTest.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (CommunicationsException ex) {
            Logger.getLogger(AtnaCommunicatorTest.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void enqueue_NullAuditMessageWrapper()
    {
        AtnaCommunicator.enqueueMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void enqueue_EmptyAuditMessageWrapper()
    {
        AtnaCommunicator.enqueueMessage(new AuditMessageWrapper());
    }
}
