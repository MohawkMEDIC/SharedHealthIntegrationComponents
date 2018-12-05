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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marc.shic.core.DomainIdentifier;

/**
 *
 * @author Mohamed
 */
public class IheConfiguration implements Serializable {
    
    private DomainIdentifier identifier;
    private IheIdentification localIdentification;
    private IheAffinityDomainConfiguration affinityDomain;
//    private List<IheAffinityDomainConfiguration> affinityDomains;
    private JKSStoreInformation keyStore;
    private JKSStoreInformation trustStore;
//    private static final Object lockObject = new Object();

    /*
     * Default constructor is needed for serialization
     */
    public IheConfiguration() {
    }

//    public IheConfiguration(List<IheAffinityDomainConfiguration> affDoms) {
//        this.affinityDomains = affDoms;
//    }

    public DomainIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(DomainIdentifier identifier) {
        this.identifier = identifier;
    }

    public IheIdentification getLocalIdentification() {
        return localIdentification;
    }

    public void setLocalIdentification(IheIdentification localIdentification) {
        this.localIdentification = localIdentification;
    }

    public IheAffinityDomainConfiguration getAffinityDomain() {
        return affinityDomain;
    }

    public void setAffinityDomain(IheAffinityDomainConfiguration affinityDomain) {
        this.affinityDomain = affinityDomain;
    }

    /**
     * Gets all affinity domains
     *
     * @return List of all affinity domains
     */
//    public List<IheAffinityDomainConfiguration> getAffinityDomains() {
//        synchronized (lockObject) {
//            if (this.affinityDomains == null) {
//                this.affinityDomains = new ArrayList<IheAffinityDomainConfiguration>();
//            }
//            return this.affinityDomains;
//        }
//    }

//    public void setAffinityDomains(List<IheAffinityDomainConfiguration> affDoms) {
//        this.affinityDomains = affDoms;
//    }

    public JKSStoreInformation getKeyStore() {
        return this.keyStore;
    }

    public void setKeyStore(JKSStoreInformation store) {
        this.keyStore = store;
    }

    /**
     *
     * @return
     */
    public JKSStoreInformation getTrustStore() {
        return this.trustStore;
    }

    public void setTrustStore(JKSStoreInformation store) {
        this.trustStore = store;
    }

    /**
     * Gets a specific affinity domain configuration by its name
     *
     * @param name The name of the affinity domain
     * @return The Affinity Domain configuration object.
     */
//    public IheAffinityDomainConfiguration getAffinityDomain(String name) {
//        IheAffinityDomainConfiguration retVal = null;
//        // find the affinity domain with value of its getName() matching name
//        try {
//            for (IheAffinityDomainConfiguration affinityDomain : affinityDomains) {
//                if (affinityDomain.getName().equalsIgnoreCase(name)) {
//                    retVal = affinityDomain;
//                }
//            }
//        } catch (NullPointerException e) {
//            throw new NullPointerException("No domain found:" + e.toString());
//        }
//
//        return retVal;
//    }

    /**
     *
     * @return
     */
//    public static IheConfiguration load(String filePath) {
//        IheConfiguration config = null;
//
//        synchronized (lockObject) {
//            try {
//                XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
//                        new FileInputStream(filePath)));
//                config = (IheConfiguration) decoder.readObject();
//            } catch (Exception ex) {
//                System.err.print("Error reading config file: "
//                        + ex.getMessage());
//            }
//
//			// override the default keystore and truststore used by java.
//            // System.setProperty("javax.net.debug", "all");
//            System.setProperty("javax.net.ssl.keyStore", config.getKeyStore()
//                    .getStoreFile());
//            System.setProperty("javax.net.ssl.keyStorePassword", config
//                    .getKeyStore().getStorePassword());
//            System.setProperty("javax.net.ssl.trustStore", config
//                    .getTrustStore().getStoreFile());
//            System.setProperty("javax.net.ssl.trustStorePassword", config
//                    .getTrustStore().getStorePassword());
//
//            return config;
//        }
//    }

    /**
     * Serializes the current IheConfiguration object to an XML file.
     *
     * @return Success
     */
//    public Boolean save(String filePath) {
//        boolean succeeded = true;
//        synchronized (lockObject) {
//            try {
//                FileOutputStream os = new FileOutputStream(filePath);
//                XMLEncoder encoder = new XMLEncoder(os);
//                encoder.writeObject(this);
//                encoder.close();
//            } catch (Exception ex) {
//                succeeded = false;
//                Logger.getLogger(IheConfiguration.class.getName()).log(
//                        Level.SEVERE,
//                        "There was a problem serializing the configuration.",
//                        ex);
//            }
//            return succeeded;
//
//        }
//    }
}
