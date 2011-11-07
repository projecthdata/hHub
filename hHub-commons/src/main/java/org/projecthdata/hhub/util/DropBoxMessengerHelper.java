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

package org.projecthdata.hhub.util;

public class DropBoxMessengerHelper {
	//a request to GET a resource
	public static final int MSG_GET = 1;
	//this message contains a Uri to monitor for the status of a network operation
	public static final int MSG_STATUS_URI = 2;
	public static final String DATA_LOCAL_URI = "localUri";
	public static final String DATA_REMOTE_URI = "remoteUri";
	public static final String DATA_TYPE = "dataType";
	public static final String DATA_CONTENT_TYPE = "contentType";
	public static final String DATA_DOCUMENT_EXTENSION = "docExtension";
	public static final String STATUS_URI = "statusUri";
	public static final String HTTP_METHOD = "httpMethod";
	public static final String DROP_BOX_SERVICE_ACTION = "org.projecthdata.service.DropBoxMessengerService";
}
