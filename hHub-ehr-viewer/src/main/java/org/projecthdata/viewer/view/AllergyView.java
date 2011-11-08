package org.projecthdata.viewer.view;

import java.util.List;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.model.Allergy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AllergyView extends LinearLayout implements OnItemSelectedListener {

	private List<Allergy> allergies = null;
	private ArrayAdapter<Allergy> adapter = null;
	private Spinner spinner = null;
	
	public AllergyView(Context context) {
		super(context);
		inflateChildren();
	}

	public AllergyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateChildren();
	}

	public AllergyView(Context context, List<Allergy> allergies) {
		super(context);
		this.allergies = allergies;
		inflateChildren();
		setAllergies(allergies);
	}

	private void inflateChildren() {
		inflate(getContext(), R.layout.allergy, this);
		this.spinner = (Spinner) findViewById(R.id.allergy_spinner);
		spinner.setOnItemSelectedListener(this);
	}

	public void setAllergies(List<Allergy> allergies) {
		this.allergies = allergies;
		adapter = new ArrayAdapter<Allergy>(getContext(), android.R.layout.simple_spinner_item, this.allergies);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		if(allergies.size() >0){
			updateView(allergies.get(0));
		}
	}

	private void updateView(Allergy allergy) {
		setTextViewText(R.id.allergy_event_type, allergy.getAdverseEventType().getDisplayName());
		setTextViewText(R.id.allergy_product, allergy.getProduct().getDisplayName());
		setTextViewText(R.id.allergy_reaction, allergy.getReaction().getDisplayName());
		setTextViewText(R.id.allergy_severity, allergy.getSeverity().getDisplayName());
		setTextViewText(R.id.allergy_status, allergy.getAlertStatus().getDisplayName());
		
	}
	
	/**
	 * Convenience method for setting the text of a TextView
	 * 
	 * @param id - the R.id of the textview
	 * @param text - the text to set
	 */
	private void setTextViewText(int id, String text) {
		TextView tv = (TextView) findViewById(id);
		tv.setText(text);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		updateView(allergies.get(position));
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
