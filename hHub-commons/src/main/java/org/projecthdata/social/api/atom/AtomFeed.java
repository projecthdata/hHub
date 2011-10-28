package org.projecthdata.social.api.atom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of documents in a section will be provided as an atom feed
 * @author elevine
 *
 */
@Root(name="feed", strict=false)
public class AtomFeed {
	
	@Element
	String title = null;
	
	@ElementList(inline=true, name="entry")
	List<Entry> entries = new ArrayList<Entry>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
}
