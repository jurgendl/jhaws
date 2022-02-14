package org.jhaws.common.web.wicket.bootstrap;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;
import org.jhaws.common.web.wicket.WicketSession;

@SuppressWarnings("serial")
public class CheckCookiesEnabled extends Panel {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(CheckCookiesEnabled.class, "CheckCookiesEnabled.js");

    public CheckCookiesEnabled() {
        super("check.cookies.enabled");

        // site uses cookies info (asked when user choice not known)
        WebMarkupContainer cookiesQ = new WebMarkupContainer("nocookies");
        cookiesQ.add(new StatelessLink<String>("allowCookies") {
            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(true);
                CheckCookiesEnabled.this.get("nocookies").setVisible(false);
            }
        });
        cookiesQ.add(new StatelessLink<String>("disallowCookies") {
            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(false);
                CheckCookiesEnabled.this.get("nocookies").setVisible(false);
            }
        });

        cookiesQ.setVisible(WicketApplication.get().getSettings().isCheckCookiesEnabled() && WicketSession.get().getCookies().getUserAllowedCookies() == null);
        add(cookiesQ);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        if (WicketApplication.get().getSettings().isCheckCookiesEnabled() && WicketSession.get().getCookies().getUserAllowedCookies() == null) {
            response.render(JavaScriptHeaderItem.forReference(JS));
        }
    }
}
