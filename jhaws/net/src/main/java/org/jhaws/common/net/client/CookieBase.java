package org.jhaws.common.net.client;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class CookieBase implements Serializable {
	private Date expiryDate;

	private Date creationDate;

	private String comment;

	private String domain;

	private String name;

	private String path;

	private String value;

	private boolean isSecure;

	private int version;

	public boolean isExpired(Date date) {
		return expiryDate != null && !expiryDate.after(date);
	}

	public boolean isPersistent() {
		return true;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSecure() {
		return this.isSecure;
	}

	public void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "CookieBase [expiryDate=" + this.expiryDate + ", creationDate=" + this.creationDate + ", comment="
				+ this.comment + ", domain=" + this.domain + ", name=" + this.name + ", path=" + this.path + ", value="
				+ this.value + ", isSecure=" + this.isSecure + ", version=" + this.version + "]";
	}
}
