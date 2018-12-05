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
 * @author Mohamed Ibrahim Date: 19-Sep-2013
 *
 */
package org.marc.shic.core.configuration.consent;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a collection of consent policies
 */
public class PolicyCollection {

    private List<PolicyDefinition> m_policies = new ArrayList<PolicyDefinition>();
    
    public PolicyCollection() {
    }
    
    public PolicyCollection(List<PolicyDefinition> policies) {
    	this.m_policies = policies;
    }
    
    /**
     * Getter for the policy definition list
     *
     * @return
     */
    public List<PolicyDefinition> getPolicies() {
        return m_policies;
    }

    /**
     * Adds a policy to the policy collection
     *
     * @param policy
     */
    public void add(PolicyDefinition policy) {
        this.m_policies.add(policy);
    }

    /**
     * Returns the first policy by its unique Code/OID.
     *
     * @param policyCode
     * @return
     */
    public PolicyDefinition get(String policyCode) {
        // get the first policy in the list matching (policy.code == policyCode)
        for (PolicyDefinition policy : m_policies) {
            if (policyCode.equalsIgnoreCase(policy.getCode())) {
                return policy;
            }
        }

        return null;
    }

    /**
     * Returns the first policy by its unique Code/OID AND code system
     *
     * @param policyCode
     * @param codeSystem
     * @return
     */
    public PolicyDefinition get(String policyCode, String codeSystem) {
        // get the first policy in the list matching (policy.code == policyCode && policy.codeSystem == codeSystem)
        for (PolicyDefinition policy : m_policies) {
            if (policyCode.equalsIgnoreCase(policy.getCode()) && codeSystem.equalsIgnoreCase(policy.getCodeSystem())) {
                return policy;
            }
        }

        return null;
    }

    /**
     * Get all the Policies that apply to a specific role
     *
     * @param roleName
     * @return
     */
    public PolicyCollection getForRole(String roleName) {
        PolicyCollection retVal = new PolicyCollection();
        
        for (PolicyDefinition policy : m_policies) {
            for (AclDefinition acl : policy.getAcl()) {
                if (roleName.equalsIgnoreCase(acl.getRole())) {
                    retVal.add(policy);
                    break;
                }
            }
        }

        return retVal;
    }
}
