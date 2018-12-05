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

import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.exceptions.NtpCommunicationException;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 *
 * @author tylerg
 */
public class NTPHelper
{

    public static Calendar getTime(String host, int port) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        NTPUDPClient ntp = new NTPUDPClient();
        TimeInfo time = null;

        ntp.setDefaultTimeout(10000);

        try
        {
            time = ntp.getTime(Inet4Address.getByName(host), port);

            cal.setTimeInMillis(time.getReturnTime());
        } catch (IOException e)
        {
            throw e;
        }

        return cal;
    }

    public static Calendar getTime(IheAffinityDomainConfiguration config) throws NtpCommunicationException
    {
        IheActorConfiguration tsActor;
        Calendar cal;

        tsActor = config.getActor(IheActorType.TS);

        try
        {
            cal = getTime(tsActor.getEndPointAddress().getHost(), tsActor
                    .getEndPointAddress().getPort());
        } catch (IOException e)
        {
            throw new NtpCommunicationException(tsActor,
                    "Unable to reach TS endpoint.", e);
        }

        return cal;
    }
}
