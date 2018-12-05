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
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ManufacturedProduct;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.everestfunc.DefaultIdentifiable;
import org.marc.shic.cda.everestfunc.Identifiable;

/**
 * Wraps an everest ManufacturedProduct object and provides various method to
 * assist in populating the data within.
 *
 * @author Ryan Albert
 */
public class ManufacturedProductTemplate extends Template<ManufacturedProduct> implements Identifiable {

	/**
	 * Constructs the ManufacturedProduct conforming to a specific standard.
	 *
	 * @param standard
	 */
	public ManufacturedProductTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public ManufacturedProductTemplate(ManufacturedProduct root, CDAStandard standard) {
		super(root, standard);
	}

	/**
	 * Gets the organization information about the manufacturer, if
	 * information exists.
	 *
	 * @return
	 */
	public OrganizationTemplate getManufactureOrganization() {
		return (OrganizationTemplate) getChild(OrganizationTemplate.class);
	}

	/**
	 * Creates and sets organization information about the manufacturer. No
	 * information will be set to the template.
	 *
	 * @return
	 */
	public OrganizationTemplate setManufactureOrganization() {
		OrganizationTemplate result = new OrganizationTemplate(getStandard());
		addChild(result);
		getRoot().setManufacturerOrganization(result.getRoot());
		return result;
	}

	/**
	 * Creates and sets organization information about the manufacturer. The
	 * specified name of the organization will be set.
	 *
	 * @param name
	 * @return
	 */
	public OrganizationTemplate setManufactureOrganization(String name) {
		OrganizationTemplate result = setManufactureOrganization();
		result.addName(EntityNameUse.Legal, name, EntityNamePartType.Given);
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////
	////// IDENTIFIABLE INTERFACE METHODS
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds an identifier. Does not remove previously defined identifiers.
	 *
	 * @param id
	 */
	@Override
	public void addId(II id) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(id);
	}

	/**
	 * Adds an ID from a root string.
	 *
	 * @param root
	 */
	@Override
	public void addId(String root) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root);
	}

	/**
	 * Adds an ID from a root string and an extension string.
	 *
	 * @param root
	 * @param extension
	 */
	@Override
	public void addId(String root, String extension) {
		DefaultIdentifiable.getIdentifiable(getRoot()).addId(root, extension);
	}

	/**
	 * Gets a set of identifiers that have been added.
	 *
	 * @return
	 */
	@Override
	public Set<II> getIds() {
		return DefaultIdentifiable.getIdentifiable(getRoot()).getIds();
	}

}
