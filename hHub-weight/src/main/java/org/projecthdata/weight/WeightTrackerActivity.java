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
package org.projecthdata.weight;

import java.sql.SQLException;

import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.projecthdata.weight.database.OrmManager;
import org.projecthdata.weight.database.WeightDatabaseHelper;
import org.projecthdata.weight.model.WeightReading;
import org.projecthdata.weight.ui.AddWeightFragment;
import org.projecthdata.weight.ui.WeightListFragment;
import org.projecthdata.weight.util.Constants;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.Window;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class WeightTrackerActivity extends FragmentActivity implements
		OrmProvider {

	private AddWeightFragment addWeightFragment = null;
	private WeightListFragment weightListFragment = null;

	private static final String TAG = "hHub-weight";
	private static final String ADD_ITEM_TITLE = "Add";
	private static final String SYNC_ITEM_TITLE = "Sync";

	private SharedPreferences prefs = null;
	private ConnectionRepository connectionRepository = null;

	private static final Integer REQUEST_CODE_OAUTH = 1;
	private static final Integer REQUEST_CODE_EHR = 2;
	
	private OrmManager ormManager = null;
	
	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		addWeightFragment = new AddWeightFragment();
		weightListFragment = new WeightListFragment();
				
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, weightListFragment,
						"weight list").commit();

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.connectionRepository = getApplicationContext()
				.getConnectionRepository();
		this.ormManager = new OrmManager(this);

	}

	public WeightDatabaseHelper getDatabaseHelper() {
		return ormManager.getDatabaseHelper();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ormManager.release();
	}

	public void onSave(View v) {
		try {
			WeightReading reading = new WeightReading();
			EditText weightEditText = (EditText) findViewById(R.id.weight_edit_text);
			Double entry = Double.parseDouble(weightEditText.getText()
					.toString());
			reading.setResultValue(entry);
			getDatabaseHelper().getWeightDao().create(reading);
			// go back to displaying the weight list
			getSupportFragmentManager().popBackStack();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(SYNC_ITEM_TITLE).setIcon(android.R.drawable.ic_menu_upload)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(ADD_ITEM_TITLE).setIcon(android.R.drawable.ic_menu_add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(ADD_ITEM_TITLE)) {
			onAddMenuItem();
		} else if (item.getTitle().equals(SYNC_ITEM_TITLE)) {
			onSyncMenuItem();
		}
		return true;
	}

	private void onAddMenuItem() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, addWeightFragment,
						"add weight").addToBackStack(null)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}

	private void onSyncMenuItem() {
		String ehrUrl = prefs.getString(
				org.projecthdata.weight.util.Constants.PREF_EHR_URL, null);

		// if the URL to the EHR exists, setup the connection and start the
		// browser activity
		if (ehrUrl != null) {
			if (isConnected()) {
				// TODO: start syncing
			} else {
				doWebOauthActivity();
			}
		} else {
			// there is no EHR URL, start the activity to get it from the user
			startActivityForResult(new Intent(this, EhrActivity.class),
					REQUEST_CODE_EHR);
		}
	}

	private void addConnectionFactory() {
		String clientSecret = getString(R.string.clientSecret);
		String clientId = getString(R.string.clientId);
		String ehrUrl = prefs.getString(Constants.PREF_EHR_URL, null);
		try {
			this.getApplicationContext()
					.getConnectionFactoryRegistry()
					.addConnectionFactory(
							new HDataConnectionFactory(clientId, clientSecret,
									ehrUrl));
		} catch (IllegalArgumentException e) {
			// expected if there is already a ConnectionFactory for this url
		}
	}

	@Override
	public HHubApplication getApplicationContext() {
		return (HHubApplication) super.getApplicationContext();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// the user has entered an EHR url
		if (requestCode == REQUEST_CODE_EHR) {
			if (resultCode == Constants.RESULT_SAVED) {
				doWebOauthActivity();
			}
		}
		else if (requestCode == REQUEST_CODE_OAUTH) {
			if(resultCode == HDataWebOauthActivity.RESULT_CODE_SUCCESS){
				//TODO: start service
			}
		}

	}

	/**
	 * Launches the Intent to initiate the OAuth handshake
	 */
	private void doWebOauthActivity() {
		//make sure the connection factory is added
		addConnectionFactory();
		// the intent to launch
		Intent intent = new Intent(this, HDataWebOauthActivity.class);
		intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL,
				prefs.getString(Constants.PREF_EHR_URL, null));
		startActivityForResult(intent, REQUEST_CODE_OAUTH);
	}

	private boolean isConnected() {
		Connection<HData> connection = connectionRepository
				.findPrimaryConnection(HData.class);
		return (connection != null);
	}

}
