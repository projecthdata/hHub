package org.projecthdata.hhub.provider;

import java.util.Arrays;

import org.projecthdata.hhub.provider.HDataContract.SectionDocMetadata;

import android.content.Context;
import android.database.Cursor;

/**
 * Based on Martin Fowler's Table Data Gateway pattern:
 * http://martinfowler.com/eaaCatalog/tableDataGateway.html
 * 
 * @author elevine
 * 
 */
public class SectionDocMetadataGateway extends TableGateway{
	
	public SectionDocMetadataGateway(Context context){
		super(context, SectionDocMetadata.CONTENT_URI, SectionDocMetadataColumns.getInstance().getAllColumnsProjection());
	}
	

	public Cursor findByRootyEntriesIdAndHrfId(Integer rootEntriesId, Integer hrfId) {
		String[] columns = {SectionDocMetadataColumns.ROOT_ENTRIES_ID, SectionDocMetadataColumns.HRF_ID};
		String[] selectionArgs = {rootEntriesId.toString(), hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}
	
	public Cursor findByExtensionAndHrfId(String extension, Integer hrfId) {
		String[] columns = {SectionDocMetadataColumns.EXTENSION, SectionDocMetadataColumns.HRF_ID};
		String[] selectionArgs = {extension, hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}
	
	public Cursor findByExtensionAndContentTypeAndHrfId(String extension, String contentType, Integer hrfId) {
		String[] columns = {SectionDocMetadataColumns.EXTENSION, SectionDocMetadataColumns.CONTENT_TYPE, SectionDocMetadataColumns.HRF_ID};
		String[] selectionArgs = {extension, contentType, hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}
	
	public Cursor findByLinkContentTypeAndExtensionAndHrfI(String linkPattern, String extension, String contentType, Integer hrfId){
		String[] columns = {SectionDocMetadataColumns.EXTENSION, SectionDocMetadataColumns.CONTENT_TYPE, SectionDocMetadataColumns.HRF_ID};
		String selection = buildAndedSelection(Arrays.asList(columns));
		selection += " AND " + SectionDocMetadataColumns.LINK + " LIKE ?";
		String[] selectionArgs = {extension, contentType, hrfId.toString(), linkPattern};
		return super.queryUri(selection, selectionArgs);
	}
	
	public Cursor findByLink(String link){
		String[] columns = {SectionDocMetadataColumns.LINK};
		String[] selectionArgs = {link};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.queryUri(selection, selectionArgs);
	}
	
	public int deleteByHrfId(Integer hrfId){
		String[] columns = {SectionDocMetadataColumns.HRF_ID};
		String[] selectionArgs = new String[]{hrfId.toString()};
		String selection = buildAndedSelection(Arrays.asList(columns));
		return super.delete(selection, selectionArgs);
	}
	
}
