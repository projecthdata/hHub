package org.projecthdata.viewer.ui;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.projecthdata.viewer.R;
import org.projecthdata.viewer.provider.VITAL_SIGNSContentProvider;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.TextView;

public class HeightListFragment extends ListFragment  implements LoaderCallbacks<Cursor> {
	
	// column to view id mappings for the cursor adapter
	private String[] fromColumns = {VITAL_SIGNSContentProvider.RESULT_DATE_TIME, VITAL_SIGNSContentProvider.RESULT_VALUE, VITAL_SIGNSContentProvider.RESULT_VALUE_UNIT};
	private int[] toViews = { R.id.height_date_time, R.id.height_value, R.id.height_unit};
	private SimpleCursorAdapter mAdapter = null;
	private String[] projection = new String[]{VITAL_SIGNSContentProvider._ID, VITAL_SIGNSContentProvider.RESULT_DATE_TIME, VITAL_SIGNSContentProvider.RESULT_VALUE, VITAL_SIGNSContentProvider.RESULT_VALUE_UNIT};
	private  DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
	
	private final ViewBinder viewBinder = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			switch (view.getId()) {
			case R.id.height_date_time:
				String dateTimeVal = cursor.getString(columnIndex);
				((TextView) view).setText(formatter.print(new DateTime(dateTimeVal)));
				return true;
			case R.id.height_value:
				((TextView) view).setText(cursor.getString(columnIndex));
				return true;
			case R.id.height_unit:
				((TextView) view).setText(cursor.getString(columnIndex));
				return true;
			default:
				return false;

			}
		}

	};

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText("Loading...");

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.height_list_item, null, fromColumns, toViews, 0);
		mAdapter.setViewBinder(viewBinder);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), VITAL_SIGNSContentProvider.CONTENT_URI, projection, null, null, null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}
}
