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
 * Date: Jan 13, 2014
 *
 */
package org.marc.shic.xds.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.xds.profiles.IXdsInteraction;
import org.marc.shic.xds.XcaCommunicator;
import org.marc.shic.xds.XdStarCommunicator;
import org.marc.shic.xds.XdrCommunicator;
import org.marc.shic.xds.XdsCommunicator;

/**
 *
 * @author tylerg
 */
public class XdStarUtility
{

    public static <T extends IXdsInteraction> T createCommunicatorByInteraction(IheConfiguration config, Class<T> interaction)
    {
        return (T) createCommunicatorFromConfig(config);
    }

    public static XdStarCommunicator createCommunicatorFromConfig(IheConfiguration configuration)
    {

        XdStarCommunicator communicator = null;
        IheAffinityDomainConfiguration affDomain = configuration.getAffinityDomain();

        if (affDomain.containsActor(IheActorType.DOC_REGISTRY) && affDomain.containsActor(IheActorType.DOC_REPOSITORY))
        {
            communicator = new XdsCommunicator(configuration);
        } else if (affDomain.containsActor(IheActorType.RESPONDING_GATEWAY_REGISTRY) && affDomain.containsActor(IheActorType.RESPONDING_GATEWAY_REPOSITORY))
        {
            communicator = new XcaCommunicator(configuration);
        } else if (affDomain.containsActor(IheActorType.DOC_RECIPIENT))
        {
            communicator = new XdrCommunicator(configuration);
        }

        return communicator;
    }

    public static String parseAuthor(String author)
    {
        Pattern pattern = Pattern.compile("^[\\d\\w]*[\\\\^]([\\d\\w]*)[\\\\^].*&ISO$");
        Matcher matcher = pattern.matcher(author);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }
}
