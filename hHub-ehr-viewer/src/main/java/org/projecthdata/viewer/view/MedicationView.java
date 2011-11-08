package org.projecthdata.viewer.view;

import java.util.List;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.model.Medication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MedicationView extends LinearLayout implements OnItemSelectedListener {

	private List<Medication> medications = null;
	private ArrayAdapter<Medication> adapter = null;
	private Spinner spinner = null;
	
	public MedicationView(Context context) {
		super(context);
		inflateChildren();
	}

	public MedicationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateChildren();
	}

	public MedicationView(Context context, List<Medication> medications) {
		super(context);
		this.medications = medications;
		inflateChildren();
		setMedications(medications);
	}

	private void inflateChildren() {
		inflate(getContext(), R.layout.medication, this);
		this.spinner = (Spinner) findViewById(R.id.medication_spinner);
		spinner.setOnItemSelectedListener(this);
	}

	public void setMedications(List<Medication> medications) {
		this.medications = medications;
		adapter = new ArrayAdapter<Medication>(getContext(), android.R.layout.simple_spinner_item, this.medications);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		if(medications.size() >0){
			updateView(medications.get(0));
		}
	}

	private void updateView(Medication medication) {
		setTextViewText(R.id.med_product_name, medication
				.getManufacturedMaterial().getFreeTextProductName());
		setTextViewText(R.id.med_display_name, medication
				.getManufacturedMaterial().getDisplayName());
		String code = "Code: " + medication.getManufacturedMaterial().getCode();
		setTextViewText(R.id.med_code, code);

		String codeSystem = "Code System: "
				+ medication.getManufacturedMaterial().getCodeSystem();
		setTextViewText(R.id.med_code_system, codeSystem);
		setTextViewText(R.id.med_narrative, medication.getNarrative().trim());
		setTextViewText(R.id.med_freq_start, medication.getFormattedEffectiveDateTime());
		String period = medication.getEffectiveFrequency().getPeriod()
				+ medication.getEffectiveFrequency().getUnit();
		setTextViewText(R.id.med_freq_period, period);
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
		updateView(medications.get(position));
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
