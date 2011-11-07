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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import org.projecthdata.R;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.social.api.connect.HDataConnectionFactory;

public class ControllerActivity extends Activity {
    private SharedPreferences prefs =null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ehrUrl = prefs.getString(Constants.PREF_EHR_URL, null);

        if (ehrUrl != null) {
            addConnectionFactory();
            startActivity(new Intent(this, BrowserActivity.class));
            finish();
        } else {
            startActivityForResult(new Intent(this, EhrActivity.class), Constants.RESULT_SAVED);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == Constants.RESULT_SAVED) {
            addConnectionFactory();
            startActivity(new Intent(this, BrowserActivity.class));
            finish();
        }
    }

    private void addConnectionFactory() {
        String clientSecret = getString(R.string.clientSecret);
        String clientId = getString(R.string.clientId);
        String ehrUrl = prefs.getString(Constants.PREF_EHR_URL, null);
        try{
            this.getApplicationContext().getConnectionFactoryRegistry().addConnectionFactory(new HDataConnectionFactory(clientId, clientSecret, ehrUrl));
        }
        catch (IllegalArgumentException e){
            //expected if there is already a ConnectionFactory for this url
        }
    }

    @Override
    public HHubApplication getApplicationContext() {
        return (HHubApplication) super.getApplicationContext();
    }
}