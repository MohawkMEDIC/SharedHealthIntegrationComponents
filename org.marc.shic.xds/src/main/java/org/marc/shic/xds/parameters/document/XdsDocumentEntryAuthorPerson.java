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
 * Date: Nov 26, 2013
 *
 */
package org.marc.shic.xds.parameters.document;

/**
 *
 * The value for this parameter is a pattern compatible with the SQL keyword
 * LIKE which allows the use of the following wildcard characters: % to match
 * any (or no) characters and _ to match a single character.
 * The match shall be applied to the text contained in the Value elements of the
 * authorPerson Slot on the author Classification (value strings of the
 * authorPerson sub-attribute)
 */
public class XdsDocumentEntryAuthorPerson<T> extends XdsDocumentParameter<T>
{

    private final String PARAMETER_NAME = "$XDSDocumentEntryAuthorPerson";

    @Override
    public String getParameterName()
    {
        return PARAMETER_NAME;
    }

    public XdsDocumentEntryAuthorPerson(T parameterValue)
    {
        super(parameterValue);
    }
}
