package org.hHub.weight;

import java.sql.SQLException;

import org.hHub.weight.database.WeightDatabaseHelper;
import org.hHub.weight.model.WeightReading;
import org.hHub.weight.ui.AddWeightFragment;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class WeightTrackerActivity extends FragmentActivity implements
		OrmProvider {

	private WeightDatabaseHelper databaseHelper = null;
	private AddWeightFragment addWeightFragment = null;
	
	private static String TAG = "hHub-weight";
	private static final String ADD_ITEM_TITLE = "Add";
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
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main);
		addWeightFragment = new AddWeightFragment();
		
		

	}

	public WeightDatabaseHelper getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					WeightDatabaseHelper.class);
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

	public void onSave(View v) {
		try {
			WeightReading reading = new WeightReading();
			EditText weightEditText = (EditText) findViewById(R.id.weight_edit_text);
			Double entry = Double.parseDouble(weightEditText.getText()
					.toString());
			reading.setResultValue(entry);
			getDatabaseHelper().getWeightDao().create(reading);
			getSupportFragmentManager().beginTransaction()
				.remove(addWeightFragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
				.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Add").setIcon(android.R.drawable.ic_input_add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getTitle().equals(ADD_ITEM_TITLE)){
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, addWeightFragment, "add weight")
				.addToBackStack(null)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
		}
		return true;
	}
	

}
