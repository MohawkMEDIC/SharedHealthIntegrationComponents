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

/**
 *
 * @author Mohamed
 */
public class AuditActor
{

    private String identifier;
    private String alternativeUserId;
    private String networkEndpoint;
    private AuditActorType actorType;
    private short networkAccessPointTypeCode;
    private boolean userIsRequestor;

    public AuditActor()
    {
    }

    public AuditActor(AuditActorType actorType)
    {
        this.actorType = actorType;
    }

    public AuditActor(AuditActorType actorType, String identifier)
    {
        this(actorType);
        this.identifier = identifier;
    }

    public AuditActor(AuditActorType actorType, String identifier, String alternativeIdentifier)
    {
        this(actorType, identifier);
        this.alternativeUserId = alternativeIdentifier;
    }

    public AuditActor(AuditActorType actorType, String identifier, String alternativeIdentifier, boolean userIsRequestor)
    {
        this(actorType, identifier, alternativeIdentifier);
        this.userIsRequestor = userIsRequestor;
    }

    public AuditActor(AuditActorType actorType, String identifier, String alternativeIdentifier, boolean userIsRequestor, String networkEndpoint, short networkAccessPointTypeCode)
    {
        this(actorType, identifier, alternativeIdentifier, userIsRequestor);
        this.networkEndpoint = networkEndpoint;
        this.networkAccessPointTypeCode = networkAccessPointTypeCode;
    }

    public boolean isUserIsRequestor()
    {
        return userIsRequestor;
    }

    public void setUserIsRequestor(boolean userIsRequestor)
    {
        this.userIsRequestor = userIsRequestor;
    }

    public AuditActorType getActorType()
    {
        return this.actorType;
    }

    public void setActorType(AuditActorType actorType)
    {
        this.actorType = actorType;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getNetworkEndpoint()
    {
        return networkEndpoint;
    }

    public void setNetworkEndpoint(String networkEndpoint)
    {
        this.networkEndpoint = networkEndpoint;
    }

    public String getAlternativeUserId()
    {
        return alternativeUserId;
    }

    public void setAlternativeUserId(String alternativeUserId)
    {
        this.alternativeUserId = alternativeUserId;
    }

    public short getNetworkAccessPointTypeCode()
    {
        return networkAccessPointTypeCode;
    }

    public void setNetworkAccessPointTypeCode(short networkAccessPointTypeCode)
    {
        this.networkAccessPointTypeCode = networkAccessPointTypeCode;
    }
}
