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
package org.projecthdata.weight.service;

import java.sql.SQLException;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.social.api.HData;
import org.projecthdata.weight.database.OrmManager;
import org.projecthdata.weight.model.WeightReading;
import org.projecthdata.weight.util.Constants;
import org.projecthdata.weight.util.Constants.SyncState;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.web.client.RestTemplate;

import com.j256.ormlite.dao.Dao;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import org.projecthdata.hdata.model.Result;

public class SyncService extends IntentService {
	private OrmManager ormManager = null;
	private ConnectionRepository connectionRepository = null;
	private DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
	private static final String NARRATIVE = "Body weight";
	private static final String CODE = "27113001";
	private static final String CODE_SYSTEM = "2.16.840.1.113883.6.96";
	private static final String RESULT_STATUS_CODE = "completed";
	private static final String UNITS = "kg";
	private SharedPreferences prefs = null;
	
	public SyncService() {
		super("SyncService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.ormManager = new OrmManager(this);
		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication)getApplicationContext()).getConnectionRepository();
		this.prefs =  PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Connection<HData> connection = connectionRepository
				.getPrimaryConnection(HData.class);
		
		this.prefs.edit().putString(Constants.PREF_SYNC_STATE, SyncState.WORKING.toString()).commit();
		//get all readings that are not synced
		 try {
			Dao<WeightReading, Integer> dao = ormManager.getDatabaseHelper().getWeightDao();
			//TODO: query the database instead of iterating over the whole table
			
			String url = this.prefs.getString(Constants.PREF_EHR_URL, "");
			Uri uri =Uri.parse(url);
			uri = uri.buildUpon().appendPath("vitalsigns").appendPath("bodyweight").build();
			RestTemplate template = connection.getApi().getRootOperations().getRestTemplate();
			
			for(WeightReading reading : dao){
				
				if(!reading.isSynched()){
					Result result = new Result();
					//date and time
					result.setResultDateTime(new DateTime(reading.getDateTime().getTime()).toString(dateFormatter));
					//narrative
					result.setNarrative(NARRATIVE);
					//result id
					result.setResultId(UUID.randomUUID().toString());
					//result type code
					result.getResultType().setCode(CODE);
					//result type code system
					result.getResultType().setCodeSystem(CODE_SYSTEM);
					//status code
					result.setResultStatusCode(RESULT_STATUS_CODE);
					//value
					result.setResultValue(reading.getResultValue().toString());
					//value unit
					result.setResultValueUnit(UNITS);
					
					template.postForObject(uri.toString(), result, String.class);
										
					reading.setSynched(true);
					dao.update(reading);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 this.prefs.edit().putString(Constants.PREF_SYNC_STATE, SyncState.READY.toString()).commit();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ormManager.release();
	}

}
