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
 * @author tylerg
 * Date: Nov 28, 2013
 *
 */
package org.marc.shic.xds.utils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBElement;
import org.apache.log4j.Logger;
import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.ICodeType;
import org.marc.shic.core.MetaData;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.SubmissionSetMetaData;
import org.marc.shic.core.XdsExtendedQueryParameterType;
import org.marc.shic.core.XdsGuidMetaDataType;
import org.marc.shic.core.XdsGuidType;
import org.marc.shic.core.XdsQuerySpecification;
import org.marc.shic.xds.XdsMessageParseException;
import org.marc.shic.xds.registry.AdhocQueryResponse;
import org.marc.shic.xds.registry.AdhocQueryType;
import org.marc.shic.xds.registry.AssociationType1;
import org.marc.shic.xds.registry.ClassificationType;
import org.marc.shic.xds.registry.ExternalIdentifierType;
import org.marc.shic.xds.registry.ExtrinsicObjectType;
import org.marc.shic.xds.registry.IdentifiableType;
import org.marc.shic.xds.registry.RegistryPackageType;
import org.marc.shic.xds.registry.SlotType1;
import org.marc.shic.xds.registry.ValueListType;

/**
 *
 * @author tylerg
 */
public class RegistryStoredQueryUtil
{

    public static final String SLOT_PATTERN_SINGLE = "^'.*'$";
    public static final String SLOT_PATTERN_MULTIPLE = "^\\('.*'\\)$";
    /**
     * Logger.
     */
    protected static final Logger LOGGER = Logger.getLogger(RegistryStoredQueryUtil.class.getName());

    private RegistryStoredQueryUtil()
    {
    }

    /**
     * Create a query slot
     */
    public static SlotType1 createQuerySlot(String slotName, String... value)
    {
        SlotType1 retSlot = new SlotType1();
        ValueListType patientValueList = new ValueListType();
        patientValueList.getValue().addAll(Arrays.asList(value));
        retSlot.setName(slotName);
        retSlot.setValueList(patientValueList);
        return retSlot;
    }

    public static void populateQueryWithExtendedParameters(AdhocQueryType adhocQuery, XdsQuerySpecification querySpec)
    {
        // Extended parameters
        for (XdsExtendedQueryParameterType<?> xp : querySpec.getExtendedParameters())
        {
            if (xp.getValue() instanceof Object[])
            {
                Object[] castedValues = (Object[]) xp.getValue();
                String[] values = new String[castedValues.length];
                //(String[]) xp.getValue();

                for (int i = 0; i < values.length; i++)
                {

                    values[i] = castedValues[i].toString();

                    if (!values[i].matches(SLOT_PATTERN_MULTIPLE))
                    {
                        values[i] = String.format("('%s')", values[i]);
                    }
                }

                adhocQuery.getSlot().add(RegistryStoredQueryUtil.createQuerySlot(xp.getParameterName(), values));
            } else
            {

                String value = xp.getValue().toString();

                if (!value.matches(SLOT_PATTERN_SINGLE))
                {
                    value = String.format("'%s'", value);
                }

                adhocQuery.getSlot().add(RegistryStoredQueryUtil.createQuerySlot(xp.getParameterName(), value));
            }
        }
    }

    public static void validateQuerySlots(XdsGuidMetaDataType<?> queryType, XdsQuerySpecification querySpec)
    {
        // Validate slots
        if (queryType.getRequiredSlots() != null)
        {
            for (String slot : queryType.getRequiredSlots())
            {
                if (!querySpec.containsExtendedParameterExist(slot))
                {
                    throw new IllegalArgumentException(String.format("Missing slot: %s", slot));
                }
            }
        }
    }

    /**
     * Parses association meta data from an association type.
     *
     * @param associationType Association type returned from the registry
     *                        response.
     * @return Populated association meta data.
     */
    public static AssociationMetaData parseAssociationMetaDataFromAssociationType(AssociationType1 associationType)
    {
        AssociationMetaData association = new AssociationMetaData();

        association.setHomeCommunityId(associationType.getHome());
        association.setId(associationType.getId());

        association.setSourceObject(associationType.getSourceObject());
        association.setTargetObject(associationType.getTargetObject());
        association.setAssociationType(associationType.getAssociationType());

        for (SlotType1 slot : associationType.getSlot())
        {
            if (slot.getName().equals("submissionSetStatus"))
            {
                association.setSubmissionSetStatus(slot.getValueList().getValue().get(0));
            }
        }

        return association;
    }

