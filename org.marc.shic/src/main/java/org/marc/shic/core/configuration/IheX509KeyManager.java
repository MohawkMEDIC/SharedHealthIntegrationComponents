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
 * @author Mohamed Ibrahim
 * Date: 17-Sep-2013
 *
 */
package org.marc.shic.core.configuration;

import org.marc.shic.core.exceptions.SslException;
import org.marc.shic.core.utils.SslUtility;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;
import org.apache.log4j.Logger;

/**
 * A custom Key Manager implementation of the X509KeyManager interface
 */
public class IheX509KeyManager implements X509KeyManager {

    private X509KeyManager m_keyManager;
    private static final Logger LOGGER = Logger
            .getLogger(IheX509KeyManager.class);

    public IheX509KeyManager(String keyStore, char[] passphrase)
            throws SslException {
        // load the KeyStore
        KeyStore ks = SslUtility.loadKeyStore(keyStore, passphrase);
        KeyManagerFactory kmf = null;

        try {
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);
        } catch (NoSuchAlgorithmException e) {
            throw new SslException(
                    "No Provider supports a KeyManagerFactorySpi implementation for the specified algorithm",
                    e);
        } catch (KeyStoreException e) {
            throw new SslException(
                    "Unable to initialize the KeyManagerFactory", e);
        } catch (UnrecoverableKeyException e) {
            throw new SslException(
                    "The key cannot be recovered (e.g. the given password is wrong).",
                    e);
        }

		// Look through the key managers created and attempt to find an
        // X509keyManager
        KeyManager[] keyManagers = kmf.getKeyManagers();
        for (int i = 0; i < keyManagers.length; i++) {
            if (keyManagers[i] instanceof X509KeyManager) {
                this.m_keyManager = (X509KeyManager) keyManagers[i];
                return;
            }
        }

        // if an X509KeyManager was not found throw an exception
        throw new SslException("Couldn't initialize X509KeyManager");
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return this.m_keyManager.getClientAliases(keyType, issuers);
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] issuers,
            Socket socket) {
        return this.m_keyManager.chooseClientAlias(keyTypes, issuers, socket);
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return this.m_keyManager.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers,
            Socket socket) {
        return this.m_keyManager.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return this.m_keyManager.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return this.m_keyManager.getPrivateKey(alias);
    }
}
