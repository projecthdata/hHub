package org.projecthdata.viewer.dao;

import java.util.HashMap;
import java.util.Map;

import org.projecthdata.viewer.model.Patient;

import android.net.Uri;

public class PatientDAO extends SectionDAO<Patient>{
	
	private Map<String,String> hStoreIdToImageUrl = new HashMap<String, String>();
	
	private static PatientDAO singletonInstance = null;
	
	private PatientDAO(){}
		
	public static PatientDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new PatientDAO();
		}
		return singletonInstance;
	}
	
	public void addPatientImage(String url){
		Uri uri = Uri.parse(url);
		String hStoreId = uri.getPathSegments().get(1);
		hStoreIdToImageUrl.put(hStoreId, url);
	}
	
	public String getImageForPatient(String hStoreId){
		return hStoreIdToImageUrl.get(hStoreId);
	}
	
	public Patient findByHStoreId(String hStoreId){
		Patient match = null;
		for(Patient patient : getItems()){
			if(patient.gethStoreId().equals(hStoreId))
				match = patient;
		}
		return match;
	}
	
}
