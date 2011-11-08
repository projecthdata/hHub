package org.projecthdata.viewer.dao;

import org.projecthdata.viewer.model.Result;

public class BodyWeightDAO extends SectionDAO<Result>{
	
	private static BodyWeightDAO singletonInstance = null;
	
	private BodyWeightDAO(){}
		
	public static BodyWeightDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new BodyWeightDAO();
		}
		return singletonInstance;
	}
	
}