    /**
     * Parses folder meta data object from the supplied registry package.
     *
     * @param registryPackage
     * @return
     */
    public static FolderMetaData parseFolderMetaDataFromRegistryPackage(RegistryPackageType registryPackage)
    {
        FolderMetaData folder = new FolderMetaData();

        if (registryPackage.getName() != null)
        {
            folder.setTitle(registryPackage.getName().getLocalizedString().get(0).getValue());
        }

        if (registryPackage.getDescription() != null)
        {
            folder.setDescription(registryPackage.getDescription().getLocalizedString().get(0).getValue());
        }

        if (registryPackage.getVersionInfo() != null)
        {
            folder.setVersion(registryPackage.getVersionInfo().getVersionName());
        }


        folder.setId(registryPackage.getId());
        folder.setHomeCommunityId(registryPackage.getHome());

        // TODO: Populate remaining meta data.
        // External identifiers
        for (ExternalIdentifierType extern : registryPackage.getExternalIdentifier())
        {
            if (XdsGuidType.XDSFolder_UniqueId.equals(extern.getIdentificationScheme()))
            {
                folder.setUniqueId(extern.getValue());
            } else if (XdsGuidType.XDSFolder_PatientId.equals(extern.getIdentificationScheme()))
            {
                PersonDemographic patient = new PersonDemographic();
                patient.addIdentifier(parseIdentifierString(extern.getValue()));
                folder.setPatient(patient);
            } else if (extern.getName() != null && extern.getName().getLocalizedString().size() > 0)
            {
                //  retVal.addExtendedAttribute(extern.getName().getLocalizedString().get(0).getValue(), extern.getValue());
            }
        }

        return folder;
    }

