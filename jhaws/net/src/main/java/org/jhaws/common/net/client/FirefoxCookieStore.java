package org.jhaws.common.net.client;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;

public class FirefoxCookieStore extends FirefoxCookieStoreBase implements CookieStore {
	public FirefoxCookieStore() {
		super();
	}

	public FirefoxCookieStore(FilePath cookieStore) {
		super(cookieStore);
	}

	@Override
	public void addCookie(Cookie cookie) {
		//
	}

	@Override
	public boolean clearExpired(Date date) {
		return false;
	}

	@Override
	public void clear() {
		//
	}

	@Override
	public List<Cookie> getCookies() {
		return getSerializableCookies().stream().map(SerializableCookie::new).collect(Collectors.toList());
	}
}
