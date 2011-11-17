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
    	((TextView) convertView.findViewById(R.id.weight_list_item_value)).setText(weight.getResultValue().toString());
    	return convertView;
	}
}
