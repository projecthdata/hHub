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

import java.util.List;

public class RootEntryAdapter extends ArrayAdapter<RootEntry> {
    private Context context = null;

    public RootEntryAdapter(Context context, List<RootEntry> entryList) {
        super(context, R.layout.root_entry_row, entryList);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.root_entry_row, null);
        }
        RootEntry entry = getItem(position);
        ((TextView) convertView.findViewById(R.id.root_entries_list_item_content_type)).setText(entry.getContentType());
        ((TextView) convertView.findViewById(R.id.root_entries_list_item_path)).setText(entry.getPath());
        ((TextView) convertView.findViewById(R.id.root_entries_list_item_extension)).setText(entry.getExtension());
        return convertView;
    }
}
