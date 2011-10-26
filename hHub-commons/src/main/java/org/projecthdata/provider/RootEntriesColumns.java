package org.projecthdata.provider;

public class RootEntriesColumns extends AbstractColumns {
	
	private static final RootEntriesColumns instance = new RootEntriesColumns();
	
	/** Unique identifier for the extension. Could be a URL to it's schema **/
	public static final String EXTENSION = "extension";
	/**
	 * A local identifier for this extension, which is unique within the
	 * root document
	 **/
	public static final String EXTENSION_ID 	= "extension_id";
	public static final String CONTENT_TYPE = "content_type";
	/**
	 *  relative path to this section 
	 **/
	public static final String PATH = "path";
	
	/**
	 * Foreign key to the HRF to which this root entry belongs
	 */
	public static final String HRF_ID = "hrf_id";
	
	public static final String[] ALL_COLUMNS_PROJECTION = {_ID, EXTENSION, EXTENSION_ID, CONTENT_TYPE, PATH};
	
    private RootEntriesColumns(){
    	super();
		columnNamesToTypes.put(EXTENSION, "TEXT");
		columnNamesToTypes.put(EXTENSION_ID, "TEXT");
		columnNamesToTypes.put(PATH, "TEXT");
		columnNamesToTypes.put(CONTENT_TYPE, "TEXT");
		columnNamesToTypes.put(HRF_ID, "INTEGER");
    }
    

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
	
	public static RootEntriesColumns getInstance(){
		return instance;
	}
}
