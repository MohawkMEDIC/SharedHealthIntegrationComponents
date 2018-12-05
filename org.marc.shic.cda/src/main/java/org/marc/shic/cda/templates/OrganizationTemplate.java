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
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organization;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.CDAStandard;
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
public class OrganizationTemplate extends Template<Organization> implements Nameable, Addressable, Identifiable, Telecommunicable {

	public OrganizationTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public OrganizationTemplate(Organization root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		return DefaultAddressable.getAddressable(getRoot()).addAddress(use, value, type);
	}

	@Override
	public void addAddress(Address address) {
		DefaultAddressable.getAddressable(getRoot()).addAddress(address);
	}

	@Override
	public Set<Address> getAddresses() {
		return DefaultAddressable.getAddressable(getRoot()).getAddresses();
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(getRoot()).getIds();
	}

	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		return DefaultNameable.getNameable(getRoot()).addName(use, nameVal, type);
	}

	@Override
	public void addName(EN name) {
		DefaultNameable.getNameable(getRoot()).addName(name);
	}

	@Override
	public Set<EN> getNames() {
		return DefaultNameable.getNameable(getRoot()).getNames();
	}

	@Override
	public TEL addTelecom(String tel) {
		return DefaultTelecommunicable.getTelecommunicable(getRoot()).addTelecom(tel);
	}

	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		return DefaultTelecommunicable.getTelecommunicable(getRoot()).addTelecom(use, tel);
	}

	@Override
	public void addTelecom(TEL tel) {
		DefaultTelecommunicable.getTelecommunicable(getRoot()).addTelecom(tel);
	}

	@Override
	public Set<TEL> getTelecoms() {
		return DefaultTelecommunicable.getTelecommunicable(getRoot()).getTelecoms();
	}
	
	
}
