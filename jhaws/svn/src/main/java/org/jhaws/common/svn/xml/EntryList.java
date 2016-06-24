package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class EntryList implements Iterable<Entry> {
	@XmlAttribute
	private String path;

	private List<Entry> entry;

	@SuppressWarnings("unchecked")
	public List<Entry> getEntry() {
		if (entry == null) {
			return Collections.EMPTY_LIST;
		}
		return entry;
	}

	public String getPath() {
		return path;
	}

	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "List [" + (path != null ? "path=" + path : "") + "]";
	}

	@Override
	public Iterator<Entry> iterator() {
		return getEntry().iterator();
	}
}
