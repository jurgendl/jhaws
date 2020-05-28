package org.jhaws.common.web.wicket.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/public")
public class PublicPage extends DefaultWebPage {
	private static final long serialVersionUID = 9191797529541011553L;

	public PublicPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void addComponents(PageParameters parameters, MarkupContainer html) {
		//
	}
}
