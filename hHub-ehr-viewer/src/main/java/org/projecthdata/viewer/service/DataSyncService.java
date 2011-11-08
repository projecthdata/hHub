package org.projecthdata.viewer.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.projecthdata.hhub.provider.SectionDocMetadataColumns;
import org.projecthdata.hhub.provider.SectionDocMetadataGateway;
import org.projecthdata.hhub.util.DropBoxMessengerHelper;
import org.projecthdata.viewer.dao.AllergiesDataGateway;
import org.projecthdata.viewer.dao.BodyHeightDataGateway;
import org.projecthdata.viewer.dao.DataGateway.State;
import org.projecthdata.viewer.dao.PatientDataGateway;
import org.projecthdata.viewer.util.SharedPrefs;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Responsible for communicating with the hHub in order to send or receive
 * documents. Manages the handling (parsing and persisting) of document
 * contents.
 * 
 * @author elevine
 * 
 */
public class DataSyncService extends Service {
	public static final String EXTRA_HRF_ID = "hrfId";
	public static final String EXTRA_TYPE = "type";
	public static final String EXTRA_OPERATION = "operation";
	public static final String EXTRA_DOCUMENT = "document";
	public static final String EXTRA_DOCUMENT_CLASS = "documentClass";
	private static final String EXTENSION_PATIENT_INFORMATION = "http://projecthdata.org/hdata/schemas/2009/06/patient_information";
	private static final String EXTENSION_PNG = "http://www.w3.org/TR/PNG";
	private static final String EXTENSION_ALLERGY = "http://projecthdata.org/hdata/schemas/2009/06/allergy";
	private static final String EXTENSION_RESULT = "http://projecthdata.org/hdata/schemas/2009/06/result";
	private static final String TAG = "DataSyncService";
	private static final String HEIGHT_LINK_PATTERN = "%/vitalsigns/bodyheight/%";

	public static enum DataType {
		PATIENT, ALLERGIES, HEIGHT, ALL
	};

	public static enum Operation {
		GET, REFRESH_ALL, POST
	};

	private SharedPreferences prefs = null;
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	/** Messenger for communicating with the service. */
	private Messenger mService = null;
	/** Flag indicating whether we have called bind on the service. */
	boolean mBound;

	private SectionDocMetadataGateway sectionGateway = null;
	private AllergiesDataGateway allergiesGateway = null;
	private BodyHeightDataGateway heightGateway = null;

	private static final int THREAD_POOL_SIZE = 5;
	private ScheduledExecutorService pool = Executors
			.newScheduledThreadPool(THREAD_POOL_SIZE);

	@Override
	public void onCreate() {
		super.onCreate();
		sectionGateway = new SectionDocMetadataGateway(this);
		allergiesGateway = new AllergiesDataGateway(this);
		heightGateway = new BodyHeightDataGateway(this);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		bindService(new Intent(DropBoxMessengerHelper.DROP_BOX_SERVICE_ACTION),
				mConnection, Context.BIND_AUTO_CREATE);
		pool.execute(new IntentHandler(intent));
		return START_STICKY;
	}

