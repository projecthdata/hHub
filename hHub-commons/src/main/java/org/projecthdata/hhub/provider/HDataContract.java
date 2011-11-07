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

package org.projecthdata.hhub.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class for interacting with hdata information through the
 * HDataProvider
 * 
 * @author elevine
 * 
 */
public class HDataContract {

	public static final String CONTENT_AUTHORITY = "org.projecthdata.hhub";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);

	public static final String PATH_ROOT_ENTRIES = "rootEntries";

	public static final String PATH_SECTION_DOC_METADATA = "sectionDocMetadata";
	public static final String PATH_HRF = "hrf";
	public static final String PATH_FILE_TRANSFER_STATUS = "fileTransferStatus";

	public static class RootEntries {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_ROOT_ENTRIES).build();
		private static final String MIME_SUBTYPE = "/vnd.projectjdata.root_entries";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ MIME_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ MIME_SUBTYPE;

	}

	public static class SectionDocMetadata {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_SECTION_DOC_METADATA).build();

		private static final String MIME_SUBTYPE = "/vnd.projectjdata.section_doc_metadata";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ MIME_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ MIME_SUBTYPE;
	}

	public static class Hrf {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_HRF).build();

		private static final String MIME_SUBTYPE = "/vnd.projectjdata.hrf";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ MIME_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ MIME_SUBTYPE;
	}

	public static class FileTransferStatus {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_FILE_TRANSFER_STATUS).build();
		private static final String MIME_SUBTYPE = "/vnd.projectjdata.file_transfer_status";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ MIME_SUBTYPE;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ MIME_SUBTYPE;
	}

}
