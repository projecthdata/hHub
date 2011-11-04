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