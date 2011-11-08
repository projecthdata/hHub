package org.projecthdata.viewer.ui;



import org.projecthdata.hhub.provider.HDataContract;
import org.projecthdata.hhub.provider.HrfColumns;
import org.projecthdata.hhub.util.DropBoxMessengerHelper;
import org.projecthdata.viewer.R;
import org.projecthdata.viewer.service.DataSyncServiceHelper;
import org.projecthdata.viewer.util.SharedPrefs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class HrfChooserListFragment extends ListFragment  implements LoaderCallbacks<Cursor>{
	
	private SimpleCursorAdapter mAdapter = null;
	
	private String[] fromColumns = { HrfColumns.URL, HrfColumns.STATUS };
	private int[] toViews = { R.id.hrf_list_item_url, R.id.hrf_list_item_status};
	private SharedPreferences sharedPrefs = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		setEmptyText("Retrieving available records...");

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.hrf_list_item, null, fromColumns, toViews, 0);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor cursor = mAdapter.getCursor();
		cursor.moveToPosition(position);
		int idColumn = cursor.getColumnIndex(HrfColumns._ID);
		int urlColumn = cursor.getColumnIndex(HrfColumns.URL);
		Editor editor = sharedPrefs.edit();
		
		String url = cursor.getString(urlColumn);
		editor.putString(SharedPrefs.PREF_HRF_URL, url);
		
		int hrfId = cursor.getInt(idColumn);
		editor.putInt(SharedPrefs.PREF_HRF_ID, hrfId);
		
		editor.commit();
		
		DataSyncServiceHelper.syncAllData(getActivity(), hrfId);
		startActivity(new Intent(getActivity(), EhrActivity.class));
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), HDataContract.Hrf.CONTENT_URI,
				HrfColumns.getInstance().getAllColumnsProjection(), null, null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
		
	}
		
}
