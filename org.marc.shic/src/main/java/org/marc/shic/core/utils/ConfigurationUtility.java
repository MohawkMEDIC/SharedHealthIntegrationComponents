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
 * @author Mohamed Ibrahim Date: 02-Nov-2013
 *
 */
package org.marc.shic.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;
import org.marc.shic.core.configuration.IheCodeMappingConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.IheValueSetConfiguration;
import org.marc.shic.core.configuration.consent.AclDefinition;
import org.marc.shic.core.configuration.consent.DemandPermission;
import org.marc.shic.core.configuration.consent.PolicyActionOutcome;
import org.marc.shic.core.configuration.consent.PolicyCollection;
import org.marc.shic.core.configuration.consent.PolicyDefinition;
import org.marc.shic.core.configuration.generated.AffinityDomain;
import org.marc.shic.core.configuration.generated.AffinityDomain.Actors;
import org.marc.shic.core.configuration.generated.AffinityDomain.Codes.CodeType;
import org.marc.shic.core.configuration.generated.AffinityDomain.Codes.CodeType.Code;
import org.marc.shic.core.configuration.generated.AffinityDomain.Policies;
import org.marc.shic.core.configuration.generated.AffinityDomain.Policies.Policy;
import org.marc.shic.core.configuration.generated.AffinityDomain.Policies.Policy.Acl.Entry;
import org.marc.shic.core.configuration.generated.AffinityDomain.ValueSets.ValueSet;
import org.marc.shic.core.exceptions.IheConfigurationException;

