package org.projecthdata.viewer.view;

import java.util.Collections;
import java.util.List;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.model.Patient;
import org.projecthdata.viewer.model.Result;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BloodPressureView extends SectionView<List<Result>>{
	
	public BloodPressureView(Context context) {
		super(context);
	}
	
	public BloodPressureView(Context context, AttributeSet attrs){
		super(context, attrs);
		
	}
	
	public BloodPressureView(Context context, List<Result> data){
		super(context, data);
	}


	@Override
	protected void inflateChildren(){
		inflate(getContext(), R.layout.bloodpressure, this);
	}
	
	
	@Override
	protected void updateView(){
		LinearLayout container = (LinearLayout)findViewById(R.id.bp_readings_section);
		Collections.sort(data);
		
		for(Result bpReading : data){
			
			String info = bpReading.getResultValue().trim()+bpReading.getResultValueUnit()+"\t\t"+bpReading.getFormattedDateTime();
			TextView tv = (TextView) inflate(getContext(), R.layout.content_body_textview, null);
			tv.setText(info);
			container.addView(tv);
	
		}

	}
		
}
