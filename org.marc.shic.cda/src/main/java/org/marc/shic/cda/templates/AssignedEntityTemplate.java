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
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultAddressable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.DefaultNameable;
import org.marc.shic.cda.everestfunc.DefaultTelecommunicable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.everestfunc.Telecommunicable;

/**
 *
 * @author Ryan Albert
 * @param <T>
 */
public abstract class AssignedEntityTemplate<T extends InfrastructureRoot> extends Template<T> implements Addressable, Codeable, Identifiable, Nameable, Telecommunicable {

	private final AssignedEntity entity;
	private final Person person;

	public AssignedEntityTemplate(T root, CDAStandard standard) {
		super(root, standard);
		entity = new AssignedEntity();
		person = new Person();
		entity.setAssignedPerson(person);
		setAssignedEntity(entity);
	}

	@Override
	public final void setCode(Code value) {
		DefaultCodeable.getCodeable(entity).setCode(value);
	}

	@Override
	public final Code getCode() {
		return DefaultCodeable.getCodeable(entity).getCode();
	}

	@Override
	public final void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(entity).addId(root, extension);
	}

	@Override
	public final void addId(String root) {
		DefaultIdentifiable.getIdentifiable(entity).addId(root);
	}

	@Override
	public final void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(entity).addId(identifier);
	}

	@Override
	public final Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(entity).getIds();
	}

	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		return DefaultNameable.getNameable(person).addName(use, nameVal, type);
	}

	@Override
	public void addName(EN name) {
		DefaultNameable.getNameable(person).addName(name);
	}

	@Override
	public Set<EN> getNames() {
		return DefaultNameable.getNameable(person).getNames();
	}

	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		return DefaultAddressable.getAddressable(entity).addAddress(use, value, type);
	}

	@Override
	public void addAddress(Address address) {
		DefaultAddressable.getAddressable(entity).addAddress(address);
	}

	@Override
	public Set<Address> getAddresses() {
		return DefaultAddressable.getAddressable(entity).getAddresses();
	}

	@Override
	public TEL addTelecom(String tel) {
		return DefaultTelecommunicable.getTelecommunicable(entity).addTelecom(tel);
	}

	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		return DefaultTelecommunicable.getTelecommunicable(entity).addTelecom(use, tel);
	}

	@Override
	public void addTelecom(TEL tel) {
		DefaultTelecommunicable.getTelecommunicable(entity).addTelecom(tel);
	}

	@Override
	public Set<TEL> getTelecoms() {
		return DefaultTelecommunicable.getTelecommunicable(entity).getTelecoms();
	}

	protected abstract void setAssignedEntity(AssignedEntity ent);

	public OrganizationTemplate getRepresentedOrganization() {
		return (OrganizationTemplate) getChild(OrganizationTemplate.class);
	}

	public void setRepresentedOrganization(OrganizationTemplate organization) {
		removeAllChildren(OrganizationTemplate.class);
		addChild(organization);

		entity.setRepresentedOrganization(organization.getRoot());
	}

	public Person getAssignedPerson() {
		return entity.getAssignedPerson();
	}
}
