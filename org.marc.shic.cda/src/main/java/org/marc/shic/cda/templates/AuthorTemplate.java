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
 * @getRoot()or Ryan Albert
 * @since 4-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultAddressable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.DefaultTelecommunicable;
import org.marc.shic.cda.everestfunc.DefaultTimeable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Telecommunicable;
import org.marc.shic.cda.everestfunc.Timeable;

/**
 *
 * @author Ryan Albert
 * @param <T>
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.2.20.1")
public abstract class AuthorTemplate extends Template<Author> implements Timeable, Codeable, Identifiable, Telecommunicable, Addressable {

	private AssignedAuthor assignedAuthor;

	public AuthorTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public AuthorTemplate(Author root, CDAStandard standard) {
		super(root, standard);
		assignedAuthor = getRoot().getAssignedAuthor();
		if (assignedAuthor == null) {
			assignedAuthor = new AssignedAuthor();
			getRoot().setAssignedAuthor(assignedAuthor);
		}
	}

	@Override
	protected void initRoot() {
		assignedAuthor = new AssignedAuthor();
		getRoot().setAssignedAuthor(assignedAuthor);
		getRoot().setContextControlCode(ContextControl.OverridingPropagating);

		setTime(Time.now());
	}

	@Override
	public final void setTime(Time time) {
		DefaultTimeable.getTimeable(getRoot(), "Time").setTime(time);
	}

	@Override
	public final Time getTime() {
		return DefaultTimeable.getTimeable(getRoot(), "Time").getTime();
	}

	@Override
	public Address addAddress(PostalAddressUse use, String value, AddressPartType type) {
		return DefaultAddressable.getAddressable(assignedAuthor).addAddress(use, value, type);
	}

	@Override
	public void addAddress(Address address) {
		DefaultAddressable.getAddressable(assignedAuthor).addAddress(address);
	}

	@Override
	public Set<Address> getAddresses() {
		return DefaultAddressable.getAddressable(assignedAuthor).getAddresses();
	}

	@Override
	public Code getCode() {
		return DefaultCodeable.getCodeable(assignedAuthor).getCode();
	}

	@Override
	public void setCode(Code value) {
		DefaultCodeable.getCodeable(assignedAuthor).setCode(value);
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(assignedAuthor).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(assignedAuthor).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(assignedAuthor).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(assignedAuthor).getIds();
	}

	@Override
	public TEL addTelecom(String tel) {
		return DefaultTelecommunicable.getTelecommunicable(assignedAuthor).addTelecom(tel);
	}

	@Override
	public TEL addTelecom(TelecommunicationsAddressUse use, String tel) {
		return DefaultTelecommunicable.getTelecommunicable(assignedAuthor).addTelecom(use, tel);
	}

	@Override
	public void addTelecom(TEL tel) {
		DefaultTelecommunicable.getTelecommunicable(assignedAuthor).addTelecom(tel);
	}

	@Override
	public Set<TEL> getTelecoms() {
		return DefaultTelecommunicable.getTelecommunicable(assignedAuthor).getTelecoms();
	}

	public static AuthorTemplate wrapAuthor(Author author, CDAStandard standard) {
		AuthorTemplate result = null;
		if (author.getAssignedAuthor() != null) {
			if (author.getAssignedAuthor().getAssignedAuthorChoiceIfAssignedAuthoringDevice() != null) {
				result = new AuthoringDeviceTemplate(author, standard);
			} else if (author.getAssignedAuthor().getAssignedAuthorChoiceIfAssignedPerson() != null) {
				result = new AuthoringPersonTemplate(author, standard);
			}
		}
		return result;
	}
}
