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
 * Date: 13-Sep-2013
 *
 */
package org.marc.shic.core.utils;

import org.marc.shic.core.exceptions.SslException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.security.auth.x500.X500Principal;

import org.apache.log4j.Logger;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.marc.shic.core.CertificateIdentifier;
import org.marc.shic.core.configuration.IheX509KeyManager;
import org.marc.shic.core.configuration.IheX509TrustManager;
import org.marc.shic.core.configuration.JKSStoreInformation;

/**
 * A utility class that handles the creation, setup of the KeyStore(s) and
 * TrustStore(s) and their entries.
 *
 */
public class SslUtility
{
    private static final Logger LOGGER = Logger.getLogger(SslUtility.class.getName());
    private static final String CSR_SIGNING_ALGORITHM = "SHA1withRSA";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final int KEY_SIZE = 2048;
    private static final String PRIVATEKEY_FILE_EXTENSION = ".privateKey";

    /**
     * Utility method that generates a KeyPair (public and private key
     * combination)
     *
     * @return
     * @throws SslException
     */
    public static KeyPair generateKeyPair() throws SslException
    {
        KeyPair retVal = null;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyGen.initialize(KEY_SIZE, new SecureRandom());
            retVal = keyGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new SslException(
                    "No provider supports the implementation of the key algorithm",
                    e);
        }

