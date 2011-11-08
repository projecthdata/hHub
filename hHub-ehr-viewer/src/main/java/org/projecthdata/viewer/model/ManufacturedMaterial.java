package org.projecthdata.viewer.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class ManufacturedMaterial {
	@Attribute @Path("codedProductName")
	private String code;
	@Attribute @Path("codedProductName")
	private String codeSystem;
	@Attribute @Path("codedProductName")
	private String displayName;
	
	@Element @Path("codedProductName")
	private String freeTextProductName;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFreeTextProductName() {
		return freeTextProductName;
	}
	public void setFreeTextProductName(String freeTextProductName) {
		this.freeTextProductName = freeTextProductName;
	}
	
	
}
