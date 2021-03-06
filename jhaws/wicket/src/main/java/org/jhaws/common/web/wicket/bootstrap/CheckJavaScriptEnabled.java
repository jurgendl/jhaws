package org.jhaws.common.web.wicket.bootstrap;

import org.apache.wicket.markup.html.panel.Panel;
import org.jhaws.common.web.wicket.WicketApplication;

public class CheckJavaScriptEnabled extends Panel {
    private static final long serialVersionUID = -6219666470496738714L;

    public CheckJavaScriptEnabled() {
        super("check.javascript.enabled");
        setVisible(WicketApplication.get().getSettings().isCheckJavaScriptEnabled());
    }
}
