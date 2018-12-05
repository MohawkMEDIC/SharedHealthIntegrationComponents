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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.log4j.Logger;
import org.marc.shic.core.exceptions.SslException;
import org.marc.shic.core.utils.SslUtility;

/**
 *
 * @author ibrahimm
 */
public class TLSConnection extends IheSocket {

    private SSLSocket m_socket;
    private final JKSStoreInformation m_keyStore;
    private final JKSStoreInformation m_trustStore;
    private static final Logger LOGGER = Logger.getLogger(SslUtility.class.getName());

    public TLSConnection(JKSStoreInformation keyStore, JKSStoreInformation trustStore) {
        this.m_keyStore = keyStore;
        this.m_trustStore = trustStore;
    }

    @Override
    public synchronized boolean isClosed() {
        return m_socket == null || m_socket.isClosed();
    }

    @Override
    public synchronized void connect(String hostname, int port) throws IOException {
        SSLSocketFactory factory = null;
        
        // attempt to get a custom SSLSocketFactory
        try {
            factory = SslUtility.getSSLSocketFactory(m_keyStore, m_trustStore);
        } catch (SslException ex) {
            LOGGER.error(ex);
            
            // couldn't create a custom SSLSocketFactory, use default..
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        
        try {
            m_socket = (SSLSocket) factory.createSocket(hostname, port);
            m_socket.setUseClientMode(true);
            m_socket.startHandshake();

        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public synchronized void close() {
        try {
            // close the socket connection
            m_socket.close();
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    @Override
    public synchronized void write(String data) throws SocketException {
        try
        {
            m_socket.getOutputStream().write(data.getBytes());
        } catch (Exception ex)
        {
            LOGGER.error("Unable to write to socket", ex);
            throw new SocketException("Unable to write to socket");
        }
    }

    @Override
    public synchronized int read() throws SocketException {
        int readValue = -1;
        try
        {
            readValue = m_socket.getInputStream().read();
        } catch (Exception ex)
        {
            LOGGER.error("Unable to read from socket", ex);
            throw new SocketException("Unable to read from socket");
        }

        return readValue;
    }

    @Deprecated
    public synchronized BufferedWriter getBufferedWriter() throws IOException {
        // get the output stream from the socket, and use it to create a writer
        BufferedOutputStream os = new BufferedOutputStream(
                m_socket.getOutputStream());
        return new BufferedWriter(new OutputStreamWriter(os));
    }

    @Deprecated
    public synchronized BufferedReader getBufferedReader() throws IOException {
        // get the input stream from the socket, and use it to create a reader
        BufferedInputStream is = new BufferedInputStream(
                m_socket.getInputStream());
        return new BufferedReader(new InputStreamReader(is));
    }
}
