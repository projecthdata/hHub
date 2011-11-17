package org.projecthdata.weight.ui;

import java.sql.SQLException;
import java.util.List;

import org.projecthdata.weight.OrmProvider;
import org.projecthdata.weight.database.WeightDatabaseHelper;
import org.projecthdata.weight.model.WeightReading;


import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class WeightListFragment extends ListFragment{
	
	private WeightDatabaseHelper databaseHelper = null;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		databaseHelper = ((OrmProvider)getActivity()).getDatabaseHelper();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			List<WeightReading> readings = databaseHelper.getWeightDao().queryForAll();
			setListAdapter(new WeightReadingAdapter(getActivity(), readings));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
}
