package org.projecthdata.social.api;

import org.springframework.social.ApiBinding;

/**
 *
 * Interface specifying a basic set of operations for interacting with an hData server.
 * Implemented by {@link org.projecthdata.social.api.impl.HDataTemplate}.
 *
 */

public interface HData extends ApiBinding {
    public String getEhrUrl();
    public RootOperations getRootOperations();
 
}
