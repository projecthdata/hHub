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