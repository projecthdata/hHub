package org.hHub.weight;

import org.hHub.weight.database.WeightDatabaseHelper;

public interface OrmProvider {
	public WeightDatabaseHelper getDatabaseHelper();
}
