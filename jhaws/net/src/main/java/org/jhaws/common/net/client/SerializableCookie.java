package org.jhaws.common.net.client;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;

@SuppressWarnings("serial")
public class SerializableCookie extends CookieBase implements SetCookie {
	public SerializableCookie() {
		super();
	}

	public SerializableCookie(Cookie cookie) {
		this.setComment(cookie.getComment());
		this.setDomain(cookie.getDomain());
		this.setExpiryDate(cookie.getExpiryDate());
		this.setName(cookie.getName());
		this.setPath(cookie.getPath());
		this.setSecure(cookie.isSecure());
		this.setValue(cookie.getValue());
		this.setVersion(cookie.getVersion());
	}

	public SerializableCookie(CookieBase cookie) {
		this.setComment(cookie.getComment());
		this.setDomain(cookie.getDomain());
		this.setExpiryDate(cookie.getExpiryDate());
		this.setName(cookie.getName());
		this.setPath(cookie.getPath());
		this.setSecure(cookie.isSecure());
		this.setValue(cookie.getValue());
		this.setVersion(cookie.getVersion());
	}

	@Override
	public int[] getPorts() {
		return null;
	}

	@Override
	public String getCommentURL() {
		return null;
	}

	public void setPersistent(Boolean b) {
		//
	}
}
