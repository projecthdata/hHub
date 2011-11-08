package org.projecthdata.viewer.ui;

import org.projecthdata.viewer.dao.DataGateway.State;
import org.projecthdata.viewer.util.SharedPrefs;
import org.projecthdata.viewer.view.PatientView;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PatientFragment extends Fragment implements OnSharedPreferenceChangeListener{
	private PatientView patientView = null;
	private SharedPreferences prefs = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		patientView =  new PatientView(getActivity());
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		refreshValues();
		return patientView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
	
	}
	
	/**
	 * Set the view with the most current values
	 */
	private void refreshValues(){
		State state = State.valueOf(prefs.getString(SharedPrefs.PREF_PATIENT_DATA_STATUS, State.SYNCING.toString())); 
		patientView.isLoading(state.equals(State.SYNCING));
		
		patientView.setGivenName(prefs.getString(SharedPrefs.PREF_PATIENT_NAME_GIVEN, ""));
		patientView.setLastName(prefs.getString(SharedPrefs.PREF_PATIENT_NAME_LASTNAME, ""));
		patientView.setPatientId(prefs.getString(SharedPrefs.PREF_PATIENT_ID, ""));

	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(SharedPrefs.PREF_PATIENT_NAME_GIVEN)){
			patientView.setGivenName(prefs.getString(SharedPrefs.PREF_PATIENT_NAME_GIVEN, ""));
		}
		else if (key.equals(SharedPrefs.PREF_PATIENT_NAME_LASTNAME)){
			patientView.setLastName(prefs.getString(SharedPrefs.PREF_PATIENT_NAME_LASTNAME, ""));
		}
		else if (key.equals(SharedPrefs.PREF_PATIENT_ID)){
			patientView.setPatientId(prefs.getString(SharedPrefs.PREF_PATIENT_ID, ""));
		}
		else if (key.equals(SharedPrefs.PREF_PATIENT_PHOTO_URL)){
			String imageUrl = prefs.getString(SharedPrefs.PREF_PATIENT_PHOTO_URL, null);
			patientView.setImageUrl(imageUrl);
		}
		else if (key.equals(SharedPrefs.PREF_PATIENT_DATA_STATUS)){
			State state = State.valueOf(prefs.getString(SharedPrefs.PREF_PATIENT_DATA_STATUS, State.SYNCING.toString())); 
			patientView.isLoading(state.equals(State.SYNCING));
		}
		
	}
	
	
	
}
