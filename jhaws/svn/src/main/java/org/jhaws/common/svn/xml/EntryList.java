package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class EntryList implements Iterable<Entry> {
	@XmlAttribute
	private String path;

	private List<Entry> entry;

	public List<Entry> getEntry() {
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
		return "List [" + (this.path != null ? "path=" + this.path : "") + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Entry> iterator() {
		if (entry == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return entry.iterator();
	}
}
