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
 * @author tylerg
 * Date: Aug 21, 2013
 *
 */
package org.marc.shic.core;

import org.marc.shic.core.XdsGuidType;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.PersonDemographic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.XdsExtendedQueryParameterType;

public class XdsQuerySpecification
{

    protected Calendar creationTimeFrom;
    protected Calendar creationTimeTo;
    protected PersonDemographic patient;
    protected List<CodeValue> classification;
    // Backing field for extended parameters
    protected List<XdsExtendedQueryParameterType<?>> extendedParameters = new ArrayList<XdsExtendedQueryParameterType<?>>();
    // Backing field for specification type (default is find documents)
    @Deprecated
    protected XdsGuidType queryType = XdsGuidType.RegistryStoredQuery_FindDocuments;
    protected String homeCommunityId;
    private CodeValue purposeOfUse;
    // Backing field for status
    protected XdsDocumentEntryStatusType[] status = new XdsDocumentEntryStatusType[]
    {
        XdsDocumentEntryStatusType.Approved
    };

    /**
     * Creates a new instance of the XdsQuerySpecification
     */
    public XdsQuerySpecification()
    {
    }
    
    public XdsQuerySpecification(String homeCommunityId) {
        this();
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Creates a new instance of the XdsQuerySpecification
     *
     * @param queryType
     */
    @Deprecated
    public XdsQuerySpecification(XdsGuidType queryType)
    {
        this.queryType = queryType;
    }

    /**
     * Get the status parameter
     */
    public XdsDocumentEntryStatusType[] getStatus()
    {
        return this.status;
    }

    /**
     * Set status filter
     */
    public void setStatus(XdsDocumentEntryStatusType[] status)
    {
        this.status = status;
    }

    /**
     * @return the queryType
     */
    @Deprecated
    public XdsGuidType getQueryType()
    {
        return queryType;
    }

    /**
     * Get all XdsExtendedQueryParameters
     */
    public List<XdsExtendedQueryParameterType<?>> getExtendedParameters()
    {
        return extendedParameters;
    }

    public boolean containsExtendedParameterExist(String parameterName)
    {
        boolean result = false;

        for (XdsExtendedQueryParameterType<?> parameter : extendedParameters)
        {
            if (parameter.getParameterName().equals(parameterName))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * @param extendedParameters the extendedParameters to set
     */
    public void addExtendedParameter(XdsExtendedQueryParameterType<?> parm)
    {
        this.extendedParameters.add(parm);
    }

    public void setCreationTimeFrom(Calendar creationTimeFrom)
    {
        this.creationTimeFrom = creationTimeFrom;
    }

    public Calendar getCreationTimeFrom()
    {
        return this.creationTimeFrom;
    }

    public void setCreationTimeTo(Calendar creationTimeTo)
    {
        this.creationTimeTo = creationTimeTo;
    }

    public Calendar getCreationTimeTo()
    {
        return this.creationTimeTo;
    }

    public void setPatient(PersonDemographic patient)
    {
        this.patient = patient;
    }

    public PersonDemographic getPatient()
    {
        return this.patient;
    }

    public void addClassification(CodeValue classification)
    {
        this.classification.add(classification);
    }

    public CodeValue getClassification(int i)
    {
        return this.classification.get(i);
    }

    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    public CodeValue getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(CodeValue purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }
}
