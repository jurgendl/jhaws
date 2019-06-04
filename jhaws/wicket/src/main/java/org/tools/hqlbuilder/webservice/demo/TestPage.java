package org.tools.hqlbuilder.webservice.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class TestPage extends DefaultWebPage {

	public TestPage(PageParameters parameters) {
		super(parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addNavigationBar(PageParameters parameters, MarkupContainer html, String id) {
		List<NavBarLink> navs = new ArrayList<>();
		navs.add(new NavBarLink("Home", "fa-fw fas fa-home", TestPage.class, null));
		navs.add(new NavBarLink("Refresh", "fa-fw fas fa-sync-alt", (Class<? extends WebPage>) getPageClass(), getPageParameters()));
		addNavigationBar(parameters, html, id, navs, true, true, true).setVisible(false);
	}

	@Override
	protected WebComponent addStatusBar(PageParameters parameters, MarkupContainer html, String id, String contentid) {
		Label content = new Label(contentid, "status");
		html.add(new WebMarkupContainer(id).add(content));
		return content;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(new FilePath(TestPage.class, "TestPage-factory.js").readAll()));
	}

	@Override
	protected void addComponents(PageParameters parameters, MarkupContainer html) {
		//
	}
}
