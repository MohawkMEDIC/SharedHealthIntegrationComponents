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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EncapsulatedDataRepresentation;
import org.marc.everest.datatypes.EntityNamePartQualifier;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.SetOperator;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.shic.core.AddressComponent;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.NameComponent;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonName;

public class CoreDataTypeHelpers {

    /**
     * Create an II Set
     */
    public static SET<II> createIISet(List<DomainIdentifier> identifiers) {
        SET<II> retVal = new SET<II>();
        if (identifiers != null) {
            for (DomainIdentifier id : identifiers) {
                retVal.add(new II(id.getRoot(), id.getExtension()));
            }
        }

        return retVal;
    }

    /**
     * Create an II Set
     */
    public static SET<II> createIIList(List<DomainIdentifier> identifiers) {
        SET<II> retVal = new SET<II>();
        for (DomainIdentifier id : identifiers) {
            retVal.add(new II(id.getRoot(), id.getExtension()));
        }

        return retVal;
    }

    /**
     * Create an HL7v3 AD from the SHC person Address
     */
    public static AD createAD(PersonAddress addr) {
        AD retVal = new AD();
        if (addr != null) {
            switch (addr.getUse()) {
                case Home:
                    retVal.setUse(SET.createSET(new CS<PostalAddressUse>(PostalAddressUse.HomeAddress)));
                    break;
                case Workplace:
                    retVal.setUse(SET.createSET(new CS<PostalAddressUse>(PostalAddressUse.WorkPlace)));
                    break;
            }

            for (AddressComponent nc : addr.getParts()) {
                ADXP part = new ADXP();

                switch (nc.getType()) {
                    case AddressLine:
                        part.setPartType(org.marc.everest.datatypes.AddressPartType.AddressLine);
                        break;
                    case City:
                        part.setPartType(org.marc.everest.datatypes.AddressPartType.City);
                        break;
                    case Country:
                        part.setPartType(org.marc.everest.datatypes.AddressPartType.Country);
                        break;
                    case State:
                        part.setPartType(org.marc.everest.datatypes.AddressPartType.State);
                        break;
                    case Zipcode:
                        part.setPartType(org.marc.everest.datatypes.AddressPartType.PostalCode);
                        break;
                }

				if (nc.getValue() != null && !nc.getValue().isEmpty()) {
                    part.setValue(nc.getValue());
                    retVal.getPart().add(part);
                }
            }
        }
        return retVal;
    }

    public static ED createEDWithReference(String reference) {
        TEL tel = new TEL();
        tel.setValue(reference);
        ED ed = new ED(tel);
        ed.setRepresentation(EncapsulatedDataRepresentation.Xml);
        return ed;
    }

    //org.marc.shic.core.AddressPartType partType
    public static ADXP createADXP(PersonAddress addr, org.marc.shic.core.AddressPartType partType) {
        ADXP part = new ADXP();
        AddressComponent nc = addr.getPartByType(partType);

        part.setPartType(convertSHICtoJEverestAddressPartType(partType));

        if (nc == null) {
            part.setNullFlavor(NullFlavor.Unknown);
        } else {
            part.setCode(nc.getValue());
        }

        return part;
    }

    /**
     * Constructs an HL7v3 PN from the SHC PersonName class
     */
    public static PN createPN(PersonName name) {
        PN retVal = new PN();
        if (name.getUse() == NameUse.Legal) {
            retVal.setUse(SET.createSET(new CS<EntityNameUse>(EntityNameUse.Legal)));
        }
        for (NameComponent nc : name.getParts()) {
            switch (nc.getType()) {
                case Family:
                    retVal.getParts().add(new ENXP(nc.getValue(), EntityNamePartType.Family));
                    break;
                case Given:
                    retVal.getParts().add(new ENXP(nc.getValue(), EntityNamePartType.Given));
                    break;
                case Middle:
                    ENXP np = new ENXP(nc.getValue(), EntityNamePartType.Given);
                    np.setQualifier(SET.createSET(new CS<EntityNamePartQualifier>(EntityNamePartQualifier.Middle)));
                    retVal.getParts().add(np);
                    break;
            }
        }
        return retVal;
    }

    public static IVL<TS> createIVL_TS(Calendar low, Calendar high, SetOperator setOperator) {
        IVL<TS> retVal = new IVL<TS>(new TS(low), new TS(high));

        if (setOperator != null) {
            retVal.setOperator(setOperator);
        }

//        if (null != low)
//        {
//            retVal.setLow(new TS(low));
//        }
//
//        if (null != high)
//        {
//            retVal.setHigh(new TS(high));
//        }
        return retVal;
    }

    public static IVL<TS> createIVL_TS(Date low, Date high, SetOperator setOperator) {

        Calendar calLow = new GregorianCalendar();
        Calendar calHigh = new GregorianCalendar();

        calLow.setTime(low);
        calHigh.setTime(high);

        return createIVL_TS(calLow, calHigh, setOperator);
    }

    public static TS createTS(Calendar time) {
        return new TS(time);
    }

    public static TS createTS(Calendar low, Calendar high) {
        TS ts = new TS();
        if (null != low) {
            ts.setValidTimeLow(new TS(low));
        }

        if (null != high) {
            ts.setValidTimeHigh(new TS(high));
        }

        return ts;
    }

    public static ON createON(String name, EntityNamePartType namePartType, EntityNameUse nameUse) {
        List<ENXP> nameParts = new ArrayList<ENXP>();
        ENXP namePart = new ENXP(name, namePartType);

        namePart.setQualifier(new SET<CS<EntityNamePartQualifier>>(new CS<EntityNamePartQualifier>(EntityNamePartQualifier.LegalStatus)));
        namePart.setType(new CS<EntityNamePartType>(EntityNamePartType.Title));

        nameParts.add(namePart);

        ON retVal = new ON(nameUse, nameParts);

        //        ArrayList<ENXP> namePartList = new ArrayList<ENXP>();
//        ENXP part = new ENXP(name, EntityNamePartType.Title);
//
//        namePartList.add(part);
//
//        ON retVal = new ON(nameUse, namePartList);
//
//
//        retVal.setParts(namePartList);
        //retVal.setParts(LIST.createLIST(part));
        return retVal;
    }

    /**
     * Create a generic Coded Element (CE) object from a CodeValue
     *
     * @param code
     *
     * @return
     */
    public static CE<?> createCodedElement(CodeValue code) {
        CE<String> retVal = new CE<String>();
        retVal.setCode(code.getCode());
        retVal.setCodeSystem(code.getCodeSystem());
        retVal.setCodeSystemName(code.getCodeSystemName());
        retVal.setDisplayName(code.getDisplayName());

        return retVal;
    }

    public static org.marc.everest.datatypes.ADXP findADXPinPersonAddress(PersonAddress addr, org.marc.shic.core.AddressPartType partType) {
        ADXP retVal = new ADXP();

        if (partType == null || addr.getPartByType(partType).equals("")) {
            retVal.setNullFlavor(NullFlavor.Unknown);
        } else {
            retVal.setPartType(convertSHICtoJEverestAddressPartType(partType));
            retVal.setCode(addr.getPartByType(partType).getValue());
        }

        return retVal;
    }

    public static org.marc.everest.datatypes.AddressPartType convertSHICtoJEverestAddressPartType(org.marc.shic.core.AddressPartType partType) {
        org.marc.everest.datatypes.AddressPartType retVal = null;

        switch (partType) {
            case AddressLine:
                retVal = org.marc.everest.datatypes.AddressPartType.AddressLine;
                break;
            case City:
                retVal = org.marc.everest.datatypes.AddressPartType.City;
                break;
            case Country:
                retVal = org.marc.everest.datatypes.AddressPartType.Country;
                break;
            case State:
                retVal = org.marc.everest.datatypes.AddressPartType.State;
                break;
            case Zipcode:
                retVal = org.marc.everest.datatypes.AddressPartType.PostalCode;
                break;
        }

        return retVal;
    }

    /**
     * Formats TEL for a phone number Also sets a nullflavor of unknown if the
     * input is empty or null
     *
     * @param value
     * @return
     */
    public static TEL createTEL(String value) {
        TEL tel = new TEL(value);

        if (value == null || value.equals("")) {
            tel = null;
        } else if (!value.startsWith("tel:")) {
            value = "tel:" + value;
            tel.setValue(value);
        }

        return tel;
    }

    public static CS<TelecommunicationsAddressUse> createTelUse(String value) {
        CS<TelecommunicationsAddressUse> telUse = new CS<TelecommunicationsAddressUse>();

        return telUse;
    }
}
