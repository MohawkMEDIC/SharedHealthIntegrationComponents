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
 * @since 24-Jan-2014
 *
 */
package org.marc.shic.cda.level2;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 *
 * @author Ryan Albert
 */
@DefineSection(title="History of Past Illnesses", order=1000, code="", codeName="History of Past Illnesses")
@TemplateID(standard = CDAStandard.PCC, oid = "1.3.6.1.4.1.19376.1.5.3.1.3.8")
public class HistoryOfPastIllnessSection extends SectionTemplate {

	public HistoryOfPastIllnessSection(CDAStandard standard) {
		this(null, standard);
	}
	
	public HistoryOfPastIllnessSection(Section root, CDAStandard standard) {
		super(root, standard);
	}
}
