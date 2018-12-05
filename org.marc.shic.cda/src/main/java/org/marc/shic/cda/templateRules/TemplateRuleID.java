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
 * @author brownp Date: Aug 19, 2013
 *
 */
package org.marc.shic.cda.templateRules;

import org.marc.everest.datatypes.II;

public enum TemplateRuleID {

    ///////////////////////////////////////////////////////////////////////////
    // SECTION TEMPLATE IDS ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Medications Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.19
     */
    PCC_MedicationsSection("1.3.6.1.4.1.19376.1.5.3.1.3.19", "Medications Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_19.class),
    /**
     * Vital Signs.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.25
     */
    PCC_VitalSignsSection("1.3.6.1.4.1.19376.1.5.3.1.3.25", "Vital Signs Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_25.class),
    /**
     * CCD Vital Signs.<br/>
     * 2.16.840.1.113883.10.20.1.16
     */
    CCD_VitalSignsSection("2.16.840.1.113883.10.20.1.16", "Vital Signs Section", Template_2_16_840_1_113883_10_20_1_16.class),
    /**
     * CCD Immunizations.<br/>
     * 2.16.840.1.113883.10.20.1.6
     */
    CCD_ImmunizationsSection("2.16.840.1.113883.10.20.1.6", "Immunizations Section", Template_2_16_840_1_113883_10_20_1_6.class),
    /**
     * CCD Family History.<br/>
     * 12.16.840.1.113883.10.20.1.4
     */
    CCD_FamilyHistorySection("2.16.840.1.113883.10.20.1.4", "CCD Family History", Template_2_16_840_1_113883_10_20_1_4.class),
    /**
     * Plan of Care.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.31
     */
    PCC_CarePlanSection("1.3.6.1.4.1.19376.1.5.3.1.3.31", "Plan of Care Section"),
    /**
     * Surgeries.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.12
     */
    PCC_SurgeriesSection("1.3.6.1.4.1.19376.1.5.3.1.3.12", "Surgeries Section"),
    /**
     * Pregnancy History Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4
     */
    PCC_PregnancyHistorySection("1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4", "Pregnancy History Section"),
    /**
     * Advance Directives.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.34
     */
    PCC_AdvanceDirectivesSection("1.3.6.1.4.1.19376.1.5.3.1.3.34", "Advance Directives Section"),
    /**
     * Allergies and Drug Sensitivies.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.13
     */
    PCC_AllergiesReactionsSection("1.3.6.1.4.1.19376.1.5.3.1.3.13", "Allergies and Drug Sensitivies Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_13.class),
    /**
     * Coded Results Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.28
     */
    PCC_CodedResultsSection("1.3.6.1.4.1.19376.1.5.3.1.3.28", "Coded Results Section"),
    /**
     * History of Past Illness Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.8
     */
    PCC_HistoryOfPastIllnessSection("1.3.6.1.4.1.19376.1.5.3.1.3.8", "History of Past Illness Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_8.class),
    /**
     * Foreign Travel.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5.3.6
     */
    ForeignTravelSection("1.3.6.1.4.1.19376.1.5.3.1.1.5.3.6", "Foreign Travel"),
    /**
     * Encounters Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5.3.3
     */
    PCC_EncountersSection("1.3.6.1.4.1.19376.1.5.3.1.1.5.3.3", "Encounters Section"),
    /**
     * Hazardous Working Conditions.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5.3.1
     */
    HazardousWorkingConditionsSection("1.3.6.1.4.1.19376.1.5.3.1.1.5.3.1", "Hazardous Working Conditions Section"),
    /**
     * Medical Devices Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5.3.5
     */
    PCC_MedicalDevicesSection("1.3.6.1.4.1.19376.1.5.3.1.1.5.3.5", "Medical Devices Section"),
    /**
     * Family Medical History.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.14
     */
    PCC_FamilyMedicalHistorySection("1.3.6.1.4.1.19376.1.5.3.1.3.14", "Family Medical History"),
    /**
     * Social History Section <br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.16
     */
    PCC_SocialHistorySection("1.3.6.1.4.1.19376.1.5.3.1.3.16", "Social History Section"),
    /**
     * Immunizations Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.23
     */
    PCC_ImmunizationsSection("1.3.6.1.4.1.19376.1.5.3.1.3.23", "Immunizations Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_23.class),
    /**
     * Active Problems Section.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.3.6
     */
    PCC_ActiveProblemsSection("1.3.6.1.4.1.19376.1.5.3.1.3.6", "Active Problems Section", Template_1_3_6_1_4_1_19376_1_5_3_1_3_6.class),
    /**
     * Problems Section.<br/>
     * 2.16.840.1.113883.10.20.1.11
     */
    CCD_ProblemsSection("2.16.840.1.113883.10.20.1.11", "Problems Section", Template_2_16_840_1_113883_10_20_1_11.class),
    /**
     * Purpose Section.<br/>
     * 2.16.840.1.113883.10.20.1.13
     */
    CCD_PurposeSection("2.16.840.1.113883.10.20.1.13", "Purpose Section", Template_2_16_840_1_113883_10_20_1_13.class),
    /**
     * Social History Section.<br/>
     * 2.16.840.1.113883.10.20.1.15
     */
    CCD_SocialHistorySection("2.16.840.1.113883.10.20.1.15", "Social History Section"),
    /**
     * Results Section.<br/>
     * 2.16.840.1.113883.10.20.1.15
     */
    CCD_ResultsSection("2.16.840.1.113883.10.20.1.14", "Results Section", Template_2_16_840_1_113883_10_20_1_14.class),
    ///////////////////////////////////////////////////////////////////////////
    // SPECIFICATION TEMPLATE IDS /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Medical Documents Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.1
     */
    PCC_MedicalDocumentsSpecification("1.3.6.1.4.1.19376.1.5.3.1.1.1", "Medical Document Specification", Template_1_3_6_1_4_1_19376_1_5_3_1_1_1.class),
    /**
     * Medical Summary Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.2
     */
    PCC_MedicalSummarySpecification("1.3.6.1.4.1.19376.1.5.3.1.1.2", "Medical Summary Specification", Template_1_3_6_1_4_1_19376_1_5_3_1_1_2.class),
    /**
     * Referral Sumary Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.3
     */
    PCC_ReferralSummarySpecification("1.3.6.1.4.1.19376.1.5.3.1.1.3", "Referral Sumary Specification"),
    /**
     * Discharge Sumamry Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.4
     */
    PCC_DischargeSummarySpecification("1.3.6.1.4.1.19376.1.5.3.1.1.4", "Discharge Sumamry Specification"),
    /**
     * PHR Extract Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.5
     */
    PCC_PhrExtractSpecification("1.3.6.1.4.1.19376.1.5.3.1.1.5", "PHR Extract Specification", Template_1_3_6_1_4_1_19376_1_5_3_1_1_5.class),
    /**
     * PHR Update Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.6
     */
    PCC_PhrUpdateSpecification("1.3.6.1.4.1.19376.1.5.3.1.1.6", "PHR Update Specification"),
    /**
     * Emergency Department Referral Specification.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.10
     */
    PCC_EmergencyDepartmentReferralSpecification("1.3.6.1.4.1.19376.1.5.3.1.1.10", "Emergency Department Referral Specification"),
    //!!!!!!!!!! EVERYTHING ELSE !!!!!!!!!!
    /**
     * Healthcare Providers and Pharmacies.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.2.3
     */
    PCC_HealthcareProvidersAndPharmacies("1.3.6.1.4.1.19376.1.5.3.1.2.3", "Healthcare Providers and Pharmacies", Template_1_3_6_1_4_1_19376_1_5_3_1_2_3.class),
    /**
     * Medication Activity.<br/>
     * 2.16.840.1.113883.10.20.1.24
     */
    CCD_MedicationActivity("2.16.840.1.113883.10.20.1.24", "Medication Activity", Template_2_16_840_1_113883_10_20_1_24.class),
    //Section Level

    /**
     * Medications.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.7
     */
    PCC_Medication("1.3.6.1.4.1.19376.1.5.3.1.4.7", "Medications", Template_1_3_6_1_4_1_19376_1_5_3_1_4_7.class),
    /**
     * Normal Dosing.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.7.1
     */
    PCC_NormalDosing("1.3.6.1.4.1.19376.1.5.3.1.4.7.1", "Normal Dosing", Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_1.class),
    /**
     * Tapered Doses.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.8
     */
    PCC_TaperedDoses("1.3.6.1.4.1.19376.1.5.3.1.4.8", "Tapered Doses", Template_1_3_6_1_4_1_19376_1_5_3_1_4_8.class),
    /**
     * Split Dosing.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.9
     */
    PCC_SplitDosing("1.3.6.1.4.1.19376.1.5.3.1.4.9", "Split Dosing", Template_1_3_6_1_4_1_19376_1_5_3_1_4_9.class),
    /**
     * Conditional Dosing.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.10
     */
    PCC_ConditionalDosing("1.3.6.1.4.1.19376.1.5.3.1.4.10", "Conditional Dosing", Template_1_3_6_1_4_1_19376_1_5_3_1_4_10.class),
    /**
     * Combination Dosing.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.11
     */
    PCC_CombinationDosing("1.3.6.1.4.1.19376.1.5.3.1.4.11", "Combination Dosing", Template_1_3_6_1_4_1_19376_1_5_3_1_4_11.class),
    /**
     * BPPC With No Scanned Part.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.7
     */
    PCC_BppcWithNoScannedPart("1.3.6.1.4.1.19376.1.5.3.1.1.7", "BPPC With No Scanned Part", Template_1_3_6_1_4_1_19376_1_5_3_1_1_7.class),
    /**
     * BPPC WIth Scanned Part.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.1.7.1
     */
    PCC_BppcWithScannedPart("1.3.6.1.4.1.19376.1.5.3.1.1.7.1", "BPPC WIth Scanned Part", Template_1_3_6_1_4_1_19376_1_5_3_1_1_7_1.class),
    /**
     * Consent Service Event.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.2.6
     */
    PCC_ConsentServiceEvent("1.3.6.1.4.1.19376.1.5.3.1.2.6", "Consent Service Event", Template_1_3_6_1_4_1_19376_1_5_3_1_2_6.class),
    /**
     * BPPC Authorization.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.2.5
     */
    PCC_BppcAuthorization("1.3.6.1.4.1.19376.1.5.3.1.2.5", "BPPC Authorization"),
    /**
     * XDS-SD.<br/>
     * 1.3.6.1.4.1.19376.1.2.20
     */
    PCC_XDS_SD("1.3.6.1.4.1.19376.1.2.20", "XDS-SD"),
    /**
     * Original Author.<br/>
     * 1.3.6.1.4.1.19376.1.2.20.1"
     */
    PCC_OriginalAuthor("1.3.6.1.4.1.19376.1.2.20.1", "Original Author", Template_1_3_6_1_4_1_19376_1_2_20_1.class),
    /**
     * Scanning Device.<br/>
     * 1.3.6.1.4.1.19376.1.2.20.2
     */
    PCC_ScanningDevice("1.3.6.1.4.1.19376.1.2.20.2", "Scanning Device", Template_1_3_6_1_4_1_19376_1_2_20_2.class),
    /**
     * Scanner Operator.<br/>
     * 1.3.6.1.4.1.19376.1.2.20.3
     */
    PCC_ScannerOperator("1.3.6.1.4.1.19376.1.2.20.3", "Scanner Operator", Template_1_3_6_1_4_1_19376_1_2_20_3.class),
    /**
     * CDT General Header Constraint.<br/>
     * 2.16.840.1.113883.10.20.3
     */
    CDT_GeneralCDAR2HeaderConstraint("2.16.840.1.113883.10.20.3", "General Header Constraints", Template_2_16_840_1_113883_10_20_3.class),
    /**
     * CDA General Header Constraints.<br/>
     * 2.16.840.1.113883.10.20.22.1.1
     */
    CDA_GeneralHeaderConstraints("2.16.840.1.113883.10.20.22.1.1", "General Header Constraints"),
    // Entry Level

    /**
     * CCD.<br/>
     * 2.16.840.1.113883.10.20.1
     */
    CCD("2.16.840.1.113883.10.20.1", "CCD", Template_2_16_840_1_113883_10_20_1.class),
    /**
     * Alerts Section.<br/>
     * 2.16.840.1.113883.10.20.1.2
     */
    CCD_AlertsSection("2.16.840.1.113883.10.20.1.2", "Alerts Section", Template_2_16_840_1_113883_10_20_1_2.class),
    /**
     * Medications Section.<br/>
     * 2.16.840.1.113883.10.20.1.8
     */
    CCD_MedicationsSection("2.16.840.1.113883.10.20.1.8", "Medications Section", Template_2_16_840_1_113883_10_20_1_8.class),
    /**
     * Product Entry.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.7.2
     */
    PCC_ProductEntry("1.3.6.1.4.1.19376.1.5.3.1.4.7.2", "Product Entry", Template_1_3_6_1_4_1_19376_1_5_3_1_4_7_2.class),
    /**
     * Product.<br/>
     * 2.16.840.1.113883.10.20.1.53
     */
    CCD_Product("2.16.840.1.113883.10.20.1.53", "Product", Template_2_16_840_1_113883_10_20_1_27.class),
    // Allergies and Intolerances Act

    /**
     * Problem Act.<br/>
     * 2.16.840.1.113883.10.20.1.27
     */
    CCD_ProblemAct("2.16.840.1.113883.10.20.1.27", "Problem Act", Template_2_16_840_1_113883_10_20_1_27.class),
    /**
     * Concern Entry.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.5.1
     */
    PCC_ConcernEntry("1.3.6.1.4.1.19376.1.5.3.1.4.5.1", "Concern Entry", Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_1.class),
    /**
     * Problem Concern Entry.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.5.2
     */
    PCC_ProblemConcernEntry("1.3.6.1.4.1.19376.1.5.3.1.4.5.2", "Problem Concern Entry", Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_2.class),
    /**
     * Allergy Intolerance Concern.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.5.3
     */
    PCC_AllergyIntoleranceConcern("1.3.6.1.4.1.19376.1.5.3.1.4.5.3", "Allergy Intolerance Concern", Template_1_3_6_1_4_1_19376_1_5_3_1_4_5_3.class),
    // Allergies and Intolerances entryRelationship/Observation

    /**
     * Problem Entry.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.5
     */
    PCC_ProblemEntry("1.3.6.1.4.1.19376.1.5.3.1.4.5", "Problem Entry", Template_1_3_6_1_4_1_19376_1_5_3_1_4_5.class),
    /**
     * Problem Observation.<br/>
     * 2.16.840.1.113883.10.20.1.28
     */
    CCD_ProblemObservation("2.16.840.1.113883.10.20.1.28", "Problem Observation", Template_2_16_840_1_113883_10_20_1_28.class),
    /**
     * Allergy Intolerance.<br/>
     * 1.3.6.1.4.1.19376.1.5.3.1.4.6
     */
    PCC_AllergyIntolerance("1.3.6.1.4.1.19376.1.5.3.1.4.6", "Allergy Intolerance", Template_1_3_6_1_4_1_19376_1_5_3_1_4_6.class),
    /**
     * Alert Observation.<br/>
     * 2.16.840.1.113883.10.20.1.18
     */
    CCD_AlertObservation("2.16.840.1.113883.10.20.1.18", "Alert Observation", Template_2_16_840_1_113883_10_20_1_18.class),
    /**
     * Vital Signs Organizer.<br/>
     * 2.16.840.1.113883.10.20.1.35
     */
    CCD_VitalSignsOrganizer("2.16.840.1.113883.10.20.1.35", "Vital Signs Organizer", Template_2_16_840_1_113883_10_20_1_35.class),
    /**
     * Results Organizer.<br/>
     * 2.16.840.1.113883.10.20.1.32
     */
    CCD_ResultsOrganizer("2.16.840.1.113883.10.20.1.32", "Results Organizer", Template_2_16_840_1_113883_10_20_1_32.class),
    /**
     * Result Observation.<br/>
     * 2.16.840.1.113883.10.20.1.31
     */
    CCD_ResultObservation("2.16.840.1.113883.10.20.1.31", "Result Observation", Template_2_16_840_1_113883_10_20_1_31.class),
    // Immunizations Section

    /**
     * RxNorm.<br/>
     * 2.16.840.1.113883.6.88
     */
    RxNorm("2.16.840.1.113883.6.88", "RxNorm");

    private String oid;
    private String templateName;
    private II instanceIdentifier;
    private Class ruleClass;

    /**
     * Builds the Enum object with an empty template rule.
     *
     * @param _OID
     */
    private TemplateRuleID(String _OID, String _templateName) {
        this(_OID, _templateName, AbstractTemplateRule.class);
    }

    private TemplateRuleID(String _OID, String _templateName, Class rule) {
        this.oid = _OID;
        this.templateName = _templateName;
        this.ruleClass = rule;
        instanceIdentifier = new II(oid);
    }

    /**
     * Returns the Template Name as a String
     *
     * @return
     */
    public String getTemplateName() {
        return this.templateName;
    }

    /**
     * Returns the OID as a string
     *
     * @return
     */
    public String getOid() {
        return this.oid;
    }

    /**
     * Gets the instance identifier from the OID of this TemplateID
     *
     * @return
     */
    public II getII() {
        return instanceIdentifier;
    }

    @Deprecated
    public TemplateRules getTemplateRules() {
        return null;
    }

    /**
     * Gets the rule object for this enum.
     *
     * @return
     */
    public AbstractTemplateRule getRule() {
        return TemplateRuleFactory.getRule(this);
    }

    /**
     * Gets the template rule class object.
     *
     * @return
     */
    public Class getRuleClass() {
        return this.ruleClass;
    }

    /**
     * Return the string value of the OID
     *
     * @return
     */
    public String toString() {
        return String.format("[%s:%s]", this.templateName, this.oid);
    }
}
