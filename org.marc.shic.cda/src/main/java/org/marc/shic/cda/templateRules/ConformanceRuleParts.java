/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
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
 * Date: October 29, 2013
 * 
 */
package org.marc.shic.cda.templateRules;

import org.marc.shic.cda.utils.LOINC_Codes;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import java.util.ArrayList;

/**
 *
 * @author Paul
 */
public enum ConformanceRuleParts {
    
    PhrExtractSummary("/hl7:ClinicalDocument/hl7:templateId[@root]",
            TemplateRuleID.PCC_PhrExtractSpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    PhrExtractCode("/hl7:ClinicalDocument/hl7:code[@code]",
            LOINC_Codes.PhrDocumentCode.getCode(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    PhrExtractCodeSystem("/hl7:ClinicalDocument/hl7:code[@codeSystem]",
            LOINC_Codes.PhrDocumentCode.getCodeSystem(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    IHE_ProblemEntryClassCode("//hl7:structuredBody/hl7:component/hl7:section/hl7:entry/hl7:act/hl7:entryRelationship/hl7:observation[@classCode]",
            "OBS",
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    IHE_ProblemEntryMoodCode("//hl7:structuredBody/hl7:component/hl7:section/hl7:entry/hl7:act/hl7:entryRelationship/hl7:observation[@moodCode]",
            "EVN",
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    HealthcareProvidersAndPharmacy("//hl7:ClinicalDocument/hl7:documentationOf/hl7:serviceEvent/hl7:performer/hl7:templateId[@root]",
            TemplateRuleID.PCC_HealthcareProvidersAndPharmacies.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    MedicalSummary("/hl7:ClinicalDocument/hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicalSummarySpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    Medications("//hl7:templateId[@root]",
            TemplateRuleID.PCC_Medication.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    NormalDosing("//hl7:templateId[@root]",
            TemplateRuleID.PCC_NormalDosing.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    TaperedDoses("//hl7:templateId[@root]",
            TemplateRuleID.PCC_TaperedDoses.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    SplitDosing("//hl7:templateId[@root]",
            TemplateRuleID.PCC_SplitDosing.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    ConditionalDosing("//hl7:templateId[@root]",
            TemplateRuleID.PCC_ConditionalDosing.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    CombinationMedications("//hl7:templateId[@root]",
            TemplateRuleID.PCC_CombinationDosing.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    MedicationActivity("//hl7:templateId[@root]",
            TemplateRuleID.CCD_MedicationActivity.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    typeId("/hl7:ClinicalDocument/hl7:typeId[@root]",
            TemplateRuleID.CDT_GeneralCDAR2HeaderConstraint.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    CCD_3_5_TemplateId("//hl7:section/hl7:templateId[@root]",
            TemplateRuleID.CCD_ProblemsSection.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    CCD_3_9_TemplateId("//hl7:section/hl7:templateId[@root]",
            TemplateRuleID.CCD_MedicationsSection.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    ProblemConcernEntryTemplateId("//hl7:templateId[@root]",
            TemplateRuleID.PCC_ProblemConcernEntry.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    ActiveProblemTemplateId("//hl7:templateId[@root]",
            TemplateRuleID.PCC_ActiveProblemsSection.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    HistoryOfPastIllnessTemplateId("//hl7:templateId[@root]",
            TemplateRuleID.PCC_HistoryOfPastIllnessSection.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    AlertObservation("//hl7:templateId[@root]",
            TemplateRuleID.CCD_AlertObservation.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Should),
    MedicationSectionTemplateId("//hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicationsSection.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    HistoryOfPastIllnessProblemCode("//hl7:code[@code]",
            LOINC_Codes.HistoryOfPastIllness.getCode(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    HistoryOfPastIllnessProblemCodeSystem("//hl7:code[@codeSystem]",
            LOINC_Codes.HistoryOfPastIllness.getCodeSystem(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    ActiveProblemCode("//hl7:code[@code]",
            LOINC_Codes.Problems.getCode(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    ActiveProblemCodeSystem("//hl7:code[@codeSystem]",
            LOINC_Codes.Problems.getCodeSystem(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    // A document conforming to the general header constraints in this DSTU SHALL indicate so by including the following
    // template id in the header of the document or by including a template id in the header for a template that requires
    // use of the general header constraint template: 
    // <templateId root=“2.16.840.1.113883.10.20.3”/> 
    CONF_HP_1("/hl7:ClinicalDocument/hl7:templateId[@root]",
            "2.16.840.1.113883.10.20.3",
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    // The root of a History and Physical SHALL be a ClinicalDocument element from the urn:hl7-org:v3 namespace. 
    // TODO Figure out if there is a better way to test that the namespace is set ... although this does work
    CONF_HP_2("/*[local-name()='ClinicalDocument' and namespace-uri()= 'urn:hl7-org:v3']/hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicalDocumentsSpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    // To indicate conformance to Level 1 (which also asserts compliance with all general or non-level-specific constraints),
    // ClinicalDocument/templateId elements MAY be present with the value shown below: 
    // <templateId root='2.16.840.1.113883.10.20.10'/> <!-- conforms to Level 1 guidance -->
    CONF_HP_3("/hl7:ClinicalDocument/hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicalDocumentsSpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.May),
    // To indicate conformance to Level 2 features (which also asserts compliance with Level 1 requirements
    // and asserts the presence of section codes), ClinicalDocument/templateId elements MAY be present with the value shown below: 
    // <templateId root='2.16.840.1.113883.10.20.20'/> <!-- conforms to Level 2 guidance -->
    CONF_HP_4("/hl7:ClinicalDocument/hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicalDocumentsSpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.May),
    // To indicate conformance to Level 3 features (which also asserts compliance with Level 2 requirements and the use of CDA entries
    // in some sections), ClinicalDocument/templateId elements MAY be present with the value shown below:
    // <templateId root='2.16.840.1.113883.10.20.30'/> <!-- conforms to Level 3 guidance -->
    CONF_HP_5("/hl7:ClinicalDocument/hl7:templateId[@root]",
            TemplateRuleID.PCC_MedicalDocumentsSpecification.getOid(),
            RuleType.AttributeValidation,
            RuleConformance.May),
    // All patient, guardianPerson, assignedPerson, maintainingPerson, relatedPerson, intendedRecipient/informationRecipient,
    // associatedPerson, and relatedSubject/subject elements SHALL have a name.
    CONF_HP_6("//hl7:patient | //hl7:guardianPerson | //hl7:assignedPerson | //hl7:maintainingPerson | //hl7:relatedPerson | //hl7:intendedRecipient/hl7:informationRecipient | //hl7:associatedPerson | //hl7:relatedSubject/hl7:subject",
            "hl7:name",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // All patientRole, assignedAuthor, assignedEntity[not(parent::dataEnterer)] and associatedEntity elements SHALL have an addr and telecom element.
    CONF_HP_7_1("//hl7:patientRole | //hl7:assignedAuthor | //hl7:assignedEntity[not(parent::dataEnterer)] | //hl7:associatedEntity",
            "hl7:addr",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_7_2("//hl7:patientRole | //hl7:assignedAuthor | //hl7:assignedEntity[not(parent::dataEnterer)] | //hl7:associatedEntity",
            "hl7:telecom",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // All guardian, dataEnterer/assignedEntity, relatedEntity,
    // intendedRecipient, relatedSubject and participantRole elements SHOULD have an addr and telecom element.
    CONF_HP_8_1("//hl7:guardian | //hl7:dataEnterer/assignedEntity | //hl7:relatedEntity | //hl7:intendedRecipient | //hl7:relatedSubject | //hl7:participantRole",
            "hl7:addr ",
            RuleType.NodeExists,
            RuleConformance.Should),
    CONF_HP_8_2("//hl7:guardian | //hl7:dataEnterer/assignedEntity | //hl7:relatedEntity | //hl7:intendedRecipient | //hl7:relatedSubject | //hl7:participantRole",
            "hl7:telecom",
            RuleType.NodeExists,
            RuleConformance.Should),
    // CONF-HP-9: All guardianOrganization, providerOrganization, wholeOrganization, representedOrganization, representedCustodianOrganization,
    // receivedOrganization, scopingOrganization and serviceProviderOrganization elements SHALL have
    // name, addr and telecom elements
    CONF_HP_9_1("//hl7:guardianOrganization | //hl7:providerOrganization | //hl7:wholeOrganization | //hl7:representedOrganization | //hl7:representedCustodianOrganization | //hl7:receivedOrganization | //hl7:scopingOrganization | //hl7:serviceProviderOrganization",
            "hl7:name",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_9_2("//hl7:guardianOrganization | //hl7:providerOrganization | //hl7:wholeOrganization | //hl7:representedOrganization | //hl7:representedCustodianOrganization | //hl7:receivedOrganization | //hl7:scopingOrganization | //hl7:serviceProviderOrganization",
            "hl7:addr",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_9_3("//hl7:guardianOrganization | //hl7:providerOrganization | //hl7:wholeOrganization | //hl7:representedOrganization | //hl7:representedCustodianOrganization | //hl7:receivedOrganization | //hl7:scopingOrganization | //hl7:serviceProviderOrganization",
            "hl7:telecom",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The extension attribute of the typeId element SHALL be POCD_HD000040. 
    CONF_HP_16("/hl7:ClinicalDocument/hl7:typeId[@extension]",
            "POCD_HD000040",
            RuleType.AttributeValidation,
            RuleConformance.Shall),
    // The ClinicalDocument/id element SHALL be present.
    // The ClinicalDocument/id/@root attribute SHALL be a syntactically correct UUID or OID. 
    CONF_HP_17("/hl7:ClinicalDocument",
            "hl7:id",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The ClinicalDocument/code element SHALL be present and specifies the type of the clinical document.
    CONF_HP_21("/hl7:ClinicalDocument",
            "hl7:code",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The title element SHALL be present and specifies the local name used for the document. 
    CONF_HP_22("/hl7:ClinicalDocument",
            "hl7:title",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The ClinicalDocument/effectiveTime element SHALL be present and specifies the creation time
    // of the document All History and Physical documents authored by direct input to a computer system
    // should record an effectiveTime that is precise to the second.  When authored in other ways, for example,
    // by filling out a paper form that is then transferred into an EHR system, the precision of effectiveTime may be less than to the second. 
    CONF_HP_23("/hl7:ClinicalDocument",
            "hl7:effectiveTime",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // CDA R2 requires that the ClinicalDocument/confidentialityCode be present. It specifies the confidentiality assigned to the document.
    // This DSTU provides no further guidance on documents with respect to the vocabulary used for confidentialityCode, nor treatment or
    // implementation of confidentiality. A CDA R2-conforming example is shown below: 
    // <confidentialityCode code='N' codeSystem='2.16.840.1.113883.5.25'/>
    ConfidentialityCode("/hl7:ClinicalDocument",
            "hl7:confidentialityCode",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The ClinicalDocument/languageCode specifies the language of the History and Physical.
    // History and Physicals must be readable by medical practitioners, caregivers, and patients. 
    CONF_HP_24("/hl7:ClinicalDocument",
            "hl7:languageCode",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The ClinicalDocument/copyTime element has been deprecated in CDA R2. 
    // CONF-HP-30: A ClinicalDocument/copyTime element SHALL NOT be present.
    CONF_HP_30("/hl7:ClinicalDocument",
            "hl7:copyTime",
            RuleType.NodeExists,
            RuleConformance.ShallNot),
    // At least one recordTarget/patientRole element SHALL be present.
    CONF_HP_31("/hl7:recordTarget",
            "hl7:patientRole ",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // A patient/birthTime element SHALL be present. The patient/birthTime element SHALL be precise at least to the year,
    // and SHOULD be precise at least to the day, and MAY omit time zone. If unknown, it SHALL be represented using a flavor of null. 
    CONF_HP_32("/hl7:recordTarget/hl7:patientRole/hl7:patient",
            "hl7:birthTime",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // A patient/administrativeGenderCode element SHALL be present. If unknown, it SHALL be represented using a flavor of null.
    // Values for administrativeGenderCode SHOULD be drawn from the HL7 AdministrativeGender vocabulary
    CONF_HP_33("/hl7:recordTarget/hl7:patientRole/hl7:patient",
            "hl7:administrativeGenderCode ",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The author/time element represents the start time of the author’s participation in the creation of the clinical document.
    // The author/time element SHALL be present.  
    CONF_HP_37("/hl7:ClinicalDocument/hl7:author",
            "hl7:time",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The assignedAuthor/id element SHALL be present.
    CONF_HP_38("/hl7:ClinicalDocument/hl7:author/hl7:assignedAuthor",
            "hl7:id",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // An assignedAuthor element SHALL contain at least one assignedPerson or assignedAuthoringDevice elements.
    CONF_HP_39_1("/hl7:ClinicalDocument/hl7:author/hl7:assignedAuthor",
            "hl7:assignedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_39_2("/hl7:ClinicalDocument/hl7:author/hl7:assignedAuthor",
            "hl7:assignedAuthoringDevice",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // When dataEnterer is present, an assignedEntity/assignedPerson element SHALL be present.
    CONF_HP_40("/hl7:ClinicalDocument/hl7:dataEnterer/hl7:assignedEntity",
            "hl7:assignedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    //  The time element MAY be present. If present, it represents the starting time of entry of the data.  
    CONF_HP_41("/hl7:ClinicalDocument/hl7:dataEnterer",
            "hl7:assignedEntity",
            RuleType.NodeExists,
            RuleConformance.May),
    // The informant element MAY be present. 
    CONF_HP_42("/hl7:ClinicalDocument",
            "hl7:informant",
            RuleType.NodeExists,
            RuleConformance.May),
    // When informant is present, an assignedEntity/assignedPerson or relatedEntity/relatedPerson element SHALL be present. 
    CONF_HP_43_1("/hl7:ClinicalDocument/hl7:informant/hl7:assignedEntity",
            "hl7:assignedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_43_2("/hl7:ClinicalDocument/hl7:informant/hl7:relatedEntity",
            "hl7:relatedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The ClinicalDocument/informationRecipient element MAY be present5.
    // When informationRecipient is used, at least one informationRecipient/intendedRecipient/informationRecipient or 
    // informationRecipient/intendedRecipient/receivedOrganization SHALL be present
    CONF_HP_50_1("/hl7:ClinicalDocument",
            "hl7:informationRecipient",
            RuleType.NodeExists,
            RuleConformance.May),
    CONF_HP_50_2("/hl7:ClinicalDocument/hl7:informationRecipient/hl7:intendedRecipient",
            "hl7:informationRecipient",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_50_3("/hl7:ClinicalDocument/hl7:informationRecipient/hl7:intendedRecipient",
            "hl7:receivedOrganization",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The assignedEntity/assignedPerson element SHALL be present in legalAuthenticator
    CONF_HP_51("/hl7:ClinicalDocument/hl7:legalAuthenticator/hl7:assignedEntity",
            "hl7:assignedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // An authenticator MAY be present . The assignedEntity/assignedPerson element SHALL be present in an authenticator element. 
    CONF_HP_52("/hl7:ClinicalDocument/hl7:authenticator/hl7:assignedEntity",
            "hl7:assignedPerson",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // The participant element MAY be present . If present, the participant/associatedEntity element
    // SHALL have an associatedPerson or scopingOrganization element
    CONF_HP_58_1("/hl7:ClinicalDocument",
            "hl7:participant",
            RuleType.NodeExists,
            RuleConformance.May),
    CONF_HP_58_2("/hl7:ClinicalDocument/hl7:participant/hl7:associatedEntity",
            "hl7:associatedPerson ",
            RuleType.NodeExists,
            RuleConformance.Shall),
    CONF_HP_58_3("/hl7:ClinicalDocument/hl7:participant/hl7:associatedEntity",
            "hl7:scopingOrganization",
            RuleType.NodeExists,
            RuleConformance.Shall),
    // CONF-HP-63: The componentOf element SHALL be present.
    CONF_HP_63("/hl7:ClinicalDocument",
            "hl7:componentOf",
            RuleType.NodeExists,
            RuleConformance.Shall),  
    // START OF PHR EXTRACT 
    // Allergies Section Template
    AllergiesSection_OID("/hl7:ClinicalDocument/hl7:componenent/hl7:section/hl7:templateId[@root]",
            "1.3.6.1.4.1.19376.1.5.3.1.3.13",
            RuleType.AttributeValidation,
            RuleConformance.Shall),  
    // START OF PHR EXTRACT ,
    // Test Value. Use as a sanity check that this node is NOT found
    CONF_HP_BAD("/hl7:ClinicalDocument",
            "hl7:bad",
            RuleType.NodeExists,
            RuleConformance.Shall);
    
    private String XpathExpression;
    private String value;
    private RuleType ruleType;
    private RuleConformance ruleConformance;
    
    //public static ConformanceRules[] 
    
    public static ConformanceRuleParts[] CONF_HP_Level1 = {
                                                typeId,
                                                CONF_HP_1,
                                                CONF_HP_3,
                                                CONF_HP_4,
                                                CONF_HP_6,
                                                CONF_HP_7_1,
                                                CONF_HP_7_2,
                                                CONF_HP_8_1,
                                                CONF_HP_8_2,
                                                CONF_HP_16,
                                                CONF_HP_17,
                                                CONF_HP_21,
                                                CONF_HP_22,
                                                CONF_HP_23,
                                                ConfidentialityCode,
                                                CONF_HP_24,
                                                CONF_HP_30};
    
    public static ConformanceRuleParts[] CONF_HP_Level2 = {
                                                typeId,
                                                CONF_HP_1,
                                                CONF_HP_2,
                                                CONF_HP_4,
                                                CONF_HP_6,
                                                CONF_HP_7_1,
                                                CONF_HP_7_2,
                                                CONF_HP_8_1,
                                                CONF_HP_8_2,
                                                CONF_HP_16,
                                                CONF_HP_17,
                                                CONF_HP_21,
                                                CONF_HP_22,
                                                CONF_HP_23,
                                                ConfidentialityCode,
                                                CONF_HP_24,
                                                CONF_HP_30,
                                                CONF_HP_51,
                                                CONF_HP_52
                                                };
    /**
     * 
     * for AttributeValidation: value = expected Fixed Value
     * for NodeExists value = node name
     * for RegEx value = the regular expression to be used for validation
     * for Length value = the string value of the length
     * 
     * @param _XpathExpression
     * @param _value
     * @param _ruleType
     * @param _ruleConformance 
     */
    private ConformanceRuleParts(String _XpathExpression, String _value, RuleType _ruleType, RuleConformance _ruleConformance)
    {
        this.XpathExpression = _XpathExpression;
     
        // for AttributeValidation: value = expected Fixed Value
        // for NodeExists value = node name
        // for RegEx value = the regular expression to be used for validation
        // for Length value = the string value of the length
        this.value = _value;
        this.ruleType = _ruleType;
        this.ruleConformance = _ruleConformance;
    }
    
    public String getXpathExpression(){return this.XpathExpression;}
    public String getValue(){return this.value;}
    public RuleType getRuleType(){return this.ruleType;}
    public RuleConformance getRuleConformance(){return this.ruleConformance;}
    
    
    public enum RuleType{
        AttributeValidation,
        NodeExists,
        RegEx,
        Length;}
    
    public enum RuleConformance{
        Shall,
        ShallNot,
        Should,
        ShouldNot,
        May,
        NeedNot;}
}
