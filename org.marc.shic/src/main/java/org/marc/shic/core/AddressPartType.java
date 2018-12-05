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
package org.marc.shic.core;

/**
 *
 * @author ibrahimm
 */
public enum AddressPartType
{

    AddressLine("@PID.11.1.1"), City("@PID.11.1.3"), State("@PID.11.1.4"), Country("@PID.11.1.6"), Zipcode("@PID.11.1.5");
    private String pidField;

    public String getPidField()
    {
        return pidField;
    }

    public void setPidField(String pidField)
    {
        this.pidField = pidField;
    }

    private AddressPartType()
    {
    }

    private AddressPartType(String pidField)
    {
        this();
        this.pidField = pidField;
    }
}