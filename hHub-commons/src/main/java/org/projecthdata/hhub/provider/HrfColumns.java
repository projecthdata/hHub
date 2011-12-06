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

public class HrfColumns extends AbstractColumns {
	public static final String URL = "url";
	public static final String STATUS = "status";

	private static final String[] ALL_COLUMNS_PROJECTION = { _ID, URL, STATUS };

	public static enum State {
		SYNCING, READY, ERROR
	};

	private static final HrfColumns instance = new HrfColumns();

	private HrfColumns() {
		super();
		columnNamesToTypes.put(URL, "TEXT");
		columnNamesToTypes.put(STATUS, "TEXT");
	}

	public static HrfColumns getInstance() {
		return instance;
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
}
