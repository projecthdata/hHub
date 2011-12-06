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

package org.projecthdata.hhub.provider;

public class SectionDocMetadataColumns extends AbstractColumns {

	public static final String LINK = "link";
	public static final String UPDATED = "updated";
	public static final String CONTENT_TYPE = "content_type";
	public static final String TITLE = "title";
	public static final String HRF_ID = "hrf_id";
	public static final String ROOT_ENTRIES_ID = "root_entries_id";
	public static final String EXTENSION = "extension";

	private static final String[] ALL_COLUMNS_PROJECTION = { _ID, LINK,
			UPDATED, CONTENT_TYPE, TITLE, HRF_ID, ROOT_ENTRIES_ID, EXTENSION };

	private static final SectionDocMetadataColumns instance = new SectionDocMetadataColumns();

	private SectionDocMetadataColumns() {
		super();
		columnNamesToTypes.put(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columnNamesToTypes.put(LINK, "TEXT");
		columnNamesToTypes.put(UPDATED, "TEXT");
		columnNamesToTypes.put(CONTENT_TYPE, "TEXT");
		columnNamesToTypes.put(TITLE, "TEXT");
		columnNamesToTypes.put(HRF_ID, "INTEGER");
		columnNamesToTypes.put(ROOT_ENTRIES_ID, "INTEGER");
		columnNamesToTypes.put(EXTENSION, "TEXT");
	}

	public static SectionDocMetadataColumns getInstance() {
		return instance;
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
}
