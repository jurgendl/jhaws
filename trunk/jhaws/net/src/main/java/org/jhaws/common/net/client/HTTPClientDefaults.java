package org.jhaws.common.net.client;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.protocol.HTTP;

/**
 * HTTPClientDefaults
 */
public interface HTTPClientDefaults {
    /** Location */
    public static final String LOCATION = "Location";

    /** Content-Encoding */
    public static final String CONTENT_ENCODING = "Content-Encoding";

    /** false */
    public static final boolean HANDLE_REDIRECTS = true;

    /** accept types */
    public static final String ACCEPT = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*";

    /** gzip, deflate */
    public static final String ACCEPT_ENCODING = "gzip, deflate";

    /** default language, en */
    public static final String ACCEPT_LANGUAGE = "en";

    /** 300 */
    public static final int KEEP_ALIVE = 300;

    /** Keep-Alive */
    public static final String PARAM_KEEP_ALIVE = "Keep-Alive";

    /** Accept-Language */
    public static final String PARAM_ACCEPT_LANGUAGE = "Accept-Language";

    /** Accept-Encoding */
    public static final String PARAM_ACCEPT_ENCODING = "Accept-Encoding";

    /** Accept */
    public static final String PARAM_ACCEPT = "Accept";

    /** false */
    public static final boolean EXPECT_CONTINUE = true;

    /** UTF-8 */
    @SuppressWarnings("deprecation")
    public static final String CHARSET = HTTP.UTF_8;

    /** true */
    public static final boolean SINGLE_COOKIE_HEADER = true;

    /** compatibility */
    public static final String BROWSER_COMPATIBILITY = CookiePolicy.BROWSER_COMPATIBILITY;

    /** 1.1 */
    public static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;

    /**
     * Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.2.9) Gecko/20100824 Firefox/3.6.9
     */
    public static final String MOZILLA = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en; rv:1.9.2.9) Gecko/20100824 Firefox/3.6.9";

    /** http.protocol.single-cookie-header */
    public static final String PARAM_SINGLE_COOKIE_HEADER = "http.protocol.single-cookie-header";

    /** 1000L */
    public static final int TIMEOUT = 1000;

    /** "Content-Type" */
    public static final String CONTENT_TYPE = "Content-Type";

    /** "Date" */
    public static final String DATE = "Date";
}
