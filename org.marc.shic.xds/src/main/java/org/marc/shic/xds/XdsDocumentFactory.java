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
package org.marc.shic.xds;

import org.marc.shic.core.AddressComponent;
import static org.marc.shic.core.AddressPartType.AddressLine;
import static org.marc.shic.core.AddressPartType.City;
import static org.marc.shic.core.AddressPartType.Country;
import static org.marc.shic.core.AddressPartType.State;
import static org.marc.shic.core.AddressPartType.Zipcode;
import static org.marc.shic.core.AddressUse.Home;
import static org.marc.shic.core.AddressUse.Workplace;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentSubmissionSetMetaData;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import static org.marc.shic.core.Gender.A;
import static org.marc.shic.core.Gender.F;
import static org.marc.shic.core.Gender.M;
import static org.marc.shic.core.Gender.N;
import static org.marc.shic.core.Gender.O;
import static org.marc.shic.core.Gender.U;
import org.marc.shic.core.exceptions.IheException;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.NTPHelper;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.NameUse;
import static org.marc.shic.core.PartType.Family;
import static org.marc.shic.core.PartType.Given;
import static org.marc.shic.core.PartType.Middle;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EncapsulatedDataCompression;
import org.marc.everest.datatypes.EncapsulatedDataIntegrityAlgorithm;
import org.marc.everest.datatypes.EntityNamePartQualifier;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
import org.marc.everest.formatters.xml.datatypes.r1.R1FormatterCompatibilityMode;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LegalAuthenticator;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.NonXMLBody;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Patient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PatientRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ServiceEvent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationSignature;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.core.utils.FormatHelpers;

/**
 *
 * @author fyfej
 */
public class XdsDocumentFactory
{
    // IHE affinity domain
    private IheAffinityDomainConfiguration m_affinityDomain;

    // Creates a new instance of the document factory
    public XdsDocumentFactory(IheAffinityDomainConfiguration affinityDomain)
    {
        this.m_affinityDomain = affinityDomain;
    }

