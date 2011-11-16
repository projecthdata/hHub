package org.projecthdata.browser;

import java.sql.SQLException;
import java.util.List;

import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.Section;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class EntriesListFragment extends ListFragment {
	public static final String TAG = "EntriesListFragment";

	private HDataDatabaseHelper databaseHelper = null;
	private HDataConnectionFactory hDataConnectionFactory = null;
	private ConnectionRepository connectionRepository = null;
	private String ehrUrl = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		//initialize the utilities for communicating with the hData server 
		this.connectionRepository = ((HHubApplication) getActivity()
				.getApplicationContext()).getConnectionRepository();

		this.ehrUrl = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString(Constants.PREF_EHR_URL, "");

		this.hDataConnectionFactory = ((HHubApplication) getActivity()
				.getApplicationContext()).getHDataConnectionFactory(ehrUrl);

		//check to see if we still have a valid connection
		if (!isConnected()) {
			doWebOauthActivity();
		} else {
			//use the connection to get the root document
			Connection<HData> connection = connectionRepository
					.getPrimaryConnection(HData.class);

			//TODO: this is a network operation, move it to a background thread
			//TODO: only perform the retrieval when no data exists or the users hits a refresh button
			Root root = null;
			try {
				root = connection.getApi().getRootOperations().getRoot();
				for (Section section : root.getSections()) {
					RootEntry rootEntry = new RootEntry();
					rootEntry.setContentType(section.getExtension()
							.getContentType());
					rootEntry.setPath(section.getPath());
					rootEntry.setExtension(section.getExtension().getContent());
					//persist each entry in the database
					getDatabaseHelper().getRootEntryDao().create(rootEntry);
				}
				//get all entries from the database and display them
				List<RootEntry> entries = getDatabaseHelper().getDao(
						RootEntry.class).queryForAll();
				setListAdapter(new RootEntryAdapter(getActivity(), entries));

			} catch (ExpiredAuthorizationException exception) {
				// TODO: when the servers supports refresh tokens, then do a
				// refresh here
				connectionRepository.removeConnection(connection.getKey());
				doWebOauthActivity();
			} catch (SQLException sqle) {
				Log.e(TAG, "Error inserting into database", sqle);
			}
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
		//HDataWebOauthActivity will launch this intent when it is done
		Intent callbackIntent = new Intent(getActivity()
				.getApplicationContext(), BrowserActivity.class);
		//the intent to launch
		Intent intent = new Intent(getActivity(), HDataWebOauthActivity.class);
		intent.putExtra(HDataWebOauthActivity.EXTRA_CALLBACK_INTENT,
				callbackIntent);
		intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL, ehrUrl);
		startActivity(intent);
		getActivity().finish();
	}

	private boolean isConnected() {
		Connection<HData> connection = connectionRepository
					.findPrimaryConnection(HData.class);
		return (connection != null);
	}

}