    /**
     * Parses submission set meta data from a registry package.
     *
     * @param registryPackage RegistryPackageType returned by a registry
     *                        response.
     * @return Populated submission set meta data.
     * @throws XdsMessageParseException
     */
    public static SubmissionSetMetaData parseSubmissionSetMetaDataFromPackage(RegistryPackageType registryPackage) throws XdsMessageParseException
    {
        SubmissionSetMetaData submissionSet = new SubmissionSetMetaData();

        submissionSet.setHomeCommunityId(registryPackage.getHome());
        submissionSet.setId(registryPackage.getId());

        if (registryPackage.getVersionInfo() != null)
        {
            submissionSet.setVersion(registryPackage.getVersionInfo().getVersionName());
        }

        // TODO: Complete all potential slots.
        for (SlotType1 slot : registryPackage.getSlot())
        {
            if (slot.getName().equals("submissionTime"))
            {
                try
                {
                    submissionSet.setCreationTime(parseDate(slot.getValueList().getValue().get(0)));
                } catch (ParseException ex)
                {
                    throw new XdsMessageParseException(slot.getValueList().getValue().get(0), "There was an error parsing a slot date.", ex);
                }
            } else
            {
                //submissionSet.addExtendedAttribute(slot.getName(), slot.getValueList().getValue());
            }
        }

        // TODO: Classifications
        for (ClassificationType classification : registryPackage.getClassification())
        {
            if (classification.getClassifiedObject().equals(registryPackage.getId())) // Ensure we're classifying the correct object
            {
                // Validate that the slot has the correct number of elements
//                if (classification.getSlot().isEmpty())
//                {
//                    throw new XdsMessageParseException(classification, "Classification does not have minimum number of slots");
//                }

                if (XdsGuidType.XDSSubmissionSet_Author.equals(classification.getClassificationScheme()))
                {
                    for (SlotType1 slot : classification.getSlot())
                    {
                        if (slot.getName().equals("authorPerson"))
                        {
                        } else if (slot.getName().equals("authorInstitution"))
                        {
                        }
                    }
                } else if (XdsGuidType.XDSSubmissionSet_CodingScheme.equals(classification.getClassificationScheme()))
                {
                }

//                if (XdsGuidType.XDSDocumentEntry_ConfidentialityCode.equals(classification.getClassificationScheme())) {
//                    // TODO: FIX
//                    //retVal.setConfidentiality(getCodeValueFromClassification(classification));
//                } else if (XdsGuidType.XDSDocumentEntry_FormatCode.equals(classification.getClassificationScheme())) {
//                    retVal.setFormat(getCodeValueFromClassification(classification));
//                } else if (XdsGuidType.XDSDocumentEntry_TypeCode.equals(classification.getClassificationScheme())) {
//                    retVal.setType(getCodeValueFromClassification(classification));
//                } else if (XdsGuidType.XDSSubmissionSet_ContentType.equals(classification.getClassificationScheme())) {
//                    retVal.setContentType(getCodeValueFromClassification(classification));
//                } else if (XdsGuidType.XDSSubmissionSet_Author.equals(classification.getClassificationScheme())) {
//                    // authorPerson & authorInstitution
//                    // There is no author CodeValue property in DocumentMetaData (only in DocumentSubmissionMetaData)...
//                    // Will add all slots (authorPerson & authorInstitution) to the extendedAttributes Hashmap.
//                    for (SlotType1 slot : classification.getSlot()) {
//                        retVal.addExtendedAttribute(slot.getName(), slot.getValueList().getValue());
//                    }
//                }
            }
        }

        for (ExternalIdentifierType extern : registryPackage.getExternalIdentifier())
        {
            if (XdsGuidType.XDSSubmissionSet_UniqueId.equals(extern.getIdentificationScheme()))
            {
                //   submissionSet.setId(this.parseIdentifierString(extern.getValue()));
                submissionSet.setUniqueId(extern.getValue());
            } else if (XdsGuidType.XDSSubmissionSet_PatientId.equals(extern.getIdentificationScheme()))
            {
                PersonDemographic patient = new PersonDemographic();
                patient.addIdentifier(parseIdentifierString(extern.getValue()));
                submissionSet.setPatient(patient);
            } else if (XdsGuidType.XDSSubmissionSet_SourceId.equals(extern.getIdentificationScheme()))
            {
            } else if (XdsGuidType.XDSSubmissionSet_ContentType.equals(extern.getIdentificationScheme()))
            {
            } else if (XdsGuidType.XDSSubmissionSet_LimitedMetaData.equals(extern.getIdentificationScheme()))
            {
            } else if (extern.getName() != null && extern.getName().getLocalizedString().size() > 0)
            {
                // retVal.addExtendedAttribute(extern.getName().getLocalizedString().get(0).getValue(), extern.getValue());
            }
        }

        return submissionSet;
    }

