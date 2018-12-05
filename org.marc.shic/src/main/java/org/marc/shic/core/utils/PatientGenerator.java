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
 * Date: Nov 4, 2013
 *
 */
package org.marc.shic.core.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.marc.shic.core.AddressPartType;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;

/**
 *
 * @author tylerg
 */
public class PatientGenerator
{

    private final String AssigningAuthority;
    private final String RootId;
    private static final Logger LOGGER = Logger.getLogger(PatientGenerator.class);
    public static final String[] postalCodeAreas =
    {
        "A", "B", "C", "E", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "T"
    };

    public PatientGenerator(String assigningAuthority, String rootId)
    {
        AssigningAuthority = assigningAuthority;
        RootId = rootId;
    }

    public DomainIdentifier generateId()
    {
        return new DomainIdentifier(RootId, UUID.randomUUID().toString(), AssigningAuthority);
    }

    public PersonDemographic generatePatient()
    {
        PersonDemographic generatedPatient = new PersonDemographic();

        generatedPatient.addName(NameGenerator.generateFullName());
        generatedPatient.setGender(generateBasicGender());
        generatedPatient.setDateOfBirth(generateDate());
        generatedPatient.addIdentifier(generateId());
        generatedPatient.addAddress(generateCanadianAddress());

        return generatedPatient;
    }

    public PersonAddress generateCanadianAddress()
    {
        // PersonAddress address = new PersonAddress(AddressUse.values()[RandomUtil.nextInt(0, AddressUse.values().length)]);
        PersonAddress address = new PersonAddress(AddressUse.Workplace);
        address.addAddressPart(NameGenerator.generateName(), AddressPartType.City);

        // Randomize
        address.addAddressPart("Ontario", AddressPartType.State);

        address.addAddressPart("Canada", AddressPartType.Country);

        address.addAddressPart(RandomUtil.nextInt(1, 1000) + " " + NameGenerator.generateName(), AddressPartType.AddressLine);

        // Randomize
        address.addAddressPart(generateCandianPostalCode(), AddressPartType.Zipcode);

        return address;
    }

    public String generateCandianPostalCode()
    {
        // FSA - 1
        StringBuilder postalCode = new StringBuilder(postalCodeAreas[RandomUtil.nextInt(0, postalCodeAreas.length)]);

        // FSA - 2
        postalCode.append(RandomUtil.nextInt(1, 9));

        // FSA - 3
        // Randomize
        postalCode.append("T ");

        // LDU - 1
        postalCode.append(RandomUtil.nextInt(1, 9));

        // LDU - 2
        postalCode.append("G");

        // LDU - 3
        postalCode.append(RandomUtil.nextInt(1, 9));

        return postalCode.toString();
    }

    public static Gender generateGender()
    {
        return Gender.values()[RandomUtil.nextInt(0, Gender.values().length)];
    }

    public static Calendar generateDate()
    {
        Calendar date = GregorianCalendar.getInstance();

        int currentYear = date.get(Calendar.YEAR);

        date.set(Calendar.YEAR, RandomUtil.nextInt(currentYear - 75, currentYear));

        date.set(Calendar.MONTH, RandomUtil.nextInt(0, 11));

        int dayOfYear = RandomUtil.nextInt(1, date.getActualMaximum(Calendar.DAY_OF_YEAR));

        date.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return date;
    }

    public static Gender generateBasicGender()
    {
        Gender gender = null;

        switch (RandomUtil.nextInt(0, 2))
        {
            case 0:
                gender = Gender.F;
                break;
            case 1:
                gender = Gender.M;
                break;
        }

        return gender;
    }
}
