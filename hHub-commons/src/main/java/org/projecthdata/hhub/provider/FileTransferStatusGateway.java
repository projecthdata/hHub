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

import org.projecthdata.hhub.provider.HDataContract.FileTransferStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.NetworkInfo.DetailedState;

public class FileTransferStatusGateway extends TableGateway{

	public FileTransferStatusGateway(Context context) {
		super(context, FileTransferStatus.CONTENT_URI, FileTransferStatusColumns.getInstance().getAllColumnsProjection());
	}
	
	public Uri insert(String localUri, String remoteUri, DetailedState status){
		ContentValues cv = new ContentValues();
		cv.put(FileTransferStatusColumns.LOCAL_URI, localUri);
		cv.put(FileTransferStatusColumns.REMOTE_URI, remoteUri);
		cv.put(FileTransferStatusColumns.STATUS, status.toString());
		return super.insert(cv);
	}
	
	private static final String SELECTION_BY_ID = "_id = ?";
	
	public DetailedState getState(Integer id){
		DetailedState currentState = null;
		Cursor cursor = queryUri(SELECTION_BY_ID, new String[]{id.toString()});
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			int colIndex = cursor.getColumnIndex(FileTransferStatusColumns.STATUS);
			String state = cursor.getString(colIndex);
			currentState = DetailedState.valueOf(state);
		}
		
		return currentState;
	}
	
	
	
	
}
