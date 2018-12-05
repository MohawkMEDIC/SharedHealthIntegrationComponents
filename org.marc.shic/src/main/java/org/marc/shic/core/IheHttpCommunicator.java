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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.core;

import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.configuration.IheActorConfiguration;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import static org.marc.shic.core.IheCommunicator.LOGGER;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.exceptions.SslException;
import org.marc.shic.core.utils.SslUtility;

/**
 *
 * @author Garrett
 */
public abstract class IheHttpCommunicator extends IheCommunicator {

    protected Map<IheActorConfiguration, BindingProvider> m_bindings = new HashMap<IheActorConfiguration, BindingProvider>();

    public IheHttpCommunicator(IheConfiguration configuration)
            throws ActorNotFoundException {
        super(configuration);
    }

    protected abstract void open(IheActorConfiguration system);

    public BindingProvider getBinding(IheActorConfiguration system) {
        // Does the system already exist in the opened bindings
        BindingProvider binding = this.m_bindings.get(system);

        // Doesn't exist, so try to open
        if (binding == null) {
            this.open(system);
            binding = this.m_bindings.get(system);
        }
        
        // since we are using a wsdl-first approach, the endpoint needs to be modified
        binding.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, system.getEndPointAddress().toString());
        
        // Attach TLS parameters to the request (keystores, truststores, etc...).
        // This eliminates the need for Spring (or any other framework) to manage cxf configuration
        // create a Client from the proxy/port
        Client client = ClientProxy.getClient(binding);
        HTTPConduit conduit = (HTTPConduit)client.getConduit();
        
        // CXF will instantiate a new Conduit to handle the request if the endpoint is changed on the binding provider (above line)
        // It's a not-so-known bug. The workaround is to override the endpoint on the conduit instead (below), that way
        // this conduit is used (contains all our parameters for this request)..
        // bug https://www.mail-archive.com/users@cxf.apache.org/msg27968.html
        conduit.getTarget().getAddress().setValue(system.getEndPointAddress().toString());
        
        TLSClientParameters tls = new TLSClientParameters();
        try {
            tls.setSSLSocketFactory(SslUtility.getSSLSocketFactory(this.getConfiguration().getKeyStore(), this.getConfiguration().getTrustStore()));
            tls.setKeyManagers(SslUtility.getKeyManagers(m_configuration.getKeyStore()));
            tls.setTrustManagers(SslUtility.getTrustManagers(m_configuration.getTrustStore()));
            
        } catch (SslException ex) {
            LOGGER.warn(ex);
        }
        
        // temporarily disable CN check
        tls.setDisableCNCheck(true);
        conduit.setTlsClientParameters(tls);
        
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(36000);
        policy.setAllowChunking(false);
        policy.setReceiveTimeout(32000);
        conduit.setClient(policy);
        
        return binding;
    }
}
