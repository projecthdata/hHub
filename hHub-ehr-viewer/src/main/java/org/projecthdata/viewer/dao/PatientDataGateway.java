package org.projecthdata.viewer.dao;

import java.io.File;

import org.projecthdata.viewer.model.Patient;
import org.projecthdata.viewer.util.SharedPrefs;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class PatientDataGateway implements DataGateway {
	private static final PatientDataGateway INSTANCE = new PatientDataGateway();
	private static final String TAG = "PatientDataGateway";
	
	private PatientDataGateway() {
	}

	@Override
	public void clear(Context context) {

		getEditor(context).remove(SharedPrefs.PREF_PATIENT_ID)
				.remove(SharedPrefs.PREF_PATIENT_NAME_GIVEN)
				.remove(SharedPrefs.PREF_PATIENT_NAME_LASTNAME)
				.remove(SharedPrefs.PREF_PATIENT_PHOTO_URL).commit();
	}
	
	
	public void parseAndSavePatient(String path, Context context)
			throws Exception{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)  || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Serializer serializer = new Persister();
			File source = new File(path);
			Patient patient = null;
			try {
				patient = serializer.read(Patient.class, source);
				savePatient(patient, context);
			} catch (Exception e) {
				Log.e(TAG, "Error parsing file at: " + path);
				e.printStackTrace();
			}
		} 
	}
	
	public void savePatient(Patient patient, Context context) {
		getEditor(context)
				.putString(SharedPrefs.PREF_PATIENT_ID, patient.getId())
				.putString(SharedPrefs.PREF_PATIENT_NAME_GIVEN,
						patient.getGivenName())
				.putString(SharedPrefs.PREF_PATIENT_NAME_LASTNAME,
						patient.getLastname()).commit();
	}

	public void savePatientPhotoUrl(String photoUrl, Context context) {
		getEditor(context).putString(SharedPrefs.PREF_PATIENT_PHOTO_URL,
				photoUrl).commit();
	}

	public static PatientDataGateway getInstance() {
		return INSTANCE;
	}

	@Override
	public State getStatus(Context context) {
		String state = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(SharedPrefs.PREF_PATIENT_DATA_STATUS,
						State.READY.toString());

		return State.valueOf(state);
	}

	@Override
	public void setStatus(Context context, State state) {
		getEditor(context).putString(SharedPrefs.PREF_PATIENT_DATA_STATUS,
				state.toString()).commit();

	}

	private Editor getEditor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).edit();
	}

}
