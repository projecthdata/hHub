package org.projecthdata.hhub.provider;

import java.util.Arrays;
import org.projecthdata.hhub.provider.HDataContract.RootEntries;
import android.content.Context;
import android.database.Cursor;

/**
 * Based on Martin Fowler's Table Data Gateway pattern: http://martinfowler.com/eaaCatalog/tableDataGateway.html
 * 
 * @author elevine
 * 
 */
public class RootEntriesGateway extends TableGateway{
	
	public RootEntriesGateway(Context context){
		super(context, RootEntries.CONTENT_URI, RootEntriesColumns.ALL_COLUMNS_PROJECTION);
	}
	
	public int deleteByHrfId(Integer hrfId){
		String[] columns = {RootEntriesColumns.HRF_ID};
		String[] selectionArgs = new String[]{hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.delete(selection, selectionArgs);
	}
	
	/**
	 * Retrieves all root entries that match the extension parameter
	 * 
	 * @param context
	 * @param extension
	 * @return
	 */
	public Cursor findByExtensionAndHrfId(String extension, Integer hrfId){
		
		String[] columns = {RootEntriesColumns.EXTENSION, RootEntriesColumns.HRF_ID};
		String[] selectionArgs = {extension, hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}
	
	
	/**
	 * Retrieves all root entries that match the extension parameter
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public  Cursor findByPathAndHrfId(String path, Integer hrfId){
		String[] columns = {RootEntriesColumns.PATH, RootEntriesColumns.HRF_ID};
		String[] selectionArgs = {path, hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		
		return super.queryUri(selection, selectionArgs);
	}
	
	

	
	

	
}