    /**
     *
     * @return
     */
//    public DocumentSubmissionSetMetaData createClinicalDocument(String oid, String title, PersonDemographic target, PersonDemographic author, byte[] scannedDocument, String scannedSignedDocumentMediaType,
//                                                                PersonDemographic legalAuthenticator, LocationDemographic location) throws CommunicationsException
//    {
//        ClinicalDocument document = new ClinicalDocument();
//        Calendar currentTime;
//
//        try {
//            currentTime = NTPHelper.getTime(m_affinityDomain);
//
//        } catch (CommunicationsException ex) {
//            Logger.getLogger(XdsDocumentFactory.class.getName()).log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//
//        document.setTitle(title);
//        document.setEffectiveTime(currentTime);
//        document.setTypeId(new II("2.16.840.1.113883.1.3", "POCD_HD000040"));
//        //   document.setId(new II(myOid, consentDocId.toString())); // TODO: Generate an ID
//        document.setTemplateId(LIST.createLIST(
//                new II("1.3.6.1.4.1.19376.1.5.3.1.1.1"),
//                new II("1.3.6.1.4.1.19376.1.5.3.1.1.7.1")));
//        document.setCode(new CE<String>("57016-8", "2.16.840.1.113883.6.1"));
//
//        // Author
//        Author hl7AuthorRole = new Author();
//        //hl7AuthorRole.setTemplateId(LIST.createLIST(new II(author.getIdentifiers().get(0).getRoot(), author.getIdentifiers().get(0).getExtension())));
//        hl7AuthorRole.setTime(currentTime);
//        hl7AuthorRole.setAssignedAuthor(new AssignedAuthor());
//        hl7AuthorRole.getAssignedAuthor().setId(createIISet(author.getIdentifiers()));
//        hl7AuthorRole.getAssignedAuthor().setAssignedAuthorChoice(new Person(
//                SET.createSET(createPN(author.getNames().get(0))) // TODO: Make this better by getting an appropriate identity
//                ));
//        document.getAuthor().add(hl7AuthorRole);
//
//        // Record Target
//        RecordTarget hl7RecordTarget = new RecordTarget();
//        hl7RecordTarget.setPatientRole(new PatientRole());
//        hl7RecordTarget.getPatientRole().setId(createIISet(target.getIdentifiers())); // TODO: ID of the patient
//        if (target.getAddresses().size() > 0) {
//            hl7RecordTarget.getPatientRole().setAddr(SET.createSET(createAD(target.getAddresses().get(0))));
//        }
//        hl7RecordTarget.getPatientRole().setPatient(new Patient());
//        hl7RecordTarget.getPatientRole().getPatient().setName(SET.createSET(createPN(target.getNames().get(0))));
//        hl7RecordTarget.getPatientRole().getPatient().setAdministrativeGenderCode(translateGender(target.getGender()));
//
//        hl7RecordTarget.getPatientRole().getPatient().setBirthTime(TS.valueOf(FormatHelpers.SimpleDateFormat_yyyyMMddhhmmss.format(target.getDateOfBirth().getTime())));
//        document.getRecordTarget().add(hl7RecordTarget);
//
//        // Legal authenticator
//        if (legalAuthenticator != null) {
//            LegalAuthenticator hl7Authenticator = new LegalAuthenticator();
//            hl7Authenticator.setTime(currentTime);
//            hl7Authenticator.setSignatureCode(ParticipationSignature.Signed);
//            hl7Authenticator.setAssignedEntity(new AssignedEntity());
//            hl7Authenticator.getAssignedEntity().setId(createIISet(legalAuthenticator.getIdentifiers()));
//            hl7Authenticator.getAssignedEntity().setAssignedPerson(new Person());
//            hl7Authenticator.getAssignedEntity().getAssignedPerson().setName(SET.createSET(createPN(legalAuthenticator.getNames().get(0))));
//            document.setLegalAuthenticator(hl7Authenticator);
//        }
//
//
//        // Set data and calculate
//        try {
//            ED consent = new ED();
//            consent.setMediaType(scannedSignedDocumentMediaType);
//            consent.setData(scannedDocument);
//            consent = consent.compress(EncapsulatedDataCompression.Deflate);
//            consent.setIntegrityCheckAlgorithm(EncapsulatedDataIntegrityAlgorithm.Sha1);
//            consent.setIntegrityCheck(consent.computeIntegrityCheck());
//            Component2 nonXmlBody = new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE);
//            nonXmlBody.setBodyChoice(new NonXMLBody(consent));
//            document.setComponent(nonXmlBody);
//        } catch (Exception e) {
//            throw new IheException("Could not generate BPPC CDA document", e);
//        }
//
//
//        // Create the result
//        XmlIts1Formatter formatter = new XmlIts1Formatter();
//        try {
//            DocumentSubmissionSetMetaData retVal = new DocumentSubmissionSetMetaData();
//
//            formatter.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture));
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////            formatter.addCachedClass(ClinicalDocument.class);
//            formatter.graph(bos, document);
//
//            // Now construct the result
//            retVal.setId(new DomainIdentifier(oid, document.getId().getExtension()));
//            retVal.setAuthor(author);
//            retVal.setPatient(target);
//
//            DocumentMetaData docMeta = new DocumentMetaData();
//            docMeta.setClassCode(new CodeValue("57016-8", "2.16.840.1.113883.6.1", "Privacy Policy Acknowledgement Document"));
//            docMeta.setFormat(new CodeValue("urn:ihe:iti:bppc-sd:2007", "1.3.6.1.4.1.19376.1.2.3", "Basic Patient Privacy Consents with Scanned Document (BPPC)"));
//            docMeta.setUniqueId(retVal.getId());
//            //docMeta.setConfidentiality(new CodeValue(bppcDocument.getConfidentialityCode().getCode().getCode(), bppcDocument.getConfidentialityCode().getCode().getCodeSystem(), bppcDocument.getConfidentialityCode().getDisplayName()));
//            docMeta.setMimeType("text/x-cda-r2+xml");
//            // TODO: Move this to a policy config file
//
//            docMeta.addConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal"));
//            // docMeta.setConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal"));
//            docMeta.setContent(bos.toByteArray());
//            docMeta.setCreationTime(currentTime);
//            docMeta.setTitle(document.getTitle().getValue());
//
//            // Classification codes
//            docMeta.setContentType(new CodeValue("History and Physical", "Connect-a-thon contentTypeCodes", "History and Physical"));
//            docMeta.setFacilityType(new CodeValue("Outpatient", "Connect-a-thon healthcareFacilityTypeCodes", "Outpatient"));
//            docMeta.setPracticeSetting(new CodeValue("General Medicine", "Connect-a-thon practiceSettingCodes", "General Medicine"));
//            docMeta.setType(new CodeValue("52033-8", "LOINC", "General Correspondence"));
//
//            docMeta.addExtendedAttribute("legalAuthenticator", generateLegalAuthenticator(legalAuthenticator));
//            docMeta.addExtendedAttribute("authorInstitution", location.getName());
//
//            retVal.addDocument(docMeta);
//            return retVal;
//        } catch (Exception e) {
//            throw new IheException("Could not construct the BPPC submission set", e);
//        }
//    }

