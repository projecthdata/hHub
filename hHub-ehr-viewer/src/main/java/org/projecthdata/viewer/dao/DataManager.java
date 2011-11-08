package org.projecthdata.viewer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * Providers convenience operations across all of the DataGateways
 * 
 * @author elevine
 *
 */
public class DataManager {
	
	private static final DataManager INSTANCE = new DataManager();
	private List<DataGateway> gateways = new ArrayList<DataGateway>();
	
	private DataManager(){
		registerDataGateway(PatientDataGateway.getInstance());
	}
	
	public void clearAll(Context context){
		for(DataGateway gateway : gateways){
			gateway.clear(context);
		}
	}
	
	public void registerDataGateway(DataGateway gateway){
		this.gateways.add(gateway);
	}
	
	public static DataManager getInstance(){
		return INSTANCE;
	}
	
}
