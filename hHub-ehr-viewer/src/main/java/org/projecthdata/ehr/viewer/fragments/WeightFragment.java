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
package org.projecthdata.ehr.viewer.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.database.EhrDatabaseHelper;
import org.projecthdata.ehr.viewer.model.WeightReading;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;
import org.projecthdata.ehr.viewer.widget.WeightReadingListAdapter;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.OrmManager;
import org.springframework.social.connect.ConnectionRepository;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

public class WeightFragment extends ListFragment implements
		OnSharedPreferenceChangeListener {
	private SharedPreferences prefs = null;
	private ViewSwitcher switcher = null;
	protected ConnectionRepository connectionRepository = null;
	private WeightReadingListAdapter adapter = null;
	private EhrDatabaseHelper ehrDatabaseHelper = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.weight, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.ehrDatabaseHelper = new EhrDatabaseHelper(getActivity());
		this.prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.switcher = (ViewSwitcher) (getActivity()
				.findViewById(R.id.weight_view_switcher));
		

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getActivity()
				.getApplicationContext()).getConnectionRepository();
		
		adapter = new WeightReadingListAdapter(getActivity(), new ArrayList<WeightReading>());
		setListAdapter(adapter);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(Constants.PREF_WEIGHT_SYNC_STATE)) {
			updateListData();
			updateSwitcher();
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		updateListData();
		updateSwitcher();
	}

	private void updateListData(){
		try {

			super.onStart();
			List<WeightReading> readings = this.ehrDatabaseHelper.getWeightReadingDao().queryForAll();
			adapter = new WeightReadingListAdapter(getActivity(), readings);
			setListAdapter(adapter);
			
		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
		}
	}
	
	private void updateSwitcher() {
		boolean doneSyncing = SyncState.READY.toString().equals(
				prefs.getString(Constants.PREF_WEIGHT_SYNC_STATE, ""));
		// if we are done syncing, show child 1
		int index = (doneSyncing) ? 1 : 0;
		switcher.setDisplayedChild(index);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.ehrDatabaseHelper.close();
	}

}
