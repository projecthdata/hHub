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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.api.Section;
import org.projecthdata.social.api.atom.AtomFeed;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RootTemplate extends AbstractHDataOperations implements
		RootOperations {
	private final RestTemplate restTemplate;
	private final HData hData;
	private HttpEntity<?> atomFeedRequestEntity = null;
	private URI ehrUri = null;
	private static final String ROOT_PATH = "root.xml";

	public RootTemplate(HData hData, RestTemplate restTemplate,
			boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.restTemplate = restTemplate;
		this.hData = hData;
		this.ehrUri = URI.create(hData.getEhrUrl());

		// setup the correct accept headers for processing an atom feed
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		acceptableMediaTypes.add(MediaType.APPLICATION_XML);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(acceptableMediaTypes);

		this.atomFeedRequestEntity = new HttpEntity<Object>(requestHeaders);

	}

	public Root getRoot() {
		String url = ehrUri.toString() + "/" + ROOT_PATH;
		return restTemplate.getForObject(url, Root.class);
	}

	/**
	 * Retrieves the atom feed for a given section
	 * 
	 * @param section
	 * @return
	 */
	public AtomFeed getSectionFeed(Section section) {
		String url = ehrUri.toString() + "/" + section.getPath();
		return getForAtomFeed(url);
	}

	/**
	 * Adds the correct accept header for a GET request of an atom feed
	 * 
	 * @param url
	 *            - the URL of the feed to request
	 * @return
	 */
	public AtomFeed getForAtomFeed(String url) {
		ResponseEntity<AtomFeed> response = restTemplate.exchange(url,
				HttpMethod.GET, atomFeedRequestEntity, AtomFeed.class);
		AtomFeed feed = null;
		try {
			feed = response.getBody();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return feed;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
