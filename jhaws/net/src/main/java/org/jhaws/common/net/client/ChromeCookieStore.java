package org.jhaws.common.net.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.SQLiteDataSource;

import com.sun.jna.platform.win32.Crypt32Util;

// https://stackoverflow.com/questions/33629474/reading-and-inserting-chrome-cookies-java
// https://github.com/benjholla/CookieMonster/blob/master/CookieMonster/pom.xml
public class ChromeCookieStore implements org.apache.http.client.CookieStore {
	protected NamedParameterJdbcTemplate jdbc;

	public ChromeCookieStore(FilePath cookieStore) {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		cookieStore = cookieStore.copyTo(cookieStore.appendExtension("backup"));
		String url = "jdbc:sqlite:" + cookieStore.getAbsolutePath();
		System.out.println(url);
		dataSource.setUrl(url);
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public ChromeCookieStore() {
		this(FilePath.getUserHomeDirectory()//
				.child("AppData")//
				.child("Local")//
				.child("Google")//
				.child("Chrome")//
				.child("User Data")//
				.child("Default")//
				.child("Cookies")//
		);
	}

	@Override
	public void addCookie(Cookie cookie) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Cookie> getCookies() {
		Map<String, Object> params = new HashMap<>();
		RowMapper<org.apache.http.cookie.Cookie> cookieRowMapper = new RowMapper<org.apache.http.cookie.Cookie>() {
			@Override
			public Cookie mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					int i = 1;
					String host_key = rs.getString(i++);
					String name = rs.getString(i++);
					String path = rs.getString(i++);
					byte[] encrypted_value = rs.getBytes(i++);
					byte[] decrypted_value = Crypt32Util.cryptUnprotectData(encrypted_value);
					// Date expires = result.getDate("expires_utc");
					org.apache.http.impl.cookie.BasicClientCookie cookie = new org.apache.http.impl.cookie.BasicClientCookie(
							name, new String(decrypted_value));
					cookie.setDomain(host_key);
					return cookie;
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		System.out.println("loading chrome cookies");
		return jdbc.query("select host_key, name, path, encrypted_value from cookies", params, cookieRowMapper);
	}

	@Override
	public boolean clearExpired(Date date) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		new ChromeCookieStore().getCookies();
	}
}
