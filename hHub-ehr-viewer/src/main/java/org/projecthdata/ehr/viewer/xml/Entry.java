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
package org.projecthdata.ehr.viewer.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name="entry", strict=false)
public class Entry {
	@Attribute(required=false, name="term") @Path("category")
	String categoryTerm = null;
	
	@Attribute(name="href") @Path("link")
	String linkHref = null;
	
	@Attribute(name="type", required=false) @Path("link")
	String linkType = null;
	
	@Element(required=false)
	String title = null;
	@Element(required=false)
	String id = null;
	
	public String getCategoryTerm() {
		return categoryTerm;
	}
	public void setCategoryTerm(String categoryTerm) {
		this.categoryTerm = categoryTerm;
	}
	public String getLinkHref() {
		return linkHref;
	}
	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	
	
}
