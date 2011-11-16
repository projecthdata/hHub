package org.hHub.weight.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "weight_reading")
public class WeightReading {
    @DatabaseField(generatedId = true)
    private int _id;
    
    @DatabaseField
    private double resultValue;
    
    @DatabaseField
    private boolean synched = false;
    
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public double getResultValue() {
		return resultValue;
	}

	public void setResultValue(double resultValue) {
		this.resultValue = resultValue;
	}

	public boolean isSynched() {
		return synched;
	}

	public void setSynched(boolean synched) {
		this.synched = synched;
	}
    
    
    
    
}
