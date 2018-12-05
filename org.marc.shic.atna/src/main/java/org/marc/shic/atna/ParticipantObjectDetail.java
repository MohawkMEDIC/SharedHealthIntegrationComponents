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
 * @author @{user}
 * Date: @{date}
 *
 */
package org.marc.shic.atna;

/**
 *
 * @author tylerg
 */
public class ParticipantObjectDetail {
    private String type;
    private byte[] value;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public byte[] getValue()
    {
        return value;
    }

    public void setValue(byte[] value)
    {
        this.value = value;
    }
    
    public ParticipantObjectDetail() {
        
    }
    
    public ParticipantObjectDetail(String type, byte[] value) {
        this.type = type;
        this.value = value;
    }
}
