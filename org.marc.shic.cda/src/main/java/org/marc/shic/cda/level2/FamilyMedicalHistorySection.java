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
 * @author brownp Date: Aug 21, 2013
 *
 */
package org.marc.shic.cda.level2;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.shic.cda.DefineSection;
import org.marc.shic.cda.TemplateID;
import org.marc.shic.cda.templates.SectionTemplate;
import org.marc.shic.cda.datatypes.CDAStandard;

@DefineSection(title = "Family Medical History", order = 5, code = "10157-6", codeName = "Family History")
@TemplateID(standard = CDAStandard.CCD, oid = "2.16.840.1.113883.10.20.1.4")
public class FamilyMedicalHistorySection extends SectionTemplate {

	public FamilyMedicalHistorySection(CDAStandard standard) {
		this(null, standard);
	}

	public FamilyMedicalHistorySection(Section root, CDAStandard standard) {
		super(root, standard);
	}
}
