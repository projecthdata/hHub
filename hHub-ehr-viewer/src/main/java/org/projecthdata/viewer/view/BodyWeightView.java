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


public class BodyWeightView extends SectionView<List<Result>>{
	
	public BodyWeightView(Context context) {
		super(context);
	}
	
	public BodyWeightView(Context context, AttributeSet attrs){
		super(context, attrs);
		
	}
	
	public BodyWeightView(Context context, List<Result> data){
		super(context, data);
	}


	@Override
	protected void inflateChildren(){
		inflate(getContext(), R.layout.bodyweight, this);
	}
	
	
	@Override
	protected void updateView(){
		LinearLayout weightContainer = (LinearLayout)findViewById(R.id.body_weight_section);
		Collections.sort(data);
		
		for(Result weight : data){
			
			String info = weight.getResultValue()+weight.getResultValueUnit()+"\t\t"+weight.getFormattedDateTime();
			TextView tv = (TextView) inflate(getContext(), R.layout.content_body_textview, null);
			tv.setText(info);
			
			weightContainer.addView(tv);
			
		}

	}
		
}
