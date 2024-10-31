package org.jhaws.common.net.client;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BraveCookieStore extends BraveCookieStoreBase implements CookieStore {
    public BraveCookieStore() {
        super();
    }

    public BraveCookieStore(FilePath cookieStore) {
        super(cookieStore);
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {
        cookies = null;
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (cookies == null) cookies = new ArrayList<>();
        cookies.add(cookie);
    }

    protected List<Cookie> cookies;

    @Override
    public List<Cookie> getCookies() {
        if (cookies == null) cookies = getSerializableCookies().stream().map(SerializableCookie::new).collect(Collectors.toList());
        return cookies;
    }
}
