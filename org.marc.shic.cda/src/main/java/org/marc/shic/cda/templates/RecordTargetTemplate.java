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
 * @author Ryan Albert
 * @since 4-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Patient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PatientRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationRecordTarget;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.DefaultAddressable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.DefaultNameable;
import org.marc.shic.cda.everestfunc.DefaultTelecommunicable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.everestfunc.Telecommunicable;

/**
 *
 * @author Ryan Albert
 */
public class RecordTargetTemplate extends Template<RecordTarget> implements Nameable, Addressable, Identifiable, Telecommunicable {

	private PatientRole patientRole;
	private Patient patient;

	public RecordTargetTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public RecordTargetTemplate(RecordTarget root, CDAStandard standard) {
		super(root, standard);
		if (getRoot().getPatientRole() != null) {
			patientRole = getRoot().getPatientRole();
			if (patientRole.getPatient() == null) {
				patientRole.setPatient(new Patient());
			}
		} else {
			patientRole = new PatientRole();
			patientRole.setPatient(new Patient());
			getRoot().setPatientRole(patientRole);
		}
		patient = patientRole.getPatient();
	}

	@Override
	protected void initRoot() {
		patientRole = new PatientRole();
		patient = new Patient();
		patientRole.setPatient(patient);
		getRoot().setPatientRole(patientRole);
		getRoot().setContextControlCode(ContextControl.OverridingPropagating);
		getRoot().overrideTypeCode(ParticipationRecordTarget.RCT);
		patient.setAdministrativeGenderCode((CE<AdministrativeGender>) Code.unavailable(NullFlavor.NoInformation).getCode(CE.class));
	}

	public void setAdministrativeGenderCode(AdministrativeGender gender) {
		patient.setAdministrativeGenderCode(gender);
	}

	public AdministrativeGender getAdministrativeGenderCode() {
		return patient.getAdministrativeGenderCode().getCode();
	}

	public void setBirthTime(Time birthTime) {
		patient.setBirthTime(birthTime.getTime(TS.class));
	}

	public Time getBirthTime() {
		return new Time(patient.getBirthTime());
	}

	@Override
	public TEL addTelecom(String tel) {
		return DefaultTelecommunicable.getTelecommunicable(patientRole).addTelecom(tel);
	}

	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		return DefaultTelecommunicable.getTelecommunicable(patientRole).addTelecom(use, tel);
	}

	@Override
	public void addTelecom(TEL tel) {
		DefaultTelecommunicable.getTelecommunicable(patientRole).addTelecom(tel);
	}

	@Override
	public Set<TEL> getTelecoms() {
		return DefaultTelecommunicable.getTelecommunicable(patientRole).getTelecoms();
	}

	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		return DefaultNameable.getNameable(patient).addName(use, nameVal, type);
	}

	@Override
	public void addName(EN name) {
		DefaultNameable.getNameable(patient).addName(name);
	}

	@Override
	public Set<EN> getNames() {
		return DefaultNameable.getNameable(patient).getNames();
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(patientRole).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(patientRole).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(patientRole).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(patientRole).getIds();
	}

	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		return DefaultAddressable.getAddressable(patientRole).addAddress(use, value, type);
	}

	@Override
	public void addAddress(Address address) {
		DefaultAddressable.getAddressable(patientRole).addAddress(address);
	}

	@Override
	public Set<Address> getAddresses() {
		return DefaultAddressable.getAddressable(patientRole).getAddresses();
	}

}
