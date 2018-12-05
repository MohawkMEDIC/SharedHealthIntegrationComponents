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
 * @since 7-Feb-2014
 *
 */
package org.marc.shic.cda.datatypes;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.CV;
import org.marc.everest.datatypes.generic.SET;
import org.marc.shic.core.CodeValue;

/**
 * Provides a level of abstraction against an everest CD (Context Descriptor)
 * object, which provides various methods to be able to convert between
 * different code types and set common values.
 *
 * @author Ryan Albert
 * @param <T> The type of value that is to be used for the Code.
 */
//@Structure(name = "CD", structureType = StructureType.DATATYPE, defaultTemplateType = String.class)
public final class Code<T> {

	private static final Logger LOGGER = Logger.getLogger(Code.class);

	CD<T> codeCD;

	/**
	 * Creates a null Code with no values set.
	 */
	public Code() {
		codeCD = new CD();
		setCode(null);
	}

	/**
	 * Creates a code with a code value set to the one specified.
	 *
	 * @param code The object to set the code value with.
	 */
	public Code(T code) {
		this(code, null, null, null);
	}

	/**
	 * Creates a code with the code value and code system set.
	 *
	 * @param code The object to set the code value with.
	 * @param codeSystem A String that contains an OID representing the code
	 * system used.
	 */
	public Code(T code, String codeSystem) {
		this(code, codeSystem, null, null);
	}

	/**
	 * Creates a code with the code value, code system, code display name,
	 * and code system name to set.
	 *
	 * @param code The object to set the code value with.
	 * @param codeSystem A String that contains an OID representing the code
	 * system used.
	 * @param codeName The display name of the code value to set to.
	 * @param codeSystemName The display name of the code system set.
	 */
	public Code(T code, String codeSystem, String codeName, String codeSystemName) {
		this();
		setCode(code);
		setCodeSystem(codeSystem);
		setDisplayName(codeName);
		setCodeSystemName(codeSystemName);
	}

	/**
	 * Creates a code from an existing code and copies all of the values.
	 *
	 * @param copy An existing Code object to create a copy from.
	 */
	public Code(Code copy) {
		codeCD = (CD<T>) copy(copy.codeCD, new CD());
	}

	/**
	 * Creates and wraps a code from an existing everest code object
	 * (CS/CV/CE/CD)
	 *
	 * @param <R> The type of everest code to copy.
	 * @param copy The everest code object deriving from CV to copy.
	 */
	public <R extends CS> Code(R copy) {
		this();
		if (copy != null) {
			copy(copy, codeCD);
		}
	}

	/**
	 * Creates a code from a standard SHIC CodeValue object.
	 *
	 * @param cv
	 */
	public Code(CodeValue cv) {
		this();
		if (cv != null) {
			setCode((T) cv.getCode());
			setCodeSystem(cv.getCodeSystem());
			setCodeSystemName(cv.getCodeSystemName());
			setDisplayName(cv.getDisplayName());
		}
	}

	/**
	 * Gets an Everest code object with the specified code type.
	 *
	 * @param <T> The type of everest code object.
	 * @param codeClass The class object of the resulting code.
	 * @return An everest code object of the specified code class.
	 */
	public <T extends CS> T getCode(Class<T> codeClass) {
		CS result = null;
		if (codeClass == CS.class) {
			result = new CS();
		} else if (codeClass == CV.class) {
			result = new CV();
		} else if (codeClass == CE.class) {
			result = new CE();
		} else if (codeClass == CD.class) {
			result = new CD();
		}
		copy(codeCD, result);
		return (T) result;
	}

	/**
	 * Gets a SHIC CodeValue from the code.
	 *
	 * @return A CodeValue
	 */
	public CodeValue getCodeValue() {
		CodeValue result = new CodeValue();
		result.setCode(String.format("%s", getCode()));
		result.setCodeSystem(getCodeSystem());
		result.setCodeSystemName(getCodeSystemName());
		result.setDisplayName(getDisplayName());

		return result;
	}

	public Code ensureSystem(String codeSystem) {
		return ensureSystem(codeSystem, null);
	}

	public Code ensureSystem(String codeSystem, String codeSystemName) {
		return translate(this, codeSystem, codeSystemName);
	}

