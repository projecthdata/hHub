/**
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
 *
 */
package org.projecthdata.browser;

import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.OrmManager;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class SectionMetadataListFragment extends ListFragment {
	
	private OrmManager<HDataDatabaseHelper> ormManager = null;
	private RootEntry rootEntry = null;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ormManager = new OrmManager<HDataDatabaseHelper>(getActivity(), HDataDatabaseHelper.class);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(this.rootEntry != null){
			updateAdapter();
		}
	}
	
	
	public void setRootEntry(RootEntry rootEntry){
		this.rootEntry = rootEntry;
		if(getActivity() != null){
			updateAdapter();
		}

	}
	
	
	private void updateAdapter(){
		SectionDocMetadata[] array = this.rootEntry.getSectionMetadata().toArray(new SectionDocMetadata[0]);
		SectionDocMetadataAdapter adapter = new SectionDocMetadataAdapter(getActivity(), array);
		setListAdapter(adapter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ormManager.release();
	}
	
}
