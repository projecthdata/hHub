package org.projecthdata.social.api.impl;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.RootOperations;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

import java.util.List;

public class HDataTemplate extends AbstractOAuth2ApiBinding implements HData{

    private static final String hDataClientId = "52552477bce63691c516b0ada3da207b";
	//private static final String accessTokenUrl = "http://zoidberg.mitre.org:8080/java-hdata-demo-web/auth/token";
	private static final String redirectUri = "hstore://projecthdata.org";
	private static final String clientSecret = "55fe6f7d1539aefd03b457332fc12dac";

    private String type = "web_server";
	private String state = "foo";
	private String responseType = "code";
    private RootOperations rootOperations= null;
    //url of the electronic health record
    private final String ehrUrl;

    public HDataTemplate(String accessToken, String ehrUrl){
        super(accessToken);
        this.ehrUrl = ehrUrl;
        this.getRestTemplate().getMessageConverters().add(new SimpleXmlHttpMessageConverter());
	}



    @Override
    public String getEhrUrl() {
        return this.ehrUrl;
    }

    public RootOperations getRootOperations(){
        if(this.rootOperations == null){
            rootOperations = new RootTemplate(this, this.getRestTemplate(), isAuthorized());
        }
        return rootOperations;
    }

}
