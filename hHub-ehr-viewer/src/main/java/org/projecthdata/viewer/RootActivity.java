package org.projecthdata.viewer;

import java.util.ArrayList;
import java.util.List;

import org.projecthdata.viewer.dao.AllergiesDAO;
import org.projecthdata.viewer.dao.BloodPressureDAO;
import org.projecthdata.viewer.dao.BodyHeightDAO;
import org.projecthdata.viewer.dao.BodyWeightDAO;
import org.projecthdata.viewer.dao.MedicationsDAO;
import org.projecthdata.viewer.dao.PatientDAO;
import org.projecthdata.viewer.events.EventBus;
import org.projecthdata.viewer.events.EventSubscriber;
import org.projecthdata.viewer.model.Allergy;
import org.projecthdata.viewer.model.Medication;
import org.projecthdata.viewer.model.Patient;
import org.projecthdata.viewer.model.root.Section;
import org.projecthdata.viewer.rest.HStoreTemplate;
import org.projecthdata.viewer.rest.HStoreTemplate2;
import org.projecthdata.viewer.rest.HStoreTemplate2.SectionType;
import org.projecthdata.viewer.view.AllergyView;
import org.projecthdata.viewer.view.BloodPressureView;
import org.projecthdata.viewer.view.BodyHeightView;
import org.projecthdata.viewer.view.BodyWeightView;
import org.projecthdata.viewer.view.MedicationView;
import org.projecthdata.viewer.view.PatientView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ViewFlipper;

