package org.projecthdata.weight.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * 
 * 
 * @author Eric Levine
 *
 */
public class OrmManager {
	private WeightDatabaseHelper databaseHelper = null;
	private Context context = null;
	
	public OrmManager(Context context){
		this.context = context;
		
	}
	
	
	public WeightDatabaseHelper getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					WeightDatabaseHelper.class);
		}
		return databaseHelper;
	}
	
	/**
	 * This should be called when the owning Context 
	 * is being destroyed
	 * 
	 */
	public void release(){
		/*
		 * Release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
}
