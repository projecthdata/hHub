package org.projecthdata.viewer.ui;

import org.projecthdata.viewer.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class EhrActivity extends FragmentActivity {
	MyAdapter mAdapter;

	ViewPager mPager;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ehr);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.ehr_view_pager);
		mPager.setAdapter(mAdapter);
		

		 //Bind the title indicator to the adapter
		 TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.ehr_view_titles);
		 titleIndicator.setViewPager(mPager);
	}

	private static int NUM_ITEMS = 3;

	public static class MyAdapter extends FragmentPagerAdapter implements TitleProvider {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment frag = null;
			switch (position) {
			case 0:
				return new PatientFragment();
			case 1:
				return new AllergiesFragment();
			case 2:
				return new HeightListFragment();
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add("Add")
         .setIcon(android.R.drawable.ic_input_add)
         .setIntent(new Intent(this, HeightFormActivity.class))
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
         
		 
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
}
