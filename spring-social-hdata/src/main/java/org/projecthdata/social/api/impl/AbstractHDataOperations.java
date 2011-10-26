package org.projecthdata.social.api.impl;

import org.springframework.social.MissingAuthorizationException;

public class AbstractHDataOperations {
    private final boolean isAuthorized;

	public AbstractHDataOperations(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	protected void requireAuthorization() {
		if (!isAuthorized) {
			throw new MissingAuthorizationException();
		}
	}
}