public class ConfigurationUtility {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationUtility.class.getName());

    /**
     * Parse an affinity domain configuration file using JAXB
     *
     * @param file
     * @return an affinity domain object
     * @throws IheConfigurationException
     */
    public static IheAffinityDomainConfiguration parseConfiguration(File file) throws IheConfigurationException {
        IheAffinityDomainConfiguration retVal = null;
        try {
            InputStream is = new FileInputStream(file);
            retVal = parseConfiguration(is);
        } catch (FileNotFoundException ex) {
            LOGGER.error("Configurarion file was not found", ex);
        }
        return retVal;
    }
    
    /**
     * Parse an affinity domain configuration file using JAXB
     *
     * @param inputstream
     * @return an affinity domain object
     * @throws IheConfigurationException
     */
    public static IheAffinityDomainConfiguration parseConfiguration(InputStream inputStream) throws IheConfigurationException {

        IheAffinityDomainConfiguration retVal = new IheAffinityDomainConfiguration();

        // parse
        AffinityDomain domain;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AffinityDomain.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            domain = (AffinityDomain) jaxbUnmarshaller.unmarshal(inputStream);

        } catch (JAXBException e) {
            LOGGER.error("An Error occurred while parsing the supplied affinity domain configuration file.", e);
            throw new IheConfigurationException(e);
        }

        // load metadata
        retVal.setOid(domain.getOid());
        retVal.setPermission(IheAffinityDomainPermission.fromString(domain.getPermission()));
        retVal.setName(domain.getName());

        // load actors
        List<IheActorConfiguration> iheActors = extractActors(domain.getActors());
        retVal.getActors().addAll(iheActors);

        // load consent policies
        PolicyCollection consentPolicies = extractPolicies(domain.getPolicies());
        retVal.setConsent(consentPolicies);

        // load value sets (optional if an SVS actor is not present)
        if (domain.getValueSets() != null && domain.getValueSets().getValueSet() != null) {
            List<IheValueSetConfiguration> valueSets = extractValueSets(domain.getValueSets().getValueSet());
            retVal.getValueSets().addAll(valueSets);
        }

        // load code types (optional if both an SVS actor and valueSets are present)
        if (domain.getCodes() != null && domain.getCodes().getCodeType() != null) {
            List<IheCodeMappingConfiguration> codes = extractCodeTypes(domain.getCodes().getCodeType());
            retVal.getCodeMappings().addAll(codes);
        }

        return retVal;
    }

    /**
     * Extracts Codes from the unmarshalled JAXB objects into an
     * IheCodeMappingConfiguration list
     *
     * @param maps
     * @return
     */
    private static List<IheCodeMappingConfiguration> extractCodeTypes(List<CodeType> codeTypes) {
        List<IheCodeMappingConfiguration> retVal = new ArrayList<IheCodeMappingConfiguration>();
        for (CodeType codeType : codeTypes) {
            IheCodeMappingConfiguration codeMappingConfiguration = new IheCodeMappingConfiguration();
            codeMappingConfiguration.setAttribute(codeType.getAttribute());
            codeMappingConfiguration.setDescription(codeType.getDescription());
            // codes
            codeMappingConfiguration.getCodes().addAll(extractCodes(codeType.getCode()));

            retVal.add(codeMappingConfiguration);
        }

        return retVal;
    }

    /**
     * Extracts Codes from the JAXB classes into a list of
     * CodeValue objects
     *
     * @param codes
     * @return
     */
    private static List<CodeValue> extractCodes(List<Code> codes) {
        List<CodeValue> retVal = new ArrayList<CodeValue>();
        for (Code code : codes) {
            CodeValue codeValue = new CodeValue(code.getCode(), code.getCodingScheme(), code.getDisplay());
            retVal.add(codeValue);
        }

        return retVal;
    }

    /**
     * Extracts ValueSets from the JAXB unmarshalled objects into an
     * IheValueSetConfiguration list
     *
     * @param valueSets
     * @return
     */
    private static List<IheValueSetConfiguration> extractValueSets(List<ValueSet> valueSets) {
        List<IheValueSetConfiguration> retVal = new ArrayList<IheValueSetConfiguration>();
        for (ValueSet valueSet : valueSets) {
            IheValueSetConfiguration valueSetConfiguration = new IheValueSetConfiguration(valueSet.getId(), valueSet.getDescription(), valueSet.getAttribute());
            retVal.add(valueSetConfiguration);
        }

        return retVal;
    }

    /**
     * Extracts consent policies from the JAXB unmarshalled objects into a
     * PolicyCollection object.
     *
     * @param policies
     * @return
     */
    private static PolicyCollection extractPolicies(Policies policies) {
        PolicyCollection retVal = new PolicyCollection();

        for (Policy policy : policies.getPolicy()) {
            PolicyDefinition definition = new PolicyDefinition();
            // metadata
            definition.setDisplayName(policy.getDisplay());
            definition.setCode(policy.getCode());
            definition.setCodeSystem(policy.getCodingScheme());

            // duration attribute is option in the config file (none = forever)
            if (policy.getDuration() != null) {
                definition.setAcknowledgementDuration(policy.getDuration());
            }

            definition.setPolicyDocumentUrl(policy.getAcknowledgement().getPolicyDocument());
            // acl
            definition.getAcl().addAll(extractAclDefinitions(policy.getAcl().getEntry()));

            retVal.add(definition);
        }

        return retVal;
    }

    /**
     * Extracts a list of AclDefinifions from the JAXB unmarshalled objects
     * containing ACL entries.
     *
     * @param entries
     * @return
     */
    private static List<AclDefinition> extractAclDefinitions(List<Entry> entries) {
        List<AclDefinition> retVal = new ArrayList<AclDefinition>();
        for (Entry entry : entries) {
            AclDefinition acl = new AclDefinition();
            acl.setRole(entry.getRole());
            acl.setPermission(DemandPermission.fromString(entry.getPermission()));
            acl.setAction(PolicyActionOutcome.fromString(entry.getAction()));

            retVal.add(acl);
        }

        return retVal;
    }

    /**
     * Extracts a list of actors from the affinity domain's JAXB unmarshalled
     * objects.
     *
     * @param iheActors
     * @return
     */
    private static List<IheActorConfiguration> extractActors(Actors actors) {
        List<IheActorConfiguration> retVal = new ArrayList<IheActorConfiguration>();

        // pixmgr
        if (actors.getPixManager() != null) {
            for (AffinityDomain.Actors.PixManager.Entry entry : actors.getPixManager().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.PAT_IDENTITY_X_REF_MGR);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }
        
        // pdq Supplier
        if (actors.getPdqSupplier() != null) {
            for (AffinityDomain.Actors.PdqSupplier.Entry entry : actors.getPdqSupplier().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.PDS);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // xds repository
        if (actors.getDocumentRepository() != null) {
            for (AffinityDomain.Actors.DocumentRepository.Entry entry : actors.getDocumentRepository().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.DOC_REPOSITORY);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // xds registry
        if (actors.getDocumentRegistry() != null) {
            for (AffinityDomain.Actors.DocumentRegistry.Entry entry : actors.getDocumentRegistry().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.DOC_REGISTRY);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // document recipient
        if (actors.getDocumentRecipient() != null) {
            for (AffinityDomain.Actors.DocumentRecipient.Entry entry : actors.getDocumentRecipient().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.DOC_RECIPIENT);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // responding gateway registry
        if (actors.getRespondingGatewayRegistry() != null) {
            for (AffinityDomain.Actors.RespondingGatewayRegistry.Entry entry : actors.getRespondingGatewayRegistry().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.RESPONDING_GATEWAY_REGISTRY);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }
        
        // responding gateway repository
        if (actors.getRespondingGatewayRepository() != null) {
            for (AffinityDomain.Actors.RespondingGatewayRepository.Entry entry : actors.getRespondingGatewayRepository().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.RESPONDING_GATEWAY_REPOSITORY);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // atna
        if (actors.getAuditRepository() != null) {
            for (AffinityDomain.Actors.AuditRepository.Entry entry : actors.getAuditRepository().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.AUDIT_REPOSITORY);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // svs
        if (actors.getSvsRepository() != null) {
            for (AffinityDomain.Actors.SvsRepository.Entry entry : actors.getSvsRepository().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.SVS);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        // time server
        if (actors.getTimeServer() != null) {
            for (AffinityDomain.Actors.TimeServer.Entry entry : actors.getTimeServer().getEntry()) {
                IheActorConfiguration actor = new IheActorConfiguration();
                actor.setActorType(IheActorType.TS);
                actor.setEndPointAddress(URI.create(entry.getEndpoint()));
                actor.setRemoteIdentification(new IheIdentification(entry.getApplication(), entry.getFacility()));
                actor.setName(entry.getName());
                actor.setOid(entry.getOid());
                actor.setSecure(entry.isSecure());

                retVal.add(actor);
            }
        }

        return retVal;
    }
}
