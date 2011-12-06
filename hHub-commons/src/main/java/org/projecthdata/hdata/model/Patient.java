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
package org.projecthdata.hdata.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import android.net.Uri;

@Root(name = "patient", strict = false)
public class Patient {

	@Element
	public String id;

	@Element(name = "given")
	@Path("name")
	public String givenName;

	@Element
	@Path("name")
	public String lastname;

	@Element(required = false)
	@Path("name")
	public String suffix;

	private String documentUri = null;
	private String hStoreId = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getDocumentUri() {
		return documentUri;
	}

	public void setDocumentUri(String documentUri) {
		this.documentUri = documentUri;
		try {
			Uri document = Uri.parse(documentUri);
			this.hStoreId = document.getPathSegments().get(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String gethStoreId() {
		return hStoreId;
	}

	public void sethStoreId(String hStoreId) {
		this.hStoreId = hStoreId;
	}

}
