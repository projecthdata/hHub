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
