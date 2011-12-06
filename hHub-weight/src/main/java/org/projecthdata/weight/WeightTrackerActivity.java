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
import org.projecthdata.weight.service.SyncService;
import org.projecthdata.weight.ui.AddWeightFragment;
import org.projecthdata.weight.ui.ChartFragment;
import org.projecthdata.weight.ui.WeightListFragment;
import org.projecthdata.weight.util.Constants;
import org.projecthdata.weight.util.Constants.SyncState;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.Window;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class WeightTrackerActivity extends FragmentActivity implements
		OrmProvider, OnSharedPreferenceChangeListener,
		OnBackStackChangedListener {

	private AddWeightFragment addWeightFragment = null;
	private WeightListFragment weightListFragment = null;
	private ChartFragment chartFragment = null;

	private static final String TAG = "hHub-weight";
	private static final String ADD_ITEM_TITLE = "Add";
	private static final String SYNC_ITEM_TITLE = "Sync";
	private static final String CLEAR_DATA_TITLE = "Clear Data";
	private static final String LOGOUT_TITLE = "Logout";

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
		// This has to be called before setContentView and you must use the
		// class in android.support.v4.view and NOT android.view
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		addWeightFragment = new AddWeightFragment();
		weightListFragment = new WeightListFragment();
		chartFragment = new ChartFragment();

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, weightListFragment,
						"weight list").commit();
		getSupportFragmentManager().beginTransaction()
				.add(chartFragment, "chart").commit();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.connectionRepository = getApplicationContext()
				.getConnectionRepository();
		this.ormManager = new OrmManager(this);
		getSupportFragmentManager().addOnBackStackChangedListener(this);

		// if the data string is present, then it contains the EHR URL.
		// This is probably coming from a scanned QR code. Start the activity to
		// save the EHR URL and kickoff the OAuth handshake
		if (getIntent().getDataString() != null) {
			Intent ehrIntent = new Intent(this, EhrActivity.class);
			ehrIntent.putExtra(EhrActivity.EXTRA_EHR_URL, getIntent()
					.getDataString());
			startActivityForResult(ehrIntent, REQUEST_CODE_EHR);
		}

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

			// hide the soft keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(weightEditText.getWindowToken(), 0);

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
		menu.add(CLEAR_DATA_TITLE).setIcon(android.R.drawable.ic_menu_delete)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(LOGOUT_TITLE).setIcon(R.drawable.ic_menu_logout)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(ADD_ITEM_TITLE)) {
			if (!addWeightFragment.isVisible()) {
				onAddMenuItem();
			}
		} else if (item.getTitle().equals(SYNC_ITEM_TITLE)) {
			onSyncMenuItem();

		} else if (item.getTitle().equals(CLEAR_DATA_TITLE)) {
			onClearDataMenuItem();
		} else if (item.getTitle().equals(LOGOUT_TITLE)) {
			onLogoutMenuItem();
		}
		return true;
	}

	private void onAddMenuItem() {
		getSupportFragmentManager()
				.beginTransaction()
				.hide(chartFragment)
				.replace(R.id.fragment_container, addWeightFragment,
						"add weight").addToBackStack(null)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();

		getSupportFragmentManager().executePendingTransactions();

	}

	private void onClearDataMenuItem() {
		try {
			Dao<WeightReading, Integer> dao = getDatabaseHelper()
					.getWeightDao();
			dao.delete(dao.deleteBuilder().prepare());
			weightListFragment.onResume();
			chartFragment.refreshChart();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void onLogoutMenuItem() {
		onClearDataMenuItem();
		String ehrUrl = prefs.getString(Constants.PREF_EHR_URL, null);
		if (ehrUrl != null) {
			if (isConnected()) {
				String providerId = this.getApplicationContext().getConnectionFactoryRegistry().getConnectionFactory(HData.class).getProviderId();
				connectionRepository.removeConnections(providerId);
			}
		}
		finish();
	}

	private void onSyncMenuItem() {
		String ehrUrl = prefs.getString(
				org.projecthdata.weight.util.Constants.PREF_EHR_URL, null);

		// if the URL to the EHR exists, setup the connection and start the
		// browser activity
		if (ehrUrl != null){
			addConnectionFactory();
			if (isConnected()) {
				startService(new Intent(this, SyncService.class));
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
		} else if (requestCode == REQUEST_CODE_OAUTH) {
			if (resultCode == HDataWebOauthActivity.RESULT_CODE_SUCCESS) {
				startService(new Intent(this, SyncService.class));
			}
		}

	}

	/**
	 * Launches the Intent to initiate the OAuth handshake
	 */
	private void doWebOauthActivity() {
		// make sure the connection factory is added
		addConnectionFactory();
		// the intent to launch
		Intent intent = new Intent(this, HDataWebOauthActivity.class);
		intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL,
				prefs.getString(Constants.PREF_EHR_URL, null));
		startActivityForResult(intent, REQUEST_CODE_OAUTH);
	}

	private boolean isConnected() {
		Connection<HData> connection = null;
		try {
			connection = connectionRepository
					.findPrimaryConnection(HData.class);
		} catch (IllegalArgumentException e) {
			String message = Log.getStackTraceString(e);
			Log.w(getClass().getSimpleName(), message);
		}
		return (connection != null);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals(Constants.PREF_SYNC_STATE)) {
			String working = SyncState.WORKING.toString().toString();
			if (sharedPreferences.getString(Constants.PREF_SYNC_STATE, working)
					.equals(working)) {
				setProgressBarIndeterminateVisibility(Boolean.TRUE);
			} else {
				setProgressBarIndeterminateVisibility(Boolean.FALSE);
			}
		}

	}

	@Override
	public void onBackStackChanged() {

		if (chartFragment != null) {
			// the chart doesn't work after all the data has been cleared
			// this makes sure a new chart View is created and populated after
			// every new reading is entered, which seems to fix this problem
			chartFragment.refreshChart();
		}

	}

}
