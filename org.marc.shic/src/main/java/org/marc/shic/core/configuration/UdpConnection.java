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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author tylerg
 */
public class UdpConnection extends IheSocket {

	private DatagramSocket socket;

	public UdpConnection() {
	}

	@Override
	public boolean isClosed() {
		return socket == null || !socket.isConnected();
	}

	@Override
	public void connect(String hostname, int port) {
		try {
			socket = new DatagramSocket();
			socket.connect(InetAddress.getByName(hostname), port);

		} catch (SocketException ex) {
			Logger.getLogger(UdpConnection.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (UnknownHostException ex) {
			Logger.getLogger(UdpConnection.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@Override
	public void close() {
		socket.close();
	}

	public synchronized void write(String data) throws IOException {
		byte[] messageBytes = data.getBytes();
		DatagramPacket packet = new DatagramPacket(messageBytes,
				messageBytes.length);

		socket.send(packet);
	}

	public synchronized int read() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
