package org.jhaws.common.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.cookie.Cookie;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.SQLiteDataSource;

import com.sun.jna.platform.win32.Crypt32Util;

// https://stackoverflow.com/questions/33629474/reading-and-inserting-chrome-cookies-java
// https://github.com/benjholla/CookieMonster
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
	public boolean clearExpired(Date date) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
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
					byte[] decrypted_value = decrypt(rs.getBytes(i++));
					Date expires = chromeUtcToDate(rs.getString(i++));
					boolean is_secure = rs.getBoolean(i++);
					org.apache.http.impl.cookie.BasicClientCookie cookie = new org.apache.http.impl.cookie.BasicClientCookie(
							name, new String(decrypted_value));
					cookie.setDomain(host_key);
					cookie.setPath(path);
					cookie.setExpiryDate(expires);
					cookie.setSecure(is_secure);
					return cookie;
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		System.out.println("loading chrome cookies");
		return jdbc.query("select host_key, name, path, encrypted_value, expires_utc, is_secure from cookies", params,
				cookieRowMapper);
	}

	private String chromeKeyringPassword = null;

	private static String getMacKeyringPassword(String application) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String[] commands = { "security", "find-generic-password", "-w", "-s", application };
		Process proc = rt.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String result = "";
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			result += s;
		}
		return result;
	}

	private byte[] decrypt(byte[] encrypted_value) {
		if (Utils.osgroup == OSGroup.Windows) {
			return Crypt32Util.cryptUnprotectData(encrypted_value);
		}

		if (Utils.osgroup == OSGroup.Nix) {
			try {
				byte[] salt = "saltysalt".getBytes(); // TODO inject
				char[] password = "peanuts".toCharArray(); // TODO inject
				char[] iv = new char[16];
				Arrays.fill(iv, ' ');
				int keyLength = 16;
				int iterations = 1;
				PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
				SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();
				SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));
				// if cookies are encrypted "v10" is a the prefix (has to be
				// removed before decryption)
				byte[] encryptedBytes = encrypted_value;
				if (new String(encrypted_value).startsWith("v10")) {
					encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
				}
				return cipher.doFinal(encryptedBytes);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		if (Utils.osgroup == OSGroup.Mac) {
			try {
				// access the decryption password from the keyring manager
				if (chromeKeyringPassword == null) {
					chromeKeyringPassword = getMacKeyringPassword("Chrome Safe Storage");
				}
				byte[] salt = "saltysalt".getBytes();
				char[] password = chromeKeyringPassword.toCharArray();
				char[] iv = new char[16];
				Arrays.fill(iv, ' ');
				int keyLength = 16;
				int iterations = 1003;
				PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
				SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();
				SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));
				// if cookies are encrypted "v10" is a the prefix (has to be
				// removed before decryption)
				byte[] encryptedBytes = encrypted_value;
				if (new String(encrypted_value).startsWith("v10")) {
					encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
				}
				return cipher.doFinal(encryptedBytes);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		throw new UnsupportedOperationException("" + Utils.osgroup);
	}

	private static Date chromeUtcToDate(String timeString) {
		if ("0".equals(timeString))
			return null;
		// chrome: ldap time (if 17 digits add 1 random digit(use 0 out of
		// convenience))
		timeString = timeString.concat("0");
		long timeNoC = Long.parseLong(timeString);
		// source:
		// http://www.epochconverter.com/epoch/ldap-timestamp.php
		// formula 1: LDAP time = (time()+11644473600)*10000000
		// formala 2: LDAP time / 10000000 - 11644473600 = time()
		timeNoC = timeNoC / 10000000L - 11644473600L;
		timeNoC = timeNoC * 1000;
		return new Date(timeNoC);
	}

	public static void main(String[] args) {
		new ChromeCookieStore().getCookies().forEach(System.out::println);
	}
}