    /**
     * Generates a BPPC consent policy document
     */
//    public DocumentSubmissionSetMetaData createBppc(PersonDemographic target, PersonDemographic author, PersonDemographic authenticator, String myOid, LocationDemographic location,
//                                                    CodeValue consentGiven, byte[] scannedSignedDocument, String scannedSignedDocumentMediaType)
//    {
//
//        // TODO: call NTP
//        TS currentTime = TS.now();
//
//        // Generate consent ID
//        UUID consentDocId = UUID.randomUUID();
//        DocumentSubmissionSetMetaData retVal = new DocumentSubmissionSetMetaData();
//
//        ClinicalDocument bppcDocument = new ClinicalDocument();
//        bppcDocument.setTypeId(new II("2.16.840.1.113883.1.3", "POCD_HD000040"));
//        bppcDocument.setId(new II(myOid, consentDocId.toString())); // TODO: Generate an ID
//        bppcDocument.setTemplateId(LIST.createLIST(
//                new II("1.3.6.1.4.1.19376.1.5.3.1.1.1"),
//                new II("1.3.6.1.4.1.19376.1.5.3.1.1.7.1")));
//        bppcDocument.setCode(new CE<String>("57016-8", "2.16.840.1.113883.6.1"));
//        bppcDocument.setTitle("Consent to Share Information");
//        bppcDocument.setEffectiveTime(currentTime);
//        bppcDocument.setConfidentialityCode(x_BasicConfidentialityKind.Normal);
//
//        // Documentation of (the consent)
//        DocumentationOf docOf = new DocumentationOf(new ServiceEvent());
//        docOf.getServiceEvent().setTemplateId(LIST.createLIST(new II("1.3.6.1.4.1.19376.1.5.3.1.2.6")));
//        docOf.getServiceEvent().setId(SET.createSET(bppcDocument.getId())); // TODO: Generate an ID
//        docOf.getServiceEvent().setCode(new CE<String>(consentGiven.getCode(), consentGiven.getCodeSystem())); // TODO: Pass the policy ID here
//        docOf.getServiceEvent().setEffectiveTime(currentTime, null); // Valid from date
//        bppcDocument.getDocumentationOf().add(docOf);
//
//        // Author
//        Author hl7AuthorRole = new Author();
//        //hl7AuthorRole.setTemplateId(LIST.createLIST(new II(author.getIdentifiers().get(0).getRoot(), author.getIdentifiers().get(0).getExtension())));
//        hl7AuthorRole.setTime(currentTime);
//        hl7AuthorRole.setAssignedAuthor(new AssignedAuthor());
//        hl7AuthorRole.getAssignedAuthor().setId(createIISet(author.getIdentifiers()));
//        hl7AuthorRole.getAssignedAuthor().setAssignedAuthorChoice(new Person(
//                SET.createSET(createPN(author.getNames().get(0))) // TODO: Make this better by getting an appropriate identity
//                ));
//        bppcDocument.getAuthor().add(hl7AuthorRole);
//
//        // Record Target
//        RecordTarget hl7RecordTarget = new RecordTarget();
//        hl7RecordTarget.setPatientRole(new PatientRole());
//        hl7RecordTarget.getPatientRole().setId(createIISet(target.getIdentifiers())); // TODO: ID of the patient
//        if (target.getAddresses().size() > 0) {
//            hl7RecordTarget.getPatientRole().setAddr(SET.createSET(createAD(target.getAddresses().get(0))));
//        }
//        hl7RecordTarget.getPatientRole().setPatient(new Patient());
//        hl7RecordTarget.getPatientRole().getPatient().setName(SET.createSET(createPN(target.getNames().get(0))));
//        hl7RecordTarget.getPatientRole().getPatient().setAdministrativeGenderCode(translateGender(target.getGender()));
//        SimpleDateFormat sdf = FormatHelpers.SimpleDateFormat_yyyyMMdd;
//        hl7RecordTarget.getPatientRole().getPatient().setBirthTime(TS.valueOf(sdf.format(target.getDateOfBirth().getTime())));
//        bppcDocument.getRecordTarget().add(hl7RecordTarget);
//
//        // Legal authenticator
//        if (authenticator != null) {
//            LegalAuthenticator hl7Authenticator = new LegalAuthenticator();
//            hl7Authenticator.setTime(currentTime);
//            hl7Authenticator.setSignatureCode(ParticipationSignature.Signed);
//            hl7Authenticator.setAssignedEntity(new AssignedEntity());
//            hl7Authenticator.getAssignedEntity().setId(createIISet(authenticator.getIdentifiers()));
//            hl7Authenticator.getAssignedEntity().setAssignedPerson(new Person());
//            hl7Authenticator.getAssignedEntity().getAssignedPerson().setName(SET.createSET(createPN(authenticator.getNames().get(0))));
//            bppcDocument.setLegalAuthenticator(hl7Authenticator);
//        }
//
//        // Attach the body
//        if (scannedSignedDocument != null) {
//            // Set data and calculate
//            try {
//                ED consent = new ED();
//                consent.setMediaType(scannedSignedDocumentMediaType);
//                consent.setData(scannedSignedDocument);
//                consent = consent.compress(EncapsulatedDataCompression.Deflate);
//                consent.setIntegrityCheckAlgorithm(EncapsulatedDataIntegrityAlgorithm.Sha1);
//                consent.setIntegrityCheck(consent.computeIntegrityCheck());
//                Component2 nonXmlBody = new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE);
//                nonXmlBody.setBodyChoice(new NonXMLBody(consent));
//                bppcDocument.setComponent(nonXmlBody);
//            } catch (Exception e) {
//                throw new IheException("Could not generate BPPC CDA document", e);
//            }
//        } else // attach a copy of the policy
//        {
//            try {
//                URL url = new URL(this.m_affinityDomain.getPolicyURL());
//                URLConnection connect = url.openConnection();
//
//                // Setup ED
//                ED consent = new ED();
//                consent.setMediaType(connect.getContentType());
//
//                // Read content
//                InputStream rawStream = connect.getInputStream();
//                InputStream stream = null;
//                try {
//                    stream = new BufferedInputStream(rawStream);
//
//                    // Prepare read
//                    byte[] data = new byte[connect.getContentLength()];
//                    int bRead = 0, offset = 0;
//
//                    // Read
//                    while (offset < connect.getContentLength()) {
//                        bRead = stream.read(data, offset, data.length - offset);
//                        if (bRead == -1) {
//                            break;
//                        }
//                        offset += bRead;
//                    }
//
//                    // Ensure data
//                    if (offset != connect.getContentLength()) {
//                        throw new IOException("Only read " + offset + " bytes; Expected " + connect.getContentLength() + " bytes");
//                    }
//
//                    // Set data and calculate
//                    consent.setData(data);
//                    consent = consent.compress(EncapsulatedDataCompression.Deflate);
//                    consent.setIntegrityCheckAlgorithm(EncapsulatedDataIntegrityAlgorithm.Sha1);
//                    consent.setIntegrityCheck(consent.computeIntegrityCheck());
//                    Component2 nonXmlBody = new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE);
//                    nonXmlBody.setBodyChoice(new NonXMLBody(consent));
//                    bppcDocument.setComponent(nonXmlBody);
//                } finally {
//                    if (stream != null) {
//                        stream.close();
//                    }
//                }
//
//            } catch (Exception e) {
//                throw new IheException("Could not construct BPPC CDA document", e);
//            }
//        }
//
//        // Create the result
//        XmlIts1Formatter formatter = new XmlIts1Formatter();
//        try {
//            formatter.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture));
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////            formatter.addCachedClass(ClinicalDocument.class);
//            formatter.graph(bos, bppcDocument);
//
//            // Now construct the result
//            retVal.setId(new DomainIdentifier(myOid, bppcDocument.getId().getExtension()));
//            retVal.setAuthor(author);
//            retVal.setPatient(target);
//
//            DocumentMetaData docMeta = new DocumentMetaData();
//            docMeta.setClassCode(new CodeValue("57016-8", "2.16.840.1.113883.6.1", "Privacy Policy Acknowledgement Document"));
//            docMeta.setFormat(new CodeValue("urn:ihe:iti:bppc-sd:2007", "1.3.6.1.4.1.19376.1.2.3", "Basic Patient Privacy Consents with Scanned Document (BPPC)"));
//            docMeta.setDocumentUniqueId(retVal.getId());
//            //docMeta.setConfidentiality(new CodeValue(bppcDocument.getConfidentialityCode().getCode().getCode(), bppcDocument.getConfidentialityCode().getCode().getCodeSystem(), bppcDocument.getConfidentialityCode().getDisplayName()));
//            docMeta.setMimeType("text/x-cda-r2+xml");
//            // TODO: Move this to a policy config file
//
//            docMeta.addConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal"));
//            //docMeta.setConfidentiality(new CodeValue("N", "2.16.840.1.113883.5.25", "Normal"));
//            docMeta.setContent(bos.toByteArray());
//            docMeta.setCreationTime(currentTime.getDateValue());
//            docMeta.setTitle(bppcDocument.getTitle().getValue());
//
//            // Classification codes
//            docMeta.setContentType(new CodeValue("History and Physical", "Connect-a-thon contentTypeCodes", "History and Physical"));
//            docMeta.setFacilityType(new CodeValue("Outpatient", "Connect-a-thon healthcareFacilityTypeCodes", "Outpatient"));
//            docMeta.setPracticeSetting(new CodeValue("General Medicine", "Connect-a-thon practiceSettingCodes", "General Medicine"));
//            docMeta.setType(new CodeValue("52033-8", "LOINC", "General Correspondence"));
//
//            docMeta.addExtendedAttribute("legalAuthenticator", generateLegalAuthenticator(authenticator));
//            docMeta.addExtendedAttribute("authorInstitution", location.getName());
//
//            retVal.addDocument(docMeta);
//            return retVal;
//        } catch (Exception e) {
//            throw new IheException("Could not construct the BPPC submission set", e);
//        }
//
//    }

