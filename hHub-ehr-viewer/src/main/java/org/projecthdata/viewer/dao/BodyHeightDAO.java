package org.projecthdata.viewer.dao;

import org.projecthdata.viewer.model.Result;

public class BodyHeightDAO extends SectionDAO<Result>{
	
	private static BodyHeightDAO singletonInstance = null;
	
	private BodyHeightDAO(){}
		
	public static BodyHeightDAO getInstance(){
		if(singletonInstance == null){
			singletonInstance = new BodyHeightDAO();
		}
		return singletonInstance;
	}
	
}
