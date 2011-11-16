package org.hHub.weight.database;

import java.sql.SQLException;

import org.hHub.weight.model.WeightReading;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class WeightDatabaseHelper extends OrmLiteSqliteOpenHelper {
	private final String LOG_NAME = getClass().getName();
	private static final String DATABASE_NAME = "weight.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<WeightReading, Integer> weightDao = null;

	public WeightDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, WeightReading.class);
		} catch (SQLException e) {
			Log.e(LOG_NAME, "Could not create new table ", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, WeightReading.class, true);
			onCreate(sqLiteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
		}

	}

	public Dao<WeightReading, Integer> getWeightDao() throws SQLException {
		if (weightDao == null) {
			weightDao = getDao(WeightReading.class);
		}
		return weightDao;
	}
}
