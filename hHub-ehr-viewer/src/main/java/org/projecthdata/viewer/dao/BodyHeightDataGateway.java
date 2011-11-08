package org.projecthdata.viewer.dao;

import java.io.File;

import org.projecthdata.hhub.provider.TableGateway;
import org.projecthdata.viewer.model.Result;
import org.projecthdata.viewer.provider.VITAL_SIGNSContentProvider;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class BodyHeightDataGateway extends TableGateway implements DataGateway {
	private static final String TAG = "BodyHeightDataGateway";
	private static String[] projection = VITAL_SIGNSContentProvider.VITAL_SIGNS_PROJECTION_MAP
			.keySet().toArray(new String[] {});
	private static final String HEIGHT_DATA_TYPE = "height";
	private static final String SELECTION = "data_type = ?";

	public BodyHeightDataGateway(Context context) {
		super(context, VITAL_SIGNSContentProvider.CONTENT_URI, projection);
	}

	@Override
	public void clear(Context context) {
		this.deleteAll();
	}

	@Override
	protected int deleteAll() {
		return context.getContentResolver().delete(super.uri, SELECTION,
				new String[] { HEIGHT_DATA_TYPE });
	}

	@Override
	public State getStatus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(Context context, State state) {
		// TODO Auto-generated method stub

	}

	public Cursor getAllBodyHeights() {
		return super.queryUri(SELECTION, new String[] { HEIGHT_DATA_TYPE });
	}

	public Uri insertBodyHeight(Result result) {
		ContentValues cv = new ContentValues();
		cv.put(VITAL_SIGNSContentProvider.DATA_TYPE, HEIGHT_DATA_TYPE);
		cv.put(VITAL_SIGNSContentProvider.NARRATIVE, result.getNarrative());
		cv.put(VITAL_SIGNSContentProvider.RESULT_DATE_TIME,
				result.getResultDateTime());
		cv.put(VITAL_SIGNSContentProvider.RESULT_STATUS_CODE,
				result.getResultStatusCode());
		cv.put(VITAL_SIGNSContentProvider.RESULT_VALUE, result.getResultValue());
		cv.put(VITAL_SIGNSContentProvider.RESULT_VALUE_UNIT, result.getResultValueUnit());
		return super.insert(cv);
	}

	public void parseAndSaveHeight(String path) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Serializer serializer = new Persister();
			File source = new File(path);
			try {
				Result result = serializer.read(Result.class, source);
				insertBodyHeight(result);
			} catch (Exception e) {
				Log.e(TAG, "Error parsing file at: " + path);
				e.printStackTrace();
			}
		}
	}

}
