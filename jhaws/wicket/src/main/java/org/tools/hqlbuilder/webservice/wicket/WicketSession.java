package org.tools.hqlbuilder.webservice.wicket;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class WicketSession extends WebSession {
    private static final long serialVersionUID = 5401902370873451702L;

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static final String LOCALE = "locale";

    public static final String THEME = "theme";

    protected String jqueryUITheme;

    protected Cookies cookies = new Cookies();

    public WicketSession(Request request) {
        super(request);
        Injector.get().inject(this);
        init();
    }

    protected void init() {
        try {
            // fallback to default/session in #setLocaleFromCookie
            setLocaleFromCookie(cookies.loadCookie(LOCALE));
            // can be null (default?)
            jqueryUITheme = cookies.loadCookie(THEME);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    protected void setLocaleFromCookie(String localeCookieValue) {
        if (StringUtils.isNotBlank(localeCookieValue)) {
            String[] tmp = localeCookieValue.split("_");
            if (tmp.length == 3) {
                setLocale(new Locale(tmp[0], tmp[1], tmp[2]));
            } else if (tmp.length == 2) {
                setLocale(new Locale(tmp[0], tmp[1]));
            } else {
                setLocale(new Locale(tmp[0]));
            }
        } else {
            // used from session (browser)
        }
    }

    public static WicketSession get() {
        return WicketSession.class.cast(Session.get());
    }

    public synchronized String getJQueryUITheme() {
        return this.jqueryUITheme;
    }

    public synchronized void setJQueryUITheme(String theme) {
        this.jqueryUITheme = theme;
        cookies.saveCookie(THEME, theme, 365);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        cookies.saveCookie(LOCALE, locale == null ? "en" : locale.toString(), 365);
    }

    @Override
    public Locale getLocale() {
        Locale locale = super.getLocale();
        return locale;
    }

    public Cookies getCookies() {
        return this.cookies;
    }
}
