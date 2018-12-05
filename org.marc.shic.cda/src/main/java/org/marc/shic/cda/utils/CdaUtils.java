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
 * @author brownp Date: Aug 16, 2013
 *
 */
package org.marc.shic.cda.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.formatters.interfaces.IFormatterParseResult;
import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
import org.marc.everest.formatters.xml.datatypes.r1.R1FormatterCompatibilityMode;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.interfaces.IGraphable;
import org.marc.everest.interfaces.ResultDetailType;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.rim.InfrastructureRoot;
import org.marc.shic.cda.ResultDetail;
import org.marc.shic.cda.Template;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.everestfunc.Telecommunicable;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templateRules.ConformanceRuleParts;
import org.marc.shic.cda.templateRules.TemplateRuleID;
import org.marc.shic.cda.templates.RecordTargetTemplate;
import org.marc.shic.core.Demographic.Phone;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.exceptions.IheException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CdaUtils {

	private static final Logger LOGGER = Logger.getLogger(CdaUtils.class);

	public static IGraphable everestParse(String dataStr) {
		return everestParse(dataStr.getBytes());
	}

	public static IGraphable everestParse(byte[] data) {

		XmlIts1Formatter formatter = new XmlIts1Formatter();
		formatter.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture));
		formatter.addCachedClass(ClinicalDocument.class);

		String xml = new String(data);
		IFormatterParseResult result = formatter.parse(new ByteArrayInputStream(data));
		return result.getStructure();
	}

	public static void pDemographicToTemplate(PersonDemographic person, Template t) {
		if (t instanceof Identifiable) {
			for (DomainIdentifier id : person.getIdentifiers()) {
				((Identifiable) t).addId(id.getRoot(), id.getExtension());
			}
		}
		if (t instanceof Nameable) {
			for (PersonName name : person.getNames()) {
				((Nameable) t).addName(CoreDataTypeHelpers.createPN(name));
			}
		}
		if (t instanceof Addressable) {
			for (PersonAddress address : person.getAddresses()) {
				((Addressable) t).addAddress(new Address(CoreDataTypeHelpers.createAD(address)));
			}
		}
		if (t instanceof Telecommunicable) {
			for (Phone phone : person.getPhones()) {
				((Telecommunicable) t).addTelecom(CoreDataTypeHelpers.createTEL(phone.getPhoneNumber()));
			}
		}
		if (t instanceof RecordTargetTemplate) {
			((RecordTargetTemplate) t).setBirthTime(new Time(person.getDateOfBirth()));
		}
	}

	/**
	 * Adds a template rule to an everest object from a template rule OID
	 * string.
	 *
	 * @param root
	 * @param id
	 */
	public static void addTemplateRuleID(InfrastructureRoot root, String id) {
		addTemplateRuleID(root, new II(id));
	}

	/**
	 * Adds a template rule to an everest object from a template ID enum.
	 *
	 * @param root
	 * @param templateId
	 */
	public static void addTemplateRuleID(InfrastructureRoot root, TemplateRuleID templateId) {
		addTemplateRuleID(root, templateId.getII());
	}

	/**
	 * Adds a template rule to an everest object from an instance
	 * identifier.
	 *
	 * @param root
	 * @param identifier
	 */
	public static void addTemplateRuleID(InfrastructureRoot root, II identifier) {
		if (root.getTemplateId() == null) {
			root.setTemplateId(new LIST<II>());
		}
		if (!root.getTemplateId().contains(identifier)) {
			root.getTemplateId().add(identifier);
		}
	}

	/**
	 * Returns the XML as a string from a graphable object such as a
	 * Clinical Document or one of it's members
	 *
	 * @param graphable
	 *
	 * @return
	 */
	public static String toXmlString(IGraphable graphable, boolean createRequiredElements) {
		XmlIts1Formatter formatter = new XmlIts1Formatter();
		String retVal = null;

		try {

			DatatypeFormatter dtfUniversal = new DatatypeFormatter(R1FormatterCompatibilityMode.Universal);
			DatatypeFormatter dtfCda = new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture);
			formatter.getGraphAides().add(dtfCda);
			formatter.setCreateRequiredElements(createRequiredElements);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			formatter.graph(bos, graphable);

			retVal = new String(bos.toByteArray(), 0);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new IheException("Could not construct the graphable object", e);
		}
        //retVal = "<?xml-stylesheet type='text/xsl' href='OscarStyleCda.xsl'?>" + retVal;

		//FIXME HACK to remove the representation="TXT" for schema validation 
		//.replaceAll("hl7:|:hl7", "")
