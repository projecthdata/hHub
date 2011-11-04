package org.projecthdata.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import org.projecthdata.hhub.HHubApplication;
import org.projecthdata.hhub.ui.HDataWebOauthActivity;
import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.Section;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.projecthdata.social.api.connect.HDataConnectionFactory;

public class BrowserActivity extends Activity {

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
                    Log.d("HDATA", section.getPath());
                }
            } catch (ExpiredAuthorizationException exception) {
                //TODO: when the servers supports refresh tokens, then do a refresh here
                connectionRepository.removeConnection(connection.getKey());
                doWebOauthActivity();
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