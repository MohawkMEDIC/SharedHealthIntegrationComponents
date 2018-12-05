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
 * @author Garrett
 * Date: Dec 2, 2013
 *
 */
package org.marc.shic.xds.parameters.document;

import org.marc.shic.core.XdsDocumentEntryStatusType;

/**
 * This class is most likely a bit ambiguous to XdsDocumentEntryStatusType, will rename/change it later.
 *
 * @author Garrett
 */
public class XdsDocumentEntryStatus<T extends XdsDocumentEntryStatusType> extends XdsDocumentParameter<T[]>
{

    private final String PARAMETER_NAME = "$XDSDocumentEntryStatus";

    @Override
    public String getParameterName()
    {
        return PARAMETER_NAME;
    }

    public XdsDocumentEntryStatus(T... parameterValue)
    {
        super(parameterValue);
    }
}
