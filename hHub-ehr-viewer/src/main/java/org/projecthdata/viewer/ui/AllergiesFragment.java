package org.projecthdata.viewer.ui;

import java.util.HashMap;
import java.util.Map;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.dao.DataGateway.State;
import org.projecthdata.viewer.provider.ALLERGIESContentProvider;
import org.projecthdata.viewer.util.SharedPrefs;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class AllergiesFragment extends Fragment implements
		OnSharedPreferenceChangeListener, LoaderCallbacks<Cursor>,
		OnItemSelectedListener {
	private SimpleCursorAdapter mAdapter = null;

	private Map<String, Integer> columnNamesToColIndex = new HashMap<String, Integer>();

	private String[] fromColumns = { ALLERGIESContentProvider.PRODUCT_DISPLAY_NAME };
	private int[] toViews = { android.R.id.text1 };
	private ViewSwitcher switcher = null;
	private SharedPreferences prefs = null;
	private TextView syncingStatusTextView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.allergy, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.allergy_spinner);
		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_spinner_item, null, fromColumns,
				toViews, 0);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);
		switcher = (ViewSwitcher) getActivity().findViewById(
				R.id.allergy_view_switcher);
		syncingStatusTextView = (TextView) getActivity().findViewById(
				R.id.allergy_status_text);
		this.prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		this.prefs.registerOnSharedPreferenceChangeListener(this);
		updateSyncStatus();
		updateDocumentCount();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(),
				ALLERGIESContentProvider.CONTENT_URI,
				ALLERGIESContentProvider.ALLERGIES_PROJECTION_MAP.keySet()
						.toArray(new String[] {}), null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);

		columnNamesToColIndex.clear();
		for (String columnName : ALLERGIESContentProvider.ALLERGIES_PROJECTION_MAP
				.keySet()) {
			columnNamesToColIndex.put(columnName,
					data.getColumnIndex(columnName));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		bindViews((Cursor) mAdapter.getItem(position));
	}

	private void bindViews(Cursor cursor) {
		String value = cursor.getString(columnNamesToColIndex
				.get(ALLERGIESContentProvider.REACTION_DISPLAY_NAME));
		TextView tv = (TextView) getActivity().findViewById(
				R.id.allergy_reaction);
		tv.setText(value);

		value = cursor.getString(columnNamesToColIndex
				.get(ALLERGIESContentProvider.PRODUCT_DISPLAY_NAME));
		tv = (TextView) getActivity().findViewById(R.id.allergy_product);
		tv.setText(value);

		value = cursor.getString(columnNamesToColIndex
				.get(ALLERGIESContentProvider.SEVERITY_DISPLAY_NAME));
		tv = (TextView) getActivity().findViewById(R.id.allergy_severity);
		tv.setText(value);

		value = cursor.getString(columnNamesToColIndex
				.get(ALLERGIESContentProvider.ADVERSE_EVENT_TYPE_DISPLAY_NAME));
		tv = (TextView) getActivity().findViewById(R.id.allergy_event_type);
		tv.setText(value);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(SharedPrefs.PREF_ALLERGY_DATA_STATUS)) {
			updateSyncStatus();
		} else if (key.equals(SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT)) {
			updateDocumentCount();
		}

	}

	private void updateSyncStatus() {
		State state = State
				.valueOf(this.prefs.getString(
						SharedPrefs.PREF_ALLERGY_DATA_STATUS,
						State.SYNCING.toString()));
		isLoading(state.equals(State.SYNCING));
	}

	private void updateDocumentCount() {
		int count = this.prefs.getInt(
				SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT, -1);
		syncingStatusTextView.setText("Documents remaining: " + count);
	}

	/**
	 * Switches between a loading view or showing the patient's data
	 * 
	 * @param loading
	 */
	public void isLoading(boolean loading) {
		int index = (loading) ? 0 : 1;
		switcher.setDisplayedChild(index);
	}
}
