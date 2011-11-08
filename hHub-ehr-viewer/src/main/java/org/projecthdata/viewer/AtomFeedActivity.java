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

package org.projecthdata.viewer;

import java.util.List;

import org.projecthdata.viewer.model.AtomFeed;
import org.projecthdata.viewer.model.Entry;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AtomFeedActivity extends ListActivity {

	private ProgressDialog pd = null;
	private AtomFeed feed =null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new FeedFetcherAsyncTask().execute();
	}

	private class FeedFetcherAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(AtomFeedActivity.this, "Getting feed",
					"Fetching feed...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				RestTemplate rest = new RestTemplate();

				feed = rest.getForObject(
						"http://rss.slashdot.org/Slashdot/slashdotatom",
						AtomFeed.class);
				
			} catch (RestClientException e) {
				Log.d("FeedFetcherAsyncTask", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			AtomFeedActivity.this.setListAdapter(new AtomFeedAdapter(AtomFeedActivity.this, feed.getEntries()));
			pd.dismiss();
		}
	}
	
	public static class AtomFeedAdapter extends BaseAdapter{
		private List<Entry> entries =null;
		private LayoutInflater inflator = null;
		
		public AtomFeedAdapter(Context context, List<Entry> entries){
			this.entries = entries;
			this.inflator =  LayoutInflater.from(context);
		}
		
		
		@Override
		public int getCount() {
			return entries.size(); 
		}

		@Override
		public Object getItem(int position) {
			return entries.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

            if (convertView == null) {
            	convertView = inflator.inflate(R.layout.atom_feed_item, null);
            	holder = new ViewHolder();
                holder.titleTextView = (TextView) convertView.findViewById(R.id.atom_feed_entry_name);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            Entry entry = entries.get(position);
            holder.titleTextView.setText(entry.getTitle());
            
            
			return convertView;
		}
		
		static class ViewHolder{
			public TextView titleTextView;
		}
		
	}

}
