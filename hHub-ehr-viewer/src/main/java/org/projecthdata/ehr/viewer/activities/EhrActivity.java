/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projecthdata.ehr.viewer.activities;


import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.fragments.PatientFragment;
import org.projecthdata.ehr.viewer.fragments.WeightFragment;
import org.projecthdata.ehr.viewer.service.HDataSyncService;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.view.Window;

public class EhrActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
	private MyAdapter mAdapter;
	private SharedPreferences prefs = null;
	private ViewPager mPager;
	private static final int NUM_ITEMS = 2;
	private static final String MENU_TITLE_REFRESH = "Refresh";
	
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
			case 1:
				return new WeightFragment();
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
				return "Weight";
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
		String rootSyncState = prefs.getString(Constants.PREF_ROOT_SYNC_STATE, SyncState.UNSTARTED.toString());
		if(rootSyncState.equals(SyncState.READY.toString())){
			setProgressBarIndeterminateVisibility(Boolean.FALSE);
		}
		else{
			setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(MENU_TITLE_REFRESH)
         .setIcon(R.drawable.ic_menu_refresh)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 
		 return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(MENU_TITLE_REFRESH.equals(item.getTitle())){
			startService(new Intent(this, HDataSyncService.class));
		}
		return true;
	}
	
}
