package org.projecthdata.hhub.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "root_entries")
public class RootEntry {
    @DatabaseField(generatedId = true)
    private int _id;

    /**
     * Unique identifier for the extension. Could be a URL to it's schema *
     */
    @DatabaseField
    private String extension = null;
    /**
     * A local identifier for this extension, which is unique within the
     * root document
     */
    @DatabaseField
    private int extensionId;

    @DatabaseField
    private String contentType;

    /**
     * relative path to this section
     */
    @DatabaseField
    private String path;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(int extensionId) {
        this.extensionId = extensionId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
