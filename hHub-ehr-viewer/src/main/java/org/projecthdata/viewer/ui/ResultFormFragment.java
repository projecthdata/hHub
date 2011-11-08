package org.projecthdata.viewer.ui;

import java.io.File;
import java.util.StringTokenizer;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.dao.BodyHeightDataGateway;
import org.projecthdata.viewer.model.Result;
import org.projecthdata.viewer.service.DataSyncService;
import org.projecthdata.viewer.util.SharedPrefs;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ResultFormFragment extends Fragment{
	
	EditText dateTimeEditText = null;
	EditText narrativeEditText = null;
	EditText resultIdEditText = null;
	EditText resultTypeCodeEditText = null;
	EditText resultTypeCodeSystemEditText = null;
	EditText statusCodeEditText = null;
	EditText valueEditText = null;
	EditText valueEditTextUnit = null;
	RestTemplate template = new RestTemplate();
	BodyHeightDataGateway heightGateway = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.result_form, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dateTimeEditText =  (EditText)getActivity().findViewById(R.id.result_form_edit_text_date_time);
		narrativeEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_narrative);
		resultIdEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_result_id);
		resultTypeCodeEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_result_type_code);
		resultTypeCodeSystemEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_result_type_code_system);
		statusCodeEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_status_code);
		valueEditText = (EditText)getActivity().findViewById(R.id.result_form_edit_text_value);
		valueEditTextUnit = (EditText)getActivity().findViewById(R.id.result_form_edit_text_value_unit);
		heightGateway = new BodyHeightDataGateway(getActivity());
	}
	
	public void onSubmit(View v){
		Result result = new Result();
		
		//date and time
		result.setResultDateTime(dateTimeEditText.getText().toString());
		//narrative
		result.setNarrative(narrativeEditText.getText().toString());
		//result id
		result.setResultId(resultIdEditText.getText().toString());
		//result type code
		result.getResultType().setCode(resultTypeCodeEditText.getText().toString());
		//result type code system
		result.getResultType().setCodeSystem(resultTypeCodeEditText.getText().toString());
		//status code
		result.setResultStatusCode(statusCodeEditText.getText().toString());
		//value
		result.setResultValue(valueEditText.getText().toString());
		//value unit
		result.setResultValueUnit(valueEditTextUnit.getText().toString());
		
		String url = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedPrefs.PREF_HRF_URL, "");
		Uri uri =Uri.parse(url);
		uri = uri.buildUpon().appendPath("vitalsigns").appendPath("bodyheight").build();
		template.postForObject(uri.toString(), result, String.class);
		heightGateway.insertBodyHeight(result);

		getActivity().finish();
	}

	public EditText getDateTimeEditText() {
		return dateTimeEditText;
	}

	public void setDateTimeEditText(EditText dateTimeEditText) {
		this.dateTimeEditText = dateTimeEditText;
	}

	public EditText getNarrativeEditText() {
		return narrativeEditText;
	}

	public void setNarrativeEditText(EditText narrativeEditText) {
		this.narrativeEditText = narrativeEditText;
	}

	public EditText getResultIdEditText() {
		return resultIdEditText;
	}

	public void setResultIdEditText(EditText resultIdEditText) {
		this.resultIdEditText = resultIdEditText;
	}

	public EditText getResultTypeCodeEditText() {
		return resultTypeCodeEditText;
	}

	public void setResultTypeCodeEditText(EditText resultTypeCodeEditText) {
		this.resultTypeCodeEditText = resultTypeCodeEditText;
	}

	public EditText getResultTypeCodeSystemEditText() {
		return resultTypeCodeSystemEditText;
	}

	public void setResultTypeCodeSystemEditText(
			EditText resultTypeCodeSystemEditText) {
		this.resultTypeCodeSystemEditText = resultTypeCodeSystemEditText;
	}

	public EditText getStatusCodeEditText() {
		return statusCodeEditText;
	}

	public void setStatusCodeEditText(EditText statusCodeEditText) {
		this.statusCodeEditText = statusCodeEditText;
	}

	public EditText getValueEditText() {
		return valueEditText;
	}

	public void setValueEditText(EditText valueEditText) {
		this.valueEditText = valueEditText;
	}

	public EditText getValueEditTextUnit() {
		return valueEditTextUnit;
	}

	public void setValueEditTextUnit(EditText valueEditTextUnit) {
		this.valueEditTextUnit = valueEditTextUnit;
	}
	
	
	
}
