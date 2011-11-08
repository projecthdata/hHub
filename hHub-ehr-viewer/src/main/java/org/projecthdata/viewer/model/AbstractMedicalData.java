package org.projecthdata.viewer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbstractMedicalData {
	private Map<String, Object> dataTypeToValue = new HashMap<String, Object>();
	
	public Set<String> getDataTypes(){
		return dataTypeToValue.keySet();
	}
	
	public Object getDataValue(String dataTypeName){
		return dataTypeToValue.get(dataTypeName);
	}
	public void addData(String dataType, Object value){
		dataTypeToValue.put(dataType, value);
	}
	
}