        return retVal;
    }

    /**
     * Stores (serializes) an RSA key through its modulus and exponent tokens.
     *
     * @param folder
     *                 Path to the folder to store the key file into
     * @param alias
     *                 The alias of the Private key (used to name the file)
     * @param modulus
     *                 Modulus of the Key
     * @param exponent
     *                 Exponent of the Key
     * @throws SslException
     */
    public static void serializePrivateKey(String folder, String alias,
                                           BigInteger modulus, BigInteger exponent) throws SslException
    {
        ObjectOutputStream oos = null;
        String file = folder + alias + PRIVATEKEY_FILE_EXTENSION;

        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));
            oos.writeObject(modulus);
            oos.writeObject(exponent);

        } catch (FileNotFoundException e) {
            throw new SslException("Unable to create file: " + file, e);
        } catch (IOException e) {
            throw new SslException("I/O stream error", e);
        } finally {
            closeStream(oos);
        }
    }

    /**
     * Utility method that retrieves a PrivateKey serialized into a given file
     *
     * @param folder
     *               The folder containing the serialized Private Key file
     * @param alias
     *               The alias of the Private key (used to name the file)
     * @return
     * @throws SslException
     */
    public static PrivateKey deserializePrivateKey(String folder, String alias)
            throws SslException
    {
        PrivateKey retVal = null;
        ObjectInputStream ois = null;
        String file = folder + alias + PRIVATEKEY_FILE_EXTENSION;

        try {
            InputStream is = new FileInputStream(file);
            ois = new ObjectInputStream(new BufferedInputStream(is));

            BigInteger mod = (BigInteger) ois.readObject();
            BigInteger exp = (BigInteger) ois.readObject();
            RSAPrivateKeySpec spec = new RSAPrivateKeySpec(mod, exp);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            retVal = keyFactory.generatePrivate(spec);

        } catch (FileNotFoundException e) {
            throw new SslException("File not found: " + file, e);
        } catch (NoSuchAlgorithmException e) {
            throw new SslException(
                    "No provider supports the implementation of the key algorithm",
                    e);
        } catch (InvalidKeySpecException e) {
            throw new SslException(
                    "The given Key Specification is inappropriate for the key factory to produce a private key",
                    e);
        } catch (IOException e) {
            throw new SslException("I/O stream error", e);
        } catch (ClassNotFoundException e) {
            throw new SslException(
                    "The class of the serialized object was not found (BigInteger)",
                    e);
        } finally {
            closeStream(ois);
        }

        return retVal;
    }

    /**
     * Checks whether a given certificate is self-signed or signed by a
     * Certificate Authority. A possible use is to check whether a certificate
     * is a Root/CA certificate or client certificate.
     *
     * @param certificate
     *
     * @return
     *
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static boolean isSelfSigned(X509Certificate certificate)
            throws SslException
    {
        boolean retVal = true;

        // if the certificate signature verfies against its public key, then its
        // self-signed/root
        try {
            PublicKey key = certificate.getPublicKey();
            certificate.verify(key);

        } catch (InvalidKeyException e) {
            LOGGER.info("Incorrect public key", e);
            retVal = false;
        } catch (SignatureException e) {
            LOGGER.info("Signature error", e);
            retVal = false;
        } catch (CertificateException e) {
            throw new SslException(
                    "There is a problem with the certificate's encoding", e);
        } catch (NoSuchAlgorithmException e) {
            throw new SslException("Unsupported signature algorithm", e);
        } catch (NoSuchProviderException e) {
            throw new SslException("There is no default provider", e);
        }

        return retVal;
    }

    /**
     * Extracts and returns a list of X.509 certificates found in a given file
     *
     * @param file
     * @return
     * @throws CertificateException
     */
    public static ArrayList<X509Certificate> extractX509Certificates(File file)
            throws SslException
    {
        ArrayList<X509Certificate> retVal = new ArrayList<X509Certificate>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            // use the CertificateFactory to extract all certificates
            CertificateFactory certificateFactory = CertificateFactory
                    .getInstance("X.509");
            retVal.addAll((List<X509Certificate>) certificateFactory
                    .generateCertificates(fis));

        } catch (IOException e) {
            throw new SslException("I/O stream error", e);
        } catch (CertificateException e) {
            throw new SslException("Certificate parsing (encoding) error", e);
        } finally {
            closeStream(fis);
        }

        return retVal;
    }
    
    /**
     * Extracts and returns a list of X.509 certificates found in a given file
     *
     * @param file
     * @return
     * @throws CertificateException
     */
    public static ArrayList<X509Certificate> extractX509Certificates(InputStream input)
            throws SslException
    {
        ArrayList<X509Certificate> retVal = new ArrayList<X509Certificate>();

        try {

            // use the CertificateFactory to extract all certificates
            CertificateFactory certificateFactory = CertificateFactory
                    .getInstance("X.509");
            retVal.addAll((List<X509Certificate>) certificateFactory
                    .generateCertificates(input));
        } catch (CertificateException e) {
            throw new SslException("Certificate parsing (encoding) error", e);
        }

        return retVal;
    }

    /**
     * A utility method that finds and retrieves a list of Root/CA certificates
     * from a list of certificates
     *
     * @param certificates
     * @return
     * @throws SslException
     */
    public static ArrayList<X509Certificate> retrieveCACertificates(
            ArrayList<X509Certificate> certificates) throws SslException
    {
        ArrayList<X509Certificate> retVal = new ArrayList<X509Certificate>();

        for (X509Certificate certificate : certificates) {
            // CA certificates (both root and intermediate) are usually
            // self-signed, and have a BasicConstraints value of != -1 (true)
            if (certificate.getBasicConstraints() != -1
                    || isSelfSigned(certificate)) {
                retVal.add(certificate);
            }
        }

        return retVal;
    }

    /**
     * Utility method to swallow Exceptions thrown while closing streams.
     *
     * @param stream
     */
    public static void closeStream(Closeable stream)
    {
        try {
            if (null != stream) {
                stream.close();
            }
        } catch (Exception e) {
            LOGGER.info("Problem closing stream", e);
        }
    }

    /**
     * Utility method to load a KeyStore from the file
     *
     * @param keyStoreFile
     *                     The file containing the key store
     * @param passphrase
     *                     The key store's passphrase (null if no passphrase was used to
     *                     create the key store)
     * @return
     * @throws SslException
     */
    public static KeyStore loadKeyStore(String keyStoreFile, char[] passphrase)
            throws SslException
    {
        KeyStore retVal = null;

        FileInputStream keyStoreFis = null;
        try {
            keyStoreFis = new FileInputStream(keyStoreFile);
        } catch (IOException e) {
            LOGGER.info(e);
        }
        
        try {
            retVal = KeyStore.getInstance(KEYSTORE_TYPE);
            retVal.load(keyStoreFis, passphrase);

        } catch (KeyStoreException e) {
            throw new SslException(
                    "There is if no Provider to support the KeyStoreSpi implementation for the specified KeyStore type",
                    e);
        } catch (IOException e) {
            throw new SslException("I/O stream error", e);
        } catch (NoSuchAlgorithmException e) {
            throw new SslException(
                    "No algorithm was found to check the integrity of the KeyStore",
                    e);
        } catch (CertificateException e) {
            throw new SslException(
                    "Unable to load certificate(s) within the KeyStore", e);
        } finally {
            closeStream(keyStoreFis);
        }

        return retVal;
    }

    public static SSLSocketFactory getSSLSocketFactory(JKSStoreInformation keyStore, JKSStoreInformation trustStore) throws SslException {
        SSLSocketFactory retVal = null;
        try {
            // create the key & trust managers using our custom x509 managers
            KeyManager[] km = getKeyManagers(keyStore);
            TrustManager[] tm = getTrustManagers(trustStore);

            // Create the context that is used to get the SSLSocketFactory
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(km, tm, new SecureRandom());
            
            retVal = context.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e);
            throw new SslException("No algorithm was found", e);
        } catch (KeyManagementException e) {
            LOGGER.error(e);
            throw new SslException("Unable to initialize the SSLContext, check the keystore and truststore", e);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new SslException("An error occurred while creating a custom SSLSocketFactory", e);
        }

        return retVal;
    }
    
    
    /**
     * Generates a PKCS10 Certification Request object from a public key, private key, and a certificate identifier.
     * @param publicKey The public key.
     * @param privateKey The private key.
     * @param principal The certificate identifier containing the properties for the certificate.
     * @return Returns a PKCS10CertificationRequest the request object
     * @throws SslException 
     */
    public static PKCS10CertificationRequest generatePKCS10(PublicKey publicKey, PrivateKey privateKey, CertificateIdentifier principal) throws SslException 
    {
    	PKCS10CertificationRequest retVal = null;
    	X500Principal subject = new X500Principal(principal.toString());
    	
        try {
            ContentSigner signer = new JcaContentSignerBuilder(CSR_SIGNING_ALGORITHM).build(privateKey);
            PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
            retVal = builder.build(signer);
            
        } catch (OperatorCreationException e) {
            LOGGER.error(e);
            throw new SslException("Problem while creating the content signer", e);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new SslException("An error occurred while creating a CSR", e);
        }
        
        return retVal;
    }
    
    /**
     * Generates a CSR from a public key, private key, and a certificate identifier.
     * @param publicKey The public key.
     * @param privateKey The private key.
     * @param principal The certificate identifier containing the properties for the certificate.
     * @return Returns a String containing the CSR.
     * @throws SslException 
     */
    public static String generateCSR(PKCS10CertificationRequest request) throws SslException 
    {
        StringWriter stringWriter = new StringWriter();
        
        try {
            JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
            writer.writeObject(request);
            writer.close();
            
        } catch (IOException e) {
            LOGGER.error(e);
            throw new SslException("IO error while writing the CSR", e);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new SslException("An error occurred while creating a CSR", e);
        }
        
        return stringWriter.getBuffer().toString();
    }

    public static KeyManager[] getKeyManagers(JKSStoreInformation keyStore) throws SslException {
        return new KeyManager[] {new IheX509KeyManager(keyStore.getStoreFile(), keyStore.getStorePassword().toCharArray())};
    }

    public static TrustManager[] getTrustManagers(JKSStoreInformation trustStore) throws SslException {
        return new TrustManager[] {new IheX509TrustManager(trustStore.getStoreFile(), trustStore.getStorePassword().toCharArray())};
    }
}
