package org.projecthdata.viewer;

import java.io.InputStream;

import org.projecthdata.viewer.model.Medication;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MedicationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medication);

		Serializer serializer = new Persister();
		InputStream source = getResources().openRawResource(
				org.projecthdata.viewer.R.raw.clopidogrel);


		try {
			Medication med = serializer.read(Medication.class, source);
			setTextViewText(R.id.med_product_name, med.getManufacturedMaterial().getFreeTextProductName());
			setTextViewText(R.id.med_display_name, med.getManufacturedMaterial().getDisplayName());
			String code = "Code: " +med.getManufacturedMaterial().getCode();
			setTextViewText(R.id.med_code, code);
			
			String codeSystem = "Code System: "+med.getManufacturedMaterial().getCodeSystem();
			setTextViewText(R.id.med_code_system, codeSystem);
			setTextViewText(R.id.med_narrative, med.getNarrative().trim());
			setTextViewText(R.id.med_freq_start, med.getEffectiveDateTime());
			String period = med.getEffectiveFrequency().getPeriod() + med.getEffectiveFrequency().getUnit();
			setTextViewText(R.id.med_freq_period, period);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private void setTextViewText(int id, String text){
		TextView tv = (TextView)findViewById(id);
		tv.setText(text);
	}
	
}
