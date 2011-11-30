package org.projecthdata.ehr.viewer.fragments;

import org.projecthdata.ehr.viewer.database.EhrDatabaseHelper;
import org.projecthdata.hhub.database.OrmManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class WeightFragment extends ListFragment implements OnSharedPreferenceChangeListener {
	private OrmManager<EhrDatabaseHelper> ehrOrmManager = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ehrOrmManager = new OrmManager<EhrDatabaseHelper>(getActivity(), EhrDatabaseHelper.class);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

}
