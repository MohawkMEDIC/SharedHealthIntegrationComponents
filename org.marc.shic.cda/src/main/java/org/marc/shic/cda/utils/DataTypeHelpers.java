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
 * @author brownp Date: Aug 16, 2013
 *
 */
package org.marc.shic.cda.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.SC;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedCustodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AuthoringDevice;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Custodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.CustodianOrganization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DataEnterer;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LegalAuthenticator;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Patient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PatientRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer1;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ServiceEvent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationFunction;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationSignature;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ServiceEventPerformer;
import org.marc.shic.cda.exceptions.InvalidStringDataException;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.Gender;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;

public class DataTypeHelpers {

	/**
	 * Creates an Author element (Original Author)
	 * 
	 * @param person
	 * @param locationDemographic
	 * @param time
	 * @param templateId
	 * @return
	 */
	public static Author createAuthor(PersonDemographic person, LocationDemographic locationDemographic, TS time, String templateId) {
		Author retVal = new Author();

		// templateId
		if (null != templateId) {
			retVal.setTemplateId(LIST.createLIST(new II(templateId)));
		}

		// time
		retVal.setTime(time);

		// assignedAuthor
		Person assignedPerson = createPerson(person);
		List<DomainIdentifier> identifiers = null;
		String phone = null;
		if (person != null) {
			identifiers = person.getIdentifiers();
			phone = person.getPhone();
		}
		retVal.setAssignedAuthor(createAssignedAuthor(identifiers, locationDemographic, assignedPerson, phone));

		return retVal;
	}

	/**
	 * Creates an Author element.
	 * 
	 * @param person
	 * @param locationDemographic
	 * @param time
	 * @return
	 */
	public static Author createAuthor(PersonDemographic person, LocationDemographic locationDemographic, TS time) {
		return createAuthor(person, locationDemographic, time, null);
	}

	/**
	 * A utility method that creates a Person Name Assuming that this is a
	 * LegalName use
	 * 
	 * @param nameUse
	 * @param given
	 * @param family
	 * @return
	 */
	public static PersonName createPersonName(String given, String family) throws InvalidStringDataException {
		PersonName retVal = new PersonName();
		retVal.setUse(NameUse.Legal);

		// Checks if given and family name are not empty/null/blank.
		checkNames(given, family);

		retVal.addNamePart(given, PartType.Given);
		// TODO: investigate, the MID qualifier isn't legitimate in the CDA.xsd
		// retVal.addNamePart(middle, PartType.Middle);
		retVal.addNamePart(family, PartType.Family);

		return retVal;
	}

	public static LocationDemographic createLocationDemographic(String IdRoot, String IdExtension, String IdName, String locationName, PersonAddress address, String phone) throws InvalidStringDataException {
		if (!isRootIdentifierValid(IdRoot)) {
			throw new InvalidStringDataException(IdRoot, "Root identifier is invalid.");
		}

		LocationDemographic retVal = new LocationDemographic();
		retVal.addIdentifier(new DomainIdentifier(IdRoot, IdExtension, IdName));
		retVal.setName(locationName);

		retVal.setAddresses(new ArrayList<PersonAddress>());

		if (null != address) {
			retVal.getAddresses().add(address);
		}

		retVal.addPhone(phone);

		return retVal;
	}

	/**
	 * 
	 * @param givenName
	 * @param familyName
	 * @param dateOfBirth
	 * @param genderString
	 *            as a single character (i.e. m, M, f, F)
	 * @param phoneNumber
	 * @return
	 */
	public static PersonDemographic createPersonDemographic(String rootIdentifier, String extension, String givenName, String familyName, Calendar dateOfBirth, String genderString, String phoneNumber) throws InvalidStringDataException {
		if (!isRootIdentifierValid(rootIdentifier)) {
			throw new InvalidStringDataException(rootIdentifier, "Root identifier is invalid.");
		}
		if (extension == null || extension.trim().isEmpty()) {
			throw new InvalidStringDataException(extension, "Extension identifier may not be null or empty.");
		}
		PersonDemographic personDemographic = new PersonDemographic();

		personDemographic.setAddresses(new ArrayList<PersonAddress>());

		personDemographic.addIdentifier(new DomainIdentifier(rootIdentifier, extension));

		if (dateOfBirth != null) {
			personDemographic.setDateOfBirth(dateOfBirth);
		}

		if (phoneNumber != null || !"".equals(phoneNumber)) {
			personDemographic.addPhone(phoneNumber);
		}

		personDemographic.setGender(GenderList.get(genderString));

		personDemographic.addName(createPersonName(givenName, familyName));

		return personDemographic;
	}

