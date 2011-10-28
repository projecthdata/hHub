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
