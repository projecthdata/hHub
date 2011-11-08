package org.projecthdata.viewer.dao;

import org.projecthdata.viewer.model.Allergy;

public class AllergiesDAO extends SectionDAO<Allergy>{
	
	private static AllergiesDAO singletonInstance = null;
	
	private AllergiesDAO(){}
		
	public static AllergiesDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new AllergiesDAO();
		}
		return singletonInstance;
	}
	
}
