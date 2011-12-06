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

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HDataOAuth2Template extends OAuth2Template {
	private RestTemplate restTemplate = null;

	public HDataOAuth2Template(String clientId, String clientSecret,
			String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
	}

	public HDataOAuth2Template(String clientId, String clientSecret,
			String authorizeUrl, String authenticateUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, authenticateUrl,
				accessTokenUrl);
	}

	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl,
			MultiValueMap<String, String> parameters) {
		return this.extractAccessGrant(getRestTemplate().postForObject(
				accessTokenUrl, parameters, Map.class));
	}

	private AccessGrant extractAccessGrant(Map<String, Object> result) {
		return new AccessGrant((String) result.get("access_token"));
		// TODO: this fixes a bug where the original code will throw an
		// exception when the expires_in is not a JSON
		// integer. Uncomment this when the server begins using the expires time
		// Integer expiresIn =
		// Integer.parseInt(result.get("expires_in").toString());
		// return createAccessGrant((String) result.get("access_token"),
		// (String) result.get("scope"), (String) result.get("refresh_token"),
		// expiresIn, result);
	}
}
