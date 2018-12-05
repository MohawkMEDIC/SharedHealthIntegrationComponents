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
 * Date: Sept 4, 2013
 *
 */
package org.marc.shic.atna;

import org.marc.shic.core.configuration.IheActorConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.marc.shic.core.configuration.IheIdentification;

/**
 *
 * @author Mohamed
 */
public class AuditMetaData
{

    private List<AuditActor> actors;
    private List<AuditParticipant> participants = new ArrayList<AuditParticipant>();
    private AuditEventType event;

    /**
     *
     * @return
     */
    public List<AuditActor> getActors()
    {
        return actors;
    }

    /**
     *
     * @return
     */
    public List<AuditParticipant> getParticipants()
    {
        return participants;
    }

    public void addActor(AuditActor actor)
    {
        this.actors.add(actor);
    }

    /**
     * Creates a new instance of the audit meta data class
     */
    public AuditMetaData()
    {
        this.actors = new ArrayList<AuditActor>();
        this.participants = new ArrayList<AuditParticipant>();
    }

    /**
     * Create new instance of audit meta data
     */
    public AuditMetaData(AuditEventType event)
    {
        this();
        this.event = event;
    }

//    /**
//     *
//     * @param identifier
//     * @param endpoint
//     * @param role
//     */
//    public void addActor(AuditActorType role, String identifier, String endpoint)
//    {
//        AuditActor actor = new AuditActor(identifier, endpoint, role);
//        actors.add(actor);
//    }
//
//    /**
//     *
//     * @param identifier
//     * @param role
//     */
//    public void addActor(AuditActorType role, String identifier)
//    {
//        AuditActor actor = new AuditActor(identifier, null, role);
//        actors.add(actor);
//    }
//
//    public void addActor(AuditActorType role, String networkId, IheIdentification identification)
//    {
//        AuditActor actor = new AuditActor(identification.getApplicationId(), networkId, role, identification.getFacilityName());
//        actors.add(actor);
//    }

    /**
     *
     * @param role
     * @param actorConfig
     */
    public void addActor(AuditActorType role, IheActorConfiguration actorConfig)
    {
        AuditActor actor = new AuditActor();
        actor.setActorType(role);
        actor.setNetworkEndpoint(actorConfig.getEndPointAddress().toString());
        actor.setIdentifier(actorConfig.getRemoteIdentification().getApplicationId());
        actor.setAlternativeUserId(actorConfig.getRemoteIdentification().getFacilityName());
        actors.add(actor);
    }

    /**
     *
     * @param role
     * @param identifier
     * @param detailData
     */
    public void addParticipant(ParticipantRoleType role, String identifier, String detailData)
    {
        AuditParticipant participant = new AuditParticipant(identifier, detailData, role);
        participants.add(participant);
    }

    public void addParticipant(ParticipantRoleType role, String identifier, byte[] query, ParticipantObjectDetail detail)
    {
        AuditParticipant participant = new AuditParticipant(role, identifier, query, detail);
        participants.add(participant);
    }

    public void addParticipant(AuditParticipant participant)
    {
        participants.add(participant);
    }

    /**
     *
     * @return
     */
    public AuditEventType getEvent()
    {
        return event;
    }

    public void setEvent(AuditEventType eventType)
    {
        this.event = eventType;
    }
}
