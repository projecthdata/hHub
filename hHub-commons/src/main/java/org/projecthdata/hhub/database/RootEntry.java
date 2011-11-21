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

import org.projecthdata.social.api.Section;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "root_entries")
public class RootEntry {
    @DatabaseField(generatedId = true)
    private int _id;

    /**
     * Unique identifier for the extension. Could be a URL to it's schema *
     */
    @DatabaseField
    private String extension = null;
    /**
     * A local identifier for this extension, which is unique within the
     * root document
     */
    @DatabaseField
    private int extensionId;

    @DatabaseField
    private String contentType;

    /**
     * relative path to this section
     */
    @DatabaseField
    private String path;
    
    @ForeignCollectionField(eager=true)
    private ForeignCollection<SectionDocMetadata> sectionMetadata =null;
    
    
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(int extensionId) {
        this.extensionId = extensionId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

	public ForeignCollection<SectionDocMetadata> getSectionMetadata() {
		return sectionMetadata;
	}

	public void setSectionMetadata(
			ForeignCollection<SectionDocMetadata> sectionMetadata) {
		this.sectionMetadata = sectionMetadata;
	}
	
	public void copy(Section section){
		this.setContentType(section.getExtension()
				.getContentType());
		this.setPath(section.getPath());
		this.setExtension(section.getExtension().getContent());
	}
}
