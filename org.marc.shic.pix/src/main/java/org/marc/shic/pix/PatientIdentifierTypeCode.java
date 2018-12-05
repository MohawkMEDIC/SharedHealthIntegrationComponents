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
 * Date: Jan 30 2014
 *
 */
package org.marc.shic.pix;

/**
 *
 * @author Garrett
 */
public enum PatientIdentifierTypeCode
{

    /**
     * Patient’s medical record number, per PID-2
     */
    MRN("MR"),
    /**
     * Patient’s identifier in the Master Patient Index, per PID-2
     */
    MPI_PI("PI"),
    /**
     * Patient’s identifier in the Master Patient Index, per PID-2
     */
    MPI_PT("PT");
    private final String value;

    /**
     * Create a status type
     *
     * @param value
     */
    private PatientIdentifierTypeCode(String value)
    {
        this.value = value;
    }

    /**
     * Get the code for the XDS document entry
     *
     * @return
     */
    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
