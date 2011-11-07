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

package org.projecthdata.social.connect;
import org.junit.Test;
import org.projecthdata.social.api.connect.HDataServiceProvider;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class HDataServiceProviderTest {

    @Test
    public void testAccessTokenUrl(){
        String url = HDataServiceProvider.getAccessTokenUrl("http://www.foo.com:8080/webapp/record");
        assertEquals("http://www.foo.com:8080/webapp/auth/token", url);
    }

    @Test
    public void testAccessTokenUrl2(){
        String url = HDataServiceProvider.getAccessTokenUrl("http://zoidberg:8080/java-hdata-demo-web/hstore/1234");
        assertEquals("http://zoidberg:8080/java-hdata-demo-web/auth/token", url);
    }


      @Test
    public void testAuthorizationUrl2(){
        String url = HDataServiceProvider.getAuthorizeUrl("http://zoidberg:8080/java-hdata-demo-web/hstore/1234");
        assertEquals("http://zoidberg:8080/java-hdata-demo-web/auth/dialog",url);
    }


    @Test
    public void testAuthorizationUrl(){
        String url = HDataServiceProvider.getAuthorizeUrl("http://www.foo.com:8080/webapp/record");
        assertEquals("http://www.foo.com:8080/webapp/auth/dialog",url);
    }
}
