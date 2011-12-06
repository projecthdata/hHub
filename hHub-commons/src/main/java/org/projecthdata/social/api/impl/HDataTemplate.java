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

package org.projecthdata.social.api.impl;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.RootOperations;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;

public class HDataTemplate extends AbstractOAuth2ApiBinding implements HData {

	private RootOperations rootOperations = null;
	// url of the electronic health record
	private final String ehrUrl;

	public HDataTemplate(String accessToken, String ehrUrl) {
		super(accessToken);
		this.ehrUrl = ehrUrl;
		this.getRestTemplate().getMessageConverters()
				.add(new SimpleXmlHttpMessageConverter());
	}

	@Override
	public String getEhrUrl() {
		return this.ehrUrl;
	}

	public RootOperations getRootOperations() {
		if (this.rootOperations == null) {
			rootOperations = new RootTemplate(this, this.getRestTemplate(),
					isAuthorized());
		}
		return rootOperations;
	}

	@Override
	protected OAuth2Version getOAuth2Version() {
		return OAuth2Version.DRAFT_10;
	}

}
