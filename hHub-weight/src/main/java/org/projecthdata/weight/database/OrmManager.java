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
package org.projecthdata.weight.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * 
 * 
 * @author Eric Levine
 *
 */
public class OrmManager {
	private WeightDatabaseHelper databaseHelper = null;
	private Context context = null;
	
	public OrmManager(Context context){
		this.context = context;
		
	}
	
	
	public WeightDatabaseHelper getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					WeightDatabaseHelper.class);
		}
		return databaseHelper;
	}
	
	/**
	 * This should be called when the owning Context 
	 * is being destroyed
	 * 
	 */
	public void release(){
		/*
		 * Release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
}
