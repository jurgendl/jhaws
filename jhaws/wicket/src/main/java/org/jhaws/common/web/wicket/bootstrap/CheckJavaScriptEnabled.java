package org.jhaws.common.web.wicket.bootstrap;

import org.apache.wicket.markup.html.panel.Panel;
import org.jhaws.common.web.wicket.WicketApplication;

@SuppressWarnings("serial")
public class CheckJavaScriptEnabled extends Panel {
    public CheckJavaScriptEnabled() {
        super("check.javascript.enabled");
        setVisible(WicketApplication.get().getSettings().isCheckJavaScriptEnabled());
    }
}
