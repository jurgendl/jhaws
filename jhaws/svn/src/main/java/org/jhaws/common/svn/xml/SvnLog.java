package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "log")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnLog extends SvnRootBean implements Iterable<LogEntry> {
	private List<LogEntry> logentry;

	@SuppressWarnings("unchecked")
	public List<LogEntry> getLogentry() {
		if (logentry == null) {
			return Collections.EMPTY_LIST;
		}
		return logentry;
	}

	@Override
	public Iterator<LogEntry> iterator() {
		return getLogentry().iterator();
	}

	public void setLogentry(List<LogEntry> logentry) {
		this.logentry = logentry;
	}
}
