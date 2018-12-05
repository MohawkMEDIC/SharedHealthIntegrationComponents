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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;

/**
 * A custom Trust Manager implementation of the X509TrustManager interface
 */
public class IheX509TrustManager implements X509TrustManager {

    private X509TrustManager m_trustManager;
    private static final Logger LOGGER = Logger
            .getLogger(IheX509TrustManager.class);

    public IheX509TrustManager(String trustStore, char[] passphrase)
            throws SslException {
        // load the TrustStore
        KeyStore ts = SslUtility.loadKeyStore(trustStore, passphrase);
        TrustManagerFactory tmf = null;

        try {
            tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(ts);
        } catch (NoSuchAlgorithmException e) {
            throw new SslException(
                    "No Provider supports a TrustManagerFactorySpi implementation for the specified algorithm",
                    e);
        } catch (KeyStoreException e) {
            throw new SslException(
                    "Unable to initialize the TrustManagerFactory", e);
        }

		// Look through the trust managers created and attempt to find an
        // X509TrustManager
        TrustManager[] trustManagers = tmf.getTrustManagers();
        for (int i = 0; i < trustManagers.length; i++) {
            if (trustManagers[i] instanceof X509TrustManager) {
                this.m_trustManager = (X509TrustManager) trustManagers[i];
                return;
            }
        }

        // if an X509TrustManager was not found throw an exception
        throw new SslException("Couldn't initialize X509TrustManager");
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            this.m_trustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException e) {
            LOGGER.info(
                    "The Client's certificate chain is not trusted by this TrustManager",
                    e);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            this.m_trustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException e) {
            LOGGER.info(
                    "The Server's certificate chain is not trusted by this TrustManager",
                    e);
            throw e;
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.m_trustManager.getAcceptedIssuers();
    }
}
