package org.projecthdata.social.api.connect;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.impl.HDataTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.support.URIBuilder;

import java.net.URI;
import java.util.StringTokenizer;

public class HDataServiceProvider extends AbstractOAuth2ServiceProvider<HData> {
    private URI ehrUrl = null;
    public static final String authorizeUrlPath = "/auth/dialog";
    public static final String accessTokenUrlPath = "/auth/token";
    //TODO: remove hard coded URLs
//    private static final String accessTokenUrl = "http://zoidberg.mitre.org:8080/java-hdata-demo-web/auth/token";
//    private static final String authorizationUrl = "http://zoidberg.mitre.org:8080/java-hdata-demo-web/auth/dialog";

    /**
     * @param clientId
     * @param clientSecret
     * @param ehrUrl       - the URL to an electronic health record
     */
    public HDataServiceProvider(String clientId, String clientSecret, String ehrUrl) {
        //TODO: agree on a way to dynamically determine these URLs from the ehr
        //super(new OAuth2Template(clientId, clientSecret, getAuthorizeUrl(ehrUrl), getAccessTokenUrl(ehrUrl)));

        super(new HDataOAuth2Template(clientId, clientSecret, HDataServiceProvider.getAuthorizeUrl(ehrUrl), getAccessTokenUrl(ehrUrl)));
        this. ehrUrl = URI.create(ehrUrl);
    }

    @Override
    public HData getApi(String accessToken) {
        return new HDataTemplate(accessToken, ehrUrl.toString());
    }

    /**
     * Get the authorization URL, based on the url for an EHR.  This will be the
     * host of the EHR with the path /auth/dialog
     *
     * @param ehrUrl
     * @return
     */
    public static String getAuthorizeUrl(String ehrUrl) {
        StringBuilder builder = getBaseUrl(ehrUrl);
        builder.append(authorizeUrlPath);
        return builder.toString();
    }

    /**
     * Get the access token url, based on the url for an EHR.  This will be the
     * host of the EHR with the path /auth/token
     *
     * @param ehrUrl
     * @return
     */
    public static String getAccessTokenUrl(String ehrUrl) {
        StringBuilder builder = getBaseUrl(ehrUrl);
        builder.append(accessTokenUrlPath);
        return builder.toString();
    }

    private static StringBuilder getBaseUrl(String ehrUrl) {
        URI uri = URIBuilder.fromUri(ehrUrl).build();
        StringBuilder builder = new StringBuilder();
        builder.append(uri.getScheme()).append("://");
        builder.append(uri.getHost());
        if (uri.getPort() >= 0) {
            builder.append(":").append(uri.getPort());
        }
        if (uri.getPath() != null) {
            StringTokenizer tokenizer = new StringTokenizer(uri.getPath(), "/");
            //if there is more than one path element, then the first one should be the webapp name
            if (tokenizer.countTokens() > 1) {
                builder.append("/").append(tokenizer.nextToken());
            }
        }
        return builder;
    }

}
