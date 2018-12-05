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
 * @since 5-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedCustodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Custodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.CustodianOrganization;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.everestfunc.Telecommunicable;

/**
 *
 * @author Ryan Albert
 */
public class CustodianTemplate extends Template<Custodian> implements Nameable, Addressable, Telecommunicable, Identifiable {

	private CustodianOrganization organization;

	public CustodianTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public CustodianTemplate(Custodian custodian, CDAStandard standard) {
		super(custodian, standard);
		if (getRoot().getAssignedCustodian() == null) {
			getRoot().setAssignedCustodian(new AssignedCustodian());
		}
		organization = getRoot().getAssignedCustodian().getRepresentedCustodianOrganization();
		if (organization == null) {
			organization = new CustodianOrganization();
			getRoot().getAssignedCustodian().setRepresentedCustodianOrganization(organization);
		}
	}

	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		EN result = organization.getName();
		CS useCode = new CS(use);
		if (result == null) {
			result = new ON();
			result.setUse(new SET());
			organization.setName((ON) result);
		}
		if (!result.getUse().contains(useCode)) {
			result.getUse().add(useCode);
		}
		result.getParts().add(new ENXP(nameVal, type));
		return result;
	}

	@Override
	public void addName(EN name) {
		organization.setName((ON) name);
	}

	@Override
	public Set<EN> getNames() {
		Set<EN> result = null;
		if (organization.getName() != null) {
			result = new SET();
			result.add(organization.getName());
			return result;
		}
		return result;
	}

	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		AD addr = organization.getAddr();
		if (addr == null) {
			addr = new AD();
			organization.setAddr(addr);
		}
		Address result = new Address(addr);
		result.addAddressUse(use);
		result.addPart(value, type);
		return result;
	}

	@Override
	public void addAddress(Address address) {
		organization.setAddr(address.getAddr());
	}

	@Override
	public Set<Address> getAddresses() {
		Set<Address> address = null;
		if (organization.getAddr() != null) {
			address = new SET();
			address.add(new Address(organization.getAddr()));
		}
		return address;
	}

	@Override
	public TEL addTelecom(String tel) {
		return addTelecom(null, tel);
	}

	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		TEL result = new TEL(tel, use);
		addTelecom(result);
		return result;
	}

	@Override
	public void addTelecom(TEL tel) {
		organization.setTelecom(tel);
	}

	@Override
	public Set<TEL> getTelecoms() {
		Set<TEL> telecom = null;
		if (organization.getTelecom() != null) {
			telecom = new SET();
			telecom.add(organization.getTelecom());
		}
		return telecom;
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(organization).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(organization).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(organization).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(organization).getIds();
	}
}
