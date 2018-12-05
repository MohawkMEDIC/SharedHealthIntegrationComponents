/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.marc.shic.xds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;

/**
 *
 * @author Medic
 */
public class XdsSamlSignature {

    private final static Logger LOGGER = Logger.getLogger(XdsSamlSignature.class.getName());
    final static Signature signature = null;
    static String password;
    static String certificateAliasName = "selfsigned";
    static String fileName;

    @SuppressWarnings("static-access")
    public Credential createCredentials(JKSStoreInformation keyStore) {
        this.password = keyStore.getStorePassword();
        this.fileName = keyStore.getStoreFile();
        KeyStore ks = null;
        FileInputStream fis = null;
        char[] password = this.password.toCharArray();

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            LOGGER.error(e);
        }
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
        }
        try {
            ks.load(fis, password);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fis.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Enumeration enums = null;
        try {
            enums = ks.aliases();
        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (; enums.hasMoreElements(); ) {
            String alias = (String)enums.nextElement();
            try {
                if(ks.isKeyEntry(alias)){
                    certificateAliasName = alias;
                    LOGGER.info(alias);
                    break;
                }
            } catch (KeyStoreException ex) {
                java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        KeyStore.PrivateKeyEntry pkEntry = null;
        KeyStore.ProtectionParameter pass = new KeyStore.PasswordProtection(this.password.toCharArray());
        try {
            pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(this.certificateAliasName, pass);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableEntryException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(XdsSamlSignature.class.getName()).log(Level.SEVERE, null, ex);
        }

        PrivateKey pk = pkEntry.getPrivateKey();
        X509Certificate certificate = (X509Certificate) pkEntry.getCertificate();
        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(certificate);
        credential.setPrivateKey(pk);
        return credential;
    }

}
