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
 * @since 7-Mar-2014
 *
 */
package org.marc.shic.cda.level1;

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.CdaUtils;

/**
 * Provides various factory methods to aid in the creation of various types of
 * CDA documents.
 *
 * @author Ryan Albert
 */
public class CDAFactory {

	/**
	 * Creates a generic CDA document where the standard applied is raw CDA.
	 *
	 *
	 * @return A DocumentTemplate object with CDAStandard = CDA
	 */
	public static DocumentTemplate createGenericCDADocument() {
		return new DocumentTemplate(CDAStandard.CDA);
	}

	/**
	 * Creates a generic CDA document with the provided standard.
	 *
	 * @param standard The CDAStandard to apply to the document.
	 * @return A DocumentTemplate object with the specified standard
	 * applied.
	 */
	public static DocumentTemplate createGenericDocument(CDAStandard standard) {
		return new DocumentTemplate(standard);
	}

	/**
	 * Creates a CDA document instantiated from a class definition that
	 * conforms to the specified CDAStandard.
	 *
	 * @param standard The CDAStandard the document must conform to.
	 * @return A type of DocumentTemplate object.
	 */
	public static DocumentTemplate createDocument(CDAStandard standard) {
		DocumentTemplate result = null;
		switch (standard) {
			case CDA:
			case CCD:
				result = new DocumentTemplate(standard);
				break;
			case PCC:
			case NexJ:
				result = createPHRDocument();
				break;
		}
		return result;
	}

	/**
	 * Creates an empty PHR Extract document (CDAStandard = PCC)
	 *
	 * @return A PhrExtractDocument object.
	 */
	public static PhrExtractDocument createPHRDocument() {
		return new PhrExtractDocument();
	}

	/**
	 * Parses a string containing an HL7v2 or HL7v3 CDA string.
	 *
	 * @param docStr A String containing the CDA data.
	 * @return A PhrExtractDocument representing the parsed CDA.
	 */
	public static PhrExtractDocument parsePHRDocument(String docStr) {
		return parsePHRDocument(docStr.getBytes());
	}

	/**
	 * Parses an HL7v2 or HL7v3 CDA document represented as bytes.
	 *
	 * @param data The byte representation of a CDA document.
	 * @return A PhrExtractDocument representing the parsed CDA.
	 */
	public static PhrExtractDocument parsePHRDocument(byte[] data) {
		return new PhrExtractDocument((ClinicalDocument) CdaUtils.everestParse(data));
	}

	/**
	 * Creates a BPPC (Basic Patient Policies and Consents) document.
	 *
	 * @param scanned A value indicating whether or not the contents of the
	 * document are scanned.
	 * @return A new BppcDocument object.
	 */
	public static BppcDocument createBPPCDocument(boolean scanned) {
		return new BppcDocument(scanned);
	}

	/**
	 * Creates an XDS-SD (Scanned Document) CDA document.
	 *
	 * @return A new ScannedDocument object.
	 */
	public static ScannedDocument createScannedDocument() {
		return new ScannedDocument();
	}
}
