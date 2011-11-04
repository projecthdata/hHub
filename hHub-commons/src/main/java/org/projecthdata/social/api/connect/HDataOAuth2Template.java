package org.projecthdata.social.api.connect;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HDataOAuth2Template extends OAuth2Template {
    private RestTemplate restTemplate = null;



    public HDataOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
    }

    public HDataOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String authenticateUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, authenticateUrl, accessTokenUrl);
    }



    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		return this.extractAccessGrant(getRestTemplate().postForObject(accessTokenUrl, parameters, Map.class));
	}

    private AccessGrant extractAccessGrant(Map<String, Object> result) {
        return new AccessGrant((String)result.get("access_token"));
        //TODO: this fixes a bug where the original code will throw an exception when the expires_in is not a JSON
        //integer.  Uncomment this when the server begins using the expires time
//        Integer expiresIn = Integer.parseInt(result.get("expires_in").toString());
//        return createAccessGrant((String) result.get("access_token"), (String) result.get("scope"), (String) result.get("refresh_token"), expiresIn, result);
    }
}
