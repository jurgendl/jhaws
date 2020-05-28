package org.jhaws.common.web.wicket.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.web.wicket.MountedPage;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;

@MountedPage("${wicket.login.mount}")
public class LogInPage extends DefaultWebPage {
	private static final long serialVersionUID = -959095871171401454L;

	public LogInPage(PageParameters parameters) {
		super(parameters);
		setStatelessHint(true);
	}

	@Override
	protected void addComponents(PageParameters parameters, MarkupContainer html) {
		add(new LogInPanel());
	}
}
