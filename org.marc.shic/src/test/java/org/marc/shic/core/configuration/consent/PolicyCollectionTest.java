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
 * @author Mohamed Ibrahim Date: 07-Nov-2013
 *
 */
package org.marc.shic.core.configuration.consent;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.utils.ConfigurationUtility;

/**
 * @author ibrahimm
 *
 */
public class PolicyCollectionTest {

    private static IheAffinityDomainConfiguration m_domain;
    private static PolicyDefinition m_policy;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File file = new File("../oht.xml");
        m_domain = ConfigurationUtility.parseConfiguration(file);

        // add a policy
        m_policy = new PolicyDefinition();
        m_policy.setCode("Policy_Code_1.2.3");
        m_policy.setCodeSystem("Policy_Code_System");
        m_policy.setDisplayName("Policy_Display_Name");

        m_domain.getConsent().add(m_policy);
    }

    /**
     * Test method for
     * {@link org.marc.shic.core.configuration.consent.PolicyCollection#add(org.marc.shic.core.configuration.consent.PolicyDefinition)}.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        assertTrue(m_domain.getConsent().getPolicies().contains(m_policy));
    }

    /**
     * Test method for
     * {@link org.marc.shic.core.configuration.consent.PolicyCollection#get(java.lang.String)}.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        assertEquals(m_domain.getConsent().get("Policy_Code_1.2.3"), m_policy);
    }
    
    /**
     * Test method for
     * {@link org.marc.shic.core.configuration.consent.PolicyCollection#getForRole(java.lang.String)}.
     */
    @Test
    public void testGetForRole() {
        System.out.println("getForRole");
        assertEquals(m_domain.getConsent().getForRole("Physicians").getPolicies().size(), 4);
        assertEquals(m_domain.getConsent().getForRole("Patients").getPolicies().size(), 0);
    }

}
