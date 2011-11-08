package org.projecthdata.viewer.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class HrfChooserActivity extends FragmentActivity {
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	       // Create the list fragment and add it as our sole content.
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
        	HrfChooserListFragment list = new HrfChooserListFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, list).commit();
        }
				
	}
}
