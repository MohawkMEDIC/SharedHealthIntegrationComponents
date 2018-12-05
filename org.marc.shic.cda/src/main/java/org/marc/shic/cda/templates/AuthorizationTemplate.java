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
 * @since 11-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Authorization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consent;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.utils.SNOMED_CT_Codes;

/**
 *
 * @author Ryan Albert
 */
public class AuthorizationTemplate extends Template<Authorization> implements Codeable, Identifiable {

	private Consent consent;

	public AuthorizationTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public AuthorizationTemplate(Authorization root, CDAStandard standard) {
		super(root, standard);
		if (getRoot().getConsent() == null) {
			consent = new Consent();
			getRoot().setConsent(consent);
		} else {
			consent = getRoot().getConsent();
		}
	}

	@Override
	protected void initRoot() {
		consent = new Consent();
		getRoot().setConsent(consent);
		consent.setCode(SNOMED_CT_Codes.CONSENT_GIVEN_FOR_EHR_SHARING.getCodedElement());
		consent.setStatusCode("completed");
	}

	@Override
	public Code getCode() {
		return DefaultCodeable.getCodeable(consent).getCode();
	}

	@Override
	public void setCode(Code value) {
		DefaultCodeable.getCodeable(consent).setCode(value);
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(consent).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(consent).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(consent).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(consent).getIds();
	}

}
