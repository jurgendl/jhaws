package org.jhaws.common.net.client.cookies;

import java.util.Date;
import java.util.HashSet;

/**
 * PreloadWinChromeCookies
 */
public class PreloadUnixChromeCookies implements CookieStoreInterceptor {
	public static final boolean windows = (System.getProperty("os.name") != null) && System.getProperty("os.name").toLowerCase().contains("win"); //$NON-NLS-3$

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
	@SuppressWarnings("unused")
	private HashSet<String> domainsLoaded = new HashSet<String>();

	/** failed */
	@SuppressWarnings("unused")
	private boolean failed = false;

	/**
	 * Creates a new PreloadUnixChromeCookies object.
	 */
	public PreloadUnixChromeCookies() {
		if (PreloadUnixChromeCookies.windows) {
			throw new RuntimeException("not unix");
		}

		if (!PreloadUnixChromeCookies.driver) {
			throw new RuntimeException("sqllite");
		}

		throw new RuntimeException("not implemented");
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
	public void beforeGetCookies(CookieStore store) {
		//
	}
}
