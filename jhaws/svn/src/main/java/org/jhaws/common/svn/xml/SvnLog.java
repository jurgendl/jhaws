package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.svn.xml.Svn.RootBeanImpl;

@XmlRootElement(name = "log")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnLog extends RootBeanImpl implements Iterable<LogEntry> {
	private List<LogEntry> logentry;

	public List<LogEntry> getLogentry() {
		return logentry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<LogEntry> iterator() {
		if (logentry == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return logentry.iterator();
	}

	public void setLogentry(List<LogEntry> logentry) {
		this.logentry = logentry;
	}
}
