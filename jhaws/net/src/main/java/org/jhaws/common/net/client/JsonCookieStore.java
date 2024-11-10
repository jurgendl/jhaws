package org.jhaws.common.net.client;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JsonCookieStore extends JsonCookieStoreBase implements CookieStore {
    public JsonCookieStore() {
        super();
    }

    public JsonCookieStore(FilePath cookieStore) {
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
