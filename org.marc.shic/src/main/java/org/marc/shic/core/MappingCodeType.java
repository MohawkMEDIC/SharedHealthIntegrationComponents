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
 * Date: Feb 10, 2014
 *
 */
package org.marc.shic.core;

/**
 *
 * @author tylerg
 */
public enum MappingCodeType implements ICodeType {

    HealthcareFacilityTypeCode("f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", "healthcareFacilityTypeCode"),
    PracticeSettingCode("cccf5598-8b07-4b77-a05e-ae952c785ead", "practiceSettingCode"),
    TypeCode("f0306f51-975f-434e-a61c-c59651d33983", "typeCode"),
    FormatCode("a09d5840-386c-46f2-b5ad-9c3699a4309d", "formatCode"),
    ClassCode("41a5887f-8865-4c09-adf7-e362475b143a", "classCode"),
    ContentTypeCode("aa543740-bdda-424e-8c96-df4873be8500", "contentTypeCode"),
    EventCodeList("2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4", "eventCodeList"),
    FolderCodeList("1ba97051-7806-41a8-a48b-8fce7af683c5", "folderCodeList"),
    ConfidentialityCode("f4f85eac-e6cb-4883-b524-f2705394840f", "confidentialityCode");

    private String code;
    private String description;

    @Override
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("urn:uuid:%s", this.code);
    }

    private MappingCodeType(String code) {
        this.code = code;
    }

    private MappingCodeType(String code, String description) {
        this(code);
        this.description = description;
    }
}
