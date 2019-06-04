package org.tools.hqlbuilder.webservice.demo;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class RegistrationPage extends DefaultWebPage {
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
