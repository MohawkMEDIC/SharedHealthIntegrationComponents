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
package org.marc.shic.core.configuration;

import java.io.IOException;
import java.net.URI;

/**
 *
 * @author ibrahimm
 */
// TODO: Possibly rename. Unrelated to IHE.
public class IheSocketConnection {

    private IheSocket m_connection;
    private URI m_endpoint;
    private JKSStoreInformation m_keyStore;
    private JKSStoreInformation m_trustStore;
    private boolean m_secure;

    public IheSocketConnection(URI endpoint, JKSStoreInformation keyStore, JKSStoreInformation trustStore, boolean secure) {
        this.m_endpoint = endpoint;
        this.m_keyStore = keyStore;
        this.m_trustStore = trustStore;
        this.m_secure = secure;
    }

    public synchronized boolean isClosed() {
        return m_connection == null || m_connection.isClosed();
    }

    public synchronized void connect() throws IOException {
        
	// instanciate the socket connection based on the secure property & protpcol (UDP)
        if (m_endpoint.getScheme().equalsIgnoreCase("udp")) {
            // udp is inherently non-secure
            m_connection = new UdpConnection();
        } else {
            // check security setting
            if (m_secure) {
                // secure tcp/mllp
                m_connection = new TLSConnection(m_keyStore, m_trustStore);
            } else {
                // unsecure tcp/mllp
                m_connection = new UnsecureConnection();
            }
        }

        // connect
        m_connection.connect(m_endpoint.getHost(), m_endpoint.getPort());
    }

    public synchronized void close() {
        // close the socket connection
        m_connection.close();
    }

    public synchronized void write(String data) throws IOException {
        m_connection.write(data);
    }

    public synchronized int read() throws IOException {
        return m_connection.read();
    }
}
