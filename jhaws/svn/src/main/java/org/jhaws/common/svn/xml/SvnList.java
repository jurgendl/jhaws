package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.svn.xml.Svn.RootBeanImpl;

@XmlRootElement(name = "lists")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnList extends RootBeanImpl implements Iterable<EntryList> {
	private List<EntryList> list;

	public List<EntryList> getList() {
		return list;
	}

	public void setList(List<EntryList> list) {
		this.list = list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<EntryList> iterator() {
		if (list == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return list.iterator();
	}
}
