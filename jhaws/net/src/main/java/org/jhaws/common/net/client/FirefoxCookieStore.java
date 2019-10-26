package org.jhaws.common.net.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.client.CookieStore.SerializableCookie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.SQLiteDataSource;

//https://stackoverflow.com/questions/33629474/reading-and-inserting-chrome-cookies-java
//https://github.com/benjholla/CookieMonster
public class FirefoxCookieStore implements org.apache.http.client.CookieStore {
	protected NamedParameterJdbcTemplate jdbc;

	public FirefoxCookieStore(FilePath cookieStore) {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		cookieStore = cookieStore.copyTo(cookieStore.appendExtension("backup"));
		String url = "jdbc:sqlite:" + cookieStore.getAbsolutePath();
		System.out.println(url);
		dataSource.setUrl(url);
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public FirefoxCookieStore() {
		this(FilePath.getUserHomeDirectory().child("AppData\\Roaming\\Mozilla\\Firefox\\Profiles").getChildren().get(0)
				.child("cookies.sqlite"));
	}

	@Override
	public void addCookie(Cookie cookie) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean clearExpired(Date date) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
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
					byte[] decrypted_value = rs.getBytes(i++);
					Date expires = new Date(1000l * rs.getInt(i++));
					boolean secure = rs.getBoolean(i++);
					SerializableCookie cookie = new SerializableCookie();
					cookie.setName(name);
					cookie.setValue(new String(decrypted_value));
					cookie.setDomain(host_key);
					cookie.setPath(path);
					cookie.setExpiryDate(expires);
					cookie.setSecure(secure);
					return cookie;
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		System.out.println("loading firefox cookies");
		return jdbc.query("select host, name, path, value, expiry, isSecure from moz_cookies", params, cookieRowMapper);
	}

	public static void main(String[] args) {
		new FirefoxCookieStore().getCookies().forEach(System.out::println);
	}
}
