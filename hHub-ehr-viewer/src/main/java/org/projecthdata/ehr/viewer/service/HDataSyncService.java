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

import java.sql.SQLException;

import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.RootSyncState;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.OrmManager;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.api.Section;
import org.projecthdata.social.api.atom.AtomFeed;
import org.projecthdata.social.api.atom.Entry;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import com.j256.ormlite.dao.Dao;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class HDataSyncService extends IntentService {
	public static final String TAG = HDataSyncService.class.getSimpleName();
	public static final String EXTRA_SYNC_ROOT = "syncRoot";

	private OrmManager<HDataDatabaseHelper> hDataOrmManager = null;
	private ConnectionRepository connectionRepository = null;
	private String ehrUrl = null;
	private SharedPreferences prefs = null;
	
	public HDataSyncService() {
		super(HDataSyncService.class.getSimpleName());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		hDataOrmManager = new OrmManager<HDataDatabaseHelper>(this,
				HDataDatabaseHelper.class);

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getApplicationContext())
				.getConnectionRepository();
		this.ehrUrl = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(Constants.PREF_EHR_URL, "");
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.hasExtra(EXTRA_SYNC_ROOT)) {
			onSyncRoot();
		}

	}

	private void onSyncRoot() {
		prefs.edit().putString(Constants.PREF_ROOT_SYNC_STATE, RootSyncState.WORKING.toString()).commit();
		//TODO: check for connection, launch OAuth activity, resume sync
		try {
			Dao<RootEntry, Integer> rootDao = hDataOrmManager
					.getDatabaseHelper().getRootEntryDao();
			// clear out existing data
			for (RootEntry entry : rootDao.queryForAll()) {
				entry.getSectionMetadata().clear();
				rootDao.delete(entry);
			}
			// fetch new data and add it to the database
			// use the connection to get the root document
			Connection<HData> connection = connectionRepository
					.getPrimaryConnection(HData.class);

			RootOperations rootOperations = connection.getApi()
					.getRootOperations();
			Root root = rootOperations.getRoot();
			for (Section section : root.getSections()) {
				processSection(rootOperations, section);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, Log.getStackTraceString(e));
		}
		prefs.edit().putString(Constants.PREF_ROOT_SYNC_STATE, RootSyncState.READY.toString()).commit();
	}

	public void processSection(RootOperations rootOperations,
			Section section) {
		try {
			
			Dao<RootEntry, Integer> rootDao = hDataOrmManager
					.getDatabaseHelper().getRootEntryDao();
			Dao<SectionDocMetadata, Integer> sectionDao = hDataOrmManager
					.getDatabaseHelper().getSectionDocMetadataDao();
			
			RootEntry rootEntry = new RootEntry();
			rootEntry.copy(section);
			// persist each entry in the database
			rootDao.create(rootEntry);

			AtomFeed sectionMetadata = rootOperations
					.getSectionFeed(section);
			for (Entry entry : sectionMetadata.getEntries()) {
				SectionDocMetadata metadata = new SectionDocMetadata();
				metadata.copy(entry);
				metadata.setRootEntry(rootEntry);
				sectionDao.create(metadata);
			}
			if (section.getSections() != null) {
				// recursively process child sections
				for (Section childSection : section.getSections()) {
					processSection(rootOperations, childSection);
				}
			}
		} catch (SQLException sqle) {
			Log.e(TAG, "Error inserting into database", sqle);
			sqle.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (hDataOrmManager != null) {
			hDataOrmManager.release();
		}
	}

}
