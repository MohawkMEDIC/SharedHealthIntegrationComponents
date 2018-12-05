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
 * @since Aug 21, 2013
 *
 */
package org.marc.shic.cda;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.templateRules.AbstractTemplateRule;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.cda.templateRules.TemplateRuleFactory;
import org.marc.shic.cda.datatypes.CDAStandard;

/**
 * A wrapper for the everest InfrastructureRoot object. Contains other Template
 * objects that also represent other InfrastructureRoot objects.
 *
 * @author Ryan Albert
 * @param <T> The type of InfrastructureRoot class that the Template wraps.
 */
public class Template<T extends InfrastructureRoot> {

	private final T root;
	private final CDAStandard templateStandard;
	private final ArrayList<ResultDetail> resultDetails = new ArrayList<ResultDetail>();
	private final LinkedHashSet<Template> childTemplates = new LinkedHashSet<Template>();
	protected final Logger LOGGER = Logger.getLogger(getClass());

	/**
	 * Sets this template to wrap a single InfrastructureRoot object that
	 * shall conform to the specified standard.
	 *
	 * @param root The InfrastructureRoot object to contain/wrap. If null,
	 * will create a new instance of the generic type provided.
	 * @param standard The standard that the template shall conform to.
	 */
	public Template(T root, CDAStandard standard) {
		templateStandard = standard;
		if (root == null) {
			this.root = createRoot();
			initRoot();
		} else {
			this.root = root;
		}
		addTemplateIDAnnotations();
	}

	/**
	 * Creates an object from the class defines as the generic type
	 * parameter for the template.
	 *
	 * @return An instantiated InfrastructureRoot object from the provided
	 * generic type parameter of the Template class definition.
	 */
	private T createRoot() {
		Exception instantiationError = null;
		try {
			Type genSuperClass = getClass().getGenericSuperclass();
			while (genSuperClass != null) {
				if (genSuperClass instanceof ParameterizedType) {
					ParameterizedType parameterized = (ParameterizedType) genSuperClass;
					return (T) ((Class) parameterized.getActualTypeArguments()[0]).newInstance();
				} else {
					genSuperClass = ((Class) genSuperClass).getGenericSuperclass();
				}
			}
		} catch (Exception ex) {
			instantiationError = ex;
		}
		throw new RuntimeException("Invalid software implementation", instantiationError);
	}

	/**
	 * Called when the root object given as a parameter of the Template is
	 * null. Should be overridden in any sub Template classes that require
	 * the root object to be initialized to some state.
	 */
	protected void initRoot() {
	}

	/**
	 * Adds all of the TemplateID annotations set upon the sub-class.
	 */
	private void addTemplateIDAnnotations() {
		ArrayList<TemplateID> annotationRules = new ArrayList();
		TemplateID setId = (TemplateID) getClass().getAnnotation(TemplateID.class);
		TemplateIDSet setIdSet = (TemplateIDSet) getClass().getAnnotation(TemplateIDSet.class);
		if (setId != null) {
			annotationRules.add(setId);
		}
		if (setIdSet != null) {
			annotationRules.addAll(Arrays.asList(setIdSet.value()));
		}
		for (TemplateID rule : annotationRules) {
			if (templateStandard.isCompatibleWith(rule.standard())) {
				addTemplateRule(rule.oid());
			}
		}
	}

	/**
	 * Adds a template rule object to the template.
	 *
	 * @param templateRule The AbstractTemplateRule to add to the template.
	 */
	public void addTemplateRule(AbstractTemplateRule templateRule) {
		addTemplateRule(templateRule.getTemplateID());
	}

	/**
	 * Adds a template rule to the template by enum constant.
	 *
	 * @param templateRuleID The TemplateRuleID to add to.
	 */
	public void addTemplateRule(TemplateRuleID templateRuleID) {
		addTemplateRule(templateRuleID.getOid());
	}

	/**
	 * Adds a template rule to the template by OID string.
	 *
	 * @param templateStr A template rule ID by OID string
	 */
	public void addTemplateRule(String templateStr) {
		if (templateStr == null || templateStr.isEmpty()) {
			return;
		}
		LIST<II> ids = getRoot().getTemplateId();
		if (ids != null) {
			for (II id : getRoot().getTemplateId()) {
				if (id.getRoot().equals(templateStr)) {
					return;
				}
			}
		} else {
			ids = new LIST<II>();
			getRoot().setTemplateId(ids);
		}
		AbstractTemplateRule rule = TemplateRuleFactory.getRule(templateStr);
		for (TemplateRuleID dependent : rule.getDependencies()) {
			addTemplateRule(dependent.getOid());
		}
		ids.add(new II(templateStr));
	}

