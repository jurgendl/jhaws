package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class LogEntry extends Commit implements Iterable<Path> {
	private String msg;

	@XmlElement(name = "path")
	@XmlElementWrapper(name = "paths")
	private List<Path> path;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@SuppressWarnings("unchecked")
	public List<Path> getPath() {
		if (path == null) {
			return Collections.EMPTY_LIST;
		}
		return path;
	}

	public void setPath(List<Path> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "LogEntry [" + (msg != null ? "msg=" + msg : "") + "]";
	}

	@Override
	public Iterator<Path> iterator() {
		return getPath().iterator();
	}
}
