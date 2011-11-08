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


public class BodyHeightView extends SectionView<List<Result>>{
	
	public BodyHeightView(Context context) {
		super(context);
	}
	
	public BodyHeightView(Context context, AttributeSet attrs){
		super(context, attrs);
		
	}
	
	public BodyHeightView(Context context, List<Result> data){
		super(context, data);
	}


	@Override
	protected void inflateChildren(){
		inflate(getContext(), R.layout.bodyheight, this);
	}
	
	
	@Override
	protected void updateView(){
		LinearLayout heightContainer = (LinearLayout)findViewById(R.id.body_height_section);
		Collections.sort(data);
		
		for(Result height : data){
			
			String info = height.getResultValue()+height.getResultValueUnit()+"\t\t"+height.getFormattedDateTime();
			TextView tv = (TextView) inflate(getContext(), R.layout.content_body_textview, null);
			tv.setText(info);
			
			heightContainer.addView(tv);
			
		}

	}
		
}
