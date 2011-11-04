package org.projecthdata.social.api.connect;

import org.projecthdata.social.api.HData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

/**
 *  From: http://static.springsource.org/spring-social/docs/1.0.x/reference/html/implementing.html
 *  Provides a simple interface for establishing Connections.  May be user directly, or added to a registry where it can
 *  be used by the framework to establish connections in a dynamic, self-service manner.
 */
public class HDataConnectionFactory extends OAuth2ConnectionFactory<HData> {
    private String clientSecret;
    private String clientId;

    /**
     *
     * @param clientId
     * @param clientSecret
     * @param ehrUrl - The EHR URL, also used as the providerId to uniquely identify the HDataServiceProvider
     */
    public HDataConnectionFactory( String clientId, String clientSecret, String ehrUrl) {
        super(ehrUrl, new HDataServiceProvider(clientId, clientSecret, ehrUrl), new HDataAdapter());
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public OAuth2Operations getOAuthOperations() {
        return super.getOAuthOperations();
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
