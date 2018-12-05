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
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Nebri
 */

public class PersonDemographic extends Demographic
{

    /**
     * The person's names.
     */
    private List<PersonName> names;
    /**
     * The person's date of birth.
     */
    private Calendar dateOfBirth;
    /**
     * The person's gender.
     */
    private Gender gender;

    public PersonDemographic()
    {
        super();
        names = new ArrayList<PersonName>();
    }

    /**
     * Get the person's date of birth.
     *
     * @return The person's date of birth.
     */
    public Calendar getDateOfBirth()
    {
        return dateOfBirth;
    }

    /**
     * Set the person's date of birth.
     *
     * @param dateOfBirth
     *                    The date of birth to set.
     */
    public void setDateOfBirth(Calendar dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfBirth(int year, int month, int day)
    {
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);

        this.dateOfBirth = dob;
    }

    /**
     * Set the person's gender.
     *
     * @return The person's gender.
     */
    public Gender getGender()
    {
        return gender;
    }

    /**
     * Set the person's gender.
     *
     * @param gender
     *               The gender to set.
     */
    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    /**
     * Add a name to the person's names.
     *
     * @param name
     *             The name to add.
     */
    public void addName(PersonName name)
    {
        names.add(name);
    }

    /**
     * Add a name to the person's names.
     *
     * @param name
     *             The name to add
     */
    public void addName(NameComponent name)
    {
        PersonName newName = new PersonName();
        newName.addNamePart(name.getValue(), name.getType());
        names.add(newName);
    }

    /**
     * Gets the person's names.
     *
     * @return ArrayList of Names
     */
    public List<PersonName> getNames()
    {
        return names;
    }

    /**
     * Get a person's name from a NameUse.
     *
     * @param use
     * @return
     */
    public PersonName getName(NameUse use)
    {
        for (PersonName searchName : names)
        {
            if (searchName.getUse() == use)
            {
                return searchName;
            }
        }

        // TODO: Possibly throw a different exception.
        throw new IllegalArgumentException("Name " + use + " was not found");
    }
}