public class RootActivity extends Activity implements
		OnSharedPreferenceChangeListener {
	private String[] sections = { "Patient Info", "Medications", "Allergies",
			"Blood Pressure", "Body Height", "Body Weight" };
	private HStoreTemplate hstore = null;
	private HStoreTemplate2 hstore2 = null;
	private Gallery sectionsGallery = null;
	private MedicationView medicationView = null;
	private AllergyView allergyView = null;
	private PatientView patientView = null;
	private BloodPressureView bloodPressureView = null;
	private BodyHeightView bodyHeightView = null;
	private BodyWeightView bodyWeightView = null;
	private ViewFlipper flipper = null;
	private SharedPreferences prefs = null;
	private HStoreTask currentHStoreTask = null;

	private static final int DIALOG_NETWORK_ERROR = 1;
	private static final int DIALOG_NO_URL = 2;
	private static final String SAVED_STATE_RESTART_HSTORE_TASK = "SAVED_STATE_RESTART_HSTORE_TASK";
	private ProgressDialog progressDialog = null;
	// flag to indicate that a new url was received from an Intent and the data
	// needs to be refreshed
	private boolean newUrlFromIntent = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get a reference to the shared prefs, and listen for any changes
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);

		// try to get a url for the hData record. If this Activity was started
		// from a QR code,
		// then the data from the Intent should have it. Otherwise, it may be
		// saved in shared preferences
		String url = getUrlFromDataOrSharedPrefs(getIntent().getData());
		if (url != null) {
			this.hstore = new HStoreTemplate(url);
			this.hstore2 = new HStoreTemplate2(url, this);
		}

		// setup the Gallery to display the sections of the hData record
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_gallery_item, sections);
		this.sectionsGallery = (Gallery) findViewById(R.id.section_gallery);
		sectionsGallery.setAdapter(adapter);

		// get references to all of the views that contain the hData content
		this.flipper = (ViewFlipper) findViewById(R.id.main_view_flipper);
		this.medicationView = (MedicationView) findViewById(R.id.main_medication);
		this.allergyView = (AllergyView) findViewById(R.id.main_allergy);
		this.patientView = (PatientView) findViewById(R.id.main_patient);
		this.bloodPressureView = (BloodPressureView) findViewById(R.id.main_bloodpressure);
		this.bodyHeightView = (BodyHeightView) findViewById(R.id.main_bodyheight);
		this.bodyWeightView = (BodyWeightView) findViewById(R.id.main_bodyweight);

		// react to changes in selected section in the Gallery
		sectionsGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				flipper.setDisplayedChild(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		// see if there is a saved instance to an HStoreTask
		Object retained = getLastNonConfigurationInstance();
		if ((retained != null) && (retained instanceof HStoreTask)) {
			this.currentHStoreTask = (HStoreTask) retained;
			currentHStoreTask.attach(this);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		// send the current HStoreTask to the next instance of RootActivity
		currentHStoreTask.detach();
		return currentHStoreTask;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// set a flag for the next onCreate call to indicate that this
		// RootActivity has been destroyed
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_STATE_RESTART_HSTORE_TASK, true);
	}

	/**
	 * First tries to get the hStore url from the data parameter. If one exists,
	 * the new URL is persisted and returned. Otherwise, the default
	 * SharePreferences are checked for an existing URL
	 * 
	 * @param data
	 * @return
	 */
	private String getUrlFromDataOrSharedPrefs(Uri data) {
		String url = null;
		if (data != null) {
			url = data.toString();
			Editor editor = prefs.edit();
			editor.putString(getString(R.string.PREF_HDATA_RECORD_URL), url);
			editor.commit();
			newUrlFromIntent = true;
		} else {
			url = prefs.getString(getString(R.string.PREF_HDATA_RECORD_URL),
					null);
		}
		return url;
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBus.registerSubscriber(this);
		// if a currentHStoreTask exists, and its running, we need to re-display
		// the progress dialog
		if ((currentHStoreTask != null)
				&& (currentHStoreTask.getStatus()
						.equals(AsyncTask.Status.RUNNING))) {
			showNetworkProgressDialog();
		}
		// otherwise, We need to start a new HStoreTask or just present data if
		// we already have some
		else {
			// if the newUrlFromIntent flag is true, try and refresh the data
			// from the network
			if (newUrlFromIntent) {
				currentHStoreTask = new HStoreTask(this);
				currentHStoreTask.execute();
				newUrlFromIntent = false;
			}
			// check one of the DAO's to see if they have some data.
			else if (PatientDAO.getInstance().getItems().size() > 0) {
				// assuming that the other DAOs have data too, update all of the
				// views with that info
				updateDataInViews();
			}
			// If a URL for the hStore was found, then an HStoreTemplate
			// instance
			// will been created. If one exists, we need to use it and fetch the
			// data
			else if (this.hstore != null) {
				currentHStoreTask = new HStoreTask(this);
				currentHStoreTask.execute();
			}
			// We have no data, and we don't have a URL to get some.
			// Tell the user so they can do something about it
			else {
				showDialog(DIALOG_NO_URL);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// If we are in the middle of retrieving data
		// dismiss the progress dialog so it does not leak across activities
		dismissNetworkProgressDialog();
		EventBus.detachCurrentSubscriber();
	}

	/**
	 * Callback method for the button to move the gallery back one item
	 * 
	 * @param v
	 */
	public void onGalleryBack(View v) {
		int pos = sectionsGallery.getSelectedItemPosition();
		if (pos > 0) {
			sectionsGallery.setSelection(--pos, true);
		}
	}

	/**
	 * Callback method for the button to move the gallery forward one item
	 * 
	 * @param v
	 */
	public void onGalleryForward(View v) {
		int pos = sectionsGallery.getSelectedItemPosition();
		if (pos < (sections.length - 1)) {
			sectionsGallery.setSelection(++pos, true);
		}

	}

	/**
	 * Refresh the data in the section views from their respecive DAOs
	 */
	private void updateDataInViews() {
		medicationView.setMedications(MedicationsDAO.getInstance().getItems());
		medicationView.invalidate();

		allergyView.setAllergies(AllergiesDAO.getInstance().getItems());
		medicationView.invalidate();

		//patientView.setData(PatientDAO.getInstance().getItems().get(0));
		patientView.invalidate();

		bloodPressureView.setData(BloodPressureDAO.getInstance().getItems());
		patientView.invalidate();

		bodyHeightView.setData(BodyHeightDAO.getInstance().getItems());
		bodyHeightView.invalidate();

		bodyWeightView.setData(BodyWeightDAO.getInstance().getItems());
		bodyWeightView.invalidate();
	}

	/**
	 * Display a progress while hData is being retrieved from the network
	 */
	private void showNetworkProgressDialog() {
		progressDialog = ProgressDialog.show(RootActivity.this, "Updating",
				"Retrieving hData record.  This could take a minute...", true);
	}

	/**
	 * AsyncTask that is attached to the current instance of RootActivity. It
	 * will use the current HStoreTemplate instance to crawl the atom feed for
	 * data. When it is done, this task will refresh RootActivity's views with
	 * the new data.
	 * 
	 * The attach/detach pattern used in this AsyncTask allows it to work
	 * correctly between configuration changes (like orientation) that may
	 * destroy and re-create the RootActivity. See:
	 * http://commonsware.com/blog/2010/09/10/asynctask-screen-rotation.html
	 * 
	 * @author elevine
	 * 
	 */
	static class HStoreTask extends AsyncTask<Void, Void, Boolean> {
		private RootActivity activity = null;

		public HStoreTask(RootActivity activity) {
			attach(activity);
		}

		void detach() {
			activity = null;
		}

		void attach(RootActivity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			activity.showNetworkProgressDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean result = false;
			// clear out any existing data to avoid duplication
			MedicationsDAO.getInstance().clearItems();
			PatientDAO.getInstance().clearItems();
			AllergiesDAO.getInstance().clearItems();
			BloodPressureDAO.getInstance().clearItems();
			BodyHeightDAO.getInstance().clearItems();
			BodyWeightDAO.getInstance().clearItems();
						
			if (activity.getHStoreTemplate2() != null) {
				// load up the data from the hstore
//				activity.getContentResolver().delete(RootEntries.CONTENT_URI, null, null);
				long startRoot = System.currentTimeMillis();
				result = activity.getHStoreTemplate2().processRootDocument();
				  
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.PATIENT_INFO);
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.MEDICATION);
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.ALLERGIES);
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.BLOOD_PRESSURE);
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.BODY_HEIGHT);
				result &= activity.getHStoreTemplate2().processSectionFor(SectionType.BODY_HEIGHT);
				
				long end = System.currentTimeMillis();
				Log.d("****", "TOTAL for Sections: " + (end - startRoot));
			}
			
			return result;
		}

		/**
		 * result - true if the hStore was successfully crawled, false if there
		 * was a network error
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			activity.dismissNetworkProgressDialog();

			if (result) {
				activity.updateDataInViews();
			} else {
				activity.showDialog(DIALOG_NETWORK_ERROR);
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Settings")
				.setIcon(android.R.drawable.ic_menu_preferences)
				.setAlphabeticShortcut('e');
		menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Refresh")
				.setIcon(R.drawable.ic_menu_refresh).setAlphabeticShortcut('r');
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getAlphabeticShortcut() == 'e') {
			startActivity(new Intent(this, SettingsActivity.class));
		} else if (item.getAlphabeticShortcut() == 'r') {
			currentHStoreTask = new HStoreTask(this);
			currentHStoreTask.execute();
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_NETWORK_ERROR:
			dialog = createNetworkErrorDialog();
			break;
		case DIALOG_NO_URL:
			dialog = createNoUrlDialog();
			break;
		}
		return dialog;
	}

	/**
	 * Creates a dialog that tells the user that something went wrong during the
	 * network operation. Gives them the ability to try again.
	 * 
	 * @return
	 */
	private Dialog createNetworkErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"A network error has occurred.  Would you like to try again?")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								currentHStoreTask = new HStoreTask(
										RootActivity.this);
								currentHStoreTask.execute();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder.create();
	}

	/**
	 * Creates a dialog to tell the user that there is no URL available to
	 * indicate where the hData record is location. Gives them the ability to go
	 * to the preferences activity to enter it.
	 * 
	 * @return
	 */
	private Dialog createNoUrlDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"A URL for the hData record has not been set.  Would you like do this now?  It can always be changed by selecting \"Settings\" through the menu key.")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(RootActivity.this,
										SettingsActivity.class);
								dialog.dismiss();
								startActivity(i);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder.create();
	}

	/**
	 * Listens to changes in the shared preferences.
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		String prefHdataRecordURL = getString(R.string.PREF_HDATA_RECORD_URL);
		if (key.equals(prefHdataRecordURL)) {
			
			try {
				dismissDialog(DIALOG_NO_URL);
			} catch (IllegalArgumentException e) {
				// if no dialog was being shown, then this is expected
			}
			String url = sharedPreferences.getString(prefHdataRecordURL, "");
			this.hstore = new HStoreTemplate(url);
		}

	}
	
	/**
	 * When the XML button in the action bar is tapped, display the 
	 * raw health record XML in a new Activity
	 *  
	 * @param v
	 */
	public void onXmlButton(View v) {
		Intent i = new Intent(this, XmlActivity.class);
		startActivity(i);
	}

	public HStoreTemplate getHStoreTemplate() {
		return hstore;
	}
	
	public HStoreTemplate2 getHStoreTemplate2() {
		return hstore2;
	}
	
	/**
	 * Checks to see if the network progress dialog, and dismisses it 
	 */
	public void dismissNetworkProgressDialog() {
		if ((progressDialog != null) && (progressDialog.isShowing()))
			progressDialog.dismiss();
	}
	
	@EventSubscriber(Section.class)
	public void onSection(Section section){
//		ContentValues values = new ContentValues();
//		values.put(RootEntriesColumns.EXTENSION, section.getExtension().getContent());
//		values.put(RootEntriesColumns.EXTENSION_ID, section.getExtensionId());
//		values.put(RootEntriesColumns.CONTENT_TYPE, section.getExtension().getContentType());
//		values.put(RootEntriesColumns.PATH, section.getPath());
//		
//		getContentResolver().insert(RootEntries.CONTENT_URI, values);
	}

}