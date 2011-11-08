package org.projecthdata.viewer.rest;

import java.util.ArrayList;
import java.util.List;

import org.projecthdata.viewer.dao.AllergiesDAO;
import org.projecthdata.viewer.dao.BloodPressureDAO;
import org.projecthdata.viewer.dao.BodyHeightDAO;
import org.projecthdata.viewer.dao.BodyWeightDAO;
import org.projecthdata.viewer.dao.MedicationsDAO;
import org.projecthdata.viewer.dao.PatientDAO;
import org.projecthdata.viewer.dao.SectionDAO;
import org.projecthdata.viewer.model.Allergy;
import org.projecthdata.viewer.model.AtomFeed;
import org.projecthdata.viewer.model.Entry;
import org.projecthdata.viewer.model.Medication;
import org.projecthdata.viewer.model.Patient;
import org.projecthdata.viewer.model.Result;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.net.Uri;
import android.util.Log;

public class HStoreTemplate {
	private static final String TAG = "HStoreTemplate";
	
	private RestTemplate rest = new RestTemplate();
	private String rootAtomFeedUrl = null;

		
	public HStoreTemplate(String rootAtomFeedUrl){
		this.rootAtomFeedUrl = rootAtomFeedUrl;
	}
	
	
	public boolean crawlHStore(){
		boolean result = false;
		try {
			AtomFeed rootFeed = getForAtomFeed(rootAtomFeedUrl);
			processAtomFeed(rootFeed);
			result = true;
		} catch (Exception e) {
			String trace = Log.getStackTraceString(e);
			Log.e(TAG, trace);
		}
		return result;
	}
		
	/**
	 * Follow each entry, which can either be a link to another atom feed or
	 * an xml document containing health data
	 * 
	 * @param atomFeed
	 */
	private void processAtomFeed(AtomFeed atomFeed){
		for(Entry entry : atomFeed.getEntries()){
			this.processEntry(entry);
		}
	}
	
	
	/**
	 * An entry can contain a link to another atom feed or a section document
	 */
	public void processEntry(Entry entry){
		if(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_ATOM_XML.toString())){
			AtomFeed sectionFeed = getForAtomFeed(entry.getLinkHref());
			processAtomFeed(sectionFeed);
		}
		//if the url path contains medications and the type is application/xml, then parse the link
		//as a Medication
		else if((entry.getLinkHref().contains("medications") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processMedicationDocument(entry);
		}
		else if((entry.getLinkHref().contains("patientinformation") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processPatientInformationDocument(entry);
		}
		else if((entry.getLinkHref().contains("patientinformation/photos") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.IMAGE_PNG.toString())))){
			//processPatientInformationPhoto(entry);
		}
		else if((entry.getLinkHref().contains("bloodpressure") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processResultDocument(entry, BloodPressureDAO.getInstance());
		}
		else if((entry.getLinkHref().contains("bodyheight") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processResultDocument(entry, BodyHeightDAO.getInstance());
		}
		else if((entry.getLinkHref().contains("bodyweight") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processResultDocument(entry, BodyWeightDAO.getInstance());
		}
		else if((entry.getLinkHref().contains("allergies") && 
				(entry.getLinkType().equalsIgnoreCase(MediaType.APPLICATION_XML.toString())))){
			processAllergyDocument(entry);
		}
	}
	
	//create a Medication object from an entry that has a link to a medication xml document
	public void processMedicationDocument(Entry entry){
		Medication medication = rest.getForObject(entry.getLinkHref(), Medication.class);
		MedicationsDAO.getInstance().addItem(medication);
	}
	
	public void processAllergyDocument(Entry entry){
		Allergy allergy = rest.getForObject(entry.getLinkHref(), Allergy.class);
		AllergiesDAO.getInstance().addItem(allergy);
	}
	
	public void processPatientInformationDocument(Entry entry){
		Patient patient = rest.getForObject(entry.getLinkHref(), Patient.class);
		patient.setDocumentUri(entry.getLinkHref());
		PatientDAO.getInstance().addItem(patient);
	}
	
	public void processPatientInformationPhoto(Entry entry){
		PatientDAO.getInstance().addPatientImage(entry.getLinkHref());
	}
	
	public void processResultDocument(Entry entry, SectionDAO<Result> dao){
		Result result = rest.getForObject(entry.getLinkHref(), Result.class);
		dao.addItem(result);
	}
	
	/**
	 * Adds the correct accept header for a GET request of an atom feed
	 * 
	 * @param url - the URL of the feed to request
	 * @return
	 */
	private AtomFeed getForAtomFeed(String url){
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		acceptableMediaTypes.add(MediaType.APPLICATION_XML);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(acceptableMediaTypes);

		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

		ResponseEntity<AtomFeed> response = rest.exchange(url, HttpMethod.GET, requestEntity, AtomFeed.class);

		AtomFeed feed = response.getBody();
		return feed;
	}
}
