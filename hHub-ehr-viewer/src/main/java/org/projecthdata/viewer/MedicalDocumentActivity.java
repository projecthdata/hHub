package org.projecthdata.viewer;

import java.util.ArrayList;
import java.util.List;

import org.projecthdata.viewer.widget.ListAdapter;
import org.projecthdata.viewer.widget.MedicalItemViewHolder;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MedicalDocumentActivity extends ListActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<String[]> testList = new ArrayList<String[]>();
		for(int index = 0; index < 30; index++){
			String title = "Title " + index;
			String content = "Lorem ipsum foo bar";
			String[] data = {title, content };
			testList.add(data);
		}
		setListAdapter(new MedicalDocumentListAdapter(this, testList, R.layout.medical_item));
	}
	
	private class MedicalDocumentListAdapter extends ListAdapter< String[]  , MedicalItemViewHolder>{

		public MedicalDocumentListAdapter(Context context, List<String[]> items, int layoutId) {
			super(context, items, layoutId);
		}

		@Override
		public MedicalItemViewHolder createViewHolder() {
			return new MedicalItemViewHolder();
		}

		@Override
		public void populatViewHolder(MedicalItemViewHolder holder, View convertView) {
			holder.setTitleView((TextView)convertView.findViewById(R.id.med_item_title));
			holder.setDescriptionView((TextView)convertView.findViewById(R.id.med_item_desc));
		}

		@Override
		public void setupViews(String[] item,
				MedicalItemViewHolder holder) {
			holder.getTitleView().setText(item[0]);
			holder.getDescriptionView().setText(item[1]);
		}
		
	}
	
	
	
}
