package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;

public class LogInOutLinksPanel extends Panel {
    private static final long serialVersionUID = 392027721536352791L;

    public LogInOutLinksPanel(String id, boolean show) {
        super(id);
        Properties webProperties = WicketApplication.get().getWebProperties();
        Authentication authentication = WicketSession.getSecurityContext().getAuthentication();
        BookmarkablePageLink<String> loginlink = new BookmarkablePageLink<String>("loginlink", LogInPage.class);
        loginlink.setVisible(show && (authentication == null || authentication.getPrincipal().equals(webProperties.getProperty("anonymous.user"))));
        add(loginlink);
        BookmarkablePageLink<String> logoutlink = new BookmarkablePageLink<String>("logoutlink", LogOutPage.class);
        logoutlink.setVisible(show && !loginlink.isVisible());
        add(logoutlink);
    }
}