	/**
	 * Removes a template rule from this template.
	 *
	 * @param templateRule The AbstractTemplateRule to remove.
	 */
	public void removeTemplateRule(AbstractTemplateRule templateRule) {
		removeTemplateRule(templateRule.getTemplateID());
	}

	/**
	 * Removes a template rule by ID from this template.
	 *
	 * @param templateRuleID The TemplateRuleID that represents a template
	 * rule set to remove.
	 */
	public void removeTemplateRule(TemplateRuleID templateRuleID) {
		removeTemplateRule(templateRuleID.getOid());
	}

	/**
	 * Removes a template rule by ID from the template.
	 *
	 * @param oid A String representing the OID of the template rule to
	 * remove.
	 */
	public void removeTemplateRule(String oid) {
		LIST<II> ids = getRoot().getTemplateId();
		if (ids != null) {
			II removeResult = null;
			for (II id : ids) {
				if (id.getRoot().equals(oid)) {
					removeResult = id;
					break;
				}
			}
			ids.remove(removeResult);
		}
	}

	/**
	 * Notifies the template to complete any generation required for a
	 * finalization of the everest root object. Override in sub-template
	 * classes to do some pre-processing.
	 */
	protected void finalizeRoot() {
		for (Template template : childTemplates) {
			template.finalizeRoot();
		}
	}

	/**
	 * Gets the root everest object that is wrapped by this structure.
	 *
	 * @return An everest InfrastructureRoot object
	 */
	public T getRoot() {
		return this.root;
	}

	/**
	 * Adds a Template to this template as a child.
	 *
	 * @param child A Template object to add as a child.
	 * @return A value indicating whether or not the Template was added as a
	 * child.
	 */
	protected boolean addChild(Template child) {
		return childTemplates.add(child);
	}

	/**
	 * Adds a child everest object to this template as a template.
	 *
	 * @param child The everest InfrastructureRoot object to wrap in a
	 * Template and add as a child to the template.
	 * @param standard The standard to which template rules should be
	 * applied for the generated child template.
	 * @return The generated wrapping template for the child.
	 */
	protected Template addChild(InfrastructureRoot child, CDAStandard standard) {
		return new Template(child, standard);
	}

	/**
	 * Removes a child template from this template.
	 *
	 * @param child The Template object to remove.
	 * @return A value indicating whether or not the specified Template was
	 * a child, and was removed.
	 */
	protected boolean removeChild(Template child) {
		return childTemplates.remove(child);
	}

	/**
	 * Removes a single Template child object with a specific class
	 * definition.
	 *
	 * @param childClass The class definition of the specific Template to
	 * search and remove from the template.
	 * @return A value indicating whether or not a Template with the
	 * specified class definition was removed.
	 */
	protected boolean removeChild(Class<? extends Template> childClass) {
		return childTemplates.remove(getChild(childClass));
	}

	/**
	 * Removes all instances of a specific Template with a specific class
	 * definition.
	 *
	 * @param childClass The class definition of a set of Template objects
	 * to remove as children.
	 * @return A boolean value indicating whether or not such a child
	 * existed and was removed.
	 */
	@SuppressWarnings("empty-statement")
	protected boolean removeAllChildren(Class<? extends Template> childClass) {
		if (removeChild(childClass)) {
			while (removeChild(childClass));
			return true;
		}
		return false;
	}

	/**
	 * Removes a single Template child object that contains a root class
	 * definition equal to a specified InfrastructureRoot class.
	 *
	 * @param childRootClass The class definition of an everest
	 * InfrastructureRoot object to remove.
	 * @return A value indicating whether or not a Template child object was
	 * removed.
	 */
	protected boolean removeChildRoot(Class<? extends InfrastructureRoot> childRootClass) {
		return childTemplates.remove(getChildRoot(childRootClass));
	}

