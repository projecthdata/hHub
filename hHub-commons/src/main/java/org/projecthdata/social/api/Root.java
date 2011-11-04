//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.16 at 09:42:47 PM EDT 
//


package org.projecthdata.social.api;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}id"/>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}version"/>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}created"/>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}lastModified"/>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}extensions"/>
 *         &lt;element ref="{http://projecthdata.org/hdata/schemas/2009/06/core}sections"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@org.simpleframework.xml.Root(strict=false, name="root")
public class Root {


    @Element(required = false)
    protected String id = "-1";

    @Element
    protected String version;

    protected DateTime created;
    protected DateTime lastModified;
    
    @ElementList
    protected List<Extension> extensions;
    
    @ElementList
    protected List<Section> sections;
    
    //maps extensionId attributes to the full Extension object
    private Map<String, Extension> extensionIdMap = new HashMap<String, Extension>(); 
    
    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Date }
     *     
     */
    @Element
    public String getCreated() {
        return created.toString(ISODateTimeFormat.dateTime());
    }
    
    public DateTime getCreatedDateTime() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Date }
     *     
     */
    @Element
    public void setCreated(String value) {
        this.created = new DateTime(value);
    }



    public DateTime getLastModifiedDateTime() {
        return lastModified;
    }
    
    
    /**
     * Gets the value of the lastModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Date }
     *     
     */
    @Element
    public String getLastModified() {
        return lastModified.toString(ISODateTimeFormat.dateTime());
    }
    
    /**
     * Sets the value of the lastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Date }
     *     
     */
    @Element
    public void setLastModified(String value) {
        this.lastModified = new DateTime(value);
    }

    /**
     * Gets the value of the extensions property.
     * 
     * @return
     *     possible object is
     *     {@link Extensions }
     *     
     */
    public List<Extension> getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Extensions }
     *     
     */
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    /**
     * Gets the value of the sections property.
     * 
     * @return
     *     possible object is
     *     {@link Sections }
     *     
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Sets the value of the sections property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sections }
     *     
     */
    public void setSections(List<Section> value) {
        this.sections = value;
    }
    
    
    @Commit
    public void commit(){
    	//map extensions by their id for quick access
    	for(Extension e : extensions){
    		extensionIdMap.put(e.getExtensionId(), e);
    	}
    	
    	//publish out each completed Section object 
    	//(which may have section children)
    	for (Section section : sections){
    		publishSection(section); 		
    	}
    	
    }
    
    /**
     * Publishes the parameter section to EventBus, and
     * recursively calls itself to publish child sections
     *
     * @param section
     */
    public void publishSection(Section section){
    	//get the associated Extension for this section
    	section.setExtension(getExtensionById(section.getExtensionId()));

    	EventBus.publish(section);
    	if (section.getSections() != null){
    		//children may have children and so on, recursively call
    		//publishSection to make sure they all get published out
    		for(Section childSection : section.getSections()){
    			publishSection(childSection);
    		}
    	}

    }

	public Extension getExtensionById(String extensionId) {
		return extensionIdMap.get(extensionId);
	}
    
    
}
