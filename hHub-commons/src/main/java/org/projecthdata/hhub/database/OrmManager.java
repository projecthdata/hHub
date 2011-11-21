package org.projecthdata.hhub.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class OrmManager<T extends OrmLiteSqliteOpenHelper> {
	private T databaseHelper = null;
	private Context context = null;
	private Class<T> openHelperType;
	
	public OrmManager(Context context, Class<T> openHelperType){
		this.context = context;
		this.openHelperType = openHelperType;
		
		
	}
	
	public T getDatabaseHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context, openHelperType);
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
