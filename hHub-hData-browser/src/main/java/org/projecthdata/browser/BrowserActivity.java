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

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.database.HDataDatabaseHelper;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.Section;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.xml.sax.SAXException;

import java.sql.SQLException;
import java.util.List;

public class BrowserActivity extends OrmLiteBaseListActivity<HDataDatabaseHelper> {
    private static final String TAG = "BrowserActivity";
    private ConnectionRepository connectionRepository = null;
    private HDataConnectionFactory hDataConnectionFactory = null;
    private String ehrUrl = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ehrUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREF_EHR_URL, "");
        this.connectionRepository = getApplicationContext().getConnectionRepository();
        this.hDataConnectionFactory = getApplicationContext().getHDataConnectionFactory(ehrUrl);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isConnected()) {
            doWebOauthActivity();
        } else {
            Connection<HData> connection = connectionRepository.getPrimaryConnection(HData.class);
            Root root = null;
            try {
                root = connection.getApi().getRootOperations().getRoot();
                for (Section section : root.getSections()) {
                    RootEntry rootEntry = new RootEntry();
                    rootEntry.setContentType(section.getExtension().getContentType());
                    rootEntry.setPath(section.getPath());
                    rootEntry.setExtension(section.getExtension().getContent());
                    getHelper().getRootEntryDao().create(rootEntry);
                }

                List<RootEntry> entries = getHelper().getDao(RootEntry.class).queryForAll();
                setListAdapter(new RootEntryAdapter(this, entries));


            } catch (ExpiredAuthorizationException exception) {
                //TODO: when the servers supports refresh tokens, then do a refresh here
                connectionRepository.removeConnection(connection.getKey());
                doWebOauthActivity();
            }
            catch(SQLException sqle){
                Log.e(TAG, "Error inserting into database", sqle);
            }
        }


    }

    private void doWebOauthActivity() {
        Intent callbackIntent = new Intent(getApplicationContext(), BrowserActivity.class);
        Intent intent = new Intent(this, HDataWebOauthActivity.class);
        intent.putExtra(HDataWebOauthActivity.EXTRA_CALLBACK_INTENT, callbackIntent);
        intent.putExtra(HDataWebOauthActivity.EXTRA_EHR_URL, ehrUrl);
        startActivity(intent);
        finish();
    }

    @Override
    public HHubApplication getApplicationContext() {
        return (HHubApplication) super.getApplicationContext();
    }

    private boolean isConnected() {
        Connection<HData> connection = connectionRepository.findPrimaryConnection(HData.class);
        return (connection != null);
    }
}