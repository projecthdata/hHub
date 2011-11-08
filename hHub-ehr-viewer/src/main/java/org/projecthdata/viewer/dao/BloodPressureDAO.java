package org.projecthdata.viewer.dao;

import org.projecthdata.viewer.model.Result;

public class BloodPressureDAO extends SectionDAO<Result>{
	
	private static BloodPressureDAO singletonInstance = null;
	
	private BloodPressureDAO(){}
		
	public static BloodPressureDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new BloodPressureDAO();
		}
		return singletonInstance;
	}
	
}
