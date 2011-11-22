package org.projecthdata.ehr.viewer.fragments;

import java.io.ByteArrayInputStream;

import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.social.api.HData;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class PatientFragment extends Fragment implements
		OnSharedPreferenceChangeListener {

	private SharedPreferences prefs = null;
	private ViewSwitcher switcher = null;
	protected ConnectionRepository connectionRepository = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.patient, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.switcher = (ViewSwitcher) (getActivity()
				.findViewById(R.id.patient_view_switcher));

		// initialize the utilities for communicating with the hData server
		this.connectionRepository = ((HHubApplication) getActivity()
				.getApplicationContext()).getConnectionRepository();

	}

	@Override
	public void onStart() {
		super.onStart();
		updateSwitcher();
		bindData();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(Constants.PREF_PATIENT_INFO_SYNC_STATE)) {
			updateSwitcher();
		} else if (key.contains("patient")) {
			bindData();
		}

	}

	private void updateSwitcher() {
		boolean doneSyncing = SyncState.READY.toString().equals(
				prefs.getString(Constants.PREF_PATIENT_INFO_SYNC_STATE, ""));
		// if we are done syncing, show child 1
		int index = (doneSyncing) ? 1 : 0;
		switcher.setDisplayedChild(index);
	}

	private void bindData() {
		String lastName = prefs.getString(Constants.PREF_PATIENT_NAME_LASTNAME,
				"");
		String givenName = prefs.getString(Constants.PREF_PATIENT_NAME_GIVEN,
				"");
		String suffix = prefs.getString(Constants.PREF_PATIENT_NAME_SUFFIX,
				null);
		String name = lastName + ", " + givenName;
		if (suffix != null) {
			name += " " + suffix;
		}

		setTextViewText(R.id.patient_name, name);
		setTextViewText(R.id.patient_id,
				prefs.getString(Constants.PREF_PATIENT_ID, ""));
		new ImageTask().execute();
	}

	/**
	 * Utility method to set the text for a given child TextView
	 * 
	 * @param id
	 * @param text
	 */
	private void setTextViewText(int id, String text) {
		TextView tv = (TextView) (getActivity().findViewById(id));
		tv.setText(text);
	}

	private class ImageTask extends AsyncTask<Void, Void, Void> {
		private Drawable drawable = null;

		@Override
		protected Void doInBackground(Void... params) {
			String imageUrl = prefs.getString(Constants.PREF_PATIENT_PHOTO_URL,
					null);

			if (imageUrl != null) {
				// grab that document and parse out the patient info
				Connection<HData> connection = connectionRepository
						.getPrimaryConnection(HData.class);

				byte[] raw = connection.getApi().getRootOperations()
						.getRestTemplate().getForObject(imageUrl, byte[].class);

				this.drawable = Drawable.createFromStream(
						new ByteArrayInputStream(raw), "src name");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (drawable != null) {
				ImageView patientImage = (ImageView) getActivity()
						.findViewById(R.id.patient_image);
				patientImage.setImageDrawable(drawable);
			}
		}

	}

}