    /**
     * Translate gender code
     */
    private CE<AdministrativeGender> translateGender(Gender gender)
    {
        CE<AdministrativeGender> retVal = new CE<AdministrativeGender>();
        switch (gender) {
            case A:
                retVal.setOriginalText(new ED("Ambiguous"));
                retVal.setTranslation(SET.createSET(
                        new CD<AdministrativeGender>(new AdministrativeGender("Q56.4", "2.16.840.1.113883.6.90"))));
                retVal.setNullFlavor(NullFlavor.Other);
                break;
            case F:
                retVal.setCodeEx(AdministrativeGender.Female);
                break;
            case M:
                retVal.setCodeEx(AdministrativeGender.Male);
                break;
            case N:
                retVal.setNullFlavor(NullFlavor.NotApplicable);
                break;
            case O:
                retVal.setNullFlavor(NullFlavor.Other);
                retVal.setOriginalText(new ED("Unspecified"));
                break;
            case U:
                retVal.setCodeEx(AdministrativeGender.Undifferentiated);
                break;
        }
        return retVal;
    }

    /**
     * Create an HL7v3 AD from the SHC person Address
     */
    private AD createAD(PersonAddress addr)
    {
        AD retVal = new AD();
        switch (addr.getUse()) {
            case Home:
                retVal.setUse(SET.createSET(new CS<PostalAddressUse>(PostalAddressUse.HomeAddress)));
                break;
            case Workplace:
                retVal.setUse(SET.createSET(new CS<PostalAddressUse>(PostalAddressUse.WorkPlace)));
                break;
        }

        for (AddressComponent nc : addr.getParts()) {
            switch (nc.getType()) {
                case AddressLine:
                    retVal.getPart().add(new ADXP(nc.getValue(), org.marc.everest.datatypes.AddressPartType.AddressLine));
                    break;
                case City:
                    retVal.getPart().add(new ADXP(nc.getValue(), org.marc.everest.datatypes.AddressPartType.City));
                    break;
                case Country:
                    retVal.getPart().add(new ADXP(nc.getValue(), org.marc.everest.datatypes.AddressPartType.Country));
                    break;
                case State:
                    retVal.getPart().add(new ADXP(nc.getValue(), org.marc.everest.datatypes.AddressPartType.State));
                    break;
                case Zipcode:
                    retVal.getPart().add(new ADXP(nc.getValue(), org.marc.everest.datatypes.AddressPartType.PostalCode));
                    break;
            }
        }
        return retVal;
    }

