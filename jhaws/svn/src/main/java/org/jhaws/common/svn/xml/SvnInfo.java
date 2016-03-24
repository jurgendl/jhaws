package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.svn.xml.Svn.RootBeanImpl;

@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnInfo extends RootBeanImpl {
	private Entry entry;

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
}
