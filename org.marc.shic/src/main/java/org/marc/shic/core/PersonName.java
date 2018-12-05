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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nebri
 */
public class PersonName
{
    private List<NameComponent> parts;
    private NameUse use;

    public PersonName()
    {
        this.parts = new ArrayList<NameComponent>();
    }
    
    public PersonName(String lastName) {
        this();
        addNamePart(lastName, PartType.Family);
    }

    public PersonName(String firstName, String lastName)
    {
        this(lastName);
        addNamePart(firstName, PartType.Given);
    }

    public PersonName(String firstName, String middleName, String lastName)
    {
        this(firstName, lastName);
        addNamePart(middleName, PartType.Middle);
    }

    /**
     *
     * @param value
     * @param type
     */
    public void addNamePart(String value, PartType type)
    {
        NameComponent namePart = new NameComponent();
        namePart.setValue(value);
        namePart.setType(type);
        parts.add(namePart);
    }
    
    /**
     * Gets a name component by its type.
     * @param type
     * @return Requested name component, or null if not found.
     */
    public NameComponent getNameComponent(PartType type) {
        for (NameComponent component : getParts()) {
            if (component.getType() == type) {
                return component;
            }
        }
        
        return null;
    }

    /**
     *
     * @return
     */
    public List<NameComponent> getParts()
    {
        return parts;
    }

    /**
     *
     * @param use
     */
    public void setUse(NameUse use)
    {
        this.use = use;
    }

    /**
     *
     * @return
     */
    public NameUse getUse()
    {
        return use;
    }
    
}
