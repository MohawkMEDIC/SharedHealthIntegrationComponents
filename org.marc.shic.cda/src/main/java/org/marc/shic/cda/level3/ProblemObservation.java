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
 * @since 4-Feb-2014
 *
 */
package org.marc.shic.cda.level3;

import java.io.UnsupportedEncodingException;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.templates.clinicalstatements.ObservationTemplate;

/**
 * Defines an observation of a problem/symptom that a patient is experiencing.
 *
 * @author Ryan Albert
 */
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.28")
public final class ProblemObservation extends ObservationTemplate {

	/**
	 * Constructs a new entry conforming to the specified standard.
	 *
	 * @param standard The standard to conform to.
	 */
	public ProblemObservation(CDAStandard standard) {
		this(null, standard);
	}

	/**
	 * Wraps an existing entry conforming to the specified standard.
	 *
	 * @param root The existing everest object to wrap.
	 * @param standard The standard to conform to.
	 */
	public ProblemObservation(Observation root, CDAStandard standard) {
		super(root, standard);
	}

	@Override
	protected void initRoot() {
		getRoot().setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		getRoot().setStatusCode(ActStatus.Completed);
	}

	public void setText(String text) {
		try {
			getRoot().setText(text);
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error("Unable to encode text.", ex);
		}
	}

	public String getText() {
		return new String(getRoot().getText().getData());
	}

	public void setStatusCode(ActStatus status) {
		getRoot().setStatusCode(status);
	}

	public ActStatus getStatusCode() {
		return getRoot().getStatusCode().getCode();
	}
}
