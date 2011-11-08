/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package org.projecthdata.viewer.provider;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import org.projecthdata.viewer.db.*;

public class VITAL_SIGNSContentProvider extends ContentProvider {

	private DatabaseOpenHelper dbHelper;
	public static HashMap<String, String> VITAL_SIGNS_PROJECTION_MAP;
	private static final String TABLE_NAME = "vital_signs";
	private static final String AUTHORITY = "org.projecthdata.viewer.provider.vital_signscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri NARRATIVE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/narrative");
	public static final Uri RESULT_DATE_TIME_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/result_date_time");
	public static final Uri RESULT_STATUS_CODE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/result_status_code");
	public static final Uri RESULT_VALUE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/result_value");
	public static final Uri DATA_TYPE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/data_type");
	public static final Uri RESULT_VALUE_UNIT_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/result_value_unit");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int VITAL_SIGNS = 1;
	private static final int VITAL_SIGNS__ID = 2;
	private static final int VITAL_SIGNS_NARRATIVE = 3;
	private static final int VITAL_SIGNS_RESULT_DATE_TIME = 4;
	private static final int VITAL_SIGNS_RESULT_STATUS_CODE = 5;
	private static final int VITAL_SIGNS_RESULT_VALUE = 6;
	private static final int VITAL_SIGNS_DATA_TYPE = 7;
	private static final int VITAL_SIGNS_RESULT_VALUE_UNIT = 8;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String NARRATIVE = "narrative";
	public static final String RESULT_DATE_TIME = "result_date_time";
	public static final String RESULT_STATUS_CODE = "result_status_code";
	public static final String RESULT_VALUE = "result_value";
	public static final String DATA_TYPE = "data_type";
	public static final String RESULT_VALUE_UNIT = "result_value_unit";

	public boolean onCreate() {
		dbHelper = new DatabaseOpenHelper(getContext());
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case VITAL_SIGNS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(VITAL_SIGNS_PROJECTION_MAP);
			break;
		case VITAL_SIGNS__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case VITAL_SIGNS_NARRATIVE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("narrative='" + url.getPathSegments().get(2) + "'");
			break;
		case VITAL_SIGNS_RESULT_DATE_TIME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("result_date_time='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case VITAL_SIGNS_RESULT_STATUS_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("result_status_code='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case VITAL_SIGNS_RESULT_VALUE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("result_value='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case VITAL_SIGNS_DATA_TYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("data_type='" + url.getPathSegments().get(2) + "'");
			break;
		case VITAL_SIGNS_RESULT_VALUE_UNIT:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("result_value_unit='"
					+ url.getPathSegments().get(2) + "'");
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case VITAL_SIGNS:
			return "vnd.android.cursor.dir/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS__ID:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_NARRATIVE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_RESULT_DATE_TIME:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_RESULT_STATUS_CODE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_RESULT_VALUE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_DATA_TYPE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";
		case VITAL_SIGNS_RESULT_VALUE_UNIT:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.vital_signs";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != VITAL_SIGNS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("vital_signs", "vital_signs", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case VITAL_SIGNS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case VITAL_SIGNS__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_NARRATIVE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"narrative="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_DATE_TIME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"result_date_time="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_STATUS_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"result_status_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_VALUE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"result_value="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_DATA_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"data_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_VALUE_UNIT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"result_value_unit="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case VITAL_SIGNS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case VITAL_SIGNS__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_NARRATIVE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"narrative="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_DATE_TIME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"result_date_time="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_STATUS_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"result_status_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_VALUE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"result_value="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_DATA_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"data_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case VITAL_SIGNS_RESULT_VALUE_UNIT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"result_value_unit="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), VITAL_SIGNS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#",
				VITAL_SIGNS__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/narrative"
				+ "/*", VITAL_SIGNS_NARRATIVE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/result_date_time" + "/*", VITAL_SIGNS_RESULT_DATE_TIME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/result_status_code" + "/*", VITAL_SIGNS_RESULT_STATUS_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/result_value" + "/*", VITAL_SIGNS_RESULT_VALUE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/data_type"
				+ "/*", VITAL_SIGNS_DATA_TYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/result_value_unit" + "/*", VITAL_SIGNS_RESULT_VALUE_UNIT);

		VITAL_SIGNS_PROJECTION_MAP = new HashMap<String, String>();
		VITAL_SIGNS_PROJECTION_MAP.put(_ID, "_id");
		VITAL_SIGNS_PROJECTION_MAP.put(NARRATIVE, "narrative");
		VITAL_SIGNS_PROJECTION_MAP.put(RESULT_DATE_TIME, "result_date_time");
		VITAL_SIGNS_PROJECTION_MAP
				.put(RESULT_STATUS_CODE, "result_status_code");
		VITAL_SIGNS_PROJECTION_MAP.put(RESULT_VALUE, "result_value");
		VITAL_SIGNS_PROJECTION_MAP.put(DATA_TYPE, "data_type");
		VITAL_SIGNS_PROJECTION_MAP
				.put(RESULT_VALUE_UNIT, "result_value_unit");

	}
}
