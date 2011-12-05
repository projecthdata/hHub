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
package org.projecthdata.ehr.viewer.widget;

import java.util.List;

import org.projecthdata.ehr.viewer.R;
import org.projecthdata.ehr.viewer.model.WeightReading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeightReadingListAdapter extends ArrayAdapter<WeightReading>{
	LayoutInflater inflater = null;
	
	public WeightReadingListAdapter(Context context, List<WeightReading> readings){
		super(context, R.layout.weight_list_item, readings);
		this.inflater = LayoutInflater.from(getContext());
		
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.weight_list_item, null);
		}
		WeightReading reading = (WeightReading)getItem(position);
		String weightValue = reading.getResultValue() + " " + reading.getResultValueUnit();
		((TextView)convertView.findViewById(R.id.weight_list_item_value_text)).setText(weightValue);
		((TextView)convertView.findViewById(R.id.weight_list_item_date_text)).setText(reading.getFormattedDateTime());
		return convertView;
	}
}
