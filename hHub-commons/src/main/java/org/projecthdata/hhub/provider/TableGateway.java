/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projecthdata.hhub.provider;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public abstract class TableGateway {
	protected Context context = null;
	protected Uri uri = null;
	protected String[] projection = null;

	public TableGateway(Context context, Uri uri, String[] projection) {
		this.context = context;
		this.uri = uri;
		this.projection = projection;
	}
	
	/**
	 * Retrieves all entries
	 * @param context
	 * @return
	 */
	public Cursor getAll(Context context) {
		return queryUri(null, null);
	}
	
	/**
	 * Generates an "anded" selection statement from a List of column names
	 * 
	 * @param columns
	 * @return
	 */
	protected String buildAndedSelection(List<String> columns) {

		StringBuilder builder = new StringBuilder();
		Iterator<String> columnsIter = columns.iterator();
		while (columnsIter.hasNext()) {
			builder.append(columnsIter.next()).append(" = ? ");
			if (columnsIter.hasNext())
				builder.append(" AND ");
		}

		return builder.toString();

	}
	
	protected Cursor queryUri(String selection, String[] selectionArgs) {
		return queryUri(this.uri, selection, selectionArgs);
	}
	
	protected Cursor queryUri(Uri uri, String selection, String[] selectionArgs){
		return context.getContentResolver().query(uri, this.projection,
				selection, selectionArgs, null);
	}
	
	protected Uri insert(ContentValues values){
		return context.getContentResolver().insert(this.uri, values);
	}
	
	protected void update(ContentValues values, String where, String[] selectionArgs){
		context.getContentResolver().update(this.uri, values, where, selectionArgs);
	}
	
	protected int delete(String where, String[] selectionArgs){
		return context.getContentResolver().delete(this.uri, where, selectionArgs);
	}
	
	protected int deleteAll(){
		return context.getContentResolver().delete(this.uri, null, null);
	}
	
	public Cursor getAll(){
		return queryUri(null, null);
	}

}
