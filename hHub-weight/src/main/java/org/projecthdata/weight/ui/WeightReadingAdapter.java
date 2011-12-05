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

import java.util.List;

import org.projecthdata.weight.R;
import org.projecthdata.weight.model.WeightReading;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeightReadingAdapter extends ArrayAdapter<WeightReading> {
	private Context context = null;
	
	public WeightReadingAdapter(Context context, List<WeightReading> entryList) {
        super(context, R.layout.weight_list_item, entryList);
        this.context = context;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO: use the ViewHolder pattern here
    	
    	if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.weight_list_item, null);
    	}
    	WeightReading weight = getItem(position);
    	((TextView) convertView.findViewById(R.id.weight_list_item_value_text)).setText(weight.getResultValue().toString());
    	String formattedDate = weight.getFormattedDateTime();
    	TextView dateView = (TextView)convertView.findViewById(R.id.weight_list_item_date_text);
    	
    	dateView.setText(formattedDate);
    	return convertView;
	}
}
