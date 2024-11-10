package org.jhaws.common.net.client5;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.client.NetscapeTxtCookieStoreBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NetscapeTxtCookieStore extends NetscapeTxtCookieStoreBase implements CookieStore {
    public NetscapeTxtCookieStore() {
        super();
    }

    public NetscapeTxtCookieStore(FilePath cookieStore) {
        super(cookieStore);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (cookies == null) cookies = new ArrayList<>();
        cookies.add(cookie);
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {
        cookies = null;
    }

    protected List<Cookie> cookies;

    @Override
    public List<Cookie> getCookies() {
        if (cookies == null)
            cookies = getSerializableCookies().stream().map(SerializableCookie::new).collect(Collectors.toList());
        return cookies;
    }
}