    /**
     * Parses document meta data objects from the supplied extrinsic object
     *
     * @throws XdsMessageParseException
     */
    public static DocumentMetaData parseDocumentMetaDataFromExtrinsicObject(ExtrinsicObjectType object) throws XdsMessageParseException
    {
        DocumentMetaData document = new DocumentMetaData();

        // Set the title of the document
        if (object.getName() != null)
        {
            document.setTitle(object.getName().getLocalizedString().get(0).getValue());
        }

        if (object.getVersionInfo() != null)
        {
            document.setVersion(object.getVersionInfo().getVersionName());
        }

        document.setId(object.getId());
        document.setHomeCommunityId(object.getHome());

        // mime type
        document.setMimeType(object.getMimeType());

        // Slot data
        for (SlotType1 slot : object.getSlot())
        {
            try
            {
                if (slot.getName().equals("repositoryUniqueId"))
                {
                    document.setRepositoryUniqueId(slot.getValueList().getValue().get(0));
                } else if (slot.getName().equals("hash"))
                {
                    document.setHash(DatatypeConverter.parseBase64Binary(slot.getValueList().getValue().get(0)));
                } else if (slot.getName().equals("creationTime"))
                {
                    document.setCreationTime(parseDate(slot.getValueList().getValue().get(0)));
                } else if (slot.getName().equals("serviceStartTime"))
                {
                    document.setServiceTimeStart(parseDate(slot.getValueList().getValue().get(0)));
                } else if (slot.getName().equals("serviceStopTime"))
                {
                    document.setServiceTimeEnd(parseDate(slot.getValueList().getValue().get(0)));
                } else
                {
                    document.addExtendedAttribute(slot.getName(), slot.getValueList().getValue());
                }
            } catch (ParseException e)
            {
                throw new XdsMessageParseException(slot.getValueList().getValue().get(0), "There was an error parsing a slot date.", e);
            }
        }

        // TODO: Classifications
        for (ClassificationType classification : object.getClassification())
        {
            if (classification.getClassifiedObject().equals(object.getId())) // Ensure we're classifying the correct object
            {
                // Validate that the slot has the correct number of elements
                if (classification.getSlot().isEmpty())
                {
                    throw new XdsMessageParseException(classification, "Classification does not have minimum number of slots");
                }
                if (XdsGuidType.XDSDocumentEntry_ConfidentialityCode.equals(classification.getClassificationScheme()))
                {
                    document.getConfidentiality().add(getCodeValueFromClassification(classification));
                } else if (XdsGuidType.XDSDocumentEntry_FormatCode.equals(classification.getClassificationScheme()))
                {
                    document.setFormat(getCodeValueFromClassification(classification));
                } else if (XdsGuidType.XDSDocumentEntry_TypeCode.equals(classification.getClassificationScheme()))
                {
                    document.setType(getCodeValueFromClassification(classification));
                } else if (XdsGuidType.XDSSubmissionSet_ContentType.equals(classification.getClassificationScheme()))
                {
                    document.setContentType(getCodeValueFromClassification(classification));
                } else if (XdsGuidType.XDSDocumentEntry_Author.equals(classification.getClassificationScheme()))
                {
                    // authorPerson & authorInstitution
                    // There is no author CodeValue property in DocumentMetaData (only in DocumentSubmissionMetaData)...
                    // Will add all slots (authorPerson & authorInstitution) to the extendedAttributes Hashmap.
                    for (SlotType1 slot : classification.getSlot())
                    {
                        document.addExtendedAttribute(slot.getName(), slot.getValueList().getValue());
                    }
                }
            }
        }

        // External identifiers
        for (ExternalIdentifierType extern : object.getExternalIdentifier())
        {
            if (XdsGuidType.XDSDocumentEntry_UniqueId.equals(extern.getIdentificationScheme()))
            {
                document.setUniqueId(extern.getValue());
            } else if (XdsGuidMetaDataType.XDSDocumentEntry_PatientId.equals(extern.getIdentificationScheme()))
            {
                DomainIdentifier patientId = parseIdentifierString(extern.getValue());
                PersonDemographic patient = new PersonDemographic();
                patient.addIdentifier(patientId);

                document.setPatient(patient);
            } else if (extern.getName() != null && extern.getName().getLocalizedString().size() > 0)
            {
                document.addExtendedAttribute(extern.getName().getLocalizedString().get(0).getValue(), extern.getValue());
            }
        }

        return document;
    }

