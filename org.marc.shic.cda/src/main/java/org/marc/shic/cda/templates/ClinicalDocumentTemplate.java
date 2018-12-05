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
 * @since 6-Feb-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Authorization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActClassClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActMoodEventOccurrence;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationSignature;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultTimeable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Timeable;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.core.PersonDemographic;

/**
 * Provides the base Template for a CDA document abstract.
 *
 * @author Ryan Albert
 */
public class ClinicalDocumentTemplate extends Template<ClinicalDocument> implements Identifiable, Codeable, Timeable {

	/**
	 * Creates a new ClinicalDocumentTemplate with the specified standard.
	 *
	 * @param standard
	 */
	protected ClinicalDocumentTemplate(CDAStandard standard) {
		this(null, standard);
	}

	protected ClinicalDocumentTemplate(ClinicalDocument root, CDAStandard standard) {
		super(root, standard);
		if (root != null) {
			//Populate authors.
			for (Author author : root.getAuthor()) {
				addChild(AuthorTemplate.wrapAuthor(author, standard));
			}
			//Populate record targets
			for (RecordTarget target : root.getRecordTarget()) {
				addChild(new RecordTargetTemplate(target, getStandard()));
			}
			//Populate custodian
			if (root.getCustodian() != null) {
				addChild(new CustodianTemplate(root.getCustodian(), getStandard()));
			}
			//Populate Legal Authenticator
			if (root.getLegalAuthenticator() != null) {
				addChild(new LegalAuthenticatorTemplate(root.getLegalAuthenticator(), getStandard()));
			}
			//Populate DataEnterer
			if (root.getDataEnterer() != null) {
				addChild(new DataEntererTemplate(root.getDataEnterer(), getStandard()));
			}
			//Populate DocumentationOf
			for (DocumentationOf doc : root.getDocumentationOf()) {
				addChild(new DocumentationOfTemplate(doc, getStandard()));
			}
			//Populate Authorization
			for (Authorization auth : root.getAuthorization()) {
				addChild(new AuthorizationTemplate(auth, getStandard()));
			}
		}
	}

	@Deprecated
	public AuthorTemplate addAuthor(PersonDemographic person) {
		AuthorTemplate result = (AuthorTemplate) addAuthor();
		CdaUtils.pDemographicToTemplate(person, result);
		return result;
	}

	/**
	 * Creates and adds a person author with no information.
	 *
	 * @return
	 */
	public AuthorTemplate addAuthor() {
		AuthorTemplate author = new AuthoringPersonTemplate(getStandard());
		addChild(author);
		getRoot().getAuthor().add(author.getRoot());
		return author;
	}

	/**
	 * Creates and adds a person author with an identifier.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public AuthorTemplate addAuthor(String rootId, String extId) {
		AuthorTemplate result = addAuthor();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and adds a person author with an identifier, and a legal
	 * first and last name.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public AuthorTemplate addAuthor(String rootId, String extId, String firstName, String lastName) {
		AuthoringPersonTemplate result = (AuthoringPersonTemplate) addAuthor(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Family);
		return result;
	}

	/**
	 * Creates and adds a person author with an identifier, a legal first
	 * and last name, and an authoring time.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param authTime
	 * @return
	 */
	public AuthorTemplate addAuthor(String rootId, String extId, String firstName, String lastName, Time authTime) {
		AuthorTemplate result = addAuthor(rootId, extId, firstName, lastName);
		result.setTime(authTime);
		return result;
	}

	@Deprecated
	public RecordTargetTemplate addRecordTarget(PersonDemographic demographic) {
		RecordTargetTemplate result = addRecordTarget();
		CdaUtils.pDemographicToTemplate(demographic, result);
		return result;
	}

