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
 * @author brownp
 * Date: Sep 10, 2013
 * 
 */

package org.marc.shic.cda.utils;

import org.marc.everest.datatypes.generic.CE;
import org.marc.shic.core.CodeValue;


public interface ICodeable {
    
    public String getCode();
    public String getDisplayName();
    public String getCodeSystem();
    public String getCodeSystemName();
    public CE getCodedElement();
    public CodeValue getCodeValue();
    
}
