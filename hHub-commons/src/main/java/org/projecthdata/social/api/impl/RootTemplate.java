package org.projecthdata.social.api.impl;

import org.projecthdata.social.api.HData;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.RootOperations;
import org.projecthdata.social.api.atom.AtomFeed;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RootTemplate extends AbstractHDataOperations implements RootOperations {
    private final RestTemplate restTemplate;
    private final HData hData;
    private HttpEntity<?> atomFeedRequestEntity = null;
    private URI ehrUri = null;
    private static final String ROOT_PATH = "root.xml";

    public RootTemplate(HData hData, RestTemplate restTemplate, boolean isAuthorizedForUser) {
        super(isAuthorizedForUser);
        this.restTemplate = restTemplate;
        this.hData = hData;
        this.ehrUri = URI.create(hData.getEhrUrl());

    }

    public Root getRoot() {
        String url = ehrUri.toString() + "/" + ROOT_PATH;
        return restTemplate.getForObject(url, Root.class);
    }


    /**
     * Adds the correct accept header for a GET request of an atom feed
     *
     * @param url - the URL of the feed to request
     * @return
     */
    public AtomFeed getForAtomFeed(String url) {
        ResponseEntity<AtomFeed> response = restTemplate.exchange(url, HttpMethod.GET,
                atomFeedRequestEntity, AtomFeed.class);
        AtomFeed feed = null;
        try {
            feed = response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return feed;
    }
}
