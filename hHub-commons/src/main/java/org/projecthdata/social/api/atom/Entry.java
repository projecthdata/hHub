package org.projecthdata.social.api.atom;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name="entry", strict=false)
public class Entry {
	@Attribute(required=false, name="term") @Path("category")
	String categoryTerm = null;
	
	@Attribute(name="href") @Path("link")
	String linkHref = null;
	
	@Attribute(name="type", required=false) @Path("link")
	String linkType = null;
	
	@Element(required=false)
	String title = null;
	@Element(required=false)
	String id = null;
	
	//TODO:  should this be mandatory, but is missing from our demo info at: http://ec2-50-16-102-83.compute-1.amazonaws.com:8080/hStore/1234/patientinformation
	@Element(required=false)
	String updated = null;
	
	public String getCategoryTerm() {
		return categoryTerm;
	}
	public void setCategoryTerm(String categoryTerm) {
		this.categoryTerm = categoryTerm;
	}
	public String getLinkHref() {
		return linkHref;
	}
	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
	
	
}