/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Garrett
 * Date: Nov 17, 2013
 *
 */
package org.marc.shic.xds.parameters;


/**
 *
 * @author Garrett
 */
public abstract class XdsParameter<T> {
    protected T parameterValue;

    public T getParameterValue()
    {
        return parameterValue;
    }

    public void setParameterValue(T parameterValue)
    {
        this.parameterValue = parameterValue;
    }
    
    public abstract String getParameterName();
    
    protected XdsParameter(T parameterValue) {
        this.parameterValue = parameterValue;
    }
}
