/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.marc.shic.xds;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.AuthnContextBuilder;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.impl.SignatureBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 *
 * @author Medic
 */
public class XdsSamlHandler implements SOAPHandler<SOAPMessageContext> {

    protected static final Logger LOGGER = Logger.getLogger(XdsSamlHandler.class.getName());
    public static final String WS_SECURITY_NS_URI
            = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private CodeValue purposeOfUse;
    private String homeCommunityId;
    private String patientId;
    private final String endpoint;
    private final JKSStoreInformation keyStore;
    
    public XdsSamlHandler(Object object, String endpoint,JKSStoreInformation keyStore) {
        if(object instanceof XdsQuerySpecification){
        this.purposeOfUse = ((XdsQuerySpecification)object).getPurposeOfUse();
        this.homeCommunityId = ((XdsQuerySpecification)object).getHomeCommunityId();
        this.patientId = ((XdsQuerySpecification)object).getExtendedParameters().get(0).getValue().toString();
        }else if(object instanceof MetaData){
            this.purposeOfUse = ((MetaData)object).getPurposeOfUse();
            this.homeCommunityId = ((MetaData)object).getHomeCommunityId();
            this.patientId = String.format("%s^^^&%s&ISO", ((MetaData)object).getPatient().getIdentifiers().get(0).getExtension(), ((MetaData)object).getPatient().getIdentifiers().get(0).getRoot());
        }
        this.endpoint = endpoint;
        this.keyStore = keyStore;
        
    }

