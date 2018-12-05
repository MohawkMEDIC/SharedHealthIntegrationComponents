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
 * Date: January 21, 2014
 *
 */
package org.marc.shic.cda.templateRules;

import java.util.EnumSet;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * A utility class created for the sole purpose of fetching template rule
 * objects.
 *
 * @author Ryan Albert
 */
public final class TemplateRuleFactory {

    private TemplateRuleFactory() {
    }

    public static final String RULE_CCD = "2.16.840.1.113883.10";
    public static final String RULE_PCC = "1.3.6.1.4.1.19376";
    private static final HashMap<TemplateRuleID, AbstractTemplateRule> ruleMap = new HashMap<TemplateRuleID, AbstractTemplateRule>();
    private static final HashMap<String, TemplateRuleID> idMap = new HashMap<String, TemplateRuleID>();
    private static final HashMap<Class, TemplateRuleID> idClassMap = new HashMap<Class, TemplateRuleID>();
    private static final EnumSet<TemplateRuleID> CCD_rules = EnumSet.noneOf(TemplateRuleID.class);
    private static final EnumSet<TemplateRuleID> PCC_rules = EnumSet.noneOf(TemplateRuleID.class);
    private static final Logger LOGGER = Logger.getLogger(TemplateRuleFactory.class);

    static {
        for (TemplateRuleID id : TemplateRuleID.values()) {
            try {
                AbstractTemplateRule templateRule = (AbstractTemplateRule) id.getRuleClass().newInstance();
                templateRule.setTemplateID(id);

                ruleMap.put(id, templateRule);
                idMap.put(id.getOid(), id);
                idClassMap.put(id.getRule().getClass(), id);
                if (id.getOid().startsWith(RULE_CCD)) {
                    CCD_rules.add(id);
                } else if (id.getOid().startsWith(RULE_PCC)) {
                    PCC_rules.add(id);
                }
            } catch (InstantiationException ex) {
                LOGGER.error(String.format("Unable to instantiate template rule %s", id), ex);
            } catch (IllegalAccessException ex) {
                LOGGER.error(String.format("Illegal access to template rule %s", id), ex);
            }
        }
    }

    /**
     * Gets a template rule object from a string OID value.
     *
     * @param ruleID
     * @return
     */
    public static AbstractTemplateRule getRule(String ruleID) {
        return ruleMap.get(idMap.get(ruleID));
    }

    /**
     * Gets a template rule object from a class object.
     *
     * @param templateRuleClass
     * @return
     */
    public static AbstractTemplateRule getRule(Class templateRuleClass) {
        return ruleMap.get(idClassMap.get(templateRuleClass));
    }

    /**
     * Gets a template rule object from a template rule ID.
     *
     * @param templateRuleID
     * @return
     */
    public static AbstractTemplateRule getRule(TemplateRuleID templateRuleID) {
        return ruleMap.get(templateRuleID);
    }

    /**
     * Gets a template rule ID from a template rule OID.
     *
     * @param ruleID
     * @return
     */
    public static TemplateRuleID getRuleID(String ruleID) {
        return idMap.get(ruleID);
    }

    /**
     * Gets a template rule ID from a template rule class.
     *
     * @param templateRuleClass
     * @return
     */
    public static TemplateRuleID getRuleID(Class templateRuleClass) {
        return idClassMap.get(templateRuleClass);
    }

    /**
     * Checks whether or not the given rule is part of the PCC (Patient care
     * coordination) implementation.
     *
     * @param templateRuleID
     * @return true if the rule is a PCC rule, false otherwise.
     */
    public static boolean isRulePCC(TemplateRuleID templateRuleID) {
        return PCC_rules.contains(templateRuleID);
    }

    /**
     * Checks whether or not the given rule is part of the CCD (Continuity of
     * Care Document) implementation.
     *
     * @param templateRuleID
     * @return true if the rule is a CCD rule, false otherwise.
     */
    public static boolean isRuleCCD(TemplateRuleID templateRuleID) {
        return CCD_rules.contains(templateRuleID);
    }
}
