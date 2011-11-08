package org.projecthdata.viewer.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;

public class EffectiveFrequency {
	
	@Attribute(name="value") @Path("period")
	String period;
	
	@Attribute @Path("period")
	String unit;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
