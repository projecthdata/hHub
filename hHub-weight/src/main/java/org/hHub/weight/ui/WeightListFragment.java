package org.hHub.weight.ui;

import java.sql.SQLException;
import java.util.List;

import org.hHub.weight.OrmProvider;
import org.hHub.weight.database.WeightDatabaseHelper;
import org.hHub.weight.model.WeightReading;


import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class WeightListFragment extends ListFragment{
	
	private WeightDatabaseHelper databaseHelper = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		databaseHelper = ((OrmProvider)getActivity()).getDatabaseHelper();
		try {
			List<WeightReading> readings = databaseHelper.getWeightDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	
}
