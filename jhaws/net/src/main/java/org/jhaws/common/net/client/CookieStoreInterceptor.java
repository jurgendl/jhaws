package org.jhaws.common.net.client;

import java.util.Date;

import org.jhaws.common.io.FilePath;

public interface CookieStoreInterceptor<S> {
	public static final FilePath user_home = FilePath.getUserHomeDirectory();

	public void beforeAddCookie(S store);

	public void beforeClear(S store);

	public void beforeClearExpired(S store, Date date);

	public void beforeGetCookies(S store);
}
