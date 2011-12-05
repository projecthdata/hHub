/**
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
 *
 */
package org.projecthdata.weight.model;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "weight_reading")
public class WeightReading {
    public static final  String COLUMN_DATE_TIME = "dateTime";

	private  DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy h:mma");
	
	@DatabaseField(generatedId = true)
    private int _id;
    
    @DatabaseField
    private Double resultValue;
    
    @DatabaseField
    private boolean synched = false;
    
    @DatabaseField
    private Date dateTime = null;
    
    public WeightReading(){
    	this.dateTime = new Date(System.currentTimeMillis());
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

	public Double getResultValue() {
		return resultValue;
	}

	public void setResultValue(Double resultValue) {
		this.resultValue = resultValue;
	}

	public boolean isSynched() {
		return synched;
	}

	public void setSynched(boolean synched) {
		this.synched = synched;
	}
    
	public String getFormattedDateTime(){
		return formatter.print(this.dateTime.getTime());
	}
    
    
    
}