    /**
     * Constructs an HL7v3 PN from the SHC PersonName class
     */
    private PN createPN(PersonName name)
    {
        PN retVal = new PN();
        if (name.getUse() == NameUse.Legal) {
            retVal.setUse(SET.createSET(new CS<EntityNameUse>(EntityNameUse.Legal)));
        }
        for (NameComponent nc : name.getParts()) {
            switch (nc.getType()) {
                case Family:
                    retVal.getParts().add(new ENXP(nc.getValue(), EntityNamePartType.Family));
                    break;
                case Given:
                    retVal.getParts().add(new ENXP(nc.getValue(), EntityNamePartType.Given));
                    break;
                case Middle:
                    ENXP np = new ENXP(nc.getValue(), EntityNamePartType.Given);
                    np.setQualifier(SET.createSET(new CS<EntityNamePartQualifier>(EntityNamePartQualifier.Middle)));
                    retVal.getParts().add(np);
                    break;
            }
        }
        return retVal;
    }

    /**
     * Create an II Set
     */
    private SET<II> createIISet(List<DomainIdentifier> identifiers)
    {
        SET<II> retVal = new SET<II>();
        for (DomainIdentifier id : identifiers) {
            retVal.add(new II(id.getRoot(), id.getExtension()));
        }
        return retVal;
    }

    /**
     * Generate an string representing the legal authenticator XCN
     */
    private static String generateLegalAuthenticator(PersonDemographic authenticator)
    {
        StringBuilder sb = new StringBuilder();

        // Get domain ID of the author
        DomainIdentifier id = authenticator.getIdentifiers().get(0);

        String[] components = new String[5];
        // Identifier
        components[0] = id.getExtension();
        components[4] = String.format("^^^^^&%s&ISO", id.getRoot());

        // Name
        if (authenticator.getNames().size() > 0) // TODO: Make this a little more clear instead of the first
        {
            PersonName name = authenticator.getNames().get(0);
            for (NameComponent nc : name.getParts()) {
                switch (nc.getType()) {
                    case Family:
                        components[1] = nc.getValue();
                        break;
                    case Given:
                        if (components[2] != null) {
                            components[3] += nc.getValue();
                        } else {
                            components[2] = nc.getValue();
                        }
                }
            }
        }

        // Generate the string
        for (int i = 0; i < components.length; i++) {
            sb.append(String.format("%s%s", components[i], i == components.length - 1 ? "" : "^"));
        }

        return sb.toString();
    }
}
