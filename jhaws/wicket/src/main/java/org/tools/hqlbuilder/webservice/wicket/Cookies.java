package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

/**
 * cookies related
 */
public class Cookies implements Serializable {
    private static final long serialVersionUID = 3474852240751279647L;

    public static final String USER_ALLOWED_COOKIES = "cookies_on";

    /**
     * null=unknown => ask user for info; true/false user allowed/disallowed cookies; false => info only available for the duration of the session in
     * which it is saved; true => info available for duration of cookies in which it is saved; user needs page to change these settings
     */
    protected Boolean userAllowedCookies = null;

    public Cookies() {
        String val = loadCookie(USER_ALLOWED_COOKIES);
        if (Boolean.TRUE.toString().equals(val)) {
            userAllowedCookies = true;
        }
    }

    /**
     * will be <null> if user disallowed cookies because cookies are not saved and deleted if exists
     */
    public String loadCookie(String cookieName) {
        return loadCookie(cookieName, null);
    }

    /**
     * will be <defaultValue> if user disallowed cookies because cookies are not saved and deleted if exists
     */
    public String loadCookie(String cookieName, String defaultValue) {
        try {
            List<Cookie> cookies = ((WebRequest) RequestCycle.get().getRequest()).getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        return cookie.getValue();
                    }
                }
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * will be impossible if user disallowed cookies
     */
    public boolean saveCookie(String cookieName, String cookieValue, int expiryTimeInDays) {
        if (Boolean.TRUE.equals(userAllowedCookies)) {
            try {
                Cookie cookie = new Cookie(cookieName, cookieValue);
                cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(expiryTimeInDays));
                ((WebResponse) RequestCycle.get().getResponse()).addCookie(cookie);
                return true;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        // user disallowed cookies
        return false;
    }

    public void removeCookieIfPresent(String cookieName) {
        try {
            List<Cookie> cookies = ((WebRequest) RequestCycle.get().getRequest()).getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        ((WebResponse) RequestCycle.get().getResponse()).clearCookie(cookie);
                        return;
                    }
                }
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * can be <null> when session is new
     */
    public Boolean getUserAllowedCookies() {
        return this.userAllowedCookies;
    }

    public void setUserAllowedCookies(Boolean userAllowedCookies) {
        // set info on session level
        this.userAllowedCookies = userAllowedCookies;
        if (Boolean.TRUE.equals(userAllowedCookies)) {
            // user allowed cookies to be saved
            saveCookie(USER_ALLOWED_COOKIES, Boolean.TRUE.toString(), 365);
        } else {
            // user disallowed cookies
            // remove all cookies
            try {
                List<Cookie> cookies = ((WebRequest) RequestCycle.get().getRequest()).getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        ((WebResponse) RequestCycle.get().getResponse()).clearCookie(cookie);
                    }
                }
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }
}
