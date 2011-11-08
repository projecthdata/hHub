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
package org.projecthdata.viewer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "hdata.db";
	private static final int DATABASE_VERSION = 1;
	private Context context = null;

	String vitalSignsTableDDL = "CREATE TABLE VITAL_SIGNS ("
		+"_id INTEGER NOT NULL PRIMARY KEY,"
		+"narrative TEXT NOT NULL,"
		+"result_date_time TEXT NOT NULL,"
		+"result_status_code TEXT NOT NULL,"
		+"result_value TEXT NOT NULL,"
		+"result_value_unit TEXT,"
		+"data_type TEXT NOT NULL);";
	
	String allergiesTableDDL = "CREATE TABLE ALLERGIES ("
		+"_id INTEGER NOT NULL PRIMARY KEY,"
		+"adverse_event_type_code TEXT NOT NULL,"
		+"adverse_event_type_code_system TEXT NOT NULL,"
		+"adverse_event_type_display_name TEXT NOT NULL,"
		+"product_code TEXT NOT NULL,"
		+"product_code_system TEXT NOT NULL,"
		+"product_display_name TEXT NOT NULL,"
		+"reaction_code TEXT NOT NULL,"
		+"reaction_code_system INTEGER NOT NULL,"
		+"reaction_display_name TEXT NOT NULL,"
		+"severity_display_name TEXT NOT NULL,"
		+"severity_code TEXT NOT NULL,"
		+"severity_code_system TEXT NOT NULL);";

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(allergiesTableDDL);
		db.execSQL(vitalSignsTableDDL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
