package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

@MountedPage("${wicket.login.mount}")
public class LogInPage extends DefaultWebPage {
    private static final long serialVersionUID = -959095871171401454L;

    public LogInPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(true);
    }

    @Override
    protected void addComponents() {
        super.addComponents();
        add(new LogInPanel());
    }
}
