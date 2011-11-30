/**
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
 *
 */
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
    public static final String PREF_PATIENT_INFO_SYNC_STATE = "patientInfoSyncState";
    public static enum SyncState {UNSTARTED,READY,WORKING};
    
    
    //Patient related information
  	public static final String PREF_PATIENT_NAME_GIVEN ="patient_name_given";
  	public static final String PREF_PATIENT_NAME_LASTNAME="patient_name_lastname";
  	public static final String PREF_PATIENT_ID = "patient_id";
  	public static final String PREF_PATIENT_NAME_SUFFIX = "patient_name_suffix";
  	public static final String PREF_PATIENT_PHOTO_URL = "patient_photo";
  	public static final String PREF_PATIENT_DATA_STATUS = "patient_data_status";
    
    
    
}
