package org.jhaws.common.svn.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Commit {
	@XmlAttribute
	private int revision;

	private String author;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public int getRevision() {
		return revision;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public String toString() {
		return "Commit [revision=" + revision + ", " + (author != null ? "author=" + author + ", " : "") + (date != null ? "date=" + date : "") + "]";
	}
}
