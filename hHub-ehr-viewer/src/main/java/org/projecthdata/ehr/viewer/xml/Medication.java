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
package org.projecthdata.ehr.viewer.xml;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name="medication",strict=false)
public class Medication{
	
	@Attribute(name="value") @Path("dose")
	private String dose;
	
	@Attribute(name="code") @Path("statusOfMedication")
	private String status;
	
	@Element
	private String narrative;
	
	
	private String effectiveDateTime; 
	private DateTime effectiveDateTimeObj = null;
	
	
	private  DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy");
	
	@Element @Path("frequency")
	private EffectiveFrequency effectiveFrequency; 
	
	@Element @Path("medicationInformation")
	private ManufacturedMaterial manufacturedMaterial;


	public String getDose() {
		return dose;
	}


	public void setDose(String dose) {
		this.dose = dose;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getNarrative() {
		return narrative;
	}



	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}


	@Attribute (name="low") @Path("frequency/effectiveDateTime")
	public String getEffectiveDateTime() {
		return effectiveDateTime;
	}


	@Attribute (name="low") @Path("frequency/effectiveDateTime")
	public void setEffectiveDateTime(String effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
		this.effectiveDateTimeObj = new DateTime(effectiveDateTime);
	}

		
	public String getFormattedEffectiveDateTime(){
		return formatter.print(this.effectiveDateTimeObj);
	}
	
	public DateTime getEffectiveDateTimeObj(){
		return this.effectiveDateTimeObj;
	}
	

	public EffectiveFrequency getEffectiveFrequency() {
		return effectiveFrequency;
	}



	public void setEffectiveFrequency(EffectiveFrequency effectiveFrequency) {
		this.effectiveFrequency = effectiveFrequency;
	}

	public ManufacturedMaterial getManufacturedMaterial() {
		return manufacturedMaterial;
	}

	public void setManufacturedMaterial(ManufacturedMaterial manufacturedMaterial) {
		this.manufacturedMaterial = manufacturedMaterial;
	}
	
	@Override
	public String toString(){
		return manufacturedMaterial.getFreeTextProductName();
	}
}
