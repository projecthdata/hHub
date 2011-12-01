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
import java.util.List;

import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;
import org.projecthdata.ehr.viewer.xml.Patient;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.social.api.HData;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import org.springframework.web.client.RestTemplate;

import com.j256.ormlite.dao.Dao;

import android.content.Intent;
import android.content.SharedPreferences.Editor;

public class PatientSyncService extends AbstractSyncService {
	public static final String TAG = PatientSyncService.class.getSimpleName();

	private Editor editor = null;
	
	public PatientSyncService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		doSyncPatientInfo();
	}

	private void doSyncPatientInfo() {
		this.editor = prefs.edit();
		editor.putString(Constants.PREF_PATIENT_INFO_SYNC_STATE,
				SyncState.WORKING.toString()).commit();

		try {
			Dao<RootEntry, Integer> rootDao = hDataOrmManager
					.getDatabaseHelper().getRootEntryDao();
			Dao<SectionDocMetadata, Integer> sectionDao = hDataOrmManager
					.getDatabaseHelper().getSectionDocMetadataDao();

			// find the first entry that has the right schema and contains xml documents
			RootEntry entry = rootDao.queryForFirst(rootDao
					.queryBuilder()
					.where()
					.eq(RootEntry.COLUMN_NAME_EXTENSION, Constants.EXTENSION_PATIENT)
					.and()
					.eq(RootEntry.COLUMN_NAME_CONTENT_TYPE,
							MediaType.APPLICATION_XML).prepare());
			
			
			//find the metadata for the first xml document in the section owned by the previous found root entry
			SectionDocMetadata metadata = sectionDao.queryForFirst(sectionDao.queryBuilder().where()
					.eq("rootEntry_id", entry.get_id()).and()
					.eq("contentType", MediaType.APPLICATION_XML).prepare());
			
			// grab that document and parse out the patient info
			Connection<HData> connection = connectionRepository
					.getPrimaryConnection(HData.class);

			RestTemplate restTemplate = connection.getApi().getRootOperations()
					.getRestTemplate();

			Patient patientInfo = restTemplate.getForObject(
					metadata.getLink(), Patient.class);
			
			save(patientInfo);
			
			//get the url for their photo
			 entry = rootDao.queryForFirst(rootDao
						.queryBuilder()
						.where()
						.eq(RootEntry.COLUMN_NAME_EXTENSION, Constants.EXTENSION_PNG)
						.and()
						.eq(RootEntry.COLUMN_NAME_CONTENT_TYPE,
								MediaType.IMAGE_PNG).prepare());
			 if(entry != null){
				 metadata = sectionDao.queryForFirst(sectionDao.queryBuilder().where()
						 .eq("rootEntry_id", entry.get_id()).and()
						 .eq("contentType", MediaType.IMAGE_PNG).prepare());

			 	this.editor.putString(Constants.PREF_PATIENT_PHOTO_URL, metadata.getLink());
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.editor.putString(Constants.PREF_PATIENT_INFO_SYNC_STATE,
				SyncState.READY.toString());
		//committing all of the changes 
		this.editor.commit();
	}

	private void save(Patient patientInfo) {
		editor.putString(Constants.PREF_PATIENT_NAME_GIVEN,
				patientInfo.getGivenName());
		editor.putString(Constants.PREF_PATIENT_NAME_LASTNAME,
				patientInfo.getLastname());
		editor.putString(Constants.PREF_PATIENT_ID, patientInfo.getId());
		editor.putString(Constants.PREF_PATIENT_NAME_SUFFIX,
				patientInfo.getSuffix());
	}

}
