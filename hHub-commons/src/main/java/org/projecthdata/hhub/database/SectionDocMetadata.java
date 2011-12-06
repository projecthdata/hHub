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

package org.projecthdata.hhub.database;

import org.projecthdata.social.api.atom.AtomFeed;
import org.projecthdata.social.api.atom.Entry;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SectionDocMetadata {
	@DatabaseField(generatedId = true)
	private int _id;
	@DatabaseField
	private String link;
	@DatabaseField
	private String updated;
	@DatabaseField
	private String contentType;
	@DatabaseField
	private String title;
	@DatabaseField
	private String extension;
	@DatabaseField(canBeNull = false, foreign = true)
	private RootEntry rootEntry = null;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public RootEntry getRootEntry() {
		return rootEntry;
	}

	public void setRootEntry(RootEntry rootEntry) {
		this.rootEntry = rootEntry;
	}

	public void copy(Entry atomFeedEntry) {
		this.setLink(atomFeedEntry.getLinkHref());
		this.setContentType(atomFeedEntry.getLinkType());
		this.setTitle(atomFeedEntry.getTitle());
		this.setUpdated(atomFeedEntry.getUpdated());
	}

}
