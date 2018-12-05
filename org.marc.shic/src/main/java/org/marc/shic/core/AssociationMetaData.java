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
 * @author tylerg
 * Date: Aug 21, 2013
 * 
 */
package org.marc.shic.core;

/**
 * 
 * @author tylerg
 */
public class AssociationMetaData extends MetaData {

	private String targetObject;
	private String sourceObject;
	private String associationType;
	private String submissionSetStatus;

	public AssociationMetaData() {
	}

	public AssociationMetaData(String targetObject, String sourceObject) {
		this.targetObject = targetObject;
		this.sourceObject = sourceObject;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(String sourceObject) {
		this.sourceObject = sourceObject;
	}

	public String getAssociationType() {
		return associationType;
	}

	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}

	public String getSubmissionSetStatus() {
		return submissionSetStatus;
	}

	public void setSubmissionSetStatus(String submissionSetStatus) {
		this.submissionSetStatus = submissionSetStatus;
	}

}
