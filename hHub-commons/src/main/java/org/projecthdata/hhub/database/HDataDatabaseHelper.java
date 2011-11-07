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

package org.projecthdata.hhub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.projecthdata.hhub.provider.HDataContract;

import java.sql.SQLException;

public class HDataDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private final String LOG_NAME = getClass().getName();
    private static final String DATABASE_NAME = "hhub.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<RootEntry, Integer> rootEntryDao;
    private Dao<SectionDocMetadata, Integer> sectionDocMetadataDao;

    public HDataDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, RootEntry.class);
            TableUtils.createTable(connectionSource, SectionDocMetadata.class);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not create new table ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, RootEntry.class, true);
            TableUtils.dropTable(connectionSource, SectionDocMetadata.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
        }

    }


    public Dao<RootEntry, Integer> getRootEntryDao() throws SQLException {
        if (rootEntryDao == null) {
            rootEntryDao = getDao(RootEntry.class);
        }
        return rootEntryDao;
    }

    public Dao<RootEntry, Integer> getSectionDocMetadataDao() throws SQLException {
        if (sectionDocMetadataDao == null) {
            sectionDocMetadataDao = getDao(SectionDocMetadata.class);
        }
        return rootEntryDao;
    }
}
