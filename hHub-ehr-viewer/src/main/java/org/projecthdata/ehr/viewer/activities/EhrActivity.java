package org.projecthdata.ehr.viewer.activities;


import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.fragments.PatientFragment;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.RootSyncState;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.Window;

public class EhrActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
	private MyAdapter mAdapter;
	private SharedPreferences prefs = null;
	private ViewPager mPager;
	private static final int NUM_ITEMS = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    //This has to be called before setContentView and you must use the
        //class in android.support.v4.view and NOT android.view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ehr);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.ehr_view_pager);
		mPager.setAdapter(mAdapter);

		// Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.ehr_view_titles);
		titleIndicator.setViewPager(mPager);
		
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateProgressBar();
	}
	


	public static class MyAdapter extends FragmentPagerAdapter implements
			TitleProvider {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment frag = null;
			switch (position) {
			case 0:
				return new PatientFragment();
			default:
				break;
			}
			return frag;
		}

		@Override
		public String getTitle(int position) {
			switch (position) {
			case 0:
				return "Patient Info";
			case 1:
				return "Allergies";
			case 2:
				return "Height";
			default:
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key.equals(Constants.PREF_ROOT_SYNC_STATE)){
			updateProgressBar();
		}
		
	}
	
	public void updateProgressBar(){
		String rootSyncState = prefs.getString(Constants.PREF_ROOT_SYNC_STATE, RootSyncState.UNSTARTED.toString());
		if(rootSyncState.equals(RootSyncState.READY.toString())){
			setProgressBarIndeterminateVisibility(Boolean.FALSE);
		}
		else{
			setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}
	}
	
}
