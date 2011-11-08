package org.projecthdata.viewer.dao;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * A gateway provides access to persisted information from a document.
 * The gateway abstracts away the details of how data is persisted, which may be in 
 * a database or shared preferences
 * 
 * @author elevine
 *
 */
public interface DataGateway {
	public static enum State {SYNCING, READY, ERROR};
	/**
	 * Delete all the data that this gateway manages
	 */
	public void clear(Context context);
	
	public State getStatus(Context context);
	public void setStatus(Context context, State state);

}
