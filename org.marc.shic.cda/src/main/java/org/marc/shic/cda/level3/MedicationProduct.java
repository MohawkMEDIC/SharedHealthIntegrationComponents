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

import java.util.HashSet;
import java.util.Set;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ManufacturedProduct;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Material;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityDeterminerDetermined;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.cda.templates.ManufacturedProductTemplate;

/**
 * A part of the medication that defines the actual physical product that is
 * consumed by the patient.
 *
 * @author Ryan Albert
 */
public final class MedicationProduct extends ManufacturedProductTemplate implements Codeable, Nameable {

	private Material material;

	/**
	 * Constructs a new product with the required parameters.
	 *
	 * @param standard The standard to conform to.
	 * @param productCode The code that indicates the type of product.
	 */
	protected MedicationProduct(CDAStandard standard, Code productCode) {
		this(null, standard);
		setCode(productCode);
	}

	/**
	 * Wraps an existing everest Section object.
	 *
	 * @param root The Section object to wrap.
	 * @param standard The standard to conform to.
	 */
	public MedicationProduct(ManufacturedProduct root, CDAStandard standard) {
		super(root, standard);
		material = getRoot().getManufacturedDrugOrOtherMaterialIfManufacturedMaterial();
		if (material == null) {
			material = new Material();
			getRoot().setManufacturedDrugOrOtherMaterial(material);
		}
	}

	@Override
	protected void initRoot() {
		addTemplateRule(TemplateRuleID.CCD_Product);

		material = new Material();
		getRoot().setManufacturedDrugOrOtherMaterial(material);
		material.setDeterminerCode(EntityDeterminerDetermined.Described);
	}

	/**
	 * Get a Code
	 *
	 * @return A Code object that represents the Template code.
	 */
	@Override
	public Code getCode() {
		return new Code(material.getCode());
	}

	/**
	 * Sets a code
	 *
	 * @param value A Code object to be set as the Template code.
	 */
	@Override
	public void setCode(Code value) {
		material.setCode((CE<String>) Code.translate(value, "2.16.840.1.113883.6.96", "RxNorm").getCode(CE.class));
	}

	/**
	 * Gets a displayable name from the medication product.
	 *
	 * @return A String containing the display name of the product.
	 */
	public String getDisplayName() {
		if (getNames() != null) {
			return getNames().iterator().next().toString();
		}
		Code codeVal = getCode();
		if (codeVal != null) {
			if (codeVal.getCode() != null) {
				return (String) codeVal.getCode();
			} else {
				return codeVal.getOriginalText();
			}
		}
		return "";
	}

	/**
	 * Adds a use to the currently assigned name, and adds a part to the
	 * name with the specified type.
	 *
	 * @param use The use to add to the assigned name.
	 * @param nameVal The String value to add to the name
	 * @param type The part type that the nameVal will be set to.
	 * @return An everest object that represents the name being
	 * created/modified.
	 */
	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		if (getNames() == null) {
			addName(new EN());
		}
		EN name = getNames().iterator().next();
		if (use != null) {
			CS useCode = new CS(use);
			if (!name.getUse().contains(useCode)) {
				name.getUse().add(useCode);
			}
		}
		name.getParts().add(new ENXP(nameVal, type));
		return name;
	}

	/**
	 * Overrides the assigned name.
	 *
	 * @param name The name to add/override.
	 */
	@Override
	public void addName(EN name) {
		material.setName(name);
	}

	/**
	 * Gets a set that contains the assigned name.
	 *
	 * @return A set that contains one name. The set returned will be null
	 * if there is no assigned name.
	 */
	@Override
	public Set<EN> getNames() {
		Set<EN> result = null;
		if (material.getName() != null) {
			result = new HashSet();
			result.add(material.getName());
		}
		return result;
	}
}
