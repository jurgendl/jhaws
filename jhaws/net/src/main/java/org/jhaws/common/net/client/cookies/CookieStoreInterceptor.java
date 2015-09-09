package org.jhaws.common.net.client.cookies;

import java.util.Date;

import org.jhaws.common.io.IODirectory;

/**
 * CookieStoreInterceptor
 */
public interface CookieStoreInterceptor {
    public static final IODirectory user_home = IODirectory.getUserDir();

    public void beforeAddCookie(CookieStore store);

    public void beforeClear(CookieStore store);

    public void beforeClearExpired(CookieStore store, Date date);

    public void beforeGetCookies(CookieStore store);
}
