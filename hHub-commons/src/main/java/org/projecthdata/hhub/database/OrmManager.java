/**
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
 *
 */
package org.projecthdata.hhub.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * <p>
 * Handles the lifecycle of an OrmLiteSqliteOpenHelper within a Context.
 * </p>
 * <p>
 * <b>Note:</b> This can only be used to manage one type of
 * OrmLiteSqliteOpenHelper within an application. This is due to a limitation
 * from the underlying OpenHelperManager class. If you have multiple
 * OrmLiteSqliteOpenHelpers, then they will have to be created and released
 * individually within a Context.
 * </p>
 * 
 * @author Eric Levine
 * 
 * @param <T>
 */
public class OrmManager<T extends OrmLiteSqliteOpenHelper> {
	private T databaseHelper = null;
	private Context context = null;
	private Class<T> openHelperType;

	public OrmManager(Context context, Class<T> openHelperType) {
		this.context = context;
		this.openHelperType = openHelperType;

	}

	public T getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					openHelperType);
		}
		return databaseHelper;
	}

	/**
	 * This should be called when the owning Context is being destroyed
	 * 
	 */
	public void release() {
		/*
		 * Release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

}
