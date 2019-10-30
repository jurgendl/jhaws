package org.jhaws.common.net.client5;

import java.util.Date;

import org.jhaws.common.io.FilePath;

public interface CookieStoreInterceptor {
    public static final FilePath user_home = FilePath.getUserHomeDirectory();

    public void beforeAddCookie(DefaultCookieStore store);

    public void beforeClear(DefaultCookieStore store);

    public void beforeClearExpired(DefaultCookieStore store, Date date);

    public void beforeGetCookies(DefaultCookieStore store);
}
