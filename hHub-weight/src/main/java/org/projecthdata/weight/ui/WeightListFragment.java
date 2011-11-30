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
