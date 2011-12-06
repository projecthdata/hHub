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

import java.util.Arrays;
import org.projecthdata.hhub.provider.HDataContract.RootEntries;
import android.content.Context;
import android.database.Cursor;

/**
 * Based on Martin Fowler's Table Data Gateway pattern:
 * http://martinfowler.com/eaaCatalog/tableDataGateway.html
 * 
 * @author elevine
 * 
 */
public class RootEntriesGateway extends TableGateway {

	public RootEntriesGateway(Context context) {
		super(context, RootEntries.CONTENT_URI,
				RootEntriesColumns.ALL_COLUMNS_PROJECTION);
	}

	public int deleteByHrfId(Integer hrfId) {
		String[] columns = { RootEntriesColumns.HRF_ID };
		String[] selectionArgs = new String[] { hrfId.toString() };
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.delete(selection, selectionArgs);
	}

	/**
	 * Retrieves all root entries that match the extension parameter
	 * 
	 * @param context
	 * @param extension
	 * @return
	 */
	public Cursor findByExtensionAndHrfId(String extension, Integer hrfId) {

		String[] columns = { RootEntriesColumns.EXTENSION,
				RootEntriesColumns.HRF_ID };
		String[] selectionArgs = { extension, hrfId.toString() };
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}

	/**
	 * Retrieves all root entries that match the extension parameter
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public Cursor findByPathAndHrfId(String path, Integer hrfId) {
		String[] columns = { RootEntriesColumns.PATH, RootEntriesColumns.HRF_ID };
		String[] selectionArgs = { path, hrfId.toString() };
		String selection = buildAndedSelection(Arrays.asList(columns));

		return super.queryUri(selection, selectionArgs);
	}

}
