package org.projecthdata.viewer.dao;

import org.projecthdata.viewer.model.Medication;

public class MedicationsDAO extends SectionDAO<Medication>{
	
	private static MedicationsDAO singletonInstance = null;
	
	private MedicationsDAO(){}
		
	public static MedicationsDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new MedicationsDAO();
		}
		return singletonInstance;
	}
	
}
