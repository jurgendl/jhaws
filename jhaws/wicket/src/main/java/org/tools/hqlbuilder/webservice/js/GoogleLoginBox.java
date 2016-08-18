package org.tools.hqlbuilder.webservice.js;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see https://developers.google.com/identity/sign-in/web/reference
 */
public class GoogleLoginBox extends Panel {
    private static final long serialVersionUID = -252625601338521811L;

    public static JavaScriptResourceReference JS_LOGIN_TEST = new JavaScriptResourceReference(GoogleLogin.class, "googlelogin.js");

    public GoogleLoginBox(String id) {
        super(id);
        add(new WebMarkupContainer("googlelogin"));
        // data-theme: light/dark
        // data-onsuccess: onSignIn => JS_LOGIN_TEST
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(GoogleLogin.JS_PLATFORM));
        response.render(JavaScriptHeaderItem.forReference(JS_LOGIN_TEST));
    }
}
