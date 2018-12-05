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
package org.marc.shic.core.configuration;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

/**
 *
 * @author ibrahimm
 */
public class UnsecureConnection extends IheSocket
{

    private Socket m_socket;
    private Logger logger = Logger.getLogger(UnsecureConnection.class);

    public UnsecureConnection()
    {
    }

    public synchronized boolean isClosed()
    {
        return m_socket == null || m_socket.isClosed();
    }

    public synchronized void connect(String hostname, int port)
    {
        try
        {
            // create the regular socket connection
            m_socket = new Socket(hostname, port);

        } catch (IOException ex)
        {
            ex.printStackTrace();
            logger.error("Unable to open socket connection", ex);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Unable to open socket connection", ex);
        }
    }

    public synchronized void close()
    {
        try
        {
            // close the socket connection
            m_socket.close();
        } catch (IOException ex)
        {
            logger.error("Unable to close socket connection", ex);
        }
    }

    @Override
    public synchronized void write(String data) throws SocketException
    {
        try
        {
            m_socket.getOutputStream().write(data.getBytes());
        } catch (Exception ex)
        {
            logger.error("Unable to write to socket", ex);
            throw new SocketException("Unable to write to socket");
        }
    }

    @Override
    public synchronized int read() throws SocketException
    {
        int readValue = -1;
        try
        {
            readValue = m_socket.getInputStream().read();
        } catch (Exception ex)
        {
            logger.error("Unable to read from socket", ex);
            throw new SocketException("Unable to read from socket");
        }

        return readValue;
    }
    // public synchronized BufferedWriter getBufferedWriter() throws IOException
    // {
    // // get the output stream from the socket, and use it to create a writer
    // BufferedOutputStream os = new
    // BufferedOutputStream(m_socket.getOutputStream());
    // return new BufferedWriter(new OutputStreamWriter(os));
    // }
    //
    // public synchronized BufferedReader getBufferedReader() throws IOException
    // {
    // // get the input stream from the socket, and use it to create a reader
    // BufferedInputStream is = new
    // BufferedInputStream(m_socket.getInputStream());
    // return new BufferedReader(new InputStreamReader(is));
    // }
}