    /**
     * Parse a registry response and store it in a generic meta data structure.
     *
     * @param <>>      The type of value to parse.
     * @param response The response to parse the information from.
     * @param tClass   The class of the type of value to parse.
     * @return Array of the parsed data in the type requested.
     * @throws ParseException
     */
    public static <T extends MetaData> T[] parseMetaDataFromRegistryResponse(AdhocQueryResponse response, Class<?> tClass) throws XdsMessageParseException
    {
        // Retrieved folders and submission sets.
        ArrayList<RegistryPackageType> containers = new ArrayList<RegistryPackageType>();
        // Retrieved documents.
        HashMap<String, DocumentMetaData> documents = new HashMap<String, DocumentMetaData>();
        // Retrieved submission sets.
        HashMap<String, SubmissionSetMetaData> submissionSets = new HashMap<String, SubmissionSetMetaData>();
        // Retrived associations, there may be multiple associations to a single object.
        HashMap<String, ArrayList<AssociationMetaData>> associations = new HashMap<String, ArrayList<AssociationMetaData>>();
        // Retrieved external classifications.
        HashMap<String, ClassificationType> externalClassifications = new HashMap<String, ClassificationType>();
        // Retrieved folders.
        HashMap<String, FolderMetaData> folders = new HashMap<String, FolderMetaData>();

        // Parsed information in the requested meta data type.
        ArrayList<? super MetaData> parsedMetaData = new ArrayList<MetaData>();

        // Loop through all the registry objects returned.
        for (JAXBElement<? extends IdentifiableType> ident : response.getRegistryObjectList().getIdentifiable())
        {

            // Verify the result as a registry object
            if (ident.getValue() instanceof AssociationType1)
            {
                // Add all associations to the list. These will be parsed once all types are retrieved.
                AssociationMetaData association = parseAssociationMetaDataFromAssociationType((AssociationType1) ident.getValue());

                // An object may have multiple associations, create a new list or add it to an existing one for the object.
                if (associations.get(association.getSourceObject()) == null)
                {
                    ArrayList<AssociationMetaData> associationList = new ArrayList<AssociationMetaData>();
                    associationList.add(association);
                    associations.put(association.getSourceObject(), associationList);
                } else
                {
                    associations.get(association.getSourceObject()).add(association);
                }

            } else if (ident.getValue() instanceof ExtrinsicObjectType)
            {
                // Document
                DocumentMetaData document = parseDocumentMetaDataFromExtrinsicObject((ExtrinsicObjectType) ident.getValue());
                documents.put(ident.getValue().getId(), document);

            } else if (ident.getValue() instanceof RegistryPackageType)
            {
                // Folder or SubmissionSet
                containers.add((RegistryPackageType) ident.getValue());

            } else if (ident.getValue() instanceof ClassificationType)
            {
                // A classification type may be internally to the registrypackage or externally.
                externalClassifications.put(((ClassificationType) ident.getValue()).getClassifiedObject(), (ClassificationType) ident.getValue());
            }
        }

        // Parse submission sets and folders.
        for (RegistryPackageType container : containers)
        {
            boolean foundClassification = false;

            // Check inner classification
            for (ClassificationType innerClassification : container.getClassification())
            {

                if (innerClassification.getClassificationNode() == null ? XdsGuidType.XDSSubmissionSet.toString() == null : innerClassification.getClassificationNode().equals(XdsGuidType.XDSSubmissionSet.toString()))
                {
                    foundClassification = true;

                    SubmissionSetMetaData submissionSet = parseSubmissionSetMetaDataFromPackage(container);
                    submissionSets.put(container.getLid(), submissionSet);

                    break;
                } else if (innerClassification.getClassificationNode() == null ? XdsGuidType.XDSFolder.toString() == null : innerClassification.getClassificationNode().equals(XdsGuidType.XDSFolder.toString()))
                {
                    foundClassification = true;

                    FolderMetaData folder = parseFolderMetaDataFromRegistryPackage(container);
                    folders.put(container.getLid(), folder);

                    break;
                }
            }

            // If a classification was not found, check external classification objects.
            if (!foundClassification)
            {
                ClassificationType classification = externalClassifications.get(container.getLid());

                if (classification.getClassificationNode() == null ? XdsGuidType.XDSSubmissionSet.toString() == null : classification.getClassificationNode().equals(XdsGuidType.XDSSubmissionSet.toString()))
                {
                    SubmissionSetMetaData submissionSet = parseSubmissionSetMetaDataFromPackage(container);
                    submissionSets.put(container.getLid(), submissionSet);
                } else if (classification.getClassificationNode() == null ? XdsGuidType.XDSFolder.toString() == null : classification.getClassificationNode().equals(XdsGuidType.XDSFolder.toString()))
                {
                    FolderMetaData folder = parseFolderMetaDataFromRegistryPackage(container);
                    folders.put(container.getLid(), folder);
                } else
                {
                    LOGGER.info(String.format("Classification is null or unknown: {0}", classification.getClassificationNode()));
                    // TODO: Throw an exception?
                }

            }
        }

        if (FolderMetaData.class.isAssignableFrom(tClass))
        {
            // If T is folder meta data, return an array of folders and any documents they contain.
            for (Map.Entry<String, FolderMetaData> entry : folders.entrySet())
            {
                String key = entry.getKey();
                FolderMetaData folder = entry.getValue();

                if (associations.containsKey(key))
                {
                    for (AssociationMetaData association : associations.get(key))
                    {
                        folder.addDocument(documents.get(association.getTargetObject()));
                    }
                }

                parsedMetaData.add(folder);
            }

        } else if (DocumentMetaData.class.isAssignableFrom(tClass))
        {
            // If T is document meta data, return an array of just the documents.
            parsedMetaData.addAll(documents.values());

        } else if (SubmissionSetMetaData.class.isAssignableFrom(tClass))
        {
            // If T is submission set meta data, return the entire tree structure.
            // Build submission set structure.
            for (Map.Entry<String, SubmissionSetMetaData> entry : submissionSets.entrySet())
            {
                String key = entry.getKey();
                SubmissionSetMetaData submissionSet = entry.getValue();

                if (associations.containsKey(key))
                {
                    for (AssociationMetaData association : associations.get(key))
                    {
                        String targetId = association.getTargetObject();

                        // Check what type of association it is.
                        if (folders.containsKey(targetId))
                        {

                            FolderMetaData folder = folders.get(targetId);

                            // There are no nested folders, no need to parse recursively.
                            for (AssociationMetaData folderAssociation : associations.get(targetId))
                            {

                                // There will only ever be documents associated with a folder.
                                folder.addDocument(documents.get(folderAssociation.getTargetObject()));
                            }

                            // Add folder to submissionSet. 
                            submissionSet.addFolder(folder);

                        } else if (documents.containsKey(targetId))
                        {
                            submissionSet.addDocument(documents.get(targetId));
                        }
                    }
                }

                parsedMetaData.add(submissionSet);
            }
        } else if (AssociationMetaData.class.isAssignableFrom(tClass))
        {
            // If T is association meta data, return an array of just the associations.            
            for (ArrayList<AssociationMetaData> associationList : associations.values())
            {
                parsedMetaData.addAll(associationList);
            }

        } else if (MetaData.class.isAssignableFrom(tClass))
        {
            parsedMetaData.addAll(documents.values());
            parsedMetaData.addAll(folders.values());
            parsedMetaData.addAll(submissionSets.values());
            for (ArrayList<AssociationMetaData> associationList : associations.values())
            {
                parsedMetaData.addAll(associationList);
            }
        }

        return (T[]) parsedMetaData.toArray((T[]) Array.newInstance(tClass, parsedMetaData.size()));
    }

