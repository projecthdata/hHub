/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projecthdata.social.api.connect;

import org.projecthdata.social.api.HData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

/**
 * From: http://static.springsource.org/spring-social/docs/1.0.x/reference/html/
 * implementing.html Provides a simple interface for establishing Connections.
 * May be user directly, or added to a registry where it can be used by the
 * framework to establish connections in a dynamic, self-service manner.
 */
public class HDataConnectionFactory extends OAuth2ConnectionFactory<HData> {
	private String clientSecret;
	private String clientId;

	/**
	 * 
	 * @param clientId
	 * @param clientSecret
	 * @param ehrUrl
	 *            - The EHR URL, also used as the providerId to uniquely
	 *            identify the HDataServiceProvider
	 */
	public HDataConnectionFactory(String clientId, String clientSecret,
			String ehrUrl) {
		super(ehrUrl, new HDataServiceProvider(clientId, clientSecret, ehrUrl),
				new HDataAdapter());
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Override
	public OAuth2Operations getOAuthOperations() {
		return super.getOAuthOperations();
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
