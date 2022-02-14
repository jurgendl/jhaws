package org.jhaws.common.net.client;

import java.util.Date;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;

@SuppressWarnings("serial")
public class SerializableCookie extends CookieBase implements SetCookie {
    public SerializableCookie() {
        super();
        setExpiryDate(new Date(new Date().getTime() + 365L * 24L * 3600L * 1000L));
    }

    public SerializableCookie(String domain, String key, String value) {
        this();
        this.setDomain(domain);
        this.setName(key);
        this.setValue(value);
    }

    public SerializableCookie(Cookie cookie) {
        super();
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
        super();
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
