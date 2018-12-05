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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.marc.shic.core.configuration.consent.PolicyCollection;

public class IheAffinityDomainConfiguration implements Serializable {

    private String oid;
    private IheAffinityDomainPermission permission;
    private String name;
    private PolicyCollection consent;
    private List<IheActorConfiguration> actors = new ArrayList<IheActorConfiguration>();
    private List<IheValueSetConfiguration> valueSets = new ArrayList<IheValueSetConfiguration>();
    private List<IheCodeMappingConfiguration> codeMappings = new ArrayList<IheCodeMappingConfiguration>();

    // The following will be deprecated
    private String policyURL;
    private IheIdentification identification;

    /*
     * Default constructor is needed for serialization
     */
    public IheAffinityDomainConfiguration() {
    }

    public IheAffinityDomainConfiguration(String name) {
        this.name = name;
    }

	// JF: Moved the actor type to a property on the actorList because
    // there may be more than one actor fulfilling a particular role
    public void addActor(IheActorConfiguration actor) {
        actors.add(actor);
    }

    /**
     * Get the complete list of actors
     */
    public List<IheActorConfiguration> getActors() {
        return actors;
    }

    /**
     * Get the actor which is acting in the specified role
     */
    public IheActorConfiguration getActor(IheActorType role) {
        IheActorConfiguration iheActor = null;

        for (IheActorConfiguration actor : this.actors) {
            if (actor.getActorType().equals(role)) {
                iheActor = actor;
                break;
            }
        }

        return iheActor;
    }

    /**
     * Check if the affinity domain contains an actor.
     *
     * @param role Actor role to check for.
     * @return True if the actor role is found.
     */
    public boolean containsActor(IheActorType role) {
        boolean doesContainActor = false;

        for (IheActorConfiguration actor : this.actors) {
            if (actor.getActorType().equals(role)) {
                doesContainActor = true;
                break;
            }
        }

        return doesContainActor;
    }

    public void setActors(ArrayList<IheActorConfiguration> actor) {
        this.actors = actor;
    }

    /**
     * Remove an actor by its name.
     *
     * @param name Name of the actor to remove.
     * @return Removed actor.
     */
    public IheActorConfiguration removeActor(String name) {
        IheActorConfiguration removedActor = null;

        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).getName().equals(name)) {
                removedActor = actors.get(i);
                actors.remove(i);
                break;
            }
        }

        return removedActor;
    }

    @Deprecated
    public void setIdentification(IheIdentification identification) {
        this.identification = identification;
    }

    @Deprecated
    public IheIdentification getIdentification() {
        return identification;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public IheAffinityDomainPermission getPermission() {
        return permission;
    }

    public void setPermission(IheAffinityDomainPermission permission) {
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolicyCollection getConsent() {
        return consent;
    }

    public void setConsent(PolicyCollection consent) {
        this.consent = consent;
    }

    /**
     * @return the valueSets
     */
    public List<IheValueSetConfiguration> getValueSets() {
        return valueSets;
    }

    /**
     * @param valueSet the valueSet to add
     */
    public void addValueSet(IheValueSetConfiguration valueSet) {
        valueSets.add(valueSet);
    }

    /**
     * @return the codeMappings
     */
    public List<IheCodeMappingConfiguration> getCodeMappings() {
        return codeMappings;
    }

    /**
     * @param codeMapping the codeMapping to add
     */
    public void addCodeMapping(IheCodeMappingConfiguration codeMapping) {
        codeMappings.add(codeMapping);
    }

    @Deprecated
    public String getPolicyURL() {
        return policyURL;
    }

    @Deprecated
    public void setPolicyURL(String policyURL) {
        this.policyURL = policyURL;
    }
}
