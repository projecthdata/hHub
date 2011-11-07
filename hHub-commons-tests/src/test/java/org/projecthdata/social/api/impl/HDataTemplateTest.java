/*
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
 */

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
