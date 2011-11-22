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

package org.projecthdata.ehr.viewer.activities;

import org.projecthdata.R;
import org.projecthdata.ehr.viewer.util.Constants;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Prompts the user to enter and save a URL for the Electronic Health Record (EHR)
 * that they wish to access
 * 
 * @author Eric Levine
 *
 */
public class EhrUrlFormActivity extends FragmentActivity {
	
    private SharedPreferences prefs = null;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ehr_url_form);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onSaveButton(View v) {
        SharedPreferences.Editor editor = this.prefs.edit();
        String ehrUrl = ((EditText)findViewById(R.id.hrf_url_edit_text)).getText().toString();
        editor.putString(Constants.PREF_EHR_URL, ehrUrl);
        editor.commit();
        
        //whoever started this activity should be expecting to get this result after we are finished
        setResult(Constants.RESULT_SAVED);
        finish();
    }
    

}