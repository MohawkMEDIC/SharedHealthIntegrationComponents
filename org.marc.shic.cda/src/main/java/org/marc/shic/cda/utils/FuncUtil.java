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
 * @since 2-Apr-2014
 *
 */

package org.marc.shic.cda.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.ListUtils;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.TEL;
import org.marc.shic.cda.datatypes.Address;
import org.marc.shic.cda.everestfunc.Addressable;
import org.marc.shic.cda.everestfunc.Codeable;
import org.marc.shic.cda.everestfunc.Identifiable;
import org.marc.shic.cda.everestfunc.Nameable;
import org.marc.shic.cda.everestfunc.Telecommunicable;
import org.marc.shic.cda.everestfunc.Timeable;

/**
 *
 * @author Ryan Albert
 */
public class FuncUtil {
	
	public static void copy(Object src, Object dst) {
		for(Class dataInterface : getDataCompatability(src, dst)) {
			if(dataInterface == Timeable.class) {
				copyTime((Timeable)src, (Timeable) dst);
			} else if(dataInterface == Addressable.class) {
				copyAddress((Addressable)src, (Addressable)dst);
			} else if(dataInterface == Codeable.class) {
				copyCode((Codeable)src, (Codeable)dst);
			} else if(dataInterface == Identifiable.class) {
				copyId((Identifiable)src, (Identifiable)dst);
			} else if(dataInterface == Nameable.class) {
				copyName((Nameable)src, (Nameable)dst);
			} else if(dataInterface == Telecommunicable.class) {
				copyTelecom((Telecommunicable)src, (Telecommunicable)dst);
			}
		}
	}
	
	private static List<Class> getDataCompatability(Object src, Object dst) {		
		return ListUtils.intersection(getAllInterfaces(src), getAllInterfaces(dst));
	}
	
	private static ArrayList<Class> getAllInterfaces(Object obj) {
		ArrayList result = new ArrayList();
		Class superClass = obj.getClass();
		while(superClass != null) {
			for(Class dataInterface : superClass.getInterfaces()) {
				if(dataInterface == Addressable.class ||
					dataInterface == Codeable.class ||
					dataInterface == Identifiable.class ||
					dataInterface == Nameable.class ||
					dataInterface == Telecommunicable.class ||
					dataInterface == Timeable.class) {
					result.add(dataInterface);
				}
			}
			superClass = superClass.getSuperclass();
		}
		return result;
	}
	
	public static void copyAddress(Addressable src, Addressable dst) {
		Set<Address> addresses = src.getAddresses();
		if (addresses != null && addresses.size() > 0) {
			for (Address addr : addresses) {
				dst.addAddress(addr);
			}
		}
	}
	
	public static void copyTime(Timeable src, Timeable dst) {
		dst.setTime(src.getTime());
	}
	
	public static void copyCode(Codeable src, Codeable dst) {
		dst.setCode(src.getCode());
	}
	
	public static void copyTelecom(Telecommunicable src, Telecommunicable dst) {
		Set<TEL> telecoms = src.getTelecoms();
		if (telecoms != null && telecoms.size() > 0) {
			for (TEL telecom : telecoms) {
				dst.addTelecom(telecom);
			}
		}
	}
	
	public static void copyId(Identifiable src, Identifiable dst) {
		Set<II> ids = src.getIds();
		if (ids != null && ids.size() > 0) {
			for (II id : ids) {
				dst.addId(new II(id.getRoot(), id.getExtension()));
			}
		}
	}

	public static void copyName(Nameable src, Nameable dst) {
		Set<EN> names = src.getNames();
		if (names != null && names.size() > 0) {
			for (EN en : names) {
				dst.addName(en);
			}
		}
	}
}
