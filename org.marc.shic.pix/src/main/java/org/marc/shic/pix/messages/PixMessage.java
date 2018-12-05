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
 * @author tylerg Date: Nov 4, 2013
 *
 */
package org.marc.shic.pix.messages;

import org.marc.shic.core.configuration.IheActorConfiguration;
import ca.uhn.hl7v2.HL7Exception;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.configuration.IheIdentification;

/**
 *
 * @author tylerg
 */
public abstract class PixMessage
{

    private DomainIdentifier m_identifier;
    private IheIdentification m_localIdentification;
    protected IheActorConfiguration pixActor;

    public PixMessage(IheActorConfiguration actor, DomainIdentifier identifier, IheIdentification localIdentification)
    {
        this.pixActor = actor;
        this.m_identifier = identifier;
        this.m_localIdentification = localIdentification;
    }

    public IheActorConfiguration getPixActor()
    {
        return pixActor;
    }

    public void setPixActor(IheActorConfiguration actor)
    {
        this.pixActor = actor;
    }

    public abstract String encodeMessage() throws HL7Exception;

    public DomainIdentifier getIdentifier()
    {
        return m_identifier;
    }

    public void setIdentifier(DomainIdentifier identifier)
    {
        this.m_identifier = identifier;
    }

    public IheIdentification getLocalIdentification()
    {
        return m_localIdentification;
    }

    public void setLocalIdentification(IheIdentification localIdentification)
    {
        this.m_localIdentification = localIdentification;
    }    
}
