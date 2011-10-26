package org.projecthdata.provider;

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
