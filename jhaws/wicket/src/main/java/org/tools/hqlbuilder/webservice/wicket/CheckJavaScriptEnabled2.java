package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.html.panel.Panel;

public class CheckJavaScriptEnabled2 extends Panel {
	private static final long serialVersionUID = -6219666470496738714L;

	public CheckJavaScriptEnabled2() {
		super("check.javascript.enabled");
		setVisible(WicketApplication.get().isCheckJavaScriptEnabled());
	}
}
