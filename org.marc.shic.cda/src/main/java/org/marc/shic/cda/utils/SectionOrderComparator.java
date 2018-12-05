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
 * @since 14-Mar-2014
 *
 */
package org.marc.shic.cda.utils;

import java.util.Comparator;
import org.apache.log4j.Logger;
import org.marc.shic.cda.templates.SectionTemplate;

/**
 * 
 * @author Ryan Albert
 */
public class SectionOrderComparator implements Comparator<SectionTemplate> {

	private static final SectionOrderComparator instance = new SectionOrderComparator();
	private static final Logger LOGGER = Logger.getLogger(SectionOrderComparator.class);

	public static SectionOrderComparator getInstance() {
		return instance;
	}

	@Override
	public int compare(SectionTemplate o1, SectionTemplate o2) {
		int section1Order = o1.getSectionOrder();
		int section2Order = o2.getSectionOrder();
		if (section1Order < section2Order) {
			return -1;
		} else if (section1Order > section2Order) {
			return 1;
		}
		return 0;
	}

}
