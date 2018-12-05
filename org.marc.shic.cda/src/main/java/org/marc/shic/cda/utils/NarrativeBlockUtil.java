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
 *
 * Date: October 29, 2013
 *
 */
package org.marc.shic.cda.utils;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author Paul
 */
public class NarrativeBlockUtil {

    private static JAXBContext context;
    private static Marshaller marshaller;
    
    public static String JAXBToXMLString(JAXBElement<?> jaxbElement, Class<?> jaxbElementParent) {
        StringWriter writer = new StringWriter();

        try {
            if (context == null) {
                context = JAXBContext.newInstance(jaxbElementParent);
                marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            }

            marshaller.marshal(jaxbElement, writer);
        } catch (JAXBException e) {
            Logger.getLogger(NarrativeBlockUtil.class).error("Failed to parse JAXB Context.", e);
        }

        return writer.toString();
    }
}
