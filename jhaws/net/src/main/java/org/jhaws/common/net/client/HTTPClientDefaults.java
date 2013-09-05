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
    public static final String CONTENT_ENCODING = HTTP.CONTENT_ENCODING;

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
    public static final String PARAM_KEEP_ALIVE = HTTP.CONN_KEEP_ALIVE;

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

    /** Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0 */
    public static final String FIREFOX = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";

    /** Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36 */
    public static final String CHROME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36";

    /**
     * Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media
     * Center PC 6.0; .NET4.0C; .NET4.0E)
     */
    public static final String IEXPLORER = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";

    /** Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 */
    public static final String ANDROID_S3 = "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

    /** http.protocol.single-cookie-header */
    public static final String PARAM_SINGLE_COOKIE_HEADER = "http.protocol.single-cookie-header";

    /** 3000L */
    public static final int TIMEOUT = 3000;

    /** "Content-Type" */
    public static final String CONTENT_TYPE = "Content-Type";

    /** "Date" */
    public static final String DATE = "Date";
}