	/**
	 * Checks if a given root id string is valid.
	 * 
	 * @param rootId
	 *            The root identifier to check
	 * @return A boolean indicating the validity of the root id.
	 */
	public static boolean isRootIdentifierValid(String rootId) {

		return rootId != null && !rootId.trim().isEmpty();
	}

	/**
	 * Creates a assignedPerson element from a PersonDemographic object
	 * 
	 * @param person
	 * @return
	 */
	private static Person createPerson(PersonDemographic person) {
		Person retVal = new Person();

		// Name
		retVal.setName(new SET<PN>());
		if (person != null) {
			for (PersonName personName : person.getNames()) {
				retVal.getName().add(CoreDataTypeHelpers.createPN(personName));
			}
		}

		return retVal;
	}

	/**
	 * Creates an Author element (Scanner) for Scanned Documents
	 * 
	 * @param organization
	 * @param time
	 * @param templateId
	 * @param manufacturer
	 * @param softwareName
	 * @return
	 */
	public static Author createScanner(LocationDemographic organization, TS time, String templateId, String manufacturer, String softwareName) {
		Author retVal = new Author();

		// templateId
		if (null != templateId) {
			retVal.setTemplateId(LIST.createLIST(new II(templateId)));
		}

		// time
		retVal.setTime(time);

		// assignedAuthor
		AuthoringDevice assignedAuthoringDevice = createAuthoringDevice(new CodeValue("CAPTURE", "1.2.840.10008.2.16.4", "Image Capture"), manufacturer, softwareName);
		retVal.setAssignedAuthor(createAssignedAuthor(organization.getIdentifiers(), organization, assignedAuthoringDevice, null));

		return retVal;
	}

	public static Author createScanner(LocationDemographic organization, TS time, String manufacturer, String softwareName) {
		Author retVal = new Author();

		// time
		retVal.setTime(time);

		// assignedAuthor
		AuthoringDevice assignedAuthoringDevice = createAuthoringDevice(new CodeValue("CAPTURE", "1.2.840.10008.2.16.4", "Image Capture"), manufacturer, softwareName);
		retVal.setAssignedAuthor(createAssignedAuthor(organization.getIdentifiers(), organization, assignedAuthoringDevice, null));

		return retVal;
	}

	/**
	 * Creates an assignedAuthor element
	 * 
	 * @param identifiers
	 * @param organization
	 * @param authorChoice
	 * @return
	 */
	;

	private static AssignedAuthor createAssignedAuthor(List<DomainIdentifier> identifiers, LocationDemographic organization, Object authorChoice, String phone) {
		AssignedAuthor retVal = new AssignedAuthor();
		retVal.setId(CoreDataTypeHelpers.createIISet(identifiers));
		retVal.setAssignedAuthorChoice(authorChoice);
		retVal.setRepresentedOrganization(createOrganization(organization));
		retVal.setTelecom(new SET<TEL>());

		TEL tel = CoreDataTypeHelpers.createTEL(phone);

		if (tel != null) {
			retVal.getTelecom().add(tel);
		}
		// if (phone != null) {
		// if (!phone.startsWith("tel:")) {
		// phone = "tel:" + phone;
		// }
		//
		// retVal.getTelecom().add(new TEL(phone));
		// }
		return retVal;
	}

	/**
	 * Creates an AuthoringDevice element (Scanned, Person, etc...)
	 * 
	 * @param code
	 * @param manufacturer
	 * @param softwareName
	 * @return
	 */
	private static AuthoringDevice createAuthoringDevice(CodeValue code, String manufacturer, String softwareName) {
		AuthoringDevice retVal = new AuthoringDevice();
		retVal.setCode((CE<String>) CoreDataTypeHelpers.createCodedElement(code));
		retVal.setManufacturerModelName(new SC(manufacturer));
		retVal.setSoftwareName(new SC(softwareName));

		return retVal;
	}

	/**
	 * Create a RepresentedOrganization
	 * 
	 * @param locationDemographic
	 * @return
	 */
	public static Organization createOrganization(LocationDemographic locationDemographic) {
		Organization organization = new Organization();
		if (locationDemographic != null) {
			organization.setId(new SET<II>());
			organization.getId().add(new II(locationDemographic.getIdentifiers().get(0).getRoot()));
			// Addr
			if (locationDemographic.getAddresses().size() > 0) {
				organization.setAddr(new SET<AD>());
				for (PersonAddress address : locationDemographic.getAddresses()) {
					// CoreDataTypeHelpers.createAD(address);
					organization.getAddr().add(CoreDataTypeHelpers.createAD(address));

					// if
					// (!organization.getAddr().contains(CoreDataTypeHelpers.createAD(address)))
					// {
					// organization.getAddr().add(CoreDataTypeHelpers.createAD(address));
					// }
				}
			}

			// Name
			if (locationDemographic.getName() != null && !locationDemographic.getName().isEmpty()) {
				organization.setName(new SET<ON>());
				organization.getName().add(CoreDataTypeHelpers.createON(locationDemographic.getName(), EntityNamePartType.Title, EntityNameUse.Legal));
			}
			TEL tel = CoreDataTypeHelpers.createTEL(locationDemographic.getPhone());

			if (tel != null) {
				organization.setTelecom(SET.createSET(tel));
			}
		}

		return organization;
	}

