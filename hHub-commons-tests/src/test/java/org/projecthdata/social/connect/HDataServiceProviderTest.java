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
