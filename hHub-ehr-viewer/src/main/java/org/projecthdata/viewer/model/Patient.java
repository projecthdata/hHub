package org.projecthdata.viewer.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import android.net.Uri;

@Root(name="patient",strict=false)
public class Patient {
	
	@Element
	public String id;
	
	@Element(name="given") @Path("name")
	public String givenName;
	
	@Element @Path("name")
	public String lastname;
	
	@Element(required=false) @Path("name")
	public String suffix;
	
	private String documentUri = null;
	private String hStoreId = null;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getDocumentUri() {
		return documentUri;
	}

	public void setDocumentUri(String documentUri) {
		this.documentUri = documentUri;
		try {
			Uri document = Uri.parse(documentUri);
			this.hStoreId = document.getPathSegments().get(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String gethStoreId() {
		return hStoreId;
	}

	public void sethStoreId(String hStoreId) {
		this.hStoreId = hStoreId;
	}
	
	
	
	
	
}
