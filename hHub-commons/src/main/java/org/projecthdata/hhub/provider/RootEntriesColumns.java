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

public class RootEntriesColumns extends AbstractColumns {

	private static final RootEntriesColumns instance = new RootEntriesColumns();

	/** Unique identifier for the extension. Could be a URL to it's schema **/
	public static final String EXTENSION = "extension";
	/**
	 * A local identifier for this extension, which is unique within the root
	 * document
	 **/
	public static final String EXTENSION_ID = "extension_id";
	public static final String CONTENT_TYPE = "content_type";
	/**
	 * relative path to this section
	 **/
	public static final String PATH = "path";

	/**
	 * Foreign key to the HRF to which this root entry belongs
	 */
	public static final String HRF_ID = "hrf_id";

	public static final String[] ALL_COLUMNS_PROJECTION = { _ID, EXTENSION,
			EXTENSION_ID, CONTENT_TYPE, PATH };

	private RootEntriesColumns() {
		super();
		columnNamesToTypes.put(EXTENSION, "TEXT");
		columnNamesToTypes.put(EXTENSION_ID, "TEXT");
		columnNamesToTypes.put(PATH, "TEXT");
		columnNamesToTypes.put(CONTENT_TYPE, "TEXT");
		columnNamesToTypes.put(HRF_ID, "INTEGER");
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}

	public static RootEntriesColumns getInstance() {
		return instance;
	}
}