//        // FIXME HACK - workaround for Everest CDA generation issues
//        for(StrReplaceHack replacement : StrReplaceHack.values())
//        {
//            System.out.println("Replacing " + replacement.getOldText()+ " With " +  replacement.getNewText());
//            retVal.replaceAll(replacement.getOldText(), replacement.getNewText());
//            retVal = retVal.replaceAll("representation=\"TXT\"", "");
//        }
		retVal = retVal.replaceAll("representation=\"TXT\"", "");
		retVal = retVal.replaceAll("representation=\"B64\"", "");
		//retVal = retVal.replaceAll("xmlns:hl7=\"urn:hl7-org:v3\"", "xmlns=\"urn:hl7-org:v3\" xmlns:hl7=\"urn:hl7-org:v3\"");// fixes parsing with dataTypes such as CD
		retVal = retVal.replaceAll("highClosed=\"false\"", "");
		retVal = retVal.replaceAll("lowClosed=\"false\"", "");
		retVal = retVal.replaceAll("hl7:CD", "CD");
		retVal = retVal.replaceAll("hl7:IVL_TS", "IVL_TS");

		return retVal;
	}

	/**
	 * Returns the XML as a string from a graphable object such as a
	 * Clinical Document or one of it's members
	 *
	 * @param graphable
	 *
	 * @return
	 */
