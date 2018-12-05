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
package org.marc.shic.cda.utils;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Paul
 */

public class XpathNamespaceContext implements NamespaceContext{
    public static XpathNamespaceContext hl7Context = new XpathNamespaceContext();
    
    public String getNamespaceURI(String prefix)
    {
        if (prefix == null) throw new NullPointerException("Null prefix");
        else if ("hl7".equals(prefix)) return "urn:hl7-org:v3";
        else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
        return XMLConstants.NULL_NS_URI;
    }
    
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }
    
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    } 
}
