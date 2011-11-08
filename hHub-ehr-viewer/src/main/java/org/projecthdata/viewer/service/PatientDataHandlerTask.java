package org.projecthdata.viewer.service;

import org.projecthdata.hhub.provider.SectionDocMetadataColumns;
import org.projecthdata.hhub.provider.SectionDocMetadataGateway;
import org.projecthdata.viewer.model.Patient;
import org.projecthdata.viewer.util.SharedPrefs;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.preference.PreferenceManager;

public class PatientDataHandlerTask implements Runnable {
	private static final String PATIENT_INFORMATION_EXTENSION = "http://projecthdata.org/hdata/schemas/2009/06/patient_information";
	private static final String PNG_EXTENSION = "http://www.w3.org/TR/PNG";

	private RestTemplate template = new RestTemplate();
	private Context context = null;
	private int hrfId;
	private SectionDocMetadataGateway sectionGateway = null;

	public PatientDataHandlerTask(Context context, int hrfId) {
		this.hrfId = hrfId;
		this.context = context;
		this.sectionGateway = new SectionDocMetadataGateway(context);
	}

	@Override
	public void run() {

		// get the URL for patient info
		Cursor cursor = sectionGateway.findByExtensionAndContentTypeAndHrfId(
				PATIENT_INFORMATION_EXTENSION,
				MediaType.APPLICATION_XML.toString(), hrfId);
		int linkColumnIndex = cursor
				.getColumnIndex(SectionDocMetadataColumns.LINK);
		cursor.moveToFirst();
		String url = cursor.getString(linkColumnIndex);
		cursor.close();

		Patient patient = template.getForObject(url, Patient.class);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(SharedPrefs.PREF_PATIENT_ID, patient.getId());
		editor.putString(SharedPrefs.PREF_PATIENT_NAME_GIVEN,
				patient.getGivenName());
		editor.putString(SharedPrefs.PREF_PATIENT_NAME_LASTNAME,
				patient.getLastname());
		editor.commit();

		// get the URL to the patient's photo
		cursor = sectionGateway.findByExtensionAndContentTypeAndHrfId(
				PNG_EXTENSION, MediaType.IMAGE_PNG.toString(), hrfId);
		linkColumnIndex = cursor.getColumnIndex(SectionDocMetadataColumns.LINK);
		cursor.moveToFirst();
		url = cursor.getString(linkColumnIndex);
		cursor.close();
		editor.putString(SharedPrefs.PREF_PATIENT_PHOTO_URL,url);
		editor.commit();
		

	}
}
