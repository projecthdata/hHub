package org.projecthdata.hhub;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import org.projecthdata.social.api.connect.HDataConnectionFactory;
import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

public class HHubApplication extends Application {

    private ConnectionFactoryRegistry connectionFactoryRegistry;
    private SQLiteOpenHelper repositoryHelper;
    private ConnectionRepository connectionRepository;

    @Override
    public void onCreate() {
        // create a new ConnectionFactoryLocator and populate it with Facebook
        // and Twitter ConnectionFactories
        connectionFactoryRegistry = new ConnectionFactoryRegistry();


        // set up the database and encryption
        repositoryHelper = new SQLiteConnectionRepositoryHelper(this);
        connectionRepository = new SQLiteConnectionRepository(repositoryHelper, connectionFactoryRegistry, AndroidEncryptors.text("password", "5c0744940b5c369b"));
    }

    public ConnectionRepository getConnectionRepository() {
        return connectionRepository;
    }


    public  ConnectionFactoryRegistry getConnectionFactoryRegistry(){
        return this.connectionFactoryRegistry;
    }
    public HDataConnectionFactory getHDataConnectionFactory(String ehrUrl){
        return (HDataConnectionFactory)connectionFactoryRegistry.getConnectionFactory(ehrUrl);
    }

}
