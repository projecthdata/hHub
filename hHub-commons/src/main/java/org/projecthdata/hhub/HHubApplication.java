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
