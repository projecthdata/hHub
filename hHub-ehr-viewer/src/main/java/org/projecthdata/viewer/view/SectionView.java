package org.projecthdata.viewer.view;


import java.util.Collections;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class SectionView<T> extends LinearLayout{
	
	protected T data = null;
	
	public SectionView(Context context) {
		super(context);
		inflateChildren();
	}
	
	public SectionView(Context context, AttributeSet attrs){
		super(context, attrs);
		inflateChildren();
	}
	
	public SectionView(Context context, T data) {
		super(context);
		this.data = data;
		inflateChildren();
		updateView();
	}
	
	protected abstract void inflateChildren();
	
	public void setData(T data){
		this.data = data;
		updateView();
	}
	
	protected abstract void updateView();
	
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
