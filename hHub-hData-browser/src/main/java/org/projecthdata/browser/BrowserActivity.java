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

import org.projecthdata.R;
import org.projecthdata.browser.EntriesListFragment.OnRootEntryClickedListener;
import org.projecthdata.hhub.database.RootEntry;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BrowserActivity extends FragmentActivity implements
		OnRootEntryClickedListener {
	private static final String TAG = "BrowserActivity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);
	}

	@Override
	public void onRootEntryClick(RootEntry entry) {
		SectionMetadataListFragment sectionFragment = new SectionMetadataListFragment();
		sectionFragment.setRootEntry(entry);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, sectionFragment, "section")
				.commit();
		
	}

}