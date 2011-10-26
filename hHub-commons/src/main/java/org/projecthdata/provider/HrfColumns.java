package org.projecthdata.provider;

public class HrfColumns extends  AbstractColumns{
	public static final String URL = "url";
	public static final String STATUS = "status";
	
	private static final String[] ALL_COLUMNS_PROJECTION = {_ID, URL, STATUS};
	
	public static enum State {SYNCING, READY, ERROR};
	
	private static final HrfColumns instance= new HrfColumns(); 
	
	private HrfColumns(){
		super();
		columnNamesToTypes.put(URL, "TEXT");
		columnNamesToTypes.put(STATUS, "TEXT");
	}
	
	public static HrfColumns getInstance(){
		return instance;
	}

	@Override
	public String[] getAllColumnsProjection() {
		return ALL_COLUMNS_PROJECTION;
	}
}
