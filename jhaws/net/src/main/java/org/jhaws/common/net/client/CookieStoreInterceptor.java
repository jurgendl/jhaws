package org.jhaws.common.net.client;

import java.util.Date;

import org.jhaws.common.io.FilePath;

public interface CookieStoreInterceptor {
    public static final FilePath user_home = FilePath.getUserHomeDirectory();

    public void beforeAddCookie(CookieStore store);

    public void beforeClear(CookieStore store);

    public void beforeClearExpired(CookieStore store, Date date);

    public void beforeGetCookies(CookieStore store);
}
