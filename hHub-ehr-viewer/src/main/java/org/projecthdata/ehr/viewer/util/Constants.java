package org.projecthdata.ehr.viewer.util;

public class Constants {
    /**
     * The URL for the EHR
     */
    public static final String PREF_EHR_URL = "ehrURL";
    /**
     * Flag to indicate if the first sync has been initiated
     */
    public static final String PREF_INITIALIZED = "initialized";
    /**
     * Result code to indicate the EHR URL has been saved
     */
    public static final int RESULT_SAVED = 1;
    
    public static final String PREF_ROOT_SYNC_STATE = "rootSyncState";
    public static enum RootSyncState {UNSTARTED,READY,WORKING};
}
