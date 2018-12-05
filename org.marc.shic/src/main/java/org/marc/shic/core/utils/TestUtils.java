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
package org.marc.shic.core.utils;

import org.marc.shic.core.AddressUse;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.configuration.IheConfiguration;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;

/**
 *
 * @author clarkc
 */
public class TestUtils
{

    // IHE Pre-connectathon
//    public static String AssigningAuthority = "NIST2010";
//    public static String RootId = "2.16.840.1.113883.3.72.5.9.1";
    // Openpix
    //public static String AssigningAuthority = "MARCHI";
    // public static String RootId = "1.3.6.1.4.1.33346.6.1.99";
    public static String AssigningAuthority = "MiH_OSCAR_A";
    public static String RootId = "1.3.6.1.4.1.33349.3.1.3.201203.2.0.0";
    public static String IHENA_AssigningAuthority = "MARCHI";

    public static PersonDemographic generateRandomPatient()
    {
        PersonDemographic patient = new PersonDemographic();

        patient.addName(NameGenerator.generateFullName());

        DomainIdentifier patientId = new DomainIdentifier();
        patientId.setExtension(UUID.randomUUID().toString());
        patientId.setAssigningAuthority(AssigningAuthority);
        patientId.setRoot(RootId);

        // Change to random gender.
        patient.setGender(Gender.M);

        // Change to random date of birth.
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1976, 12, 6);
        patient.setDateOfBirth(dateOfBirth);

        patient.addAddress(createAddress(AddressUse.Home, "456 " + NameGenerator.generateName() + " Ave", NameGenerator.generateName(), NameGenerator.generateName(), NameGenerator.generateName(), "FAS FAF"));

