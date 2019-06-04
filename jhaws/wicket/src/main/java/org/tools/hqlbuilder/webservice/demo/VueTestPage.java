package org.tools.hqlbuilder.webservice.demo;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.vue.Vue;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class VueTestPage extends DefaultWebPage {
    public VueTestPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected Component addBreadcrumb(PageParameters parameters, MarkupContainer html, String id) {
        Component breadcrumb = super.addBreadcrumb(parameters, html, id);
        breadcrumb.setVisible(false);
        return breadcrumb;
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        //
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(Vue.JS_APP));
        response.render(JavaScriptHeaderItem.forScript(
                ";var testVueApplication = AppVue('" + WicketApplication.getRestPath() + VueTestRest.PATH + "','appVueTest');", "vuetest_rest_init"));
        response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), getClass().getSimpleName() + ".css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), getClass().getSimpleName() + ".js")));
    }
}