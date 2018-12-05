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
 * @author tylerg Date: Sept 9 2013
 *
 */
package org.marc.shic.pix;

import org.marc.shic.core.AddressPartType;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import static org.marc.shic.core.PartType.Family;
import static org.marc.shic.core.PartType.Given;
import static org.marc.shic.core.PartType.Middle;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonName;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.datatype.XAD;
import ca.uhn.hl7v2.model.v231.datatype.XPN;
import ca.uhn.hl7v2.model.v231.segment.PID;
import ca.uhn.hl7v2.model.v231.datatype.CX;
import ca.uhn.hl7v2.model.v231.segment.MRG;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author tylerg
 */
public class PixUtility {

    public static final String PatientDateOfBirthFormat = "yyyyMMdd";

    private PixUtility() {
    }

    /**
     * Format a patient's date of birth into a string.
     *
     * @param patientDateOfBirth The date of birth to format.
     * @return Formatted date of birth in yyyyMMdd.
     */
    public static String formatPatientDateOfBirth(Calendar patientDateOfBirth) {
        SimpleDateFormat ft = new SimpleDateFormat(PatientDateOfBirthFormat);
        return ft.format(patientDateOfBirth.getTime());
    }

    public static List<PersonName> parseNamesFromXPN(ca.uhn.hl7v2.model.v25.datatype.XPN[] names) {
        List<PersonName> patientNames = new ArrayList<PersonName>();
        for (ca.uhn.hl7v2.model.v25.datatype.XPN patientName : names) {
            PersonName name = new PersonName();

            // TODO: Parse name type code
            name.setUse(NameUse.Legal);

            name.addNamePart(patientName.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue(), PartType.Middle);
            name.addNamePart(patientName.getFamilyName().getSurname().getValue(), PartType.Family);
            name.addNamePart(patientName.getGivenName().getValue(), PartType.Given);

            patientNames.add(name);
        }

        return patientNames;
    }

    public static List<PersonName> parseNamesFromXPN(ca.uhn.hl7v2.model.v231.datatype.XPN[] names) {
        List<PersonName> patientNames = new ArrayList<PersonName>();
        for (ca.uhn.hl7v2.model.v231.datatype.XPN patientName : names) {
            PersonName name = new PersonName();

            // TODO: Parse name type code
            name.setUse(NameUse.Legal);

            name.addNamePart(patientName.getMiddleInitialOrName().getValue(), PartType.Middle);
            name.addNamePart(patientName.getFamilyLastName().getFamilyName().getValue(), PartType.Family);
            name.addNamePart(patientName.getGivenName().getValue(), PartType.Given);

            patientNames.add(name);
        }

        return patientNames;
    }

    public static List<DomainIdentifier> parseIdentifiersFromCX(ca.uhn.hl7v2.model.v25.datatype.CX[] identifiers) {
        List<DomainIdentifier> patientIdentifiers = new ArrayList<DomainIdentifier>();

        for (ca.uhn.hl7v2.model.v25.datatype.CX id : identifiers) {
            patientIdentifiers.add(parseIdentifierFromCx(id));
        }

        return patientIdentifiers;
    }

    public static DomainIdentifier parseIdentifierFromCx(ca.uhn.hl7v2.model.v25.datatype.CX id) {
        DomainIdentifier identifier;
        String root = id.getAssigningAuthority().getUniversalID().getValue();
        String extension = id.getCx1_IDNumber().getValue();
        String assigningAuthority = id.getAssigningAuthority().getNamespaceID().getValue();

        identifier = new DomainIdentifier(root, extension, assigningAuthority);

        return identifier;
    }

    public static List<DomainIdentifier> parseIdentifiersFromCX(ca.uhn.hl7v2.model.v231.datatype.CX[] identifiers) {
        List<DomainIdentifier> patientIdentifiers = new ArrayList<DomainIdentifier>();

        for (ca.uhn.hl7v2.model.v231.datatype.CX id : identifiers) {
            patientIdentifiers.add(parseIdentifierFromCx(id));
        }

        return patientIdentifiers;
    }

    public static DomainIdentifier parseIdentifierFromCx(ca.uhn.hl7v2.model.v231.datatype.CX id) {
        DomainIdentifier identifier;
        String root = id.getAssigningAuthority().getUniversalID().getValue();
        String extension = id.getCx1_ID().getValue();
        String assigningAuthority = id.getAssigningAuthority().getNamespaceID().getValue();

        identifier = new DomainIdentifier(root, extension, assigningAuthority);

        return identifier;
    }

