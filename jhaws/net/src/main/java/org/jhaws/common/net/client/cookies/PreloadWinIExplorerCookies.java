package org.jhaws.common.net.client.cookies;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PreloadWinIExplorerCookies
 */
public class PreloadWinIExplorerCookies implements CookieStoreInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PreloadWinIExplorerCookies.class);

    protected static final boolean windows = (System.getProperty("os.name") != null) && System.getProperty("os.name").toLowerCase().contains("win"); //$NON-NLS-3$

    private HashSet<String> domainsLoaded = new HashSet<String>();

    private boolean failed = false;

    /**
     * Creates a new PreloadWinIExplorerCookies object.
     */
    public PreloadWinIExplorerCookies() {
        if (!PreloadWinIExplorerCookies.windows) {
            throw new RuntimeException("not windows");
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

            String d = store.getClient().getDomain();

            if (this.domainsLoaded.contains(d)) {
                return;
            }

            this.domainsLoaded.add(d);

            int p = d.lastIndexOf('.');
            final String domain = d.substring(0, p);
            PreloadWinIExplorerCookies.logger.info("loadIExplorerCookiesWin(String) - domain=" + domain); //$NON-NLS-1$

            IODirectory dir = new IODirectory(CookieStoreInterceptor.user_home, "Cookies");
            PreloadWinIExplorerCookies.logger.info("loadIExplorerCookiesWin(String) - IODirectory dir=" + dir); //$NON-NLS-1$

            IOFile[] cookie_files = dir.listIOFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName();

                    return name.contains(domain);
                }
            });

            if (cookie_files.length == 0) {
                return;
            }

            byte[] buffer = cookie_files[0].read();
            String content = new String(buffer);
            String[] cookie_infos = content.split("\\Q*\n\\E");

            for (String cookie_info : cookie_infos) {
                String[] cookie_info_part = cookie_info.split("\n");
                int pos = cookie_info_part[2].indexOf('/');

                String ckey = cookie_info_part[0];
                String cvalue = cookie_info_part[1];
                String cdomain = cookie_info_part[2].substring(0, pos);
                String cpath = cookie_info_part[2].substring(pos);

                BasicClientCookie cookie = new BasicClientCookie(ckey, cvalue);
                cookie.setVersion(0);
                cookie.setDomain(cdomain);
                cookie.setPath(cpath);

                store.addCookie(cookie);
                PreloadWinIExplorerCookies.logger.info("loadIExplorerCookiesWin(String) - BasicClientCookie cookie=" + cookie); //$NON-NLS-1$
            }
        } catch (Exception ex) {
            PreloadWinIExplorerCookies.logger.error(ExceptionUtils.getStackTrace(ex));
            this.failed = true;
        }
    }
}
