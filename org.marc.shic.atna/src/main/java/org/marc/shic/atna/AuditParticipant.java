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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohamed
 */
public class AuditParticipant
{

    private String identifier;
    private ParticipantRoleType participantRoleType;
    private byte[] query;
    private List<ParticipantObjectDetail> details;
    private short typeCode;
    private short typeCodeRole;

    /**
     * Creates a new instance of audit participant
     */
    public AuditParticipant()
    {
        details = new ArrayList<ParticipantObjectDetail>();
    }

    /**
     * Creates a new instance of the audit participant
     */
    public AuditParticipant(String identifier, String detailData, ParticipantRoleType participantRoleType)
    {
        this();
        this.identifier = identifier;
        this.participantRoleType = participantRoleType;
    }

    public AuditParticipant(String identifier, String detailData, ParticipantRoleType participantRoleType, byte[] query)
    {
        this(identifier, detailData, participantRoleType);
        this.query = query;
    }

    public AuditParticipant(ParticipantRoleType participantRoleType, String identifier, byte[] query, ParticipantObjectDetail detail)
    {
        // Fix this class
        this(identifier, null, participantRoleType);

        if (detail != null)
        {
            this.addDetail(detail);
        }
        this.query = query;
    }

    public List<ParticipantObjectDetail> getDetails()
    {
        return details;
    }

    public void addDetail(ParticipantObjectDetail detail)
    {
        this.details.add(detail);
    }

    public short getTypeCode()
    {
        return typeCode;
    }

    public void setTypeCode(short typeCode)
    {
        this.typeCode = typeCode;
    }

    public short getTypeCodeRole()
    {
        return typeCodeRole;
    }

    public void setTypeCodeRole(short typeCodeRole)
    {
        this.typeCodeRole = typeCodeRole;
    }

    /**
     *
     * @return
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     *
     * @param identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public ParticipantRoleType getParticipantRoleType()
    {
        return participantRoleType;
    }

    public void setParticipantRoleType(ParticipantRoleType participantRoleType)
    {
        this.participantRoleType = participantRoleType;
    }

    /**
     * Get the AdhocQueryRequest, base64 encoded.
     *
     * @return Base64 encoded query.
     */
    public byte[] getQuery()
    {
        return query;
    }

    /**
     * Set the AdhocQueryRequest, base64 encoded.
     *
     * @param query Base64 encoded query.
     */
    public void setQuery(byte[] query)
    {
        this.query = query;
    }
}