	/**
	 * Copies one everest code to another everest code.
	 *
	 * @param src The code information to copy from
	 * @param dst The code to copy information to.
	 * @return The dst code
	 */
	public static CS copy(CS src, CS dst) {
		dst.setCode(src.getCode());
		dst.setNullFlavor(src.getNullFlavor());
		if (src instanceof CV && dst instanceof CV) {
			CV dstCv = (CV) dst;
			CV srcCv = (CV) src;
			dstCv.setCodeSystem(srcCv.getCodeSystem());
			dstCv.setCodeSystemName(srcCv.getCodeSystemName());
			dstCv.setCodeSystemVersion(srcCv.getCodeSystemVersion());
			dstCv.setDisplayName(srcCv.getDisplayName());
			dstCv.setOriginalText(srcCv.getOriginalText());
			if (src instanceof CE && dst instanceof CE) {
				CE dstCE = (CE) dst;
				CE srcCE = (CE) src;
				dstCE.setTranslation(srcCE.getTranslation());
				if (src instanceof CD && dst instanceof CD) {
					CD dstCD = (CD) dst;
					CD srcCD = (CD) src;
					dstCD.setTranslation(srcCD.getTranslation());
				}
			}
		}

		return dst;
	}

	/**
	 * Searches for a code in a specified code system. Current functionality
	 * sets the code system and original text of what is sought.
	 *
	 * @param codeName The code display name to search for.
	 * @param codeSystem The code system to search in.
	 * @return A Code object that best represents the given parameters.
	 */
	public static Code search(String codeName, String codeSystem) {
		Code result = new Code(null, codeSystem);
		result.setOriginalText(codeName);
		return result;
	}

	/**
	 * Searches for a code in all available code systems. Current
	 * functionality sets the original text of what is sought.
	 *
	 * @param codeName The code display name to search for.
	 * @return A Code object that best represents the provided code display
	 * name.
	 */
	public static Code search(String codeName) {
		return search(codeName, null);
	}

	/**
	 * Translates a given code to a specific code system. If the code does
	 * not exist, or was not found in the target code system, the resulting
	 * code value will be "UNK". If the code system given does not exist,
	 * and the name is not able to be distinguished, then the codeSystemName
	 * parameter will be used to fill this space in (if not null). Otherwise
	 * if it is null, it will use the unknown code system name constant
	 * defined in this class.
	 *
	 * @param originalCode The Code to base translation on.
	 * @param codeSystem The code system to convert the Code to.
	 * @param codeSystemName The name of the code system (optional).
	 * @return The resulting translated Code.
	 */
	public static Code translate(Code originalCode, String codeSystem, String codeSystemName) {
		Code result = originalCode;
		if (originalCode.getCodeSystem() == null || !originalCode.getCodeSystem().equals(codeSystem)) {
			result = new Code(null, codeSystem, null, codeSystemName);
			result.setOriginalText(originalCode.getOriginalText());

			Object transCode = originalCode.getCode();
			if (transCode != null) {
				result.setOriginalText((String) transCode);
				result.addTranslation(new Code(originalCode));
			}
		}
		return result;
	}

	/**
	 * Creates an unknown code with an unknown code system.
	 *
	 * @return A code set to null
	 */
	public static Code unknown() {
		return new Code();
	}

	/**
	 * Creates a non-existing code with text that describes what the code
	 * would have been if provided.
	 *
	 * @param flavor The NullFlavor to set to.
	 * @param originalText The text that describes the code.
	 * @return A Code with the set NullFlavor and text.
	 */
	public static Code unavailable(NullFlavor flavor, String originalText) {
		Code result = unavailable(flavor);
		result.setOriginalText(originalText);

		return result;
	}

	/**
	 * Creates a code with a specific null flavor.
	 *
	 * @param flavor The NullFlavor to set to.
	 * @return A Code with the set NullFlavor.
	 */
	public static Code unavailable(NullFlavor flavor) {
		Code result = new Code();
		result.setNullFlavor(flavor);

		return result;
	}

	/**
	 * Creates a code with a null flavor that means that no information can
	 * be provided.
	 *
	 * @return A Code with the set default NullFlavor.
	 */
	public static Code unavailable() {
		Code result = new Code();
		result.setNullFlavor(NullFlavor.Other);

		return result;
	}

	@Deprecated
	public static Code nullCode(NullFlavor flavor) {
		return unavailable(flavor);
	}

	/**
	 * Generates a set of Code objects that represent all of the code
	 * translations set upon this Code.
	 *
	 * @return A set of Code objects if there are translations. null will be
	 * returned if there are none.
	 */
	public Set<Code> getTranslation() {
		SET<CD<T>> cdTranslations = codeCD.getTranslation();

		if (cdTranslations != null) {
			Set<Code> codeTranslations = new HashSet();
			for (CD trans : cdTranslations) {
				codeTranslations.add(new Code(trans));
			}
			return codeTranslations;
		}
		return null;
	}

	/**
	 * Adds a translated code value to the code.
	 *
	 * @param translation The Code translation.
	 */
	public void addTranslation(Code translation) {
		if (codeCD.getTranslation() == null) {
			codeCD.setTranslation(new SET());
		}
		codeCD.getTranslation().add(translation.codeCD);
	}

