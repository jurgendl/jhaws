package org.jhaws.common.net.client.cookies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PreloadWinChromeCookies
 */
@SuppressWarnings("deprecation")
public class PreloadWinChromeCookies implements CookieStoreInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(PreloadWinChromeCookies.class);

	protected static final boolean windows = (System.getProperty("os.name") != null) && System.getProperty("os.name").toLowerCase().contains("win"); //$NON-NLS-3$

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

	/** domainsLoaded */
	private HashSet<String> domainsLoaded = new HashSet<String>();

	/** failed */
	private boolean failed = false;

	/**
	 * Creates a new PreloadWinChromeCookies object.
	 */
	public PreloadWinChromeCookies() {
		if (!PreloadWinChromeCookies.windows) {
			throw new RuntimeException("not windows");
		}

		if (!PreloadWinChromeCookies.driver) {
			throw new RuntimeException("sqllite");
		}

	}

	/**
	 *
	 * @see org.jhaws.common.net.client.cookies.CookieStoreInterceptor#beforeAddCookie(util.html.client.cookies.PersistentCookieStore)
	 */
	@Override
	public void beforeAddCookie(CookieStore store) {
		//
	}

	/**
	 *
	 * @see org.jhaws.common.net.client.cookies.CookieStoreInterceptor#beforeClear(util.html.client.cookies.PersistentCookieStore)
	 */
	@Override
	public void beforeClear(CookieStore store) {
		//
	}

	/**
	 *
	 * @see org.jhaws.common.net.client.cookies.CookieStoreInterceptor#beforeClearExpired(util.html.client.cookies.PersistentCookieStore, java.util.Date)
	 */
	@Override
	public void beforeClearExpired(CookieStore store, Date date) {
		//
	}

	/**
	 *
	 * @see org.jhaws.common.net.client.cookies.CookieStoreInterceptor#beforeGetCookies(util.html.client.cookies.PersistentCookieStore)
	 */
	@Override
	public synchronized void beforeGetCookies(CookieStore store) {
		try {
			if (this.failed) {
				return;
			}

			String domain = store.getClient().getDomain();

			if (this.domainsLoaded.contains(domain)) {
				return;
			}

			this.domainsLoaded.add(domain);

			IODirectory ffr = new IODirectory(CookieStoreInterceptor.user_home, "Local Settings/Application Data/Google/Chrome/User Data/Default");
			IOFile ff3c = new IOFile(ffr, "Cookies");
			String url = "jdbc:sqlite:/" + ff3c.getAbsolutePath().replace('\\', '/');
			String query = "select name, value, host_key, path, expires_utc from cookies where host_key like '" + domain + "%' or host_key like '." + domain
					+ "%' or host_key like 'www." + domain + "%'"; //$NON-NLS-1$ //$NON-NLS-2$
			Connection con = DriverManager.getConnection(url, null, null);
			// con.setReadOnly(true);
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString(1);
				String value = rs.getString(2);
				String host = rs.getString(3);
				String path = rs.getString(4);
				String expiry = rs.getObject(5).toString();

				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(Long.parseLong(expiry) / 1000);
				gc.set(Calendar.YEAR, gc.get(Calendar.YEAR) - 369);

				BasicClientCookie cookie = new BasicClientCookie(name, value);
				cookie.setVersion(0);
				cookie.setDomain(host);
				cookie.setPath(path);
				cookie.setExpiryDate(gc.getTime());

				store.addCookie(cookie);
			}
		} catch (Exception e) {
			PreloadWinChromeCookies.logger.error(ExceptionUtils.getStackTrace(e));
			this.failed = true;
		}
	}
}
