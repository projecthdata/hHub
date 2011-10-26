package org.projecthdata.provider;

public class FileTransferStatusColumns extends AbstractColumns {
	
	private static final FileTransferStatusColumns instance = new FileTransferStatusColumns();
	
	public static final String LOCAL_URI = "local_uri";
	public static final String REMOTE_URI = "remote_uri";
	public static final String STATUS = "status";

	public static final String[] ALL_COLUMNS_PROJECTION = {_ID, LOCAL_URI, REMOTE_URI, STATUS};
	
    private FileTransferStatusColumns(){
    	super();
		columnNamesToTypes.put(LOCAL_URI, "TEXT");
		columnNamesToTypes.put(REMOTE_URI, "TEXT");
		columnNamesToTypes.put(STATUS, "TEXT");
    }
    

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
	
	public static FileTransferStatusColumns getInstance(){
		return instance;
	}
}