	/**
	 * Make a request for the DropBoxService to retrieve the patient's
	 * information (name and photo)
	 * 
	 * @param hrfId
	 */
	private void requestPatientInfoDocument(int hrfId) {
		// set the patient info state to syncing
		PatientDataGateway.getInstance().setStatus(DataSyncService.this,
				State.SYNCING);
		// get the URL for patient info
		Cursor cursor = sectionGateway.findByExtensionAndContentTypeAndHrfId(
				EXTENSION_PATIENT_INFORMATION,
				MediaType.APPLICATION_XML.toString(), hrfId);
		int linkColumnIndex = cursor
				.getColumnIndex(SectionDocMetadataColumns.LINK);
		cursor.moveToFirst();
		String url = cursor.getString(linkColumnIndex);
		cursor.close();

		// Create and send a message to the service, using a supported 'what'
		// value
		Message msg = Message
				.obtain(null, DropBoxMessengerHelper.MSG_GET, 0, 0);
		Bundle data = new Bundle();
		data.putString(DropBoxMessengerHelper.DATA_REMOTE_URI, url);
		data.putString(DropBoxMessengerHelper.DATA_CONTENT_TYPE,
				MediaType.APPLICATION_XML.toString());
		data.putString(DropBoxMessengerHelper.DATA_DOCUMENT_EXTENSION,
				EXTENSION_PATIENT_INFORMATION);
		data.putString(DropBoxMessengerHelper.DATA_TYPE,
				DataType.PATIENT.toString());
		msg.setData(data);
		// provide a messenger for the service to send information back
		msg.replyTo = mMessenger;
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void requestHeightDocuments(int hrfId) {
		Cursor cursor = sectionGateway
				.findByLinkContentTypeAndExtensionAndHrfI(HEIGHT_LINK_PATTERN,
						EXTENSION_RESULT, MediaType.APPLICATION_XML.toString(),
						hrfId);
		int linkColumnIndex = cursor
				.getColumnIndex(SectionDocMetadataColumns.LINK);

		if (cursor.moveToFirst()) {
			this.heightGateway.clear(this);

			do {
				String url = cursor.getString(linkColumnIndex);

				// Create and send a message to the service, using a supported
				// 'what' value
				Message msg = Message.obtain(null,
						DropBoxMessengerHelper.MSG_GET, 0, 0);
				Bundle data = new Bundle();
				data.putString(DropBoxMessengerHelper.DATA_REMOTE_URI, url);
				data.putString(DropBoxMessengerHelper.DATA_CONTENT_TYPE,
						MediaType.APPLICATION_XML.toString());
				data.putString(DropBoxMessengerHelper.DATA_DOCUMENT_EXTENSION,
						EXTENSION_RESULT);
				data.putString(DropBoxMessengerHelper.DATA_TYPE,
						DataType.HEIGHT.toString());
				msg.setData(data);
				// provide a messenger for the service to send information back
				msg.replyTo = mMessenger;
				try {
					mService.send(msg);
				} catch (RemoteException e) {
					Log.e(TAG, Log.getStackTraceString(e));
					e.printStackTrace();
				}
			} while (cursor.moveToNext());

		}
		cursor.close();

	}

	private void requestAllergiesDocuments(int hrfId) {
		Cursor cursor = sectionGateway.findByExtensionAndContentTypeAndHrfId(
				EXTENSION_ALLERGY, MediaType.APPLICATION_XML.toString(), hrfId);

		int linkColumnIndex = cursor
				.getColumnIndex(SectionDocMetadataColumns.LINK);

		if (cursor.moveToFirst()) {

			// clear out all current allergies from the database
			allergiesGateway.clear(this);
			// set status to syncing and the count of how many docs we are
			// retrieving

			if (cursor.getCount() > 0) {
				// set how many allergies we should get back
				prefs.edit()
						.putInt(SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT,
								cursor.getCount())
						.putString(SharedPrefs.PREF_ALLERGY_DATA_STATUS,
								State.SYNCING.toString()).commit();
			}

			do {
				String url = cursor.getString(linkColumnIndex);

				// Create and send a message to the service, using a supported
				// 'what' value
				Message msg = Message.obtain(null,
						DropBoxMessengerHelper.MSG_GET, 0, 0);
				Bundle data = new Bundle();
				data.putString(DropBoxMessengerHelper.DATA_REMOTE_URI, url);
				data.putString(DropBoxMessengerHelper.DATA_CONTENT_TYPE,
						MediaType.APPLICATION_XML.toString());
				data.putString(DropBoxMessengerHelper.DATA_DOCUMENT_EXTENSION,
						EXTENSION_ALLERGY);
				data.putString(DropBoxMessengerHelper.DATA_TYPE,
						DataType.ALLERGIES.toString());
				msg.setData(data);
				// provide a messenger for the service to send information back
				msg.replyTo = mMessenger;
				try {
					mService.send(msg);
				} catch (RemoteException e) {
					Log.e(TAG, Log.getStackTraceString(e));
					e.printStackTrace();
				}
			} while (cursor.moveToNext());

		}
		cursor.close();

	}

	private void requestAllDocuments(int hrfId) {
		requestPatientInfoDocument(hrfId);
		requestAllergiesDocuments(hrfId);
		requestHeightDocuments(hrfId);
	}

	/**
	 * Handler of incoming messages (callbacks) from the DropBoxService.
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// a URI that contains info regarding a DropBoxService request
			case DropBoxMessengerHelper.MSG_STATUS_URI:

				String localUri = msg.getData().getString(
						DropBoxMessengerHelper.DATA_LOCAL_URI);
				DataType type = DataType.valueOf(msg.getData().getString(
						DropBoxMessengerHelper.DATA_TYPE));
				switch (type) {
				case PATIENT:
					handleGetPatient(localUri);
					break;
				case ALLERGIES:
					handleGetAllergy(localUri);
					break;
				case HEIGHT:
					handleGetHeight(localUri);
				}
			}
		}
	}

	private void handleGetPatient(String localUrl) {

		// clear out the data
		PatientDataGateway.getInstance().clear(DataSyncService.this);

		// parse and persist the latest info
		try {
			PatientDataGateway.getInstance().parseAndSavePatient(localUrl,
					DataSyncService.this);
			PatientDataGateway.getInstance().setStatus(DataSyncService.this,
					State.READY);
		} catch (Exception e) {
			PatientDataGateway.getInstance().setStatus(DataSyncService.this,
					State.ERROR);
			e.printStackTrace();
		}
	}

	private void handleGetHeight(String localUrl) {
		heightGateway.parseAndSaveHeight(localUrl);
	}

	private void handleGetAllergy(String localUrl) {

		try {
			int allergiesCount = prefs.getInt(
					SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT, 0);
			allergiesGateway.parseAndSaveAllergy(localUrl);
			allergiesCount--;
			// update the number of allergies documents remaining
			Editor editor = prefs.edit();
			editor.putInt(SharedPrefs.PREF_DATA_SYNC_ALLERGIES_COUNT,
					allergiesCount);
			// if we aren't exprecting anymore, set the status accordingly
			if (allergiesCount <= 0) {
				editor.putString(SharedPrefs.PREF_ALLERGY_DATA_STATUS,
						State.READY.toString());
			}
			editor.commit();

		} catch (Exception e) {
			allergiesGateway.setStatus(null, State.ERROR);
			e.printStackTrace();
		}

	}

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the object we can use to
			// interact with the service. We are communicating with the
			// service using a Messenger, so here we get a client-side
			// representation of that from the raw IBinder object.
			mService = new Messenger(service);
			mBound = true;

		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			mService = null;
			mBound = false;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	private class IntentHandler implements Runnable {
		Intent intent = null;

		public IntentHandler(Intent intent) {
			this.intent = intent;
		}

		@Override
		public void run() {
			// if mService is null, wait for the connection to happen
			// reschedule this intent for some time in the future
			if (mService == null) {
				pool.schedule(new IntentHandler(intent), 500,
						TimeUnit.MILLISECONDS);
			} else {
				Bundle extras = intent.getExtras();
				int hrfId = extras.getInt(EXTRA_HRF_ID);
				DataType type = (DataType) extras.getSerializable(EXTRA_TYPE);

				switch (type) {
				case PATIENT:
					requestPatientInfoDocument(hrfId);
					break;
				case ALL:
					requestAllDocuments(hrfId);
					break;
				default:
					break;
				}
			}

		}

	}

}
