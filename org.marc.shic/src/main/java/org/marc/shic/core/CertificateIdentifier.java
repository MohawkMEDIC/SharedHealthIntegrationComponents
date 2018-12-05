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
 * @author Nityan Khanna
 * Date: 12-Mar-2014
 *
 */
package org.marc.shic.core;

/**
 * A class containing properties to identify a certificate.
 * @author Nityan Khanna
 */
public class CertificateIdentifier {
    
    private String commonName;
    private String organization;
    private String organizationalUnit;
    private String locality;
    private String state;
    private String country;
    
    /**
     * Initializes a new instance of the CertificateIdentifier class.
     */
    public CertificateIdentifier() {
    }

    /**
     * Initializes a new instance of the CertificateIdentifier class with a CN, O, OU, L, ST, and C.
     * @param commonName The common name.
     * @param organization The organization.
     * @param organizationalUnit The organizational unit.
     * @param locality The locality.
     * @param state The state or province.
     * @param country The country.
     */
    public CertificateIdentifier(String commonName, String organizationalUnit, String organization, String locality, String state, String country) {
        this.commonName = commonName;
        this.organizationalUnit = organizationalUnit;
        this.organization = organization;
        this.locality = locality;
        this.state = state;
        this.country = country;
    }

    /**
     * Gets the common name.
     * @return Returns the common name.
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * Gets the country.
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the locality.
     * @return Returns the locality.
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Gets the organization.
     * @return Returns the organization.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Gets the organizational unit.
     * @return Returns the organizational unit.
     */
    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    /**
     * Gets the state/province.
     * @return Returns the state/province.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the common name.
     * @param commonName The common name.
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * Sets the country.
     * @param country The country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the locality.
     * @param locality The locality.
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * Sets the organization.
     * @param organization The organization.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Sets the organizational unit.
     * @param organizationalUnit The organizational unit.
     */
    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    /**
     * Sets the state.
     * @param state The state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the certificate as a string in the form of "CN=..., OU=..., O=..., L=..., ST=..., C=...
     * @return Returns the certificate as a string in the form of "CN=..., OU=..., O=..., L=..., ST=..., C=...
     */
    @Override
    public String toString() {
        return "CN=" + commonName + ", OU=" + organizationalUnit + ", O=" + organization + ", L=" + locality + ", ST=" + state + ", C=" + country;
    }
}
    
    