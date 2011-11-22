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
package org.projecthdata.ehr.viewer.service;

import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.OrmManager;
import org.springframework.social.connect.ConnectionRepository;

import android.app.IntentService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Sets up some common elements for syncing data
 * 
 * @author Eric Levine
 *
 */
public abstract class AbstractSyncService extends IntentService{
	protected OrmManager<HDataDatabaseHelper> hDataOrmManager = null;
	protected ConnectionRepository connectionRepository = null;
	protected SharedPreferences prefs = null;
	
	public AbstractSyncService(String serviceName){
		super(serviceName);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		hDataOrmManager = new OrmManager<HDataDatabaseHelper>(this,
				HDataDatabaseHelper.class);

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getApplicationContext())
				.getConnectionRepository();

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (hDataOrmManager != null) {
			hDataOrmManager.release();
		}
	}
}
