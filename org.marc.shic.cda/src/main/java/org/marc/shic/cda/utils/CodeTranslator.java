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
 * @author Paul Date: Oct 28, 2013
 *
 */
package org.marc.shic.cda.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.SET;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CodeTranslator {

    CD originalCode;

    public CD translateCD(CD cd, String targetCodeSystem) {
        Code source = new Code(cd.getCode().toString(), cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getDisplayName());
        Code translationTarget = new Code();
        translationTarget.setM_codeSystem(targetCodeSystem);
        
        CD translatedCode = new CD();
        translatedCode.setTranslation(new SET<CD<?>>());
        translatedCode.getTranslation().add(cd);

        translatedCode.setCodeSystem(targetCodeSystem);
        
        Code result = codeLookup(source, translationTarget);
        
        //translatedCode.setCode("Some STATIC code");
        //if unable to translate
        if(result == null)
            translatedCode.setNullFlavor(NullFlavor.Other);
        else
        {
            translatedCode.setCode(result.getM_code());
            translatedCode.setCodeSystemName(result.getM_codeSystemName());
            translatedCode.setCodeSystemVersion(result.getM_codeSystemVersion());
            translatedCode.setDisplayName(result.getM_displayName());
        }
        // else translate code

        return translatedCode;
    }

    public CE translateCE(CE ce, String targetCodeSystem) {
        Code source = new Code(ce.getCode().toString(), ce.getCodeSystem(), ce.getCodeSystemName(), ce.getCodeSystemVersion(), ce.getDisplayName());
        Code translationTarget = new Code();
        translationTarget.setM_codeSystem(targetCodeSystem);
        
        CE translatedCode = new CE();
        translatedCode.setTranslation(new SET<CD<?>>());
        CD originalCode = new CD();
        originalCode.setCode(ce.getCode());
        originalCode.setCodeSystem(ce.getCodeSystem());
        originalCode.setCodeSystemName(ce.getCodeSystemName());
        originalCode.setCodeSystemVersion(ce.getCodeSystemVersion());
        originalCode.setDisplayName(ce.getDisplayName());
        translatedCode.getTranslation().add(originalCode);

        translatedCode.setCodeSystem(targetCodeSystem);
        
        Code result = codeLookup(source, translationTarget);
        
        //translatedCode.setCode("Some STATIC code");
        //if unable to translate
        if(result == null)
            translatedCode.setNullFlavor(NullFlavor.Other);
        else
        {
            translatedCode.setCode(result.getM_code());
            translatedCode.setCodeSystemName(result.getM_codeSystemName());
            translatedCode.setCodeSystemVersion(result.getM_codeSystemVersion());
            translatedCode.setDisplayName(result.getM_displayName());
        }

        return translatedCode;
    }

    private Code codeLookup(Code source, Code destination) {
        Code result = new Code();

        Document document = readTranslationDocument();
        XPathExpression xpr = buildXpathExpression(source, destination);
        result = evaluateXpathExpression(document, xpr, source, destination);
        
        return result;
    }

    private Document readTranslationDocument() {
        File xmlFile = new File(".//src//main//resources//SampleTranslation.xml");
        if (!xmlFile.exists()) {
            return null;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbf = null;
        Document document = null;
        try {
            dbf = dbFactory.newDocumentBuilder();
            document = dbf.parse(xmlFile);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CodeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(CodeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CodeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return document;
    }

    private XPathExpression buildXpathExpression(Code source, Code destination) {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;

        StringBuilder sb = new StringBuilder();
        
        ///CodeList/Code[@code='Some Drug Material Code' and @codeSystem = '1.2.3.4.5' and @codeSystemVersion = 'version 1']/Translations/Code[@codeSystem = "1.2.3.4.5.6"]
        sb.append("/CodeList/Code[");

        if (!source.getM_code().equals("")) {
            sb.append(String.format("@code='%s'", source.getM_code()));
        }

        if (!source.getM_codeSystem().equals("")) {
            sb.append(String.format(" and @codeSystem = '%s'", source.getM_codeSystem()));
        }

        if (source.getM_codeSystemVersion() != null && !source.getM_codeSystemVersion().equals("")) {
            sb.append(String.format(" and @codeSystemVersion = '%s'", source.getM_codeSystemVersion()));
        }

        sb.append(String.format("]/Translations/Code[@codeSystem = '%s']", destination.getM_codeSystem()));

        try {
            expr = xpath.compile(sb.toString());
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CodeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return expr;
    }

    private Code evaluateXpathExpression(Document document, XPathExpression xpr, Code source, Code result) {
        NodeList translation = null;
        try {
            translation = (NodeList) xpr.evaluate(document, XPathConstants.NODESET);
            
            if(translation == null || translation.getLength() == 0)
                return null;
            
            NamedNodeMap map = translation.item(0).getAttributes();
            
            if (map.getNamedItem("codeSystem").getTextContent().equals(result.getM_codeSystem())
                    && (result.getM_codeSystemVersion() == null || result.getM_codeSystemVersion().equals("") || map.getNamedItem("codeSystemVersion").getTextContent().equals(result.getM_codeSystemVersion())))
            {
                result.setM_code(map.getNamedItem("code").getTextContent());
                result.setM_codeSystemName(map.getNamedItem("codeSystemName").getTextContent());
                result.setM_displayName(map.getNamedItem("displayName").getTextContent());

                return result;
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CodeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private class Code {

        private String m_code;
        private String m_codeSystem;
        private String m_codeSystemName;
        private String m_codeSystemVersion;
        private String m_displayName;

        public Code() {
        }

        public Code(String code, String codeSystem, String codeSystemName, String codeSystemVersion, String displayName) {
            this.m_code = code;
            this.m_codeSystem = codeSystem;
            this.m_codeSystemName = codeSystemName;
            this.m_codeSystemVersion = codeSystemVersion;
            this.m_displayName = displayName;
        }

        public String getM_code() {
            return m_code;
        }

        public void setM_code(String m_code) {
            this.m_code = m_code;
        }

        public String getM_codeSystem() {
            return m_codeSystem;
        }

        public void setM_codeSystem(String m_codeSystem) {
            this.m_codeSystem = m_codeSystem;
        }

        public String getM_codeSystemName() {
            return m_codeSystemName;
        }

        public void setM_codeSystemName(String m_codeSystemName) {
            this.m_codeSystemName = m_codeSystemName;
        }

        public String getM_codeSystemVersion() {
            return m_codeSystemVersion;
        }

        public void setM_codeSystemVersion(String m_codeSystemVersion) {
            this.m_codeSystemVersion = m_codeSystemVersion;
        }

        public String getM_displayName() {
            return m_displayName;
        }

        public void setM_displayName(String m_displayName) {
            this.m_displayName = m_displayName;
        }
    }
}
