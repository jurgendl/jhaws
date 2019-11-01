package org.jhaws.common.net.client5;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.SetCookie;
import org.jhaws.common.net.client.CookieBase;

@SuppressWarnings("serial")
public class SerializableCookie extends CookieBase implements SetCookie {
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

	public SerializableCookie(CookieBase cookie) {
		this.setDomain(cookie.getDomain());
		this.setExpiryDate(cookie.getExpiryDate());
		this.setName(cookie.getName());
		this.setPath(cookie.getPath());
		this.setSecure(cookie.isSecure());
		this.setValue(cookie.getValue());
		this.setCreationDate(cookie.getCreationDate());
	}

	@Override
	public String getAttribute(String name) {
		if (DOMAIN_ATTR.equals(name))
			return getDomain();
		if (EXPIRES_ATTR.equals(name))
			return null;
		if ("name".equals(name))
			return getName();
		if (PATH_ATTR.equals(name))
			return getPath();
		if ("value".equals(name))
			return getValue();
		if (SECURE_ATTR.equals(name))
			return "" + isSecure();
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAttribute(String name) {
		if (DOMAIN_ATTR.equals(name))
			return getDomain() != null;
		if (EXPIRES_ATTR.equals(name))
			return getExpiryDate() != null;
		if ("name".equals(name))
			return getName() != null;
		if (PATH_ATTR.equals(name))
			return getPath() != null;
		if ("value".equals(name))
			return getValue() != null;
		if (SECURE_ATTR.equals(name))
			return isSecure();
		throw new UnsupportedOperationException();
	}
}
