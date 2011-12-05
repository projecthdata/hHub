package org.projecthdata.ehr.viewer.service;

import java.sql.SQLException;
import java.util.List;

import org.projecthdata.ehr.viewer.database.EhrDatabaseHelper;
import org.projecthdata.ehr.viewer.model.WeightReading;
import org.projecthdata.ehr.viewer.util.Constants;
import org.projecthdata.ehr.viewer.util.Constants.SyncState;
import org.projecthdata.hdata.model.Result;
import org.projecthdata.hhub.database.OrmManager;
import org.projecthdata.hhub.database.RootEntry;
import org.projecthdata.hhub.database.SectionDocMetadata;
import org.projecthdata.social.api.HData;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import org.springframework.web.client.RestTemplate;

import com.j256.ormlite.dao.Dao;

import android.content.Intent;
import android.content.SharedPreferences.Editor;

public class WeightSyncService extends AbstractSyncService {
	public static final String TAG = WeightSyncService.class.getSimpleName();
	private EhrDatabaseHelper ehrDatabaseHelper = null;
	private Editor editor = null;

	public WeightSyncService() {
		super(TAG);
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ehrDatabaseHelper = new EhrDatabaseHelper(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ehrDatabaseHelper.close();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		this.editor = prefs.edit();
		editor.putString(Constants.PREF_WEIGHT_SYNC_STATE,
				SyncState.WORKING.toString()).commit();
		try {
			Dao<RootEntry, Integer> rootDao = hDataOrmManager
					.getDatabaseHelper().getRootEntryDao();
			Dao<SectionDocMetadata, Integer> sectionDao = hDataOrmManager
					.getDatabaseHelper().getSectionDocMetadataDao();

			// find the first entry that has the right schema and contains xml
			// documents
			RootEntry entry = rootDao.queryForFirst(rootDao
					.queryBuilder()
					.where()
					.eq(RootEntry.COLUMN_NAME_EXTENSION,
							Constants.EXTENSION_RESULT)
					.and()
					.like(RootEntry.COLUMN_PATH, "%bodyweight%")
					.and()
					.eq(RootEntry.COLUMN_NAME_CONTENT_TYPE,
							MediaType.APPLICATION_XML).prepare());

			List<SectionDocMetadata> metadatas = sectionDao.query(sectionDao
					.queryBuilder().where().eq("rootEntry_id", entry.get_id())
					.and().eq("contentType", MediaType.APPLICATION_XML)
					.prepare());
			// grab that document and parse out the patient info
			Connection<HData> connection = connectionRepository
					.getPrimaryConnection(HData.class);

			RestTemplate restTemplate = connection.getApi().getRootOperations()
					.getRestTemplate();
			
			Dao<WeightReading, Integer> weightDao = ehrDatabaseHelper.getWeightReadingDao();
			//delete all entries in the table
			weightDao.delete(weightDao.deleteBuilder().prepare());
			//add new entries to the table
			for (SectionDocMetadata metadata : metadatas) {
				Result result = restTemplate.getForObject(metadata.getLink(),
						Result.class);
				// copy result into a WeightReading an persist it
				WeightReading weight = new WeightReading();
				weight.copy(result);
				weightDao.create(weight);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString(Constants.PREF_WEIGHT_SYNC_STATE,
				SyncState.READY.toString()).commit();
	}

}
