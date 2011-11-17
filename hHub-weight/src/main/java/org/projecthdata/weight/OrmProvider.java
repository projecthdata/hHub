package org.projecthdata.weight;

import org.projecthdata.weight.database.WeightDatabaseHelper;

public interface OrmProvider {
	public WeightDatabaseHelper getDatabaseHelper();
}
