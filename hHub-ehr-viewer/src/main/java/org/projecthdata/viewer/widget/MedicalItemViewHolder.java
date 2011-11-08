package org.projecthdata.viewer.widget;

import android.widget.TextView;

public class MedicalItemViewHolder {
	public TextView titleView = null;
	public TextView descriptionView = null;
	
	public TextView getTitleView() {
		return titleView;
	}
	public void setTitleView(TextView titleView) {
		this.titleView = titleView;
	}
	public TextView getDescriptionView() {
		return descriptionView;
	}
	public void setDescriptionView(TextView descriptionView) {
		this.descriptionView = descriptionView;
	}
	
	
}
