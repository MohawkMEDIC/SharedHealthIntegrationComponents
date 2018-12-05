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
 * @since 3-Apr-2014
 *
 */
package org.marc.shic.cda.templates;

import java.util.Set;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.everestfunc.DefaultNameable;
import org.marc.shic.cda.everestfunc.Nameable;

/**
 *
 * @author Ryan Albert
 */
public class AuthoringPersonTemplate extends AuthorTemplate implements Nameable {
	
	private Person person;
	
	public AuthoringPersonTemplate(CDAStandard standard) {
		this(null, standard);
	}

	public AuthoringPersonTemplate(Author root, CDAStandard standard) {
		super(root, standard);
		person = getRoot().getAssignedAuthor().getAssignedAuthorChoiceIfAssignedPerson();
		if(getRoot().getAssignedAuthor().getAssignedAuthorChoiceIfAssignedPerson() == null) {
			person = new Person();
			getRoot().getAssignedAuthor().setAssignedAuthorChoice(person);
		}
	}

	@Override
	public EN addName(EntityNameUse use, String nameVal, EntityNamePartType type) {
		return DefaultNameable.getNameable(person).addName(use, nameVal, type);
	}

	@Override
	public void addName(EN name) {
		DefaultNameable.getNameable(person).addName(name);
	}

	@Override
	public Set<EN> getNames() {
		return DefaultNameable.getNameable(person).getNames();
	}

	
}
