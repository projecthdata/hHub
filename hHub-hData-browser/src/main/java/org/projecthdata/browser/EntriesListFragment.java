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

package org.projecthdata.browser;

import java.sql.SQLException;
import java.util.List;

import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.api.Section;
import org.projecthdata.social.api.atom.AtomFeed;
import org.projecthdata.social.api.atom.Entry;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class EntriesListFragment extends ListFragment {
	public static final String TAG = "EntriesListFragment";

	private HDataDatabaseHelper databaseHelper = null;
	private ConnectionRepository connectionRepository = null;
	private String ehrUrl = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getActivity()
				.getApplicationContext()).getConnectionRepository();

		this.ehrUrl = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString(Constants.PREF_EHR_URL, "");
	}

	@Override
	public void onResume() {
		super.onResume();

		// check to see if we still have a valid connection
		if (!isConnected()) {
			doWebOauthActivity();
		} else {
			// TODO: only perform the retrieval when no data exists or the users
			// hits a refresh button
			new GetDataTask().execute();
		}
	}

	private HDataDatabaseHelper getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(getActivity(),
					HDataDatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		/*
		 * Release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * Launches the Intent that initiated the OAuth handshake
	 */
	private void doWebOauthActivity() {
		// the intent to launch
		Intent intent = new Intent(getActivity(), HDataWebOauthActivity.class);
		intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL, ehrUrl);
		startActivity(intent);
	}

	private boolean isConnected() {
		Connection<HData> connection = connectionRepository
				.findPrimaryConnection(HData.class);
		return (connection != null);
	}
	
	
	public void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
		RootEntry entry  = (RootEntry)getListAdapter().getItem(position);
		if(getActivity() instanceof OnRootEntryClickedListener){
			((OnRootEntryClickedListener)getActivity()).onRootEntryClick(entry);
		}
	};

	ProgressDialog dialog = null;

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(getActivity(), "",
					"Loading. Please wait...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// use the connection to get the root document
			Connection<HData> connection = connectionRepository
					.getPrimaryConnection(HData.class);

			
			Root root = null;
			try {
				RootOperations rootOperations = connection.getApi().getRootOperations();
				root = rootOperations.getRoot();
				for (Section section : root.getSections()) {
					RootEntry rootEntry = new RootEntry();
					rootEntry.copy(section);
					// persist each entry in the database
					getDatabaseHelper().getRootEntryDao().create(rootEntry);
					
					AtomFeed sectionMetadata = rootOperations.getSectionFeed(section);
					for(Entry entry : sectionMetadata.getEntries()){
						SectionDocMetadata metadata = new SectionDocMetadata();
						metadata.copy(entry);
						getDatabaseHelper().getSectionDocMetadataDao().create(metadata);
					}
					
				}
			} catch (SQLException sqle) {
				Log.e(TAG, "Error inserting into database", sqle);
			}
			return null;
		}
		
		
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				// get all entries from the database and display them

				List<RootEntry> entries = getDatabaseHelper().getDao(
						RootEntry.class).queryForAll();
				setListAdapter(new RootEntryAdapter(getActivity(), entries));
				dialog.dismiss();
			} catch (SQLException sqle) {
				Log.e(TAG, "Error inserting into database", sqle);
			}
		}
	}
	
	public static interface OnRootEntryClickedListener{
		public void onRootEntryClick(RootEntry entry);
	}

}
