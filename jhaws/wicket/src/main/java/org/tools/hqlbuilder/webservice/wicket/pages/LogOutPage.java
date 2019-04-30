package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@MountedPage("${wicket.logout.mount}")
public class LogOutPage extends DefaultWebPage {
	private static final long serialVersionUID = -1844173741599209281L;

	public LogOutPage(PageParameters parameters) {
		super(parameters);
		setStatelessHint(true);
	}

	@Override
	protected void addComponents(PageParameters parameters, MarkupContainer html) {
		add(new LogOutPanel());
	}
}
