package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class ChangeList implements Iterable<Entry> {
	@XmlAttribute
	private String name;

	private List<Entry> entry;

	public List<Entry> getEntry() {
		return entry;
	}

	public String getName() {
		return name;
	}

	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ChangeList [" + (this.name != null ? "name=" + this.name : "") + "]";
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
