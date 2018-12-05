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
 * Date: 11-Nov-2013
 *
 */
package org.marc.shic.core.configuration.consent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.exceptions.UnknownPolicyException;

/**
 * A class that handles Consent Policy and permission management.
 *
 * @author ibrahimm
 */
public class PolicyEnforcer
{

    private IIdentityProvider m_identityProvider;
    private IheAffinityDomainConfiguration m_affinityDomain;

    /**
     * Construct the object with an IIdentityProvider implementation and an
     * affinity domain object.
     *
     * @param identityProvider
     * @param affinityDomain
     */
    public PolicyEnforcer(IIdentityProvider identityProvider, IheAffinityDomainConfiguration affinityDomain)
    {
        this.m_identityProvider = identityProvider;
        this.m_affinityDomain = affinityDomain;
    }

    /**
     * Demand a permission of a specific resource. If there is more than one
     * policy
     * attached to a resource, the most strict one is applied.
     *
     * @param permission
     * @param resource
     * @return
     */
    public Map<PolicyDefinition, PolicyActionOutcome> demand(DemandPermission permission, IConfidential resource) throws UnknownPolicyException
    {
        // PolicyActionOutcome retVal = PolicyActionOutcome.Deny;
        Map<PolicyDefinition, PolicyActionOutcome> policyOutcomes = new HashMap<PolicyDefinition, PolicyActionOutcome>();

        // get a list of policies from our configuration that apply to the document
        List<PolicyDefinition> policies = new ArrayList<PolicyDefinition>();
        for (CodeValue code : resource.getConfidentiality())
        {
            PolicyDefinition policy = m_affinityDomain.getConsent().get(code.getCode(), code.getCodeSystem());
            if (policy == null)
            {
                throw new UnknownPolicyException(String.format("Unknown policy found. Code: %s, CodeSystem:%s", code.getCode(), code.getCodeSystem()));
            }

            policies.add(policy);
        }

        
        // based on the user role, find all actions the user can perform for the permission
        for (PolicyDefinition policyDefinition : policies)
        {
            List<PolicyActionOutcome> outcome = new ArrayList<PolicyActionOutcome>();
            for (AclDefinition acl : policyDefinition.getAcl())
            {
                if ((acl.getPermission() == DemandPermission.All || acl.getPermission() == permission) && ("*".equals(acl.getRole()) || m_identityProvider.isUserInRole(acl.getRole())))
                {
                    outcome.add(acl.getAction());
                }
            }
            
            PolicyActionOutcome mostStrict = PolicyActionOutcome.Allow;
            for (PolicyActionOutcome action : outcome) {
                if (action.ordinal() > mostStrict.ordinal()) {
                    mostStrict = action;
                }
            }
            
             policyOutcomes.put(policyDefinition, mostStrict);
        }

        return policyOutcomes;
    }

    /**
     * Get all policies attached to a resource.
     *
     * @param resource The confidential resource to query for policies on.
     * @return List of policies attached to a confidential resource.
     */
    public List<PolicyDefinition> getAllPolicies(IConfidential resource)
    {
        List<PolicyDefinition> policies = new ArrayList<PolicyDefinition>();

        for (CodeValue code : resource.getConfidentiality())
        {
            PolicyDefinition policy = m_affinityDomain.getConsent().get(code.getCode(), code.getCodeSystem());

            if (policy != null)
            {
                policies.add(policy);        
            }
        }

        return policies;
    }

    /**
     * Query policies that the current user identity can perform for the
     * specified permission.
     *
     * @param permission
     * @return
     */
    public PolicyCollection query(DemandPermission permission)
    {
        PolicyCollection retVal = new PolicyCollection();

        // get all policies the user can access for the specific permission
        for (PolicyDefinition def : m_affinityDomain.getConsent().getPolicies())
        {
            for (AclDefinition acl : def.getAcl())
            {
                if (m_identityProvider.isUserInRole(acl.getRole()) && acl.getPermission() == permission)
                {
                    retVal.add(def);
                    break;
                }
            }
        }

        return retVal;
    }
}
