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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import org.projecthdata.R;

public class EhrActivity extends Activity {


    private String ehrUrl = null;
    private SharedPreferences prefs = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //check to see if an ehr has been chosen
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onSaveButton(View v) {
        SharedPreferences.Editor editor = this.prefs.edit();
        String ehrUrl = ((EditText)findViewById(R.id.hrf_url_edit_text)).getText().toString();
        editor.putString(Constants.PREF_EHR_URL, ehrUrl);
        editor.commit();

        setResult(Constants.RESULT_SAVED);
        finish();
    }
}