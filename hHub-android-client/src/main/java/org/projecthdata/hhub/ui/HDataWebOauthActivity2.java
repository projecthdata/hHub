/**
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
 *
 */
package org.projecthdata.hhub.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import org.projecthdata.R;
import org.projecthdata.social.api.HData;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * With code from: https://github.com/SpringSource/spring-android-samples/blob/master/spring-android-showcase/client/src/org/springframework/android/showcase/AbstractWebViewActivity.java
 */
public class HDataWebOauthActivity2 extends Activity {
    protected static final String TAG = HDataWebOauthActivity2.class.getSimpleName();

    private String clientId = "52552477bce63691c516b0ada3da207b";

    private String redirectUri = "hstore://projecthdata.org";
    private String clientSecret = "55fe6f7d1539aefd03b457332fc12dac";


    private Activity activity;
    private WebView webView;
    private ProgressDialog progressDialog = null;
    private boolean _destroyed = false;
    private ConnectionRepository connectionRepository;
    private HDataConnectionFactory hDataConnectionFactory;
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
        this.hDataConnectionFactory = new HDataConnectionFactory(clientId, clientSecret, "server here");
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);
                if (progress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }
        });
        webView.setWebViewClient(new MyWebViewClient());

    }

    @Override
    public void onStart() {
        super.onStart();
       String url = getAuthorizeUrl();
        // display the authorization page
        getWebView().loadUrl(getAuthorizeUrl());
    }

    private String getAuthorizeUrl() {

        String type = "web_server";
        String state = "foo";
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectUri);
        params.setState(state);
        params.add("type", type);
        return hDataConnectionFactory.getOAuthOperations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);
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

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Uri uri = Uri.parse(url);
            String authCode = uri.getQueryParameter("code");


            /*
            * if there was an error with the oauth process, return the error
            * description
            *
            * The error query string will look like this:
            *
            * ?error_reason=user_denied&error=access_denied&error_description=The
            * +user+denied+your+request
            */
            if (uri.getQueryParameter("error") != null) {
                CharSequence errorReason = uri.getQueryParameter("error_description").replace("+", " ");
                Toast.makeText(getApplicationContext(), errorReason, Toast.LENGTH_LONG).show();

            } else if (authCode != null) {

                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                params.add("client_id", clientId);
                params.add("grant_type", "authorization_code");
                params.add("redirect_uri", redirectUri);
                params.add("client_secret", clientSecret);

                params.add("code", authCode);
                RestTemplate template = new RestTemplate();
                Map<String,Object> response = template.postForObject("server here", params, Map.class);
                String token = (String) response.get("access_token");
                AccessGrant accessGrant = new AccessGrant(token);
                Connection<HData> connection = hDataConnectionFactory.createConnection(accessGrant);

                    try {
                        connectionRepository.addConnection(connection);
                    } catch (DuplicateConnectionException e) {
                        // connection already exists in repository!
                    }
//                AccessGrant accessGrant = hDataConnectionFactory.getOAuthOperations().exchangeForAccess(authCode, "scope", params);


                if (accessGrant.getAccessToken() != null) {

                    Toast.makeText(HDataWebOauthActivity2.this.getApplicationContext(), accessGrant.getAccessToken(), Toast.LENGTH_LONG).show();

//                     connectionRepository.addConnection();

                }

            }
        }
    }
}