/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projecthdata.browser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.projecthdata.R;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;

import java.util.List;

/**
 * Allows a ListActivity or ListFragment to render a  List of RootEntry objects.  
 * These contain information about each entry in an hData root document. 
 * 
 * @author Eric Levine
 *
 */
public class SectionDocMetadataAdapter extends ArrayAdapter<SectionDocMetadata> {
    private Context context = null;

    public SectionDocMetadataAdapter(Context context, List<SectionDocMetadata> entryList) {
        super(context, R.layout.section_metadata_list_item, entryList);
        this.context = context;
    }

    public SectionDocMetadataAdapter(Context context, SectionDocMetadata[] entries) {
        super(context, R.layout.section_metadata_list_item, entries);
        this.context = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO: use the ViewHolder pattern here
    	
    	if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.root_entry_row, null);
        }
    	SectionDocMetadata metadata = getItem(position);
    	
        ((TextView) convertView.findViewById(R.id.section_metadata_list_item_title)).setText(metadata.getTitle());
//        ((TextView) convertView.findViewById(R.id.section_metadata_list_item_content_type)).setText(metadata.getContentType());
        ((TextView) convertView.findViewById(R.id.section_metadata_list_item_link)).setText(metadata.getLink());
        
        return convertView;
    }
}
