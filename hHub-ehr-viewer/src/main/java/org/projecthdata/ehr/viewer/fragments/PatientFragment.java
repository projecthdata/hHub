package org.projecthdata.ehr.viewer.fragments;


import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class PatientFragment extends Fragment implements OnSharedPreferenceChangeListener{
	
	private SharedPreferences prefs = null;
	private ViewSwitcher switcher = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.patient, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.switcher = (ViewSwitcher)(getActivity().findViewById(R.id.patient_view_switcher));
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateSwitcher();
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.equals(Constants.PREF_PATIENT_INFO_SYNC_STATE)){
			updateSwitcher();
		}
		else if(key.contains("patient")){
			bindData();
		}
		
	}
	
	private void updateSwitcher(){
		boolean doneSyncing = SyncState.READY.toString().equals(prefs.getString(Constants.PREF_PATIENT_INFO_SYNC_STATE, ""));
		//if we are done syncing, show child 1
		int index = (doneSyncing)? 1 : 0;
		switcher.setDisplayedChild(index);
	}
	
	private void bindData(){
		String lastName = prefs.getString(Constants.PREF_PATIENT_NAME_LASTNAME, "");
		String givenName = prefs.getString(Constants.PREF_PATIENT_NAME_GIVEN, "");
		String suffix = prefs.getString(Constants.PREF_PATIENT_NAME_SUFFIX, null);
		String name = lastName+", "+givenName;
		if(suffix != null){
			name +=" " + suffix;
		}
		
		setTextViewText(R.id.patient_name, name);
		setTextViewText(R.id.patient_id, prefs.getString(Constants.PREF_PATIENT_ID, ""));
		
	}
	
	/**
	 * Utility method to set the text for a given child TextView
	 * 
	 * @param id
	 * @param text
	 */
	private void setTextViewText(int id, String text){
		TextView tv = (TextView)(getActivity().findViewById(id));
		tv.setText(text);
	}
	
}
