package org.projecthdata.ehr.viewer.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WeightReading {
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField
    private String resultStatusCode;
    @DatabaseField
    private String narrative;
    @DatabaseField
    private String resultValue;
    @DatabaseField
    private String resultValueUnit;
    @DatabaseField
    private String resultId;
    @DatabaseField
    private Date dateTime;
    
	public String getResultStatusCode() {
		return resultStatusCode;
	}
	public void setResultStatusCode(String resultStatusCode) {
		this.resultStatusCode = resultStatusCode;
	}
	public String getNarrative() {
		return narrative;
	}
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	public String getResultValueUnit() {
		return resultValueUnit;
	}
	public void setResultValueUnit(String resultValueUnit) {
		this.resultValueUnit = resultValueUnit;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
}
