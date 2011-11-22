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
import org.projecthdata.ehr.viewer.util.Constants.SyncState;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.api.Section;
import org.projecthdata.social.api.atom.AtomFeed;
import org.projecthdata.social.api.atom.Entry;
import org.springframework.social.connect.Connection;

import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class HDataSyncService extends AbstractSyncService {
	public static final String TAG = HDataSyncService.class.getSimpleName();
	public static final String EXTRA_SYNC_ROOT = "syncRoot";

	public HDataSyncService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		onSyncRoot();
		// sync the document data
		startService(new Intent(this, PatientSyncService.class));

	}

	/**
	 * Clears out all root and section data, retrieves the current root document
	 * and atom feeds] , and adds their information to the database.
	 */
	private void onSyncRoot() {
		prefs.edit()
				.putString(Constants.PREF_ROOT_SYNC_STATE,
						SyncState.WORKING.toString()).commit();
		// TODO: check for connection, launch OAuth activity, resume sync
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
		prefs.edit()
				.putString(Constants.PREF_ROOT_SYNC_STATE,
						SyncState.READY.toString()).commit();
	}

	/**
	 * For the given Section, add it to the root entries table. It will also
	 * retrieve the atom feed for the section and add each Entry into the
	 * metadata table. This function will recursively call itself to process
	 * child sections
	 * 
	 * @param rootOperations
	 * @param section
	 */
	private void processSection(RootOperations rootOperations, Section section) {
		try {

			Dao<RootEntry, Integer> rootDao = hDataOrmManager
					.getDatabaseHelper().getRootEntryDao();
			Dao<SectionDocMetadata, Integer> sectionDao = hDataOrmManager
					.getDatabaseHelper().getSectionDocMetadataDao();

			RootEntry rootEntry = new RootEntry();
			rootEntry.copy(section);
			// persist each entry in the database
			rootDao.create(rootEntry);

			AtomFeed sectionMetadata = rootOperations.getSectionFeed(section);
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

}
