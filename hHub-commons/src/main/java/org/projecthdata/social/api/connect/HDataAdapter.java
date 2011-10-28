package org.projecthdata.social.api.connect;

import org.projecthdata.social.api.HData;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;


/**
 * This is currently not relevant to hData, but required by the Spring Social framework:
 *
 * From http://static.springsource.org/spring-social/docs/1.0.x/reference/html/implementing.html:
 * The role of the ApiAdapter is to map a provider's native API interface onto this uniform Connection model.
 * A connection delegates to its adapter to perform operations such as testing the validity of its API credentials,
 * setting metadata values, fetching a user profile, and updating user status
 */
public class HDataAdapter implements ApiAdapter<HData> {
    public boolean test(HData hData) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setConnectionValues(HData hData, ConnectionValues connectionValues) {

    }

    public UserProfile fetchUserProfile(HData hData) {
        return null;
    }

    public void updateStatus(HData hData, String s) {

    }
}