        return patient;
    }

    public static PersonDemographic getDemoPatient()
    {
        PersonDemographic myPerson = new PersonDemographic();
        DomainIdentifier personId = new DomainIdentifier();

        // Populate DomainIdentifier
        personId.setExtension("522457318");
        //   personId.setAssigningAuthority("IHENA");
        personId.setAssigningAuthority(AssigningAuthority);
        personId.setRoot(RootId);
        //  personId.setRoot("IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO");
        // personId.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.1.1.0");
        //     personId.setRoot("1.3.6.1.4.1.21367.2010.1.2.300");
        myPerson.addIdentifier(personId);

        // Name
        myPerson.addName(createPersonName(NameUse.Legal, "Craigy", "", "Clarky"));

        // Address
        myPerson.addAddress(createAddress(AddressUse.Home, "456 Fake Ave", "Fakeburg", "Fakeshire", "Fakeland", "FAS FAF"));

        // Populate Date of Birth
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1976, 12, 6);
        myPerson.setDateOfBirth(dateOfBirth);

        // Set Gender
        myPerson.setGender(Gender.M);

        // Set phone
        myPerson.addPhone("tel:905-555-5555");

        return myPerson;
    }
    
    public static DocumentMetaData getNistDemoDocument() {
        DocumentMetaData document = new DocumentMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
//        identifier.setRoot("1.3.6.1.4.1.21367.2005.3.7");
        
        document.setSourceId("1.3.6.1.4.1.21367.2005.3.7");
        
        document.setServiceTimeStart(Calendar.getInstance());
        document.setServiceTimeEnd(Calendar.getInstance());

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
//        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.21367.2005.3.7");
        author.addIdentifier(authInfo);

        document.setAuthor(author);
        
        document.setMimeType("text/xml");
        document.setTitle("First Doc");
        document.setCreationTime(new GregorianCalendar(2012, 10, 7));
        byte[] content = "doc11".getBytes();
        document.setContent(content);

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");

        document.setContentType(contentType);

        CodeValue classCode = new CodeValue();
        classCode.setCode("History and Physical");
        classCode.setCodeSystem("Connect-a-thon classCodes");
        classCode.setDisplayName("History and Physical");
        document.setClassCode(classCode);

        CodeValue confidentialityCode = new CodeValue();
        confidentialityCode.setCode("N");
        confidentialityCode.setCodeSystem("2.16.840.1.113883.5.25");
        confidentialityCode.setDisplayName("Normal");
        document.addConfidentiality(confidentialityCode);

        CodeValue formatCode = new CodeValue();
        formatCode.setCode("PDF");
        formatCode.setCodeSystem("Connect-a-thon formatCodes");
        formatCode.setDisplayName("PDF");
        document.setFormat(formatCode);

        CodeValue healthcareFacilityTypeCode = new CodeValue();
        healthcareFacilityTypeCode.setCode("Outpatient");
        healthcareFacilityTypeCode.setCodeSystem("Connect-a-thon healthcareFacilityTypeCodes");
        healthcareFacilityTypeCode.setDisplayName("Outpatient");
        document.setFacilityType(healthcareFacilityTypeCode);

        CodeValue practiceSettingCode = new CodeValue();
        practiceSettingCode.setCode("General Medicine");
        practiceSettingCode.setCodeSystem("Connect-a-thon practiceSettingCodes");
        practiceSettingCode.setDisplayName("General Medicine");
        document.setPracticeSetting(practiceSettingCode);

        CodeValue typeCode = new CodeValue();
        typeCode.setCode("34133-9");
        typeCode.setCodeSystem("LOINC");
        typeCode.setDisplayName("Summarization of Episode Note");
        document.setType(typeCode);

        document.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");
        document.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");
        
        return document;
    }
    
    public static DocumentMetaData getDemoDocument() {
        DocumentMetaData document = new DocumentMetaData();
//        DomainIdentifier identifier = new DomainIdentifier();
//        identifier.setExtension("");
//        identifier.setAssigningAuthority("MiH_OSCAR_A");
//        identifier.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
//        document.setId(identifier);
        
        document.setSourceId("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        
        document.setServiceTimeStart(Calendar.getInstance());
        document.setServiceTimeEnd(Calendar.getInstance());

        PersonDemographic author = new PersonDemographic();
        //author.setGender(Gender.M);
        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart("doctor", PartType.Given);
        authName.addNamePart("oscardoc", PartType.Family);
        author.addName(authName);
        author.addPhone("905-111-1111");

        DomainIdentifier authInfo = new DomainIdentifier();
        authInfo.setAssigningAuthority("MiH_OSCAR_A");
        authInfo.setExtension("999998");
        authInfo.setRoot("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0");
        author.addIdentifier(authInfo);

        document.setAuthor(author);
        
        document.setMimeType("text/xml");
        document.setTitle("First Doc");
        document.setCreationTime(new GregorianCalendar(2012, 10, 7));
        byte[] content = "doc11".getBytes();
        document.setContent(content);

        CodeValue contentType = new CodeValue();
        contentType.setCode("History and Physical");
        contentType.setCodeSystem("Connect-a-thon contentTypeCodes");
        contentType.setDisplayName("History and Physical");

        document.setContentType(contentType);

        CodeValue classCode = new CodeValue();
        classCode.setCode("History and Physical");
        classCode.setCodeSystem("Connect-a-thon classCodes");
        classCode.setDisplayName("History and Physical");
        document.setClassCode(classCode);

        CodeValue confidentialityCode = new CodeValue();
        confidentialityCode.setCode("N");
        confidentialityCode.setCodeSystem("2.16.840.1.113883.5.25");
        confidentialityCode.setDisplayName("Normal");
        document.addConfidentiality(confidentialityCode);

        CodeValue formatCode = new CodeValue();
        formatCode.setCode("PDF");
        formatCode.setCodeSystem("Connect-a-thon formatCodes");
        formatCode.setDisplayName("PDF");
        document.setFormat(formatCode);

        CodeValue healthcareFacilityTypeCode = new CodeValue();
        healthcareFacilityTypeCode.setCode("Outpatient");
        healthcareFacilityTypeCode.setCodeSystem("Connect-a-thon healthcareFacilityTypeCodes");
        healthcareFacilityTypeCode.setDisplayName("Outpatient");
        document.setFacilityType(healthcareFacilityTypeCode);

        CodeValue practiceSettingCode = new CodeValue();
        practiceSettingCode.setCode("General Medicine");
        practiceSettingCode.setCodeSystem("Connect-a-thon practiceSettingCodes");
        practiceSettingCode.setDisplayName("General Medicine");
        document.setPracticeSetting(practiceSettingCode);

        CodeValue typeCode = new CodeValue();
        typeCode.setCode("34133-9");
        typeCode.setCodeSystem("LOINC");
        typeCode.setDisplayName("Summarization of Episode Note");
        document.setType(typeCode);

        document.addExtendedAttribute("legalAuthenticator", "999998^oscardoc^doctor^^dr^^^^^&1.3.6.1.4.1.33349.3.1.3.201203.2.0.0&ISO");
        document.addExtendedAttribute("authorInstitution", "Test Oscar Clinic");
        
        return document;
    }

    /**
     * A utility method that generates an Author for testing
     *
     * @return
     */
    public static PersonDemographic generateDemoAuthor()
    {
        PersonDemographic retVal = new PersonDemographic();
        retVal.setGender(Gender.M);
        retVal.setDateOfBirth(new GregorianCalendar(1980, 8, 21));
        retVal.addPhone("tel:905-575-1212");

        retVal.addIdentifier(new DomainIdentifier("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0", "author-extension", "MiH_OSCAR_A"));
        retVal.addName(createPersonName(NameUse.Legal, "Mo", "G", "Ibrahim"));
        retVal.addAddress(createAddress(AddressUse.Workplace, "135 Fennell Avenue West", "Hamilton", "Ontario", "Canada", "L8N 3T2"));

        return retVal;
    }

    public static LocationDemographic generateCorporateLocation()
    {
        LocationDemographic retVal = new LocationDemographic();
        retVal.addIdentifier(new DomainIdentifier("1.3.6.1.4.1.33349.3.1.3.201203.2.0.0", "location-extension", "MiH_OSCAR_A"));
        retVal.setName("Mohawk College - Fennell Campus");
        retVal.addPhone("tel:905-575-1212");

        retVal.addAddress(createAddress(AddressUse.Workplace, "135 Fennell Avenue West", "Hamilton", "Ontario", "Canada", "L8N 3T2"));

        return retVal;
    }

    public static void setUpConfig()
    {

//        IheConfiguration.load("config.xml");

    }

    /**
     * Creates a URI from a given URL
     *
     * @param urlString
     * @return
     */
    public static URI createURI(String urlString)
    {

        URL url = null;
        try
        {
            url = new URL(urlString);
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(TestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        URI uri = null;
        try
        {

            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(TestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uri;
    }

    /**
     * A utility method that creates a Person Name
     *
     * @param nameUse
     * @param given
     * @param middle
     * @param family
     * @return
     */
    public static PersonName createPersonName(NameUse nameUse, String given, String middle, String family)
    {
        PersonName retVal = new PersonName();
        retVal.setUse(nameUse);
        retVal.addNamePart(given, PartType.Given);

        // TODO: investigate, the MID qualifier isn't legitimate in the CDA.xsd
        //retVal.addNamePart(middle, PartType.Middle);
        retVal.addNamePart(family, PartType.Family);

        return retVal;
    }

    /**
     * A utility method that creates a Person Address
     *
     * @param addressUse
     * @param addressLine
     * @param city
     * @param state
     * @param country
     * @param zipCode
     * @return
     */
    public static PersonAddress createAddress(AddressUse addressUse, String addressLine, String city, String state, String country, String zipCode)
    {
        PersonAddress retVal = new PersonAddress(addressUse);
        retVal.addAddressPart(addressLine, org.marc.shic.core.AddressPartType.AddressLine);
        retVal.addAddressPart(city, org.marc.shic.core.AddressPartType.City);
        retVal.addAddressPart(state, org.marc.shic.core.AddressPartType.State);
        retVal.addAddressPart(country, org.marc.shic.core.AddressPartType.Country);
        retVal.addAddressPart(zipCode, org.marc.shic.core.AddressPartType.Zipcode);

        return retVal;
    }

    public static PersonAddress createAddress(AddressUse addressUse, String addressLine, String city, String state, String zipCode)
    {
        PersonAddress retVal = new PersonAddress(addressUse);
        retVal.addAddressPart(addressLine, org.marc.shic.core.AddressPartType.AddressLine);
        retVal.addAddressPart(city, org.marc.shic.core.AddressPartType.City);
        retVal.addAddressPart(state, org.marc.shic.core.AddressPartType.State);
        retVal.addAddressPart(zipCode, org.marc.shic.core.AddressPartType.Zipcode);

        return retVal;
    }
}
