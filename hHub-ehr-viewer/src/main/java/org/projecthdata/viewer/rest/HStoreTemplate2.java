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
import org.projecthdata.viewer.model.root.Root;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class HStoreTemplate2 {
	public enum SectionType {
		MEDICATION, PATIENT_INFO, ALLERGIES, BLOOD_PRESSURE, BODY_HEIGHT, BODY_WEIGHT
	};

	private static final String MEDICATION_XSD = "http://projecthdata.org/hdata/schemas/2009/06/medication";
	private static final String PATIENT_INFORMATION_XSD = "http://projecthdata.org/hdata/schemas/2009/06/patient_information";
	private static final String ALLERGY_XSD = "http://projecthdata.org/hdata/schemas/2009/06/allergy";

	private static final String TAG = "HStoreTemplate2";

	private RestTemplate rest = new RestTemplate();
	private Uri basePatientUri = null;
	private static final String ROOT_PATH = "root.xml";
	private static final String BLOOD_PRESSURE_PATH = "%bloodpressure%";
	private static final String BODY_HEIGHT_PATH = "%bodyheight%";
	private static final String BODY_WEIGHT_PATH = "%bodyweight%";
	private Context context = null;
	private HttpEntity<?> atomFeedRequestEntity = null;


	public HStoreTemplate2(String patientUri, Context context) {
		this.basePatientUri = Uri.parse(patientUri);
		this.context = context;

		// setup the correct accept headers for processing an atom feed
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		acceptableMediaTypes.add(MediaType.APPLICATION_XML);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(acceptableMediaTypes);

		this.atomFeedRequestEntity = new HttpEntity<Object>(requestHeaders);
	}

	public boolean processRootDocument() {
		boolean result = false;
		try {
			String url = basePatientUri.buildUpon().appendPath(ROOT_PATH)
					.build().toString();
			// this will result in the root document being parsed and inserted
			// into the database
			rest.getForObject(url, Root.class);
			result = true;
		} catch (Exception e) {
			String trace = Log.getStackTraceString(e);
			Log.e(TAG, trace);
		}
		return result;
	}

	public boolean processSectionFor(SectionType type) {
		boolean result = false;
//		try {
//			// query the database for the patient URL
//			Cursor c = getUrlsFor(type);
//			if (c.moveToFirst()) {
//				int pathColumnIndex = c.getColumnIndex(RootEntriesColumns.PATH);
//				do {
//					String url = basePatientUri.buildUpon()
//							.appendEncodedPath(c.getString(pathColumnIndex)).build()
//							.toString();
//					AtomFeed atomFeed = getForAtomFeed(url);
//					for (Entry entry : atomFeed.getEntries()) {
//						if (entry.getLinkType().equalsIgnoreCase(
//								MediaType.APPLICATION_XML.toString())) {
//							processDocument(type, entry);
//						}
//					}
//				} while (c.moveToNext());
//			}
//			c.close();
//			result = true;
//		} catch (Exception e) {
//			String trace = Log.getStackTraceString(e);
//			Log.e(TAG, trace);
//		}
		return result;
	}

	public Cursor getUrlsFor(SectionType type) {
		return null;

	}

	public void processDocument(SectionType type, Entry entry) {
		switch (type) {
		case MEDICATION: {
			processMedicationDocument(entry);
			break;
		}
		case PATIENT_INFO: {
			processPatientInformationDocument(entry);
			break;
		}
		case ALLERGIES: {
			processAllergyDocument(entry);
			break;
		}
		case BLOOD_PRESSURE: {
			processResultDocument(entry, BloodPressureDAO.getInstance());
			break;
		}
		case BODY_HEIGHT: {
			processResultDocument(entry, BodyHeightDAO.getInstance());
			break;
		}
		case BODY_WEIGHT: {
			processResultDocument(entry, BodyWeightDAO.getInstance());
			break;
		}
		}
		
	}

	/**
	 * Adds the correct accept header for a GET request of an atom feed
	 * 
	 * @param url
	 *            - the URL of the feed to request
	 * @return
	 */
	private AtomFeed getForAtomFeed(String url) {
		ResponseEntity<AtomFeed> response = rest.exchange(url, HttpMethod.GET,
				atomFeedRequestEntity, AtomFeed.class);
		AtomFeed feed = response.getBody();
		return feed;
	}

	public void processPatientInformationDocument(Entry entry) {
		Patient patient = rest.getForObject(entry.getLinkHref(), Patient.class);
		patient.setDocumentUri(entry.getLinkHref());
		PatientDAO.getInstance().addItem(patient);
	}

	// create a Medication object from an entry that has a link to a medication
	// xml document
	public void processMedicationDocument(Entry entry) {
		Medication medication = rest.getForObject(entry.getLinkHref(),
				Medication.class);
		MedicationsDAO.getInstance().addItem(medication);
	}

	public void processAllergyDocument(Entry entry) {
		Allergy allergy = rest.getForObject(entry.getLinkHref(), Allergy.class);
		AllergiesDAO.getInstance().addItem(allergy);
	}
	
	public void processResultDocument(Entry entry, SectionDAO<Result> dao){
		Result result = rest.getForObject(entry.getLinkHref(), Result.class);
		dao.addItem(result);
	}

}
