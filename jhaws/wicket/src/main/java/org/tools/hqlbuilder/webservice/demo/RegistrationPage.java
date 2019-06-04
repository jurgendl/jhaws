package org.tools.hqlbuilder.webservice.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class RegistrationPage extends DefaultWebPage {

    public RegistrationPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected Component addBreadcrumb(PageParameters parameters, MarkupContainer html, String id) {
        return super.addBreadcrumb(parameters, html, id).setVisible(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addNavigationBar(PageParameters parameters, MarkupContainer html, String id) {
        List<NavBarLink> navs = new ArrayList<>();
        navs.add(new NavBarLink("Home", "fa-fw fas fa-home", DefaultWebPage.class, null));
        navs.add(new NavBarLink("Refresh", "fa-fw fas fa-sync-alt", (Class<? extends WebPage>) getPageClass(), getPageParameters()));
        addNavigationBar(parameters, html, id, navs, false, false, true);
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        //
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        Label.class.cast(getPage().get("page.title")).setDefaultModelObject("...");
    }
}
