package org.jhaws.common.net.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jhaws.common.io.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

@Deprecated
public class PreloadWinChromeCookies implements CookieStoreInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(PreloadWinChromeCookies.class);

	protected static final boolean windows = (System.getProperty("os.name") != null)
			&& System.getProperty("os.name").toLowerCase().contains("win"); //$NON-NLS-2$

	private static final boolean driver;

	static {
		boolean _driver;
		try {
			Class.forName("org.sqlite.JDBC");
			_driver = true;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			_driver = false;
		}
		driver = _driver;
	}

	private Boolean failed;

	public PreloadWinChromeCookies() {
		if (!PreloadWinChromeCookies.windows) {
			throw new RuntimeException("not windows");
		}

		if (!PreloadWinChromeCookies.driver) {
			throw new RuntimeException("sqllite");
		}

	}

	@Override
	public void beforeAddCookie(CookieStore store) {
		//
	}

	@Override
	public void beforeClear(CookieStore store) {
		//
	}

	@Override
	public void beforeClearExpired(CookieStore store, Date date) {
		//
	}

	@Override
	public synchronized void beforeGetCookies(CookieStore store) {
		try {
			if (failed == null) {
				FilePath ffr = new FilePath(CookieStoreInterceptor.user_home.toFile(),
						"AppData/Local/Google/Chrome/User Data/Default/");
				FilePath ff3c = ffr.child("Cookies");
				FilePath ff3cc = ffr.child("Cookies_copy");
				ff3c.copyTo(ff3cc);
				SQLiteConfig config = new SQLiteConfig();
				config.setReadOnly(true);
				String url = "jdbc:sqlite:/" + ff3cc.getAbsolutePath().replace('\\', '/');
				String query = "select name, value, host_key, path, expires_utc from cookies";
				Connection con = DriverManager.getConnection(url, config.toProperties());
				con.setAutoCommit(false);

				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {
					String name = rs.getString(1);
					String value = rs.getString(2);
					String host = rs.getString(3);
					String path = rs.getString(4);
					String expiry = rs.getObject(5).toString();
					if (host.startsWith("."))
						host = host.substring(1);

					GregorianCalendar gc = new GregorianCalendar();
					gc.setTimeInMillis(Long.parseLong(expiry) / 1000);
					gc.set(Calendar.YEAR, gc.get(Calendar.YEAR) - 369);

					BasicClientCookie cookie = new BasicClientCookie(name, value);
					cookie.setVersion(0);
					cookie.setDomain(host);
					cookie.setPath(path);
					cookie.setExpiryDate(gc.getTime());

					System.out.println(cookie);
					store.addCookie(cookie);
				}
				this.failed = false;
			}
		} catch (Exception e) {
			PreloadWinChromeCookies.logger.error(ExceptionUtils.getStackTrace(e));
			this.failed = true;
		}
	}
}
