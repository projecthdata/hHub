package org.projecthdata.viewer.dao;

import java.util.ArrayList;
import java.util.List;

public abstract class SectionDAO<T> {
	private List<T> items = new ArrayList<T>();

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
	public void addItem(T item){
		items.add(item);
	}
	
	public void clearItems(){
		items.clear();
	}
}
