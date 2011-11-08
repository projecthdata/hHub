package org.projecthdata.viewer.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.projecthdata.viewer.R;
import org.projecthdata.viewer.dao.PatientDAO;
import org.projecthdata.viewer.model.Patient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class PatientView extends LinearLayout{
	private String imageUrl = null;
	private ViewSwitcher switcher = null;
	private String givenName = "";
	private String lastName = "";
	private String suffix = null;
	private String patientId = null;
	
	public PatientView(Context context) {
		super(context);
		inflateChildren();
	}
	
	public PatientView(Context context, AttributeSet attrs){
		super(context, attrs);
		inflateChildren();
	}
	

	private void inflateChildren(){
		inflate(getContext(), R.layout.patient, this);
		switcher = (ViewSwitcher)findViewById(R.id.patient_view_switcher);
	}
		
	private void updateName(){
		String name = lastName+", "+givenName;
		
		if(this.suffix != null){
			name +=" " + this.suffix;
		}
		
		setTextViewText(R.id.patient_name, name);
	}
	
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
		updateName();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		updateName();
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
		updateName();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		//TODO: 
		/*
		this.imageUrl = imageUrl;
		InputStream is;
		try {
			is = (InputStream) new URL(imageUrl).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			ImageView patientImage = (ImageView)findViewById(R.id.patient_image);
			patientImage.setImageDrawable(d);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/ 
	}
	
	
	
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
		setTextViewText(R.id.patient_id, this.patientId);
	}

	/**
	 * Switches between a loading view or showing the patient's data
	 * @param loading
	 */
	public void isLoading(boolean loading){
		int index = (loading)? 0 : 1;
		switcher.setDisplayedChild(index);
	}
	
	/**
	 * Utility method to set the text for a given child TextView
	 * 
	 * @param id
	 * @param text
	 */
	protected void setTextViewText(int id, String text){
		TextView tv = (TextView)findViewById(id);
		tv.setText(text);
	}
		
}