	/**
	 * Gets the display name unique to the set code.
	 *
	 * @return A String object.
	 */
	public String getDisplayName() {
		return codeCD.getDisplayName();
	}

	/**
	 * Sets the display name unique to the set code.
	 *
	 * @param value A String object.
	 */
	public void setDisplayName(String value) {
		codeCD.setDisplayName(value);
	}

	/**
	 * Gets the text that describes the Code.
	 *
	 * @return A String representation of the description.
	 */
	public String getOriginalText() {
		ED data = codeCD.getOriginalText();

		if (data != null && data.getData() != null) {
			return new String(data.getData());
		}
		return null;
	}

	/**
	 * Sets the text that describes the Code.
	 *
	 * @param value A String representation of the description.
	 */
	public void setOriginalText(String value) {
		if (value != null) {
			codeCD.setOriginalText(new ED(value));
		} else {
			codeCD.setOriginalText((ED) null);
		}
	}

	/**
	 * Gets the code system the code represents.
	 *
	 * @return A String representation of the code system.
	 */
	public String getCodeSystem() {
		return codeCD.getCodeSystem();
	}

	/**
	 * Sets the code system the code represents.
	 *
	 * @param value A String representation of the code system.
	 */
	public void setCodeSystem(String value) {
		codeCD.setCodeSystem(value);
	}

	/**
	 * Gets the display name of the code system the code represents.
	 *
	 * @return A String representation of the code system name.
	 */
	public String getCodeSystemName() {
		return codeCD.getCodeSystemName();
	}

	/**
	 * Sets the display name of the code system the code represents.
	 *
	 * @param value A String representation of the code system name.
	 */
	public void setCodeSystemName(String value) {
		codeCD.setCodeSystemName(value);
	}

	/**
	 * Gets the version of the code system the code represents.
	 *
	 * @return A String representation of the version of the code system.
	 */
	public String getCodeSystemVersion() {
		return codeCD.getCodeSystemVersion();
	}

	/**
	 * Sets the version of the code system the code represents.
	 *
	 * @param value A String representation of the version of the code
	 * system.
	 */
	public void setCodeSystemVersion(String value) {
		codeCD.setCodeSystemVersion(value);
	}

	/**
	 * Gets the code value set to the code.
	 *
	 * @return An object of type T that is set as the code.
	 */
	public T getCode() {
		return codeCD.getCode();
	}

	/**
	 * Sets the value of the code to the object specified.
	 *
	 * @param value An object of type T that is set as the value of the
	 * code.
	 */
	public void setCode(T value) {
		if (value == null) {
			setNullFlavor(NullFlavor.Other);
		} else {
			codeCD.setNullFlavor((CS) null);
		}
		codeCD.setCode(value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[");
		if (getCode() != null) {
			result.append("code:");
			result.append(getCode());
			result.append(' ');
		}
		if (getCodeSystem() != null) {
			result.append("system:");
			result.append(getCodeSystem());
			result.append(' ');
		}
		if (getDisplayName() != null) {
			result.append("name:");
			result.append(getDisplayName());
			result.append(' ');
		}
		if (getCodeSystemName() != null) {
			result.append("sysName:");
			result.append(getCodeSystemName());
			result.append(' ');
		}
		return result.toString();
	}

	/**
	 * Generates a hash code value based on the wrapped everest code object.
	 *
	 * @return A hash code integer.
	 */
	@Override
	public int hashCode() {
		return codeCD.hashCode();
	}

	/**
	 * Checks whether or not the code is equal with another code (or object)
	 *
	 * @param obj The object to compare the code to.
	 * @return True if the compared object is a Code and contains equal
	 * values. False otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			return codeCD.equals(((Code) obj).codeCD);
		}
		return false;
	}

	/**
	 * Gets the null flavor set to the Code.
	 *
	 * @return A NullFlavor set to the code. null if no NullFlavor has been
	 * set.
	 */
	public NullFlavor getNullFlavor() {
		CS<NullFlavor> nullCode = codeCD.getNullFlavor();
		if (nullCode != null) {
			return nullCode.getCode();
		}
		return null;
	}

	/**
	 * Sets the null flavor of the code to the one specified.
	 *
	 * @param value A NullFlavor to set the code to. A null value will
	 * remove the NullFlavor.
	 */
	public void setNullFlavor(NullFlavor value) {
		codeCD.setNullFlavor(value);
	}

	/**
	 * Checks whether or not the code is null, which means that no code has
	 * been set, and a NullFlavor has been set.
	 *
	 * @return A value indicating the presence of a NullFlavor.
	 */
	public boolean isNull() {
		return codeCD.isNull();
	}

}
