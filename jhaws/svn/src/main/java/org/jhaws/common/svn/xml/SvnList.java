package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lists")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnList extends SvnRootBean implements Iterable<EntryList> {
	private List<EntryList> list;

	@SuppressWarnings("unchecked")
	public List<EntryList> getList() {
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public void setList(List<EntryList> list) {
		this.list = list;
	}

	@Override
	public Iterator<EntryList> iterator() {
		return getList().iterator();
	}
}