	/**
	 * Translate gender code
	 */
	public static CE<AdministrativeGender> translateGender(Gender gender) {
		CE<AdministrativeGender> retVal = new CE<AdministrativeGender>();

		// If gender is null then it is Unknown.
		if (gender == null) {
			gender = Gender.U;
		}

		switch (gender) {
		case A:
			retVal.setOriginalText(new ED("Ambiguous"));
			retVal.setTranslation(SET.createSET(new CD<AdministrativeGender>(new AdministrativeGender("Q56.4", "2.16.840.1.113883.6.90"))));
			retVal.setNullFlavor(NullFlavor.Other);
			retVal.setDisplayName("Ambiguous");
			break;
		case F:
			retVal.setCodeEx(AdministrativeGender.Female);
			retVal.setDisplayName("Female");
			break;
		case M:
			retVal.setCodeEx(AdministrativeGender.Male);
			retVal.setDisplayName("Male");
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
			retVal.setDisplayName("Undifferentiated");
			break;
		}
		retVal.setCodeSystemName("Administrative Gender");
		return retVal;
	}

	/**
	 * Create a recordTarget from a PersonDemographic
	 * 
	 * @param target
	 * @return
	 */
	public static RecordTarget createRecordTarget(PersonDemographic target) {
		RecordTarget hl7RecordTarget = new RecordTarget();

		hl7RecordTarget.setPatientRole(createPatientRole(target));
		hl7RecordTarget.getPatientRole().setPatient(createPatient(target));

		return hl7RecordTarget;
	}

	/**
	 * Create a patientRole from a PersonDemographic
	 * 
	 * @param target
	 * @return
	 */
	public static PatientRole createPatientRole(PersonDemographic target) {
		PatientRole retVal = new PatientRole();
		if (target != null) {
			retVal.setId(CoreDataTypeHelpers.createIISet(target.getIdentifiers()));

			TEL tel = CoreDataTypeHelpers.createTEL(target.getPhone());

			if (tel != null) {
				retVal.setTelecom(SET.createSET(tel));
			}
			// new TEL(target.getPhone()))

			// initialize the AD set and add all addresses
			retVal.setAddr(new SET<AD>());
			for (PersonAddress address : target.getAddresses()) {
				retVal.getAddr().add(CoreDataTypeHelpers.createAD(address));
			}
		}
		return retVal;
	}

