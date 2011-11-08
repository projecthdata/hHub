package org.projecthdata.viewer.dao;

import java.io.File;

import org.projecthdata.hhub.provider.TableGateway;
import org.projecthdata.viewer.model.Allergy;
import org.projecthdata.viewer.provider.ALLERGIESContentProvider;
import org.projecthdata.viewer.util.SharedPrefs;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class AllergiesDataGateway extends TableGateway implements DataGateway {
	private static String[] projection = ALLERGIESContentProvider.ALLERGIES_PROJECTION_MAP.keySet().toArray(new String[]{});
	private static final String TAG = "AllergiesDataGateway";
	
	public AllergiesDataGateway(Context context) {
		super(context, ALLERGIESContentProvider.CONTENT_URI, projection);
	}

	public void parseAndSaveAllergy(String path)
			throws Exception {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Serializer serializer = new Persister();
			File source = new File(path);
			try {
				Allergy allergy = serializer.read(Allergy.class, source);
				insert(allergy);
			} catch (Exception e) {
				Log.e(TAG, "Error parsing file at: " + path);
				e.printStackTrace();
			}
		}
	}
	
	public Uri insert(Allergy allergy) {
		ContentValues values = new ContentValues();
		values.put(ALLERGIESContentProvider.ADVERSE_EVENT_TYPE_CODE, allergy
				.getAdverseEventType().getCode());
		values.put(ALLERGIESContentProvider.ADVERSE_EVENT_TYPE_CODE_SYSTEM,
				allergy.getAdverseEventType().getCodeSystem());
		values.put(ALLERGIESContentProvider.ADVERSE_EVENT_TYPE_DISPLAY_NAME,
				allergy.getAdverseEventType().getDisplayName());
		values.put(ALLERGIESContentProvider.PRODUCT_DISPLAY_NAME, allergy
				.getProduct().getDisplayName());
		values.put(ALLERGIESContentProvider.PRODUCT_CODE, allergy.getProduct()
				.getCode());
		values.put(ALLERGIESContentProvider.PRODUCT_CODE_SYSTEM, allergy
				.getProduct().getCodeSystem());
		values.put(ALLERGIESContentProvider.REACTION_CODE, allergy
				.getReaction().getCode());
		values.put(ALLERGIESContentProvider.REACTION_CODE_SYSTEM, allergy
				.getReaction().getCodeSystem());
		values.put(ALLERGIESContentProvider.REACTION_DISPLAY_NAME, allergy
				.getReaction().getDisplayName());
		values.put(ALLERGIESContentProvider.SEVERITY_CODE, allergy
				.getSeverity().getCode());
		values.put(ALLERGIESContentProvider.SEVERITY_CODE_SYSTEM, allergy
				.getSeverity().getCodeSystem());
		values.put(ALLERGIESContentProvider.SEVERITY_DISPLAY_NAME, allergy
				.getSeverity().getDisplayName());

		return super.insert(values);
	}

	@Override
	public State getStatus(Context context) {
		if (context == null)
			context = super.context;
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(SharedPrefs.PREF_PATIENT_DATA_STATUS,
						State.READY.toString());

		return State.valueOf(state);
	}

	@Override
	public void setStatus(Context context, State state) {
		if (context == null)
			context = super.context;
		PreferenceManager
				.getDefaultSharedPreferences(context)
				.edit()
				.putString(SharedPrefs.PREF_PATIENT_DATA_STATUS,
						state.toString()).commit();

	}

	@Override
	public void clear(Context context) {
		if (context == null)
			context = super.context;

		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.remove(SharedPrefs.PREF_PATIENT_DATA_STATUS).remove(SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT).commit();

		super.deleteAll();
	}

}
