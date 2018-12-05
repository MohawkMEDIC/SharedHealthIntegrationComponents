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

import java.util.ArrayList;
import java.util.List;
import org.marc.shic.core.PersonName;

/**
 *
 * @author tylerg
 */
public class NameGenerator
{

    public static final String Vowels = "aeiou";
    public static final String Constants = "bcdfghjklmnpqrstvwxyz";
    public static final String[] NameStartingConstants =
    {
        "b", "c", "d", "f", "g", "h", "k",
        "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
        "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
        "th"
    };
    public static final String[] NameEndingConstants =
    {
        "b", "c", "d", "f", "g", "h", "k",
        "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
        "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
        "th"
    };
    public static final String[] NameParts =
    {
        "a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
        "ji", "y", "oi", "au", "oo"
    };
    public static final String[] NameRules =
    {
        "ve", "sveve", "sve", "svsvd", "veve"
    };

    private NameGenerator()
    {
    }

    private static String getRandomVowel()
    {
        return Character.toString(Vowels.charAt(RandomUtil.nextInt(0, Vowels.length())));
    }

    private static String getRandomConstant()
    {
        return Character.toString(Constants.charAt(RandomUtil.nextInt(0, Constants.length())));
    }

    private static String getRandomNameStart()
    {
        return NameStartingConstants[RandomUtil.nextInt(0, NameStartingConstants.length)];
    }

    private static String getRandomNamePart()
    {
        return NameParts[RandomUtil.nextInt(0, NameParts.length)];
    }

    private static String getRandomNameRule()
    {
        return NameRules[RandomUtil.nextInt(0, NameRules.length)];
    }

    private static String getRandomNameEnding()
    {
        return NameEndingConstants[RandomUtil.nextInt(0, NameEndingConstants.length)];
    }

    private static boolean isCharacterVowel(String character)
    {
        return Vowels.contains(character);
    }

    private static boolean isCharacterConstant(String character)
    {
        return Constants.contains(character);
    }

    /**
     * Generate a random name.
     *
     * @return Generated name.
     */
    public static String generateName()
    {
        StringBuilder name = new StringBuilder();
        String nameRule = getRandomNameRule();

        // Loop through the selected rule.
        for (int i = 0; i < nameRule.length(); i++)
        {
            char currentRule = nameRule.charAt(i);
            switch (currentRule)
            {
                case 's':
                    // Starting
                    name.append(getRandomNameStart());
                    break;

                case 'v':
                    // Vowel part
                    name.append(getRandomNamePart());
                    break;

                case 'e':
                    // Ending
                    name.append(getRandomNameEnding());
                    break;
            }
        }

        return capitalize(name.toString());
    }

    /**
     * Generates a full name with a first, middle and last name.
     *
     * @return
     */
    public static PersonName generateFullName()
    {
        return new PersonName(generateName(), generateName(), generateName());
    }

    /**
     * Generates a collection of full names.
     * @param amount The amount of full names to generate.
     * @return Collection of generated full names.
     */
    public static List<PersonName> generatePersonNames(int amount)
    {
        List<PersonName> names = new ArrayList<PersonName>();

        for (int i = 0; i < amount; i++)
        {
            names.add(generateFullName());
        }

        return names;
    }

    /**
     * Generate a collection of names.
     *
     * @param amount The amount of names to generate.
     * @return
     */
    public static List<String> generateNames(int amount)
    {
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < amount; i++)
        {
            names.add(generateName());
        }

        return names;
    }

 

    /**
     * Capitalize the first letter of a name.
     * @param name The name to capitalize.
     * @return Capitalized name.
     */
    public static String capitalize(String name)
    {
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    }
}
