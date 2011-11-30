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

import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.social.api.HData;
import org.projecthdata.weight.database.OrmManager;
import org.projecthdata.weight.model.WeightReading;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import com.j256.ormlite.dao.Dao;

import android.app.IntentService;
import android.content.Intent;

public class SyncService extends IntentService {
	private OrmManager ormManager = null;
	private ConnectionRepository connectionRepository = null;
	
	public SyncService() {
		super("SyncService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.ormManager = new OrmManager(this);
		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication)getApplicationContext()).getConnectionRepository();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Connection<HData> connection = connectionRepository
				.getPrimaryConnection(HData.class);
		
		//get all readings that are not synced
		 try {
			Dao<WeightReading, Integer> dao = ormManager.getDatabaseHelper().getWeightDao();
			//TODO: query the database instead of iterating over the whole table
			for(WeightReading reading : dao){
				
				if(!reading.isSynched()){
					//TODO: upload it
//					connection.getApi().getRootOperations().
					
					reading.setSynched(true);
					dao.update(reading);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ormManager.release();
	}

}
