package org.jhaws.common.net.client5;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.SetCookie;

@SuppressWarnings("serial")
public class SerializableCookie implements SetCookie, Serializable {
	private Date expiryDate;

	private Date creationDate;

	private String domain;

	private String name;

	private String path;

	private String value;

	private boolean isSecure;

	public SerializableCookie() {
		super();
	}

	public SerializableCookie(Cookie cookie) {
		this.setDomain(cookie.getDomain());
		this.setExpiryDate(cookie.getExpiryDate());
		this.setName(cookie.getName());
		this.setPath(cookie.getPath());
		this.setSecure(cookie.isSecure());
		this.setValue(cookie.getValue());
		this.setCreationDate(cookie.getCreationDate());
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#getDomain()
	 */
	@Override
	public String getDomain() {
		return this.domain;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#getExpiryDate()
	 */
	@Override
	public Date getExpiryDate() {
		return this.expiryDate;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#getPath()
	 */
	@Override
	public String getPath() {
		return this.path;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#getValue()
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#isExpired(java.util.Date)
	 */
	@Override
	public boolean isExpired(Date date) {
		return false;
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#isPersistent()
	 */
	@Override
	public boolean isPersistent() {
		return (null != this.expiryDate);
	}

	/**
	 *
	 * @see org.apache.http.cookie.Cookie#isSecure()
	 */
	@Override
	public boolean isSecure() {
		return this.isSecure;
	}

	/**
	 *
	 * @see org.apache.http.cookie.SetCookie#setDomain(java.lang.String)
	 */
	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 *
	 * @see org.apache.http.cookie.SetCookie#setExpiryDate(java.util.Date)
	 */
	@Override
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @see org.apache.http.cookie.SetCookie#setPath(java.lang.String)
	 */
	@Override
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 *
	 * @see org.apache.http.cookie.SetCookie#setSecure(boolean)
	 */
	@Override
	public void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	/**
	 *
	 * @see org.apache.http.cookie.SetCookie#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
				.append("expiryDate", this.expiryDate).append("domain", this.domain).append("name", this.name)
				.append("path", this.path).append("creationDate", this.creationDate).append("value", this.value)
				.append("isSecure", this.isSecure).toString();
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAttribute(String name) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
