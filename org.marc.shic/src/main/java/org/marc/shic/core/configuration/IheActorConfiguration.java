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
 * 
 * Date: October 29, 2013
 * 
 */
package org.marc.shic.core.configuration;

import java.io.Serializable;
import java.net.URI;

/**
 * Represents a given IHE actor in an affinity domain.
 * 
 * @author Nebri
 */
public class IheActorConfiguration implements Serializable {

	private String oid;
	private String name;
	private IheActorType actorType;
	private URI endPointAddress;
	private Boolean secure;
	private IheIdentification remoteIdentification;
	
	// TODO The following will be deprecated
	private JKSCertificateInformation clientCertificateInfo;
	private JKSCertificateInformation serverCertificateInfo;
	private IheIdentification localIdentification;
	private IheActorOptionsConfiguration options;
	private Boolean binaryOnly;

	public IheActorConfiguration() {

	}

	public IheActorConfiguration(String name, boolean binaryOnly) {
		this("", name, binaryOnly, "", null, null, null, null);
	}

	public IheActorConfiguration(String actorType, String name,
			boolean binaryOnly, String endpointAddress) {
		this(IheActorType.fromString(actorType), name, binaryOnly,
				endpointAddress, null, null, null, null);
	}

	public IheActorConfiguration(IheActorType actorType, String name,
			boolean binaryOnly, String endpointAddress) {
		this(actorType, name, binaryOnly, endpointAddress, null, null, null,
				null);
	}

	public IheActorConfiguration(String actorType, String name,
			boolean binaryOnly, String endpointAddress,
			String localIdentification, String remoteIdentification,
			String clientCertificateInfo, String serverCertificateInfo) {
		this(IheActorType.fromString(actorType), name, binaryOnly,
				endpointAddress, localIdentification, remoteIdentification,
				serverCertificateInfo, clientCertificateInfo);
	}

	public IheActorConfiguration(IheActorType actorType, String name,
			boolean binaryOnly, String endpointAddress,
			String localIdentification, String remoteIdentification,
			String clientCertificateInfo, String serverCertificateInfo) {
		this.actorType = actorType;
		this.name = name;
		this.binaryOnly = binaryOnly;
		this.endPointAddress = URI.create(endpointAddress);
		this.localIdentification = new IheIdentification(localIdentification);
		this.remoteIdentification = new IheIdentification(remoteIdentification);
		this.serverCertificateInfo = new JKSCertificateInformation();
		this.clientCertificateInfo = new JKSCertificateInformation();
	}

	@Deprecated
	public IheActorOptionsConfiguration getOptions() {
		return options;
	}

	@Deprecated
	public void setOptions(IheActorOptionsConfiguration options) {
		this.options = options;
	}

	/**
	 * @return the actorType
	 */
	public IheActorType getActorType() {
		return actorType;
	}

	/**
	 * @param actorType
	 *            the actorType to set
	 */
	public void setActorType(IheActorType actorType) {
		this.actorType = actorType;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOid() {
		return oid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	public Boolean isSecure() {
		return secure;
	}

	@Deprecated
	public void setBinaryOnly(Boolean binaryOnly) {
		this.binaryOnly = binaryOnly;
	}

	@Deprecated
	public Boolean isBinaryOnly() {
		return binaryOnly;
	}

	@Deprecated
	public Boolean getBinaryOnly() {
		return binaryOnly;
	}

	public void setEndPointAddress(URI endPointAddress) {
		this.endPointAddress = endPointAddress;
	}

	public URI getEndPointAddress() {
		return endPointAddress;
	}

	@Deprecated
	public JKSCertificateInformation getClientCertificateInfo() {
		return clientCertificateInfo;
	}

	@Deprecated
	public void setClientCertificateInfo(
			JKSCertificateInformation clientCertificateInfo) {
		this.clientCertificateInfo = clientCertificateInfo;
	}

	@Deprecated
	public JKSCertificateInformation getServerCertificateInfo() {
		return serverCertificateInfo;
	}

	@Deprecated
	public void setServerCertificateInfo(
			JKSCertificateInformation serverCertificateInfo) {
		this.serverCertificateInfo = serverCertificateInfo;
	}

	public IheIdentification getRemoteIdentification() {
		return remoteIdentification;
	}

	public void setRemoteIdentification(IheIdentification remoteIdentification) {
		this.remoteIdentification = remoteIdentification;
	}

	@Deprecated
	public IheIdentification getLocalIdentification() {
		return localIdentification;
	}

	@Deprecated
	public void setLocalIdentification(IheIdentification localIdentification) {
		this.localIdentification = localIdentification;
	}
}
