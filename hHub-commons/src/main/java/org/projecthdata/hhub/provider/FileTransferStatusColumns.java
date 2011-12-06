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

public class FileTransferStatusColumns extends AbstractColumns {

	private static final FileTransferStatusColumns instance = new FileTransferStatusColumns();

	public static final String LOCAL_URI = "local_uri";
	public static final String REMOTE_URI = "remote_uri";
	public static final String STATUS = "status";

	public static final String[] ALL_COLUMNS_PROJECTION = { _ID, LOCAL_URI,
			REMOTE_URI, STATUS };

	private FileTransferStatusColumns() {
		super();
		columnNamesToTypes.put(LOCAL_URI, "TEXT");
		columnNamesToTypes.put(REMOTE_URI, "TEXT");
		columnNamesToTypes.put(STATUS, "TEXT");
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}

	public static FileTransferStatusColumns getInstance() {
		return instance;
	}
}
