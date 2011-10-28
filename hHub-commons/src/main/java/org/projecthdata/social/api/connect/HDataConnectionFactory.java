package org.projecthdata.social.api.connect;

import org.projecthdata.social.api.HData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 *  From: http://static.springsource.org/spring-social/docs/1.0.x/reference/html/implementing.html
 *  Provides a simple interface for establishing Connections.  May be user directly, or added to a registry where it can
 *  be used by the framework to establish connections in a dynamic, self-service manner.
 */
public class HDataConnectionFactory extends OAuth2ConnectionFactory<HData> {

    public HDataConnectionFactory(String clientId, String clientSecret, String ehrUrl) {
        super("hdata", new HDataServiceProvider(clientId, clientSecret, ehrUrl), new HDataAdapter());
    }
}
