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

public class ALLERGIESContentProvider extends ContentProvider {

	private DatabaseOpenHelper dbHelper;
	public static HashMap<String, String> ALLERGIES_PROJECTION_MAP;
	private static final String TABLE_NAME = "allergies";
	private static final String AUTHORITY = "org.projecthdata.viewer.provider.allergiescontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri ADVERSE_EVENT_TYPE_CODE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/adverse_event_type_code");
	public static final Uri ADVERSE_EVENT_TYPE_CODE_SYSTEM_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/adverse_event_type_code_system");
	public static final Uri ADVERSE_EVENT_TYPE_DISPLAY_NAME_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/adverse_event_type_display_name");
	public static final Uri PRODUCT_CODE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/product_code");
	public static final Uri PRODUCT_CODE_SYSTEM_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/product_code_system");
	public static final Uri PRODUCT_DISPLAY_NAME_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/product_display_name");
	public static final Uri REACTION_CODE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/reaction_code");
	public static final Uri REACTION_CODE_SYSTEM_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/reaction_code_system");
	public static final Uri REACTION_DISPLAY_NAME_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/reaction_display_name");
	public static final Uri SEVERITY_DISPLAY_NAME_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/severity_display_name");
	public static final Uri SEVERITY_CODE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/severity_code");
	public static final Uri SEVERITY_CODE_SYSTEM_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/severity_code_system");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int ALLERGIES = 1;
	private static final int ALLERGIES__ID = 2;
	private static final int ALLERGIES_ADVERSE_EVENT_TYPE_CODE = 3;
	private static final int ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM = 4;
	private static final int ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME = 5;
	private static final int ALLERGIES_PRODUCT_CODE = 6;
	private static final int ALLERGIES_PRODUCT_CODE_SYSTEM = 7;
	private static final int ALLERGIES_PRODUCT_DISPLAY_NAME = 8;
	private static final int ALLERGIES_REACTION_CODE = 9;
	private static final int ALLERGIES_REACTION_CODE_SYSTEM = 10;
	private static final int ALLERGIES_REACTION_DISPLAY_NAME = 11;
	private static final int ALLERGIES_SEVERITY_DISPLAY_NAME = 12;
	private static final int ALLERGIES_SEVERITY_CODE = 13;
	private static final int ALLERGIES_SEVERITY_CODE_SYSTEM = 14;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String ADVERSE_EVENT_TYPE_CODE = "adverse_event_type_code";
	public static final String ADVERSE_EVENT_TYPE_CODE_SYSTEM = "adverse_event_type_code_system";
	public static final String ADVERSE_EVENT_TYPE_DISPLAY_NAME = "adverse_event_type_display_name";
	public static final String PRODUCT_CODE = "product_code";
	public static final String PRODUCT_CODE_SYSTEM = "product_code_system";
	public static final String PRODUCT_DISPLAY_NAME = "product_display_name";
	public static final String REACTION_CODE = "reaction_code";
	public static final String REACTION_CODE_SYSTEM = "reaction_code_system";
	public static final String REACTION_DISPLAY_NAME = "reaction_display_name";
	public static final String SEVERITY_DISPLAY_NAME = "severity_display_name";
	public static final String SEVERITY_CODE = "severity_code";
	public static final String SEVERITY_CODE_SYSTEM = "severity_code_system";

	public boolean onCreate() {
		dbHelper = new DatabaseOpenHelper(getContext());
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case ALLERGIES:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(ALLERGIES_PROJECTION_MAP);
			break;
		case ALLERGIES__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("adverse_event_type_code='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("adverse_event_type_code_system='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("adverse_event_type_display_name='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_PRODUCT_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("product_code='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case ALLERGIES_PRODUCT_CODE_SYSTEM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("product_code_system='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_PRODUCT_DISPLAY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("product_display_name='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_REACTION_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("reaction_code='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case ALLERGIES_REACTION_CODE_SYSTEM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("reaction_code_system='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_REACTION_DISPLAY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("reaction_display_name='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_SEVERITY_DISPLAY_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("severity_display_name='"
					+ url.getPathSegments().get(2) + "'");
			break;
		case ALLERGIES_SEVERITY_CODE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("severity_code='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case ALLERGIES_SEVERITY_CODE_SYSTEM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("severity_code_system='"
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
		case ALLERGIES:
			return "vnd.android.cursor.dir/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES__ID:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_PRODUCT_CODE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_PRODUCT_CODE_SYSTEM:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_PRODUCT_DISPLAY_NAME:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_REACTION_CODE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_REACTION_CODE_SYSTEM:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_REACTION_DISPLAY_NAME:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_SEVERITY_DISPLAY_NAME:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_SEVERITY_CODE:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";
		case ALLERGIES_SEVERITY_CODE_SYSTEM:
			return "vnd.android.cursor.item/vnd.org.projecthdata.viewer.provider.allergies";

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
		if (URL_MATCHER.match(url) != ALLERGIES) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("allergies", "allergies", values);
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
		case ALLERGIES:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case ALLERGIES__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"adverse_event_type_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"adverse_event_type_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"adverse_event_type_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"product_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"product_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"product_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"reaction_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"reaction_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"reaction_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"severity_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"severity_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(
					TABLE_NAME,
					"severity_code_system="
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
		case ALLERGIES:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case ALLERGIES__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"adverse_event_type_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"adverse_event_type_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"adverse_event_type_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"product_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"product_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_PRODUCT_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"product_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"reaction_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"reaction_code_system="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_REACTION_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"reaction_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_DISPLAY_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"severity_display_name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_CODE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"severity_code="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case ALLERGIES_SEVERITY_CODE_SYSTEM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(
					TABLE_NAME,
					values,
					"severity_code_system="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), ALLERGIES);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#",
				ALLERGIES__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/adverse_event_type_code" + "/*",
				ALLERGIES_ADVERSE_EVENT_TYPE_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/adverse_event_type_code_system" + "/*",
				ALLERGIES_ADVERSE_EVENT_TYPE_CODE_SYSTEM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/adverse_event_type_display_name" + "/*",
				ALLERGIES_ADVERSE_EVENT_TYPE_DISPLAY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/product_code" + "/*", ALLERGIES_PRODUCT_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/product_code_system" + "/*", ALLERGIES_PRODUCT_CODE_SYSTEM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/product_display_name" + "/*",
				ALLERGIES_PRODUCT_DISPLAY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/reaction_code" + "/*", ALLERGIES_REACTION_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/reaction_code_system" + "/*",
				ALLERGIES_REACTION_CODE_SYSTEM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/reaction_display_name" + "/*",
				ALLERGIES_REACTION_DISPLAY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/severity_display_name" + "/*",
				ALLERGIES_SEVERITY_DISPLAY_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/severity_code" + "/*", ALLERGIES_SEVERITY_CODE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/severity_code_system" + "/*",
				ALLERGIES_SEVERITY_CODE_SYSTEM);

		ALLERGIES_PROJECTION_MAP = new HashMap<String, String>();
		ALLERGIES_PROJECTION_MAP.put(_ID, "_id");
		ALLERGIES_PROJECTION_MAP.put(ADVERSE_EVENT_TYPE_CODE,
				"adverse_event_type_code");
		ALLERGIES_PROJECTION_MAP.put(ADVERSE_EVENT_TYPE_CODE_SYSTEM,
				"adverse_event_type_code_system");
		ALLERGIES_PROJECTION_MAP.put(ADVERSE_EVENT_TYPE_DISPLAY_NAME,
				"adverse_event_type_display_name");
		ALLERGIES_PROJECTION_MAP.put(PRODUCT_CODE, "product_code");
		ALLERGIES_PROJECTION_MAP
				.put(PRODUCT_CODE_SYSTEM, "product_code_system");
		ALLERGIES_PROJECTION_MAP.put(PRODUCT_DISPLAY_NAME,
				"product_display_name");
		ALLERGIES_PROJECTION_MAP.put(REACTION_CODE, "reaction_code");
		ALLERGIES_PROJECTION_MAP.put(REACTION_CODE_SYSTEM,
				"reaction_code_system");
		ALLERGIES_PROJECTION_MAP.put(REACTION_DISPLAY_NAME,
				"reaction_display_name");
		ALLERGIES_PROJECTION_MAP.put(SEVERITY_DISPLAY_NAME,
				"severity_display_name");
		ALLERGIES_PROJECTION_MAP.put(SEVERITY_CODE, "severity_code");
		ALLERGIES_PROJECTION_MAP.put(SEVERITY_CODE_SYSTEM,
				"severity_code_system");

	}
}
