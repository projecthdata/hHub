package org.projecthdata.provider;

public class SectionDocMetadataColumns extends  AbstractColumns{
	
	public static final String LINK = "link";
	public static final String UPDATED = "updated";
	public static final String CONTENT_TYPE = "content_type";
	public static final String TITLE = "title";
	public static final String HRF_ID = "hrf_id";
	public static final String ROOT_ENTRIES_ID = "root_entries_id";
	public static final String EXTENSION = "extension";
	
	private static final String[] ALL_COLUMNS_PROJECTION = {_ID, LINK, UPDATED, CONTENT_TYPE, 
		TITLE, HRF_ID, ROOT_ENTRIES_ID, EXTENSION};
	
	private static final SectionDocMetadataColumns instance= new SectionDocMetadataColumns(); 
	
	private SectionDocMetadataColumns(){
		super();
		columnNamesToTypes.put(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columnNamesToTypes.put(LINK, "TEXT");
		columnNamesToTypes.put(UPDATED, "TEXT");
		columnNamesToTypes.put(CONTENT_TYPE, "TEXT");
		columnNamesToTypes.put(TITLE, "TEXT");
		columnNamesToTypes.put(HRF_ID, "INTEGER");
		columnNamesToTypes.put(ROOT_ENTRIES_ID, "INTEGER");
		columnNamesToTypes.put(EXTENSION, "TEXT");
	}
	
	public static SectionDocMetadataColumns getInstance(){
		return instance;
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
}
