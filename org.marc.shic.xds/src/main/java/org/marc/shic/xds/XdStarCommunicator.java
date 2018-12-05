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
 * Date: Nov 28, 2013
 *
 */
package org.marc.shic.xds;

import javax.net.ssl.SSLSocketFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOMFeature;
import org.marc.shic.core.IheHttpCommunicator;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.SslException;
import org.marc.shic.core.utils.SslUtility;
import org.marc.shic.xds.registry.DocumentRegistryPortType;
import org.marc.shic.xds.registry.DocumentRegistryService;
import org.marc.shic.xds.repository.DocumentRepositoryPortType;
import org.marc.shic.xds.repository.DocumentRepositoryService;

/**
 *
 * @author tylerg
 */
public abstract class XdStarCommunicator extends IheHttpCommunicator
{

    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String REGISTRY_SOAP_PORT = "DocumentRegistry_Port_Soap12";
    private static final String REPOSITORY_SOAP_PORT = "DocumentRepository_Port_Soap12";
    protected static final String REGISTRY_SUCCESS_STATUS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
 
  
    public XdStarCommunicator(IheConfiguration configuration)
            throws ActorNotFoundException
    {
        super(configuration);
    }

    /**
     * Opens a connection to the actor specified if one is not already open. If
     * a connection is already open then does nothing.
     */
    @Override
    protected void open(IheActorConfiguration system)
    {

        // First, fetch the actor type from the affinity domain configuration
        if (system == null || this.m_bindings.containsKey(system)) // Already created an actor in this configuration
        {
            return;
        }

        // Create the service for this system
        Service service;
        BindingProvider binding = null;

        // Create appropriate service 
        if (system.getActorType().equals(IheActorType.DOC_REGISTRY) || system.getActorType().equals(IheActorType.RESPONDING_GATEWAY_REGISTRY))
        {
            service = new DocumentRegistryService();
            binding = (BindingProvider) service.getPort(new QName(NAMESPACE_URI, REGISTRY_SOAP_PORT), DocumentRegistryPortType.class, new AddressingFeature(true, true));

        } else if (system.getActorType().equals(IheActorType.DOC_REPOSITORY) || system.getActorType().equals(IheActorType.DOC_RECIPIENT) || system.getActorType().equals(IheActorType.RESPONDING_GATEWAY_REPOSITORY))
        {
            service = new DocumentRepositoryService();
            binding = (BindingProvider) service.getPort(new QName(NAMESPACE_URI, REPOSITORY_SOAP_PORT), DocumentRepositoryPortType.class, new AddressingFeature(true, true), new MTOMFeature(true, 1));
        } else
        {
            throw new IllegalArgumentException(String.format("Unsupported actor: %s", system.getActorType().toString()));
        }
        
        // Add to the binding collection
        this.m_bindings.put(system, binding);
    }

    private SSLSocketFactory getCustomSSLSocketFactory()
    {
        SSLSocketFactory retVal;

        try
        {
            // attempt to get a custom SSLSocketFactory using both stores in the configuration (keystore & truststore)
            retVal = SslUtility.getSSLSocketFactory(this.getConfiguration().getKeyStore(), this.getConfiguration().getTrustStore());
        } catch (SslException ex)
        {
            LOGGER.info("Couldn't create a custom SSLSocketFactory, using default.", ex);
            // couldn't create a custom SSLSocketFactory, use default..
            retVal = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }

        return retVal;
    }

  
}
