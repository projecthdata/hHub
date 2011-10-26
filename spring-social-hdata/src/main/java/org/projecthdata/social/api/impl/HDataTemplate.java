package org.projecthdata.social.api.impl;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.connect.HDataServiceProvider;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class HDataTemplate extends AbstractOAuth2ApiBinding implements HData{

    private static final String hDataClientId = "52552477bce63691c516b0ada3da207b";
	//private static final String accessTokenUrl = "http://zoidberg.mitre.org:8080/java-hdata-demo-web/auth/token";
	private static final String redirectUri = "hstore://projecthdata.org";
	private static final String clientSecret = "55fe6f7d1539aefd03b457332fc12dac";

    private String type = "web_server";
	private String state = "foo";
	private String responseType = "code";
    private RootOperations rootOperations= null  ;

    public HDataTemplate(String accessToken, String ehrUrl){
        super(accessToken);
	}

    public RestTemplate getRestTemplate(){
        return getRestTemplate();
    }

    public RootOperations getRootOperations(){
        return null;
    }

}
