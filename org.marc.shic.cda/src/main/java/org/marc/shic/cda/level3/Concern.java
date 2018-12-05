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
 *
 * Date: January 17, 2014
 *
 */
package org.marc.shic.cda.level3;

import java.util.Calendar;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Act;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryAct;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentActMood;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.clinicalstatements.ClinicalStatementTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * A concern is a type of entry under certain sections.
 *
 * TODO: Create the base concern class to work with all types of concerns
 * (allergy, medical state, etc.)
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.4.5.1")
public class Concern extends ClinicalStatementTemplate<Act> {

	private Act act;
	private Calendar startTime;
	private Calendar endTime;

	/**
	 * Constructs a new entry conforming to the specified standard.
	 *
	 * @param standard The standard to conform to.
	 */
	public Concern(CDAStandard standard) {
		this(null, standard);

	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public Concern(Act root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	protected void initRoot() {
		act = new Act(x_ActClassDocumentEntryAct.Act, x_DocumentActMood.Eventoccurrence);
		//getRoot().setClinicalStatement(act);
		act.getCode().setNullFlavor(NullFlavor.NotApplicable);
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setStartTime(Calendar time) {
		act.getEffectiveTime().setLow(new TS(time));
		this.startTime = time;
	}

	public void setEndTime(Calendar time) {
		act.getEffectiveTime().setHigh(new TS(time));
		this.endTime = time;
	}

	public Act getAct() {
		return act;
	}

	public ActStatus getStatusCode() {
		return act.getStatusCode().getCode();
	}

	public void setStatusCode(ActStatus status) {
		act.setStatusCode(status);
	}

	@Override
	public void setReferenceText(String reference) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