	/**
	 * Create a patient from a PersonDemographic
	 * 
	 * @param target
	 * @return
	 */
	public static Patient createPatient(PersonDemographic target) {
		Patient patient = new Patient();

		patient.setName(new SET<PN>());
		if (target != null) {

			for (PersonName personName : target.getNames()) {
				patient.getName().add(CoreDataTypeHelpers.createPN(personName));
			}

			patient.setAdministrativeGenderCode(translateGender(target.getGender()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			patient.setBirthTime(TS.valueOf(sdf.format(target.getDateOfBirth().getTime())));
		}
		return patient;

	}

	/**
	 * Create a LegalAuthenticator from a PersonDemographic
	 * 
	 * @param authenticatorDemographic
	 * @param effectiveTime
	 * @return
	 */
	public static LegalAuthenticator createLegalAuthenticator(PersonDemographic authenticatorDemographic, Calendar effectiveTime) {
		LegalAuthenticator legalAuthenticator = new LegalAuthenticator();
		legalAuthenticator.setTime(effectiveTime);
		legalAuthenticator.setSignatureCode(ParticipationSignature.Signed);
		legalAuthenticator.setContextControlCode(ContextControl.OverridingPropagating);
		legalAuthenticator.setAssignedEntity(createAssignedEntity(authenticatorDemographic, null));

		return legalAuthenticator;
	}

	/**
	 * Creates a Custodian element from an ID and extension.
	 * 
	 * @param rootId
	 *            The ID of the Custodian. May not be set to null.
	 * @param extension
	 *            The extension of the Custodian.
	 * @return The generated Custodian.
	 */
	public static Custodian createCustodian(String rootId, String extension) throws InvalidStringDataException {
		if (!isRootIdentifierValid(rootId)) {
			throw new InvalidStringDataException(rootId, "The root id given was null.");
		}
		SET<II> custodianIdentifiers = new SET<II>();
		custodianIdentifiers.add(new II(rootId, extension));
		Custodian custodian = new Custodian(new AssignedCustodian(new CustodianOrganization(custodianIdentifiers)));

		return custodian;
	}

	/**
	 * Creates a Custodian element from a LocationDemographic object.
	 * 
	 * @param custodianOrganization
	 *            Must not be a null object.
	 * @return
	 */
	public static Custodian createCustodian(LocationDemographic custodianOrganization) {
		Custodian retVal = new Custodian();
		retVal.setAssignedCustodian(createAssignedCustodian(custodianOrganization));

		return retVal;
	}

	/**
	 * Creates an AssignedCustodian element from a LocationDemographic object
	 * 
	 * @param custodianOrganization
	 * @return
	 */
	private static AssignedCustodian createAssignedCustodian(LocationDemographic custodianOrganization) {
		AssignedCustodian retVal = new AssignedCustodian();
		retVal.setRepresentedCustodianOrganization(createRepresentedCustodianOrganization(custodianOrganization));
		return retVal;
	}

	/**
	 * Creates a representedCustodianOrganization element from a
	 * LocationDemographic object
	 * 
	 * @param custodianOrganization
	 * @return
	 */
	private static CustodianOrganization createRepresentedCustodianOrganization(LocationDemographic custodianOrganization) {
		if (custodianOrganization == null) {
			throw new IllegalArgumentException("The custodian organization location is null. Mandatory ID attribute not satisfied.");
		}
		CustodianOrganization retVal = new CustodianOrganization();

		// Id
		SET<II> identifiers = CoreDataTypeHelpers.createIISet(custodianOrganization.getIdentifiers());
		retVal.setId(identifiers);

		// Name
		retVal.setName(CoreDataTypeHelpers.createON(custodianOrganization.getName(), EntityNamePartType.Title, EntityNameUse.Legal));

		// Addr (can only set one..)
		for (PersonAddress address : custodianOrganization.getAddresses()) {
			retVal.setAddr(CoreDataTypeHelpers.createAD(address));
		}

		return retVal;
	}

	/**
	 * Creates a dataEnterer element representing the operator/scanner of the
	 * document
	 * 
	 * @param operator
	 * @param time
	 * @param templateId
	 * @return
	 */
	public static DataEnterer createDataEnterer(PersonDemographic operator, TS time, String templateId) {
		DataEnterer retVal = createDataEnterer(operator, time);

		// templateId
		if (null != templateId) {
			retVal.setTemplateId(LIST.createLIST(new II(templateId)));
		}

		return retVal;
	}

	public static DataEnterer createDataEnterer(PersonDemographic operator, TS time) {
		DataEnterer retVal = new DataEnterer();

		// time
		retVal.setTime(time);

		// assignedEntity
		retVal.setAssignedEntity(createAssignedEntity(operator, null));

		return retVal;
	}

	/**
	 * Creates an assignedEntity element containing an Id and an assignedPerson
	 * elements
	 * 
	 * @param person
	 * @return
	 */
	public static AssignedEntity createAssignedEntity(PersonDemographic person, LocationDemographic location) {
		AssignedEntity retVal = new AssignedEntity();

		if (person != null) {
			if (person.getAddresses().size() > 0) {
				retVal.setAddr(new SET<AD>());
				for (PersonAddress address : person.getAddresses()) {
					retVal.getAddr().add(CoreDataTypeHelpers.createAD(address));
				}
			}
			TEL tel = CoreDataTypeHelpers.createTEL(person.getPhone());

			if (tel != null) {
				retVal.setTelecom(new SET<TEL>());
				retVal.getTelecom().add(tel);
			}

			retVal.setId(CoreDataTypeHelpers.createIISet(person.getIdentifiers()));
			retVal.setAssignedPerson(createPerson(person));
		}
		/*
		 * if (retVal.getAddr().size() == 0) { AD ad = new AD();
		 * ad.setNullFlavor(NullFlavor.Unknown); retVal.getAddr().add(ad); }
		 */

		if (null != location) {
			retVal.setRepresentedOrganization(createOrganization(location));
		}

		return retVal;
	}

	/**
	 * Adds a DocumentationOf node to the CDA document
	 * 
	 * @return
	 */
	public static DocumentationOf createDocumentationOf(String classCode, CodeValue consent, Calendar effectiveTimeStart, Calendar effectiveTimeStop, String serviceEventTemplateId) {
		DocumentationOf retVal = new DocumentationOf();
		retVal.setServiceEvent(createServiceEvent(classCode, consent, effectiveTimeStart, effectiveTimeStop, serviceEventTemplateId));

		return retVal;
	}

	public static DocumentationOf createDocumentationOf(String classCode, CodeValue consent, Calendar effectiveTimeStart, Calendar effectiveTimeStop) {
		DocumentationOf retVal = new DocumentationOf();
		retVal.setServiceEvent(createServiceEvent(classCode, consent, effectiveTimeStart, effectiveTimeStop));

		return retVal;
	}

	/**
	 * 
	 * @param classCode
	 * @param consent
	 * @param effectiveTimeStart
	 * @param effectiveTimeStop
	 * @param serviceEventTemplateId
	 * @param performerTemplateId
	 * @return
	 */
	public static ServiceEvent createServiceEvent(String classCode, CodeValue consent, Calendar effectiveTimeStart, Calendar effectiveTimeStop, String serviceEventTemplateId) {
		ServiceEvent serviceEvent = createServiceEvent(classCode, consent, effectiveTimeStart, effectiveTimeStop);

		CdaUtils.addTemplateRuleID(serviceEvent, serviceEventTemplateId);

		return serviceEvent;
	}

	public static ServiceEvent createServiceEvent(String classCode, CodeValue consent, Calendar effectiveTimeStart, Calendar effectiveTimeStop) {
		ServiceEvent serviceEvent = new ServiceEvent(new CS<String>(classCode));

		serviceEvent.setId(SET.createSET(new II(UUID.randomUUID())));

		if (null != consent) {
			serviceEvent.setCode((CE<String>) CoreDataTypeHelpers.createCodedElement(consent));
		}

		serviceEvent.setEffectiveTime(CoreDataTypeHelpers.createIVL_TS(effectiveTimeStart, effectiveTimeStop, null));

		// initialize the list of assigned entities
		serviceEvent.setPerformer(new ArrayList<Performer1>());

		return serviceEvent;
	}

	public static Performer1 createPerformer1(PersonDemographic assignedEntity, LocationDemographic location, Calendar effectiveTimeLow, Calendar effectiveTimeHigh) {
		Performer1 performer = new Performer1(x_ServiceEventPerformer.PRF, createAssignedEntity(assignedEntity, location));
		performer.setTime(CoreDataTypeHelpers.createIVL_TS(effectiveTimeLow, effectiveTimeHigh, null));
		performer.setTemplateId(new ArrayList<II>());
		performer.setFunctionCode(ParticipationFunction.PrimaryCarePhysician);

		return performer;
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
	public static PersonAddress createAddress(AddressUse addressUse, String addressLine, String city, String state, String country, String zipCode) {
		if ((addressLine == null || !addressLine.isEmpty()) && (city == null || city.isEmpty()) && (state == null || state.isEmpty()) && (country == null || country.isEmpty()) && (zipCode == null || zipCode.isEmpty())) {
			return null;
		}
		PersonAddress retVal = new PersonAddress(addressUse);

		retVal.addAddressPart(addressLine == null ? "" : addressLine, org.marc.shic.core.AddressPartType.AddressLine);
		retVal.addAddressPart(city == null ? "" : city, org.marc.shic.core.AddressPartType.City);
		retVal.addAddressPart(state == null ? "" : state, org.marc.shic.core.AddressPartType.State);
		retVal.addAddressPart(country == null ? "" : country, org.marc.shic.core.AddressPartType.Country);
		retVal.addAddressPart(zipCode == null ? "" : zipCode, org.marc.shic.core.AddressPartType.Zipcode);

		return retVal;
	}

	private static HashMap<String, Gender> GenderList = new HashMap<String, Gender>();

	static {
		GenderList.put("f", Gender.F);
		GenderList.put("F", Gender.F);
		GenderList.put("m", Gender.M);
		GenderList.put("M", Gender.M);
	}

	private static void checkNames(String given, String family) throws InvalidStringDataException {

		// Checks if given and family name are not null/empty/blank.
		if (given == null || given.trim().isEmpty()) {
			throw new InvalidStringDataException(given, "The given name cannot be null or empty.");
		}
		if (family == null || family.trim().isEmpty()) {
			throw new InvalidStringDataException(family, "The family name cannot be null or empty.");
		}
	}

	public static Calendar getCalendar(Date date) {
		Calendar retVal = new GregorianCalendar();
		retVal.setTime(date);
		return retVal;
	}
}
