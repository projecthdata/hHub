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
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * This is currently not relevant to hData, but required by the Spring Social
 * framework:
 * 
 * From http://static.springsource.org/spring-social/docs/1.0.x/reference/html/
 * implementing.html: The role of the ApiAdapter is to map a provider's native
 * API interface onto this uniform Connection model. A connection delegates to
 * its adapter to perform operations such as testing the validity of its API
 * credentials, setting metadata values, fetching a user profile, and updating
 * user status
 */
public class HDataAdapter implements ApiAdapter<HData> {
	public boolean test(HData hData) {
		return true; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	public void setConnectionValues(HData hData,
			ConnectionValues connectionValues) {

	}

	public UserProfile fetchUserProfile(HData hData) {
		return UserProfile.EMPTY;
	}

	public void updateStatus(HData hData, String s) {

	}
}
