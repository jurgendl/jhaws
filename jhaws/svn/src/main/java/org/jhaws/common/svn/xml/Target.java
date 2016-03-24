package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class Target implements Iterable<Entry> {
	@XmlAttribute
	private String path;

	private List<Entry> entry;

	private Against against;

	public Against getAgainst() {
		return against;
	}

	public List<Entry> getEntry() {
		return entry;
	}

	public String getPath() {
		return path;
	}

	public void setAgainst(Against against) {
		this.against = against;
	}

	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Entry> iterator() {
		if (entry == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return entry.iterator();
	}

	@Override
	public String toString() {
		return "Target [" + (this.path != null ? "path=" + this.path + ", " : "") + (this.against != null ? "against=" + this.against : "") + "]";
	}
}
