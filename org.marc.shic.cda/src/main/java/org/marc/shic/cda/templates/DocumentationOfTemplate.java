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
 * @since 10-Mar-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.DocumentationOf;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer1;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ServiceEvent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActMoodEventOccurrence;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationFunction;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ServiceEventPerformer;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.DefaultCodeable;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.DefaultTimeable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Timeable;
import org.marc.shic.cda.utils.CdaUtils;

/**
 *
 * @author Ryan Albert
 */
public class DocumentationOfTemplate extends Template<DocumentationOf> implements Timeable, Codeable, Identifiable {

	private ServiceEvent event;

	public DocumentationOfTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public DocumentationOfTemplate(DocumentationOf root, CDAStandard standard) {
		super(root, standard);
		if (getRoot().getServiceEvent() != null) {
			event = getRoot().getServiceEvent();
			for (Performer1 perf : event.getPerformer()) {
				addChild(new Performer1Template(perf, standard));
			}
		} else {
			event = new ServiceEvent();
			getRoot().setServiceEvent(event);
		}
		if (standard == CDAStandard.BPPC) {
			CdaUtils.addTemplateRuleID(event, "1.3.6.1.4.1.19376.1.5.3.1.2.6");
		}
	}

	@Override
	protected void initRoot() {
		event = new ServiceEvent();
		getRoot().setServiceEvent(event);
		if (getStandard() == CDAStandard.BPPC) {
			event.overrideMoodCode(ActMoodEventOccurrence.Eventoccurrence);
			event.setClassCode("ACT");
			setTime(new Time());
		}
	}

	/**
	 * Sets the DocumentationOf time that represents the time period that
	 * the document represents.
	 *
	 * @param documentationTime
	 */
	@Override
	public void setTime(Time documentationTime) {
		DefaultTimeable.getTimeable(event, "EffectiveTime").setTime(documentationTime);
	}

	@Override
	public Time getTime() {
		return DefaultTimeable.getTimeable(event, "EffectiveTime").getTime();
	}

	@Override
	public Code getCode() {
		return DefaultCodeable.getCodeable(event).getCode();
	}

	@Override
	public void setCode(Code value) {
		DefaultCodeable.getCodeable(event).setCode(value);
	}

	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(event).addId(root, extension);
	}

	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(event).addId(root);
	}

	@Override
	public void addId(II identifier) {
		DefaultIdentifiable.getIdentifiable(event).addId(identifier);
	}

	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(event).getIds();
	}

	/**
	 * Creates and adds a performer to the DocumentationOf with no
	 * information.
	 *
	 * @return
	 */
	public Performer1Template addPerformer() {
		Performer1Template result = new Performer1Template(getStandard());
		result.getRoot().setFunctionCode(ParticipationFunction.PrimaryCarePhysician);
		result.getRoot().setTypeCode(x_ServiceEventPerformer.PRF);
		addChild(result);
		event.getPerformer().add(result.getRoot());
		return result;
	}

	/**
	 * Creates and adds a performer to the DocumentationOf with an
	 * identifier.
	 *
	 * @param rootId
	 * @param extId
	 * @return
	 */
	public Performer1Template addPerformer(String rootId, String extId) {
		Performer1Template result = addPerformer();
		result.addId(rootId, extId);
		return result;
	}

	/**
	 * Creates and adds a performer to the DocumentationOf with an
	 * identifier and a legal first and last name.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public Performer1Template addPerformer(String rootId, String extId, String firstName, String lastName) {
		Performer1Template result = addPerformer(rootId, extId);
		result.addName(EntityNameUse.Legal, firstName, EntityNamePartType.Given);
		result.addName(EntityNameUse.Legal, lastName, EntityNamePartType.Family);
		return result;
	}

	/**
	 * Creates and adds a performer to the DocumentationOf with an
	 * identifier, a legal first and last name, and the period of
	 * performance.
	 *
	 * @param rootId
	 * @param extId
	 * @param firstName
	 * @param lastName
	 * @param performingPeriod
	 * @return
	 */
	public Performer1Template addPerformer(String rootId, String extId, String firstName, String lastName, Time performingPeriod) {
		Performer1Template result = addPerformer(rootId, extId, firstName, lastName);
		result.setTime(performingPeriod);
		return result;
	}

}
