package org.projecthdata.social.api.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.projecthdata.social.api.Root;
import org.projecthdata.social.api.impl.HDataTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.test.client.MockRestServiceServer;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.*;

@RunWith(RobolectricTestRunner.class)
public class HDataTemplateTest {
    MockRestServiceServer mockServer = null;
    HDataTemplate hDataTemplate = null;


    @Before
    public void setup() {
        hDataTemplate = new HDataTemplate("fakeToken", "http://hstore.com/1234");
        mockServer = MockRestServiceServer.createServer(hDataTemplate.getRestTemplate());
    }

    @Test
    public void test() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_XML);

        mockServer.expect(requestTo("http://hstore.com/1234/root.xml")).andExpect(method(GET))
                .andRespond(withResponse(new ClassPathResource("../../resources/root.xml", getClass()), responseHeaders));

        Root root = hDataTemplate.getRootOperations().getRoot();
        assertEquals(5, root.getExtensions().size());
    }

}
