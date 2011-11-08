package org.projecthdata.viewer.ui;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.projecthdata.viewer.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class HeightFormActivity extends FragmentActivity {
	private DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
	private ResultFormFragment frag = null;
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.height_form_activity);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		frag = (ResultFormFragment)getSupportFragmentManager().findFragmentById(R.id.height_form_fragment);
		frag.getResultIdEditText().setText(UUID.randomUUID().toString());
		frag.getNarrativeEditText().setText("Body height");
		frag.getResultTypeCodeEditText().setText("50373000");
		frag.getResultTypeCodeSystemEditText().setText("2.16.840.1.113883.6.96");
		frag.getDateTimeEditText().setText(new DateTime().toString(dateFormatter));
		frag.getValueEditTextUnit().setText("cm");
		frag.getStatusCodeEditText().setText("completed");
	}
	
	public void onSubmit(View v){
		frag.onSubmit(v);
	}
}
