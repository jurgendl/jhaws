package org.jhaws.common.svn.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Path {
	@XmlAttribute(name = "copyfrom-rev")
	private String copyFromRev;

	public String getCopyFromRev() {
		return copyFromRev;
	}

	public void setCopyFromRev(String copyFromRev) {
		this.copyFromRev = copyFromRev;
	}

	@Override
	public String toString() {
		return "Path [" + (copyFromRev != null ? "copyFromRev=" + copyFromRev : "") + "]";
	}
}
