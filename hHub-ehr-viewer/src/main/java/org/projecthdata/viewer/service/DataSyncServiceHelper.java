package org.projecthdata.viewer.service;

import org.projecthdata.viewer.service.DataSyncService.DataType;

import android.content.Context;
import android.content.Intent;

public class DataSyncServiceHelper {
	

	
	
	public static void syncPatientData(Context context, int hrfId){
		Intent intent = new Intent(context, DataSyncService.class);
		intent.putExtra(DataSyncService.EXTRA_TYPE, DataType.PATIENT);
		intent.putExtra(DataSyncService.EXTRA_HRF_ID, hrfId);
		context.startService(intent);
	}
		
	public static void syncAllData(Context context, int hrfId){
		Intent intent = new Intent(context, DataSyncService.class);
		intent.putExtra(DataSyncService.EXTRA_TYPE, DataType.ALL);
		intent.putExtra(DataSyncService.EXTRA_HRF_ID, hrfId);
		context.startService(intent);
	}
	
}
