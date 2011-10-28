package org.projecthdata.hhub.provider;

import java.util.HashMap;
import java.util.Map;

import android.provider.BaseColumns;

public abstract class AbstractColumns implements BaseColumns{
	public Map<String, String> columnNamesToTypes = new HashMap<String, String>();
	
	
	
	public AbstractColumns(){
		columnNamesToTypes.put(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
	}
	
	
	public abstract String[] getAllColumnsProjection();
	
	public Map<String, String> getColumnNamesToTypes(){
		return this.columnNamesToTypes;
	}
}
