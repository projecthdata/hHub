package org.projecthdata.social.connect;
import org.junit.Test;

import static org.junit.Assert.*;

public class HDataServiceProviderTest {

    @Test
    public void accessTokenUrl(){
        String url = HDataServiceProvider.getAccessTokenUrl("http://www.foo.com:8080/webapp/record");
        assertEquals("http://www.foo.com:8080/webapp/auth/token", url);
    }

}