    @Override
    public Set<QName> getHeaders() {
        return new TreeSet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound.booleanValue() && this.purposeOfUse != null) {
            try {
                DefaultBootstrap.bootstrap();
                SOAPMessage message = context.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

                Name wsseHeaderName = envelope.createName("Security", "wsse", WS_SECURITY_NS_URI);

                if (envelope.getHeader() == null) {
                    envelope.addHeader();
                }

                SOAPHeaderElement header = envelope.getHeader().addHeaderElement(wsseHeaderName);

                AssertionBuilder aBuilder = new AssertionBuilder();
                Assertion assertion = aBuilder.buildObject();
                assertion.setVersion(SAMLVersion.VERSION_20);
                assertion.setID(UUID.randomUUID().toString());
                assertion.setIssueInstant(new DateTime());

                IssuerBuilder iBuilder = new IssuerBuilder();
                Issuer issuer = iBuilder.buildObject();
                issuer.setValue("https://issuer.example.com");
                assertion.setIssuer(issuer);

                SignatureBuilder sigBuilder = new SignatureBuilder();
                Signature signature = sigBuilder.buildObject();
                Credential credential = new XdsSamlSignature().createCredentials(this.keyStore);
                signature.setSigningCredential(credential);
                signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
                signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
                SecurityConfiguration secConfig = Configuration.getGlobalSecurityConfiguration();
                //Useless
                //String keyInfoGeneratorProfile = "XMLSignature";
                SecurityHelper.prepareSignatureParams(signature, credential, secConfig, null);
                assertion.setSignature(signature);

                SubjectBuilder sBuilder = new SubjectBuilder();
                Subject subject = sBuilder.buildObject();
                NameIDBuilder nBuilder = new NameIDBuilder();
                NameID nameID = nBuilder.buildObject();
                nameID.setValue("Oscar");
                nameID.setFormat(NameIdentifier.X509_SUBJECT);
                SubjectConfirmationBuilder scb = new SubjectConfirmationBuilder();
                SubjectConfirmation confirmation = scb.buildObject();
                confirmation.setMethod("urn:oasis:names:tc:SAML:2.0:cm:sender-vouches");
                subject.getSubjectConfirmations().add(confirmation);
                subject.setNameID(nameID);
                assertion.setSubject(subject);

                AuthnStatementBuilder asb = new AuthnStatementBuilder();
                AuthnStatement statement = asb.buildObject();
                statement.setAuthnInstant(new DateTime());
                AuthnContextBuilder acb = new AuthnContextBuilder();
                AuthnContext authContext = acb.buildObject();
                AuthnContextClassRefBuilder accrb = new AuthnContextClassRefBuilder();
                AuthnContextClassRef accr = accrb.buildObject();
                accr.setAuthnContextClassRef(AuthnContext.X509_AUTHN_CTX);
                authContext.setAuthnContextClassRef(accr);
                statement.setAuthnContext(authContext);
                assertion.getAuthnStatements().add(statement);

                AudienceRestrictionBuilder arb = new AudienceRestrictionBuilder();
                AudienceRestriction audienceRestriction = arb.buildObject();
                Audience audience = new AudienceBuilder().buildObject();
                audience.setAudienceURI(this.endpoint);
                audienceRestriction.getAudiences().add(audience);
                ConditionsBuilder cb = new ConditionsBuilder();
                Conditions conditions = cb.buildObject();
                conditions.setNotBefore(new DateTime());
                conditions.getAudienceRestrictions().add(audienceRestriction);
                //Not used by IHE
                //conditions.setNotOnOrAfter(new DateTime().plusDays(1));
                assertion.setConditions(conditions);

                AttributeStatementBuilder attstmtb = new AttributeStatementBuilder();
                AttributeStatement attstmt = attstmtb.buildObject();
                //attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xspa:1.0:subject:subject-id","XSPA subject","Oscar Clinic"));
                //attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xspa:1.0:subject:organization-id","Organization ID","Mohawk College"));
                attstmt.getAttributes().add(createAttribute("urn:ihe:iti:xca:2010:homeCommunityId", "Home Community ID", this.homeCommunityId));
                attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", "XSPA Purpose Of Use", this.purposeOfUse));
                attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xspa:2.0:subject:npi", "NPI", "1234567890"));
                //Role not used?
                //attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xacml:2.0:subject:role","Subject Role","1234567890"));
                attstmt.getAttributes().add(createAttribute("urn:oasis:names:tc:xacml:2.0:resource:resource-id", "Patient", this.patientId));
                assertion.getAttributeStatements().add(attstmt);

                MarshallerFactory marshallerFactory = Configuration
                        .getMarshallerFactory();
                Marshaller marshaller = marshallerFactory
                        .getMarshaller(assertion);
                Element assertionElement = marshaller.marshall(assertion);

                Signer.signObject(signature);

                header.appendChild(soapPart.importNode(
                        assertionElement, true));
            } catch (SOAPException e) {
                LOGGER.info("SAML ERROR: " + e);
            } catch (ConfigurationException e) {
                LOGGER.info("SAML ERROR: " + e);
            } catch (MarshallingException e) {
                LOGGER.info("SAML ERROR: " + e);
            } catch (SecurityException e) {
                LOGGER.info("SAML ERROR: " + e);
            } catch (SignatureException e) {
                LOGGER.info("SAML ERROR: " + e);
            } catch (DOMException e) {
                LOGGER.info("SAML ERROR: " + e);
            }
        }
        return true;
    }

    private Attribute createAttribute(String name, String friendlyName, CodeValue value) {
            
            XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();
         
            XMLObjectBuilder<XSAny> xsAnyBuilder = bf.getBuilder(XSAny.TYPE_NAME);

            XSAny role = xsAnyBuilder.buildObject("urn:hl7-org", "PurposeOfUse", "hl7");
            role.getUnknownAttributes().put(new QName("code"), value.getCode());
            role.getUnknownAttributes().put(new QName("codeSystem"), value.getCodeSystem());
            role.getUnknownAttributes().put(new QName("codeSystemName"), value.getCodeSystemName());
            role.getUnknownAttributes().put(new QName("displayName"), value.getDisplayName());
            role.getUnknownAttributes().put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi"), "CE");

            XSAny roleAttributeValue = xsAnyBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
            roleAttributeValue.getUnknownXMLObjects().add(role);

            Attribute attribute = (Attribute) bf.getBuilder(Attribute.DEFAULT_ELEMENT_NAME).buildObject(Attribute.DEFAULT_ELEMENT_NAME);
            attribute.setName("PurposeOfUse");
            attribute.setNameFormat("urn:hl7-org:v3");
            attribute.getAttributeValues().add(roleAttributeValue);

            return attribute;

    }

    private Attribute createAttribute(String name, String friendlyName, String value) {
        AttributeBuilder attbldr = new AttributeBuilder();
        Attribute attr = attbldr.buildObject();
        attr.setName(name);
        attr.setFriendlyName(friendlyName);
        attr.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
        XSStringBuilder stringBuilder = (XSStringBuilder) Configuration
                .getBuilderFactory().getBuilder(XSString.TYPE_NAME);
        XSString stringValue = stringBuilder
                .buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
                        XSString.TYPE_NAME);
        stringValue.setValue(value);
        attr.getAttributeValues().add(stringValue);
        return attr;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        LOGGER.error("Exception in Client handler.");
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

}
