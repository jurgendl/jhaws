package org.jhaws.common.net.client.cookies;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;
import org.common.io.IODirectory;
import org.common.io.IOFile;

/**
 * PreloadWinFirefoxCookies
 */
public class PreloadWinFirefoxCookies implements CookieStoreInterceptor {
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(PreloadWinFirefoxCookies.class);

    /** field */
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
     * Creates a new PreloadWinFirefoxCookies object.
     */
    public PreloadWinFirefoxCookies() {
        if (!PreloadWinFirefoxCookies.windows) {
            throw new RuntimeException("not windows");
        }

        if (!PreloadWinFirefoxCookies.driver) {
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
     * @see org.jhaws.common.net.client.cookies.CookieStoreInterceptor#beforeClearExpired(util.html.client.cookies.PersistentCookieStore,
     *      java.util.Date)
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
        try {
            if (this.failed) {
                return;
            }

            if (store.getClient() == null) {
                throw new NullPointerException("HTTPClient not set");
            }

            String domain = store.getClient().getDomain();

            if (domain == null) {
                throw new NullPointerException("HTTPClient.domain not set");
            }

            if (this.domainsLoaded.contains(domain)) {
                return;
            }

            this.domainsLoaded.add(domain);

            final Set<Cookie> cookies = new HashSet<Cookie>();
            IODirectory ffr = new IODirectory(CookieStoreInterceptor.user_home, "Application Data/Mozilla/Firefox");
            IOFile ini = new IOFile(ffr, "profiles.ini");
            PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - IOFile ini=" + ini); //$NON-NLS-1$

            Properties p = new Properties();
            p.load(new FileInputStream(ini));

            IOFile ff2c = new IOFile(new IODirectory(ffr, p.getProperty("Path")), "cookies.txt");
            PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - IOFile text=" + ff2c);

            if (ff2c.exists()) {
                String line;
                BufferedReader ffcr = new BufferedReader(new InputStreamReader(new FileInputStream(ff2c)));

                while ((line = ffcr.readLine()) != null) {
                    if (line.startsWith(domain) || line.startsWith("." + domain) || line.startsWith("www." + domain)) {
                        String[] cookie_info_part = line.split("\t");
                        String ckey = cookie_info_part[5];
                        String cvalue = cookie_info_part[6];
                        String cdomain = cookie_info_part[0];
                        String cpath = cookie_info_part[2];
                        int cmaxlife = Integer.parseInt(cookie_info_part[4]);
                        Date expires = new Date(cmaxlife * 1000L);

                        BasicClientCookie cookie = new BasicClientCookie(ckey, cvalue);
                        cookie.setVersion(0);
                        cookie.setDomain(cdomain);
                        cookie.setPath(cpath);
                        cookie.setExpiryDate(expires);

                        cookies.add(cookie);
                        PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - BasicClientCookie cookie=" + cookie); //$NON-NLS-1$
                    }
                }
            } else {
                try {
                    IOFile ff3c = new IOFile(new IODirectory(ffr, p.getProperty("Path")), "cookies.sqlite");
                    ff3c.checkExistence();
                    PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - IOFile sqlite=" + ff3c);

                    String url = "jdbc:sqlite:/" + ff3c.getAbsolutePath().replace('\\', '/');
                    PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - String url=" + url);

                    String query = "select name, value, host, path, expiry from moz_cookies where host like '" + domain + "%' or host like '." + domain + "%' or host like 'www." + domain + "%'"; //$NON-NLS-3$ //$NON-NLS-4$
                    PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - String query=" + query); //$NON-NLS-1$

                    Connection con = DriverManager.getConnection(url, null, null);
                    con.setReadOnly(true);

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        String name = rs.getString(1);
                        String value = rs.getString(2);
                        String host = rs.getString(3);
                        String path = rs.getString(4);
                        String expiry = rs.getString(5);
                        Date expires = new Date(Long.parseLong(expiry) * 1000L);

                        BasicClientCookie cookie = new BasicClientCookie(name, value);
                        cookie.setVersion(0);
                        cookie.setDomain(host);
                        cookie.setPath(path);
                        cookie.setExpiryDate(expires);

                        cookies.add(cookie);
                        PreloadWinFirefoxCookies.logger.info("loadFirefoxCookiesWin(String) - BasicClientCookie cookie=" + cookie); //$NON-NLS-1$
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception ex) {
            PreloadWinFirefoxCookies.logger.error(ex, ex);
            this.failed = true;
        }
    }
}