	/**
	 * Removes all instances of Template child objects that wrap a specific
	 * InfrastructureRoot class.
	 *
	 * @param childRootClass The InfrastructureRoot class that
	 * @return A value indicating whether or not a Template was removed.
	 */
	@SuppressWarnings("empty-statement")
	protected boolean removeAllChildrenRoot(Class<? extends InfrastructureRoot> childRootClass) {
		if (removeChildRoot(childRootClass)) {
			while (removeChildRoot(childRootClass));
			return true;
		}
		return false;
	}

	/**
	 * Gets the children of this template. The collection returned is
	 * unmodifiable.
	 *
	 * @return An Iterable object that allows for iteration through a number
	 * of Template objects.
	 */
	public Iterable<Template> getChildren() {
		return Collections.unmodifiableSet(childTemplates);
	}

	/**
	 * Gets a set of templates that encapsulate a specific everest object.
	 *
	 * @param <R> The type of Template that is expected.
	 * @param rootClass The everest object that the set of templates should
	 * encapsulate.
	 * @return A List of Template objects that wrap the specified
	 * InfrastructureRoot class.
	 */
	public <R extends Template> List<R> getChildrenRoot(Class<? extends InfrastructureRoot> rootClass) {
		ArrayList<Template> childrenResult = new ArrayList<Template>();
		for (Template child : childTemplates) {
			if (child.getRoot().getClass() == rootClass) {
				childrenResult.add(child);
			}
		}
		return (List<R>) childrenResult;
	}

	/**
	 * Gets a set of child Template objects that
	 *
	 * @param <R> The type of Template that is expected.
	 * @param childClass The class definition of the InfrastructureRoot
	 * object that the Template object wraps.
	 * @return A List of child Templates.
	 */
	public <R extends Template> List<R> getChildren(Class<? extends Template> childClass) {
		ArrayList<Template> childrenResult = new ArrayList<Template>();
		for (Template child : childTemplates) {
			if (child.getClass() == childClass) {
				childrenResult.add(child);
			}
		}
		return (List<R>) childrenResult;
	}

	/**
	 * Gets the first instance of a child Template that matches the class
	 * object specified.
	 *
	 * @param child The class of the child Template object to get.
	 * @return A child Template object. null if a child Template was not
	 * found.
	 */
	public Template getChild(Class<? extends Template> child) {
		for (Template childTemplate : childTemplates) {
			if (childTemplate.getClass() == child) {
				return childTemplate;
			}
		}
		return null;
	}

	/**
	 * Gets a child Template object that wraps a specific InfrastructureRoot
	 * class.
	 *
	 * @param rootChild The InfrastructureRoot class that a child Template
	 * wraps.
	 * @return The resulting child Template. null if the Template was not
	 * found.
	 */
	public Template getChildRoot(Class<? extends InfrastructureRoot> rootChild) {
		for (Template childTemplate : childTemplates) {
			if (childTemplate.getRoot().getClass() == rootChild) {
				return childTemplate;
			}
		}
		return null;
	}

	/**
	 * Gets the standard that this template implements.
	 *
	 * @return A CDAStandard set upon the template.
	 */
	public CDAStandard getStandard() {
		return templateStandard;
	}

	/**
	 * Gets a list of validation result details.
	 *
	 * @return
	 */
	public List<ResultDetail> getResultDetails() {
		return this.resultDetails;
	}

	/**
	 * Adds a validation result to the template.
	 *
	 * @param result
	 */
	public void addResultDetail(ResultDetail result) {
		resultDetails.add(result);
	}

	/**
	 * Determine if the implementation of the template validates against the
	 * set standard.
	 *
	 * @return A boolean indicating whether or not the implementation for
	 * the standard failed validating due to errors. Will not be true if
	 * only warnings/informational results are generated.
	 */
	public boolean validateStandard() {
		resultDetails.clear();
		for (Template child : childTemplates) {
			child.validateStandard();
		}
		for (II id : getRoot().getTemplateId()) {
			AbstractTemplateRule rule = TemplateRuleFactory.getRule(id.getRoot());
			if (rule != null) {
				rule.apply(this);
			}
		}
		return resultDetails.isEmpty();
	}

	/**
	 * validateStandard() does the same thing. No need for the parameter.
	 *
	 * @param createRequiredElementst Does nothing.
	 * @return
	 */
	@Deprecated
	public boolean validateStandard(boolean createRequiredElementst) {
		return validateStandard();
	}
}