	/**
	 * Creates and adds a record target with no information set.
	 *
	 * @return
	 */
	public RecordTargetTemplate addRecordTarget() {
		RecordTargetTemplate result = new RecordTargetTemplate(getStandard());
		addChild(result);
		getRoot().getRecordTarget().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and adds a record target with an identifier.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public RecordTargetTemplate addRecordTarget(String rootId, String extId) {
		RecordTargetTemplate result = addRecordTarget();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and adds a record target with an identifier, and a legal
	 * first and last name.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public RecordTargetTemplate addRecordTarget(String rootId, String extId, String firstName, String lastName) {
		RecordTargetTemplate result = addRecordTarget(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Family);
		return result;
	}

	/**
	 * Creates and adds a record target with an identifier, a legal first
	 * and last name, and a gender.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @return
	 */
	public RecordTargetTemplate addRecordTarget(String rootId, String extId, String firstName, String lastName, AdministrativeGender gender) {
		RecordTargetTemplate result = addRecordTarget(rootId, extId, firstName, lastName);
		result.setAdministrativeGenderCode(gender);
		return result;
	}

	/**
	 * Creates and adds a record target with an identifier, a legal first
	 * and last name, a gender, and a primary telephone number.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param tel
	 * @return
	 */
	public RecordTargetTemplate addRecordTarget(String rootId, String extId, String firstName, String lastName, AdministrativeGender gender, String tel) {
		RecordTargetTemplate result = addRecordTarget(rootId, extId, firstName, lastName, gender);
		result.addTelecom(tel);
		return result;
	}

	@Deprecated
	public LegalAuthenticatorTemplate setLegalAuthenticator(PersonDemographic person) {
		LegalAuthenticatorTemplate result = setLegalAuthenticator();
		CdaUtils.pDemographicToTemplate(person, result);
		return result;
	}
	/**
	 * Creates and sets the legal authenticator of the document with no
	 * information.
	 *
	 * @return
	 */
	public LegalAuthenticatorTemplate setLegalAuthenticator() {
		LegalAuthenticatorTemplate result = new LegalAuthenticatorTemplate(getStandard());
		addChild(result);
		getRoot().setLegalAuthenticator(result.getRoot());
		return result;
	}

	/**
	 * Creates and sets the legal authenticator of the document with an
	 * identifier.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public LegalAuthenticatorTemplate setLegalAuthenticator(String rootId, String extId) {
		LegalAuthenticatorTemplate result = setLegalAuthenticator();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and sets the legal authenticator of the document with an
	 * identifier, and a legal first and last name.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public LegalAuthenticatorTemplate setLegalAuthenticator(String rootId, String extId, String firstName, String lastName) {
		LegalAuthenticatorTemplate result = setLegalAuthenticator(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Family);
		return result;
	}

	/**
	 * Creates and sets the legal authenticator of the document with an
	 * identifier, a legal first and last name, and a participation
	 * signature.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param sig
	 * @return
	 */
	public LegalAuthenticatorTemplate setLegalAuthenticator(String rootId, String extId, String firstName, String lastName, ParticipationSignature sig) {
		LegalAuthenticatorTemplate result = setLegalAuthenticator(rootId, extId, firstName, lastName);
		result.getRoot().setSignatureCode(sig);
		return result;
	}

	/**
	 * Creates and sets the custodian of the document with no information.
	 *
	 * @return
	 */
	public CustodianTemplate setCustodian() {
		CustodianTemplate result = new CustodianTemplate(getStandard());
		addChild(result);
		getRoot().setCustodian(result.getRoot());
		return result;
	}

	/**
	 * Creates and sets the custodian of the document with an identifier
	 * attached.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public CustodianTemplate setCustodian(String rootId, String extId) {
		CustodianTemplate result = setCustodian();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and sets the custodian of the document with an identifier and
	 * a legal custodian organization name attached.
	 *
	 * @param rootId
	 * @param extId
	 * @param orgName
	 * @return
	 */
	public CustodianTemplate setCustodian(String rootId, String extId, String orgName) {
		CustodianTemplate result = setCustodian(rootId, extId);
		result.addName(EntityNameUse.Legal, orgName, EntityNamePartType.Title);
		return result;
	}

	/**
	 * Creates and sets the custodian of the document with an identifier, a
	 * legal custodian organization name, and telephone number attached.
	 *
	 * @param rootId
	 * @param extId
	 * @param orgName
	 * @param tel
	 * @return
	 */
	public CustodianTemplate setCustodian(String rootId, String extId, String orgName, TEL telecom) {
		CustodianTemplate result = setCustodian(rootId, extId, orgName);
		result.addTelecom(telecom);
		return result;
	}

	/**
	 * Creates and adds an empty DocumentationOf template.
	 *
	 * @return
	 */
	public DocumentationOfTemplate addDocumentationOf() {
		DocumentationOfTemplate result = new DocumentationOfTemplate(getStandard());
		addChild(result);
		getRoot().getDocumentationOf().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and adds a data enterer with no information.
	 *
	 * @return
	 */
	public DataEntererTemplate setDataEnterer() {
		DataEntererTemplate result = new DataEntererTemplate(getStandard());
		addChild(result);
		getRoot().setDataEnterer(result.getRoot());
		return result;
	}

	/**
	 * Creates and adds a data enterer with an identifier.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public DataEntererTemplate setDataEnterer(String rootId, String extId) {
		DataEntererTemplate result = setDataEnterer();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and adds a data enterer with an identifier and a legal first
	 * and last name.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public DataEntererTemplate setDataEnterer(String rootId, String extId, String firstName, String lastName) {
		DataEntererTemplate result = setDataEnterer(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Family);
		return result;
	}

	/**
	 * Creates and adds a data entere rwith an identifier, a legal first and
	 * last name, and the context of control.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param control
	 * @return
	 */
	public DataEntererTemplate setDataEnterer(String rootId, String extId, String firstName, String lastName, ContextControl control) {
		DataEntererTemplate result = setDataEnterer(rootId, extId, firstName, lastName);
		result.getRoot().setContextControlCode(control);
		return result;
	}

	/**
	 * Creates and adds an authorization with consent to the document.
	 *
	 * @return
	 */
	public AuthorizationTemplate addConsentPolicy() {
		AuthorizationTemplate result = new AuthorizationTemplate(getStandard());
		addChild(result);
		getRoot().getAuthorization().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and adds an authorization with a consent identified by ID to
	 * the document.
	 *
	 * @param policyIdRoot
	 * @param policyIdExt
	 * @return
	 */
	public AuthorizationTemplate addConsentPolicy(String policyIdRoot, String policyIdExt) {
		AuthorizationTemplate result = addConsentPolicy();
		result.addId(policyIdRoot, policyIdExt);
		return result;
	}

	/**
	 * Applies the data in the template to a clinical document everest
	 * object and applies all of the template rules located in the document
	 * and all of its children.
	 *
	 * @return A compiled clinical document everest object.
	 */
	public final ClinicalDocument getDocument() {
		finalizeRoot();
		return getRoot();
	}

	/////////////////////////////////////////////////////////////////////////////
	////// EVEREST-SPECIFIC FUNCTIONS
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the confidentiality code.
	 *
	 * @return
	 */
	public x_BasicConfidentialityKind getConfidentialityCode() {
		CS<x_BasicConfidentialityKind> resultCode = getRoot().getConfidentialityCode();
		if (resultCode != null) {
			return resultCode.getCode();
		}
		return null;
	}

	/**
	 * Sets the confidentiality code.
	 *
	 * @param code
	 */
	public void setConfidentialityCode(x_BasicConfidentialityKind code) {
		getRoot().setConfidentialityCode(code);
	}

	/**
	 * Gets the class code for the document.
	 *
	 * @return
	 */
	public ActClassClinicalDocument getClassCode() {
		CS<ActClassClinicalDocument> resultCode = getRoot().getClassCode();
		if (resultCode != null) {
			return resultCode.getCode();
		}
		return null;
	}

	/**
	 * Sets the class code for the document.
	 *
	 * @param value
	 */
	public void setClassCode(ActClassClinicalDocument value) {
		getRoot().overrideClassCode(value);
	}

	/**
	 * Gets the mood code for the document.
	 *
	 * @return
	 */
	public ActMoodEventOccurrence getMoodCode() {
		CS<ActMoodEventOccurrence> resultCode = getRoot().getMoodCode();
		if (resultCode != null) {
			return resultCode.getCode();
		}
		return null;
	}

	/**
	 * Sets the mood code for the document.
	 *
	 * @param value
	 */
	public void setMoodCode(ActMoodEventOccurrence value) {
		getRoot().overrideMoodCode(value);
	}

	/////////////////////////////////////////////////////////////////////////////
	////// IDENTIFIABLE INTERFACE METHODS
	/////////////////////////////////////////////////////////////////////////////
	@Override
	public void addId(String root, String extension) {
		addId(new II(root, extension));
	}

	@Override
	public void addId(String root) {
		addId(root, null);
	}

	@Override
	public void addId(II identifier) {
		getRoot().setId(identifier);
	}

	@Override
	public Set<II> getIds() {
		Set<II> result = null;
		if (getRoot().getId() != null) {
			result = new SET<II>();
			result.add(getRoot().getId());
		}
		return result;
	}

	@Override
	public Code getCode() {
		return DefaultCodeable.getCodeable(getRoot()).getCode();
	}

	@Override
	public void setCode(Code value) {
		DefaultCodeable.getCodeable(getRoot()).setCode(value);
	}

	@Override
	public Time getTime() {
		return DefaultTimeable.getTimeable(getRoot(), "EffectiveTime").getTime();
	}

	@Override
	public void setTime(Time value) {
		DefaultTimeable.getTimeable(getRoot(), "EffectiveTime").setTime(value);
	}

}