//    public static String toXmlString()
//    {
//        XmlIts1Formatter formatter = new XmlIts1Formatter();
//
//        String retVal = null;
//
//
//        ArrayList<IStructureFormatter> f = formatter.getGraphAides();
//        DatatypeFormatter dtf = new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture);
//        
//
//        //formatter.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        //formatter.graph(bos, graphable);
//
//        retVal = new String(bos.toByteArray(), 0);
//
//        //remove the hl7 namespace for testing validation against the CDA.xsd
//        return retVal.replaceAll("hl7:", "");
//    }
	/**
	 * Create a Document object from an xmlString
	 *
	 * @param xmlString
	 *
	 * @return
	 */
	public static Document createXmlDocument(String xmlString) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;

		Document document = null;

		//remove the hl7 namespace for testing validation against the CDA.xsd
		//xmlString = xmlString.replaceAll("hl7:|:hl7", "");
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(xmlString)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * Create a String result from a Document
	 *
	 * @param document
	 *
	 * @return
	 */
	public static String XmlDocumentToString(Document document) {
		String retVal = null;

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			retVal = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (Exception e) {
		}

		return retVal;
	}
	// String xmlString = CdaUtils.toXmlString(document.getDocument(), createRequiredElements);

	public static boolean testXpathValue(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule) {
		boolean validated = false;

		switch (conformanceRule.getRuleType()) {
			case AttributeValidation:
				validated = testXpathAttributeValidation(documentTemplate, conformanceRule);
				break;
			case NodeExists:
				validated = testXpathNodeExists(documentTemplate, conformanceRule);
				break;
			case RegEx:
				validated = testXpathRegEx(documentTemplate, conformanceRule);
				break;
			case Length:
				validated = testXpathLength(documentTemplate, conformanceRule);
				break;
			default:
				break;
		}

		return validated;

	}

	private static boolean testXpathAttributeValidation(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule) {
		Document document;
		NodeList nodelist;
		boolean validated = false;

		try {
			document = createXmlDocument(CdaUtils.toXmlString(documentTemplate.getDocument(), false));
			nodelist = (NodeList) createXPathExpression(conformanceRule).evaluate(document.getDocumentElement(), XPathConstants.NODESET);

			LOGGER.debug(String.format("Rule: %s", conformanceRule.getXpathExpression()));
			LOGGER.debug(String.format("Expected value: %s", conformanceRule.getValue()));
			LOGGER.debug(String.format("Found %s nodes", nodelist.getLength()));

			//TODO: Change the substring to regular expressions or something less ugly
			for (int index = 0; index < nodelist.getLength(); index++) {
				if (conformanceRule.getXpathExpression().contains("@")) {
					for (int i = 0; i < nodelist.item(index).getAttributes().getLength(); i++) {
						//LOGGER.debug("Found Node name: " + i + " " + nodelist.item(index).getAttributes().item(i).getNodeName());
						//LOGGER.debug("Found Node value: " + i + " " + nodelist.item(index).getAttributes().item(i).getNodeValue());
					}
					LOGGER.debug("Searching for Node name: " + conformanceRule.getXpathExpression().substring(conformanceRule.getXpathExpression().indexOf("@") + 1, conformanceRule.getXpathExpression().lastIndexOf("]")));
					LOGGER.debug("Node value found: " + nodelist.item(index).getAttributes().getNamedItem(conformanceRule.getXpathExpression().substring(conformanceRule.getXpathExpression().indexOf("@") + 1, conformanceRule.getXpathExpression().lastIndexOf("]"))).getTextContent());

					if (nodelist.item(index).getAttributes().getNamedItem(conformanceRule.getXpathExpression().substring(conformanceRule.getXpathExpression().indexOf("@") + 1, conformanceRule.getXpathExpression().lastIndexOf("]"))).getTextContent().equals(conformanceRule.getValue())) {
						LOGGER.debug("Match found");
						validated = true;
					} else {
						LOGGER.debug("Match NOT found");
					}
				}
			}
		} catch (Exception ex) {
			documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(), ex, ResultDetailType.ERROR));
			LOGGER.error(ex.getMessage(), ex);
		}

		return RuleConformanceEvaluator(documentTemplate, conformanceRule, validated);
	}

	/**
	 * Checks to see if a node exists for the given xpath expression
	 *
	 *
	 * @param documentTemplate
	 * @param conformanceRule
	 * @param detailedOutput
	 * @return
	 */
	private static boolean testXpathNodeExists(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule) {
		Document document;
		NodeList nodelist = null;
		boolean validated = false;

		try {
			document = createXmlDocument(CdaUtils.toXmlString(documentTemplate.getDocument(), false));
			nodelist = (NodeList) createXPathExpression(conformanceRule).evaluate(document.getDocumentElement(), XPathConstants.NODESET);

			LOGGER.debug(String.format("Rule: %s", conformanceRule.getXpathExpression()));
			LOGGER.debug(String.format("Expected value: %s", conformanceRule.getValue()));
			LOGGER.debug(String.format("Found %s nodes", nodelist.getLength()));

			if (nodelist.getLength() == 0) {
				// if the parent node does not exist, the child requirement does not exist
				return true;
			} else {
				// loop through all of the children 
				for (int index = 0; index < nodelist.getLength(); index++) {
                        //LOGGER.debug(String.format("Found node: %s.", nodelist.item(index).getNodeName()));

					// to see if the target node exists
					NodeList children = nodelist.item(index).getChildNodes();
					for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
						//LOGGER.debug(String.format("Found child node: %s.", children.item(childIndex).getNodeName()));

						if (children.item(childIndex).getNodeName().equals(conformanceRule.getValue())) {
							validated = true; // find 1 instance is a pass
							break;
						}
					}
					// end the loop if any node fails
					if (!validated) {
						break;
					}
				}
			}
		} catch (XPathExpressionException ex) {
			documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(), ex, ResultDetailType.ERROR));
			LOGGER.error(ex.getMessage(), ex);
		}

		return RuleConformanceEvaluator(documentTemplate, conformanceRule, validated);
	}

	private static boolean testXpathLength(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule) {
		return false;
	}

	private static boolean testXpathRegEx(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule) {
		return false;
	}

	private static XPathExpression createXPathExpression(final ConformanceRuleParts conformanceRule) {
		XPathFactory xPathfactory = XPathFactory.newInstance();

		XPath xpath = xPathfactory.newXPath();
		xpath.setNamespaceContext(XpathNamespaceContext.hl7Context);

		XPathExpression xPathExpression = null;

		try {
			switch (conformanceRule.getRuleType()) {
				case AttributeValidation:
					xPathExpression = xpath.compile(conformanceRule.getXpathExpression());
					break;
				case NodeExists:
					// for NodeExists, the target node is in the value field
					xPathExpression = xpath.compile(conformanceRule.getXpathExpression());
					break;
				case Length:
					break;
				case RegEx:
					break;
			}

		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
		}

		return xPathExpression;
	}

	private static boolean RuleConformanceEvaluator(DocumentTemplate documentTemplate, final ConformanceRuleParts conformanceRule, boolean testResult) {
		switch (conformanceRule.getRuleConformance()) {
			case May:
				if (!testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s MAY be set and is not.", conformanceRule.getValue())),
						ResultDetailType.INFORMATION));

				}

				testResult = true; // result passes regardless or its' existence

				break;
			case NeedNot:
				if (testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s NEED NOT be set and is.", conformanceRule.getValue())),
						ResultDetailType.INFORMATION));

				}

				testResult = true; // result passes regardless or its' existence

				break;
			case Shall:
				if (!testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s SHALL be set and is not.", conformanceRule.getValue())),
						ResultDetailType.ERROR));

				}

				break;
			case ShallNot:
				if (testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s SHALL NOT be set and is.", conformanceRule.getValue())),
						ResultDetailType.ERROR));
				} else {
					testResult = !testResult; // invert the results as we are checking for NOT
				}
				break;
			case Should:
				if (!testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s SHOULD be set and is not.", conformanceRule.getValue())),
						ResultDetailType.WARNING));

				}

				testResult = true; // pass tests with either result, a warning is generated for NOT

				break;
			case ShouldNot:
				if (testResult) {
					documentTemplate.getResultDetails().add(new ResultDetail(conformanceRule.getXpathExpression(),
						new Exception(String.format("%s SHOULD NOT be set and is.", conformanceRule.getValue())),
						ResultDetailType.WARNING));
				}

				testResult = true;// pass tests with either result, a warning is generated for True

				break;

		}

		return testResult;
	}

	/**
	 * HACK
	 *
	 * This is used to work around Everest bugs
	 *
	 */
	public enum StrReplaceHack {

		highClosed("highClosed=\\\".*\\\"", ""),
		lowClosed("lowClosed=\\\".*\\\"", ""),
		nameSpace("xmlns:hl7=\\\"urn:hl7-org:v3\\\"", "xmlns=\\\"urn:hl7-org:v3\\\" xmlns:hl7=\\\"urn:hl7-org:v3\\\""),
		representationTxt("representation=\\\"TXT\\\"", ""),
		representationB64("representation=\\\"B64\\\"", "");

		//retVal = retVal.replaceAll("representation=\"TXT\"", "");
		//retVal = retVal.replaceAll("xmlns:hl7=\"urn:hl7-org:v3\"", "xmlns=\"urn:hl7-org:v3\" xmlns:hl7=\"urn:hl7-org:v3\"");// fixes parsing with dataTypes such as CD
		String oldText;
		String newText;

		StrReplaceHack(String _oldText, String _newText) {
			this.oldText = _oldText;
			this.newText = _newText;
		}

		public String getOldText() {
			return this.oldText;
		}

		public String getNewText() {
			return this.newText;
		}
	}
}
