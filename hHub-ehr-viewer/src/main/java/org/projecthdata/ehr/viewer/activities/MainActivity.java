package org.projecthdata.ehr.viewer.activities;

import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.fragments.PatientFragment;
import org.projecthdata.ehr.viewer.service.HDataSyncService;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.RootSyncState;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * This Activity does not have any UI. Based on the state of the application, it
 * will choose another Activity to start
 * 
 * @author Eric Levine
 * 
 */
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int REQUEST_CODE_EHR_URL = 1;
	private static final int REQUEST_CODE_OAUTH = 2;
	private SharedPreferences prefs = null;
	private ConnectionRepository connectionRepository = null;
	String ehrUrl = null;
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
		setContentView(R.layout.splash);
		
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.ehrUrl = prefs.getString(Constants.PREF_EHR_URL, null);

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getApplicationContext())
				.getConnectionRepository();

		// if the URL to the EHR exists, setup the connection and start the
		// browser activity
		if (this.ehrUrl != null) {
			ehrUrlExists();
		} else {
			// there is no EHR URL, start the activity to get it from the user
			startActivityForResult(new Intent(this, EhrUrlFormActivity.class),
					Constants.RESULT_SAVED);
		}

	}

	private void ehrUrlExists() {
		addConnectionFactory();
		if (!isConnected()) {
			doWebOauthActivity(this.ehrUrl);
		} else {
			// TODO: check for connection
			// kick off a sync if one hasn't already happened
			String rootSyncState = prefs.getString(
					Constants.PREF_ROOT_SYNC_STATE,
					RootSyncState.UNSTARTED.toString());
			if (rootSyncState.equals(RootSyncState.UNSTARTED.toString())) {
				Intent syncIntent = new Intent(this, HDataSyncService.class);
				syncIntent.putExtra(HDataSyncService.EXTRA_SYNC_ROOT, true);
				startService(syncIntent);
			}
			startActivity(new Intent(this, EhrActivity.class));
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_EHR_URL) {
			// the new EHR URL has been set, now authenticate
			if (resultCode == Constants.RESULT_SAVED) {
				this.ehrUrl =prefs.getString(Constants.PREF_EHR_URL, null);
				addConnectionFactory();
				doWebOauthActivity(this.ehrUrl);
			}
		} else if (requestCode == REQUEST_CODE_OAUTH) {
			// we are authenticated, kick off a sync and the next Activity
			if (resultCode == HDataWebOauthActivity.RESULT_CODE_SUCCESS) {
				// We are authenticated, start syncing the root document
				Intent syncIntent = new Intent(this, HDataSyncService.class);
				syncIntent.putExtra(HDataSyncService.EXTRA_SYNC_ROOT, true);
				startService(syncIntent);
				startActivity(new Intent(this, EhrActivity.class));
				finish();
			}
		}
	}

	/**
	 * Launches the Intent that initiated the OAuth handshake
	 */
	private void doWebOauthActivity(String ehrUrl) {
		// the intent to launch
		Intent intent = new Intent(this, HDataWebOauthActivity.class);
		intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL, ehrUrl);
		startActivityForResult(intent, REQUEST_CODE_OAUTH);
	}

	private void addConnectionFactory() {
		String clientSecret = getString(R.string.clientSecret);
		String clientId = getString(R.string.clientId);
		try {
			this.getApplicationContext()
					.getConnectionFactoryRegistry()
					.addConnectionFactory(
							new HDataConnectionFactory(clientId, clientSecret,
									this.ehrUrl));
		} catch (IllegalArgumentException e) {
			// expected if there is already a ConnectionFactory for this url
		}
	}

	@Override
	public HHubApplication getApplicationContext() {
		return (HHubApplication) super.getApplicationContext();
	}

	private boolean isConnected() {
		Connection<HData> connection = connectionRepository
				.findPrimaryConnection(HData.class);
		return (connection != null);
	}

}