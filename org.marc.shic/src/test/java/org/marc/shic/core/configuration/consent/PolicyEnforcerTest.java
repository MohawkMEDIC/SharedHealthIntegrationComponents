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
 * Date: 07-Nov-2013
 *
 */
package org.marc.shic.core.configuration.consent;

import java.io.File;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;

/**
 *
 * @author ibrahimm
 */
public class PolicyEnforcerTest {
    
    private static PolicyEnforcer m_enforcer;
    private static TestIdentifyProvider m_provider = new TestIdentifyProvider("mo");
    private static String m_affinityDomainConfig = "../oht.xml";
    
    public PolicyEnforcerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IheConfigurationException {
        IheAffinityDomainConfiguration domain = ConfigurationUtility.parseConfiguration(new File(m_affinityDomainConfig));
        m_enforcer = new PolicyEnforcer(m_provider, domain);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of demand method, of class PolicyEnforcer.
     */
//    @Test
    public void testDemand() {
        System.out.println("demand");
        PolicyActionOutcome expResult = PolicyActionOutcome.Deny;
        Map<PolicyDefinition, PolicyActionOutcome> result = m_enforcer.demand(DemandPermission.Export, null);
        
//        assertEquals(expResult, result);
    }

    /**
     * Test of query method, of class PolicyEnforcer.
     */
    @Test
    public void testQuery() {
        System.out.println("query");
        PolicyCollection result = m_enforcer.query(DemandPermission.Export);
        
        assertEquals(result.getPolicies().size(), 2);
    }
    
}