    public static List<PersonAddress> parseAddressesFromXAD(ca.uhn.hl7v2.model.v231.datatype.XAD[] addresses) {
        List<PersonAddress> personAddresses = new ArrayList<PersonAddress>();

        for (ca.uhn.hl7v2.model.v231.datatype.XAD patientAddress : addresses) {
            AddressUse use;

            if (patientAddress.getAddressType().getValue().equals("M")) {
                use = AddressUse.Home;
            } else if (patientAddress.getAddressType().getValue().equals("W")) {
                use = AddressUse.Workplace;
            } else {
                use = AddressUse.Other;
            }

            PersonAddress parsedAddress = new PersonAddress(use);

            // TODO: Use proper address
            parsedAddress.addAddressPart(patientAddress.getStreetAddress().getValue(), AddressPartType.AddressLine);

            parsedAddress.addAddressPart(patientAddress.getCity().getValue(), AddressPartType.City);
            parsedAddress.addAddressPart(patientAddress.getStateOrProvince().getValue(), AddressPartType.State);
            parsedAddress.addAddressPart(patientAddress.getZipOrPostalCode().getValue(), AddressPartType.Zipcode);
            parsedAddress.addAddressPart(patientAddress.getCountry().getValue(), AddressPartType.Country);

            personAddresses.add(parsedAddress);
        }

        return personAddresses;
    }

    public static List<PersonAddress> parseAddressesFromXAD(ca.uhn.hl7v2.model.v25.datatype.XAD[] addresses) {
        ArrayList<PersonAddress> personAddresses = new ArrayList<PersonAddress>();

        for (ca.uhn.hl7v2.model.v25.datatype.XAD patientAddress : addresses) {
            AddressUse use;

            if (patientAddress.getAddressType().getValue() == null) {
                use = AddressUse.Other;
            } else {
                if (patientAddress.getAddressType().getValue().equals("M")) {
                    use = AddressUse.Home;
                } else if (patientAddress.getAddressType().getValue().equals("W")) {
                    use = AddressUse.Workplace;
                } else {
                    use = AddressUse.Other;
                }
            }

            PersonAddress parsedAddress = new PersonAddress(use);

            // TODO: Use proper address
            parsedAddress.addAddressPart(patientAddress.getStreetAddress().getStreetOrMailingAddress().getValue(), AddressPartType.AddressLine);

            parsedAddress.addAddressPart(patientAddress.getCity().getValue(), AddressPartType.City);
            parsedAddress.addAddressPart(patientAddress.getStateOrProvince().getValue(), AddressPartType.State);
            parsedAddress.addAddressPart(patientAddress.getZipOrPostalCode().getValue(), AddressPartType.Zipcode);
            parsedAddress.addAddressPart(patientAddress.getCountry().getValue(), AddressPartType.Country);

            personAddresses.add(parsedAddress);
        }

        return personAddresses;
    }

    /**
     * Populates a PID with patient addresses.
     *
     * @param pid The PID to populate.
     * @param patientAddresses The addresses to populate the PID with.
     * @throws DataTypeException
     * @throws HL7Exception
     */
    public static void populatePidWithAddresses(PID pid, List<PersonAddress> patientAddresses) throws DataTypeException, HL7Exception {
        for (int i = 0; i < patientAddresses.size(); i++) {
            PersonAddress address = patientAddresses.get(i);

            // Hapi creates the index if it doesn't exist.
            XAD pidAddress = pid.getPatientAddress(i);

            if (address.getPartByType(AddressPartType.Country) != null) {
                pidAddress.getCountry().setValue(address.getPartByType(AddressPartType.Country).getValue());
            }

            // TODO: Add support for XAD11
            pidAddress.getCity().setValue(address.getPartByType(AddressPartType.City).getValue());
            pidAddress.getStateOrProvince().setValue(address.getPartByType(AddressPartType.State).getValue());
            pidAddress.getZipOrPostalCode().setValue(address.getPartByType(AddressPartType.Zipcode).getValue());
            pidAddress.getStreetAddress().setValue(address.getPartByType(AddressPartType.AddressLine).getValue());
            pidAddress.getAddressType().setValue(address.getUse().toString());
        }
    }

    /**
     * Populates a PID with patient names.
     *
     * @param pid The PID to populate.
     * @param patientNames The names to populate the PID with.
     * @throws HL7Exception
     */
    public static void populatePidWithNames(PID pid, List<PersonName> patientNames) throws HL7Exception {
        for (int i = 0; i < patientNames.size(); i++) {
            PersonName name = patientNames.get(i);

            XPN pidName = pid.getPatientName(i);

            populateXPN(pidName, name);

        }
    }

    public static void populateMrgWithNames(MRG mrg, List<PersonName> patientNames) throws HL7Exception {
        for (int i = 0; i < patientNames.size(); i++) {
            PersonName name = patientNames.get(i);

            XPN pidName = mrg.getPriorPatientName(i);

            populateXPN(pidName, name);

        }
    }

    private static void populateXPN(XPN pidName, PersonName name) throws HL7Exception {
        for (NameComponent namePart : name.getParts()) {
            switch (namePart.getType()) {
                case Family:
                    pidName.getFamilyLastName().getFamilyName().setValue(namePart.getValue());
                    break;
                case Given:
                    pidName.getGivenName().setValue(namePart.getValue());
                    break;
                case Middle:
                    pidName.getMiddleInitialOrName().setValue(namePart.getValue());
                    break;
            }

            // TODO: Add support for more than legal.
            // Legal
            pidName.getNameTypeCode().setValue("L");
        }
    }
}
