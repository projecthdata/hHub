/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projecthdata.ehr.viewer.model;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.projecthdata.hdata.model.Result;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WeightReading {
    public static final String COLUMN_DATE_TIME="dateTime";
    
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
    
    private  DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
    
    
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
	
	public String getFormattedDateTime(){
		return formatter.print(this.dateTime.getTime());
	}
	
	public void copy(Result result){
		setNarrative(result.getNarrative());
		setDateTime(result.getDateTimeObj().toDate());
		setResultStatusCode(result.getResultStatusCode());
		setResultValue(result.getResultValue());
		setResultValueUnit(result.getResultValueUnit());
		setResultId(result.getResultId());
	}
}
