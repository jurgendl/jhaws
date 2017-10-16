package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;

public class CheckCookiesEnabled extends Panel {
    private static final long serialVersionUID = -5913144741901927426L;

    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(CheckCookiesEnabled.class, "CheckCookiesEnabled.js");

    public CheckCookiesEnabled() {
        super("check.cookies.enabled");

        // site uses cookies info (asked when user choice not known)
        WebMarkupContainer cookiesQ = new WebMarkupContainer("nocookies");
        cookiesQ.add(new StatelessLink<String>("allowCookies") {
            private static final long serialVersionUID = -8778073423020169707L;

            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(true);
                CheckCookiesEnabled.this.get("nocookies").setVisible(false);
            }
        });
        cookiesQ.add(new StatelessLink<String>("disallowCookies") {
            private static final long serialVersionUID = 4600682011663940306L;

            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(false);
                CheckCookiesEnabled.this.get("nocookies").setVisible(false);
            }
        });

        cookiesQ.setVisible(WicketApplication.get().isCheckCookiesEnabled() && WicketSession.get().getCookies().getUserAllowedCookies() == null);
        add(cookiesQ);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        if (WicketApplication.get().isCheckCookiesEnabled() && WicketSession.get().getCookies().getUserAllowedCookies() == null) {
            response.render(JavaScriptHeaderItem.forReference(JS));
        }
    }
}
