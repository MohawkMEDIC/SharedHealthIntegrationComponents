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

import java.util.Random;

/**
 *
 * @author tylerg
 */
public class RandomUtil
{

    private RandomUtil()
    {
    }

    /**
     * Get an integer between a min low and a high,  low inclusive.
     *
     * @param low
     * @param high
     * @return
     */
    public static int nextInt(int low, int high)
    {
        Random rand = new Random();

        return rand.nextInt((high - low)) + low;
    }

    public static int nextInt(int seed, int low, int high)
    {
        Random rand = new Random(seed);

        return rand.nextInt((high - low)) + low;
    }
}
