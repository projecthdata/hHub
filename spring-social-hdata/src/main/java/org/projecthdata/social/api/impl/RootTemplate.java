package org.projecthdata.social.api.impl;

import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.springframework.web.client.RestTemplate;

public class RootTemplate extends AbstractHDataOperations implements RootOperations {
    private final RestTemplate restTemplate;

    public RootTemplate(RestTemplate restTemplate, boolean isAuthorizedForUser) {
        super(isAuthorizedForUser);
        this.restTemplate = restTemplate;
    }

    public Root getRoot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
