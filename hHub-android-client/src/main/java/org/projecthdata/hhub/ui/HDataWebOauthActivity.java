/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projecthdata.hhub.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import org.projecthdata.R;


/**
 * With code from: https://github.com/SpringSource/spring-android-samples/blob/master/spring-android-showcase/client/src/org/springframework/android/showcase/AbstractWebViewActivity.java
 */
public class HDataWebOauthActivity extends Activity {
    protected static final String TAG = HDataWebOauthActivity.class.getSimpleName();

    private Activity activity;

    private WebView webView;

    private ProgressDialog progressDialog = null;

    private boolean _destroyed = false;

//     private ConnectionRepository connectionRepository;
//
//    private  connectionFactory;
    // ***************************************
    // Activity methods
    // ***************************************


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        webView = new WebView(this);
        setContentView(webView);
        activity = this;

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);
                if (progress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }
        });
    }

    // ***************************************
    // Protected methods
    // ***************************************
    protected WebView getWebView() {
        return webView;
    }

    // ***************************************
    // Public methods
    // ***************************************
    public void showLoadingProgressDialog() {
        showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && !_destroyed) {
            progressDialog.dismiss();
        }
    }
}