    public static String[] parseStatusTypes(ICodeType... statusTypes)
    {
        String[] statusSlots = new String[statusTypes.length];
        for (int i = 0; i < statusSlots.length; i++)
        {
            statusSlots[i] = statusTypes[i].getCode();
        }

        return statusSlots;
    }

    /**
     * Gets the specified named slot from the list of slots
     */
    public static CodeValue getCodeValueFromClassification(ClassificationType classification)
    {
        CodeValue retVal = new CodeValue();

        // First, lookup the classification scheme
        for (SlotType1 slot : classification.getSlot())
        {
            if (slot.getValueList().getValue().size() > 0)
            {
                if (slot.getName().equals("codingScheme"))
                {
                    retVal.setCodeSystem(slot.getValueList().getValue().get(0));
                }
            }
        }
        retVal.setCode(classification.getNodeRepresentation());
        return retVal;
    }

    /**
     * Parse a date
     *
     * @throws ParseException
     */
    public static Calendar parseDate(String date) throws ParseException
    {
        // Date format
        String dateFormatString = "yyyyMMddHHmmss.fff";
        Calendar retVal = Calendar.getInstance();

        // Now parse the date string
        if (date.length() > dateFormatString.length())
        {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
            retVal.setTime(sdf.parse(date.substring(0, dateFormatString.length() + (dateFormatString.contains("Z") ? 1 : 0))));
        } else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString.substring(0, date.length()));
            retVal.setTime(sdf.parse(date));
        }

        return retVal;
    }

    /**
     * Get an external identifier from an extrinsic object
     */
    public static DomainIdentifier parseIdentifierString(String identifier)
    {
        // Is this a CX type?
        Pattern cxPattern = Pattern.compile("^(\\d*)?[\\^]{3}&([0-9\\.]*)?&ISO$");
        Matcher matcher = cxPattern.matcher(identifier);
        if (matcher.find()) // It is a CX so extract data
        {
            return new DomainIdentifier(matcher.group(2), matcher.group(1));
        } else
        {
            return new DomainIdentifier(null, identifier);
        }
    }
}
