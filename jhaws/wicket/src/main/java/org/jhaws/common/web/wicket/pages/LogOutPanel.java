package org.jhaws.common.web.wicket.pages;

import java.util.Properties;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.jhaws.common.web.wicket.UserData;
import org.jhaws.common.web.wicket.WicketApplication;
import org.jhaws.common.web.wicket.WicketSession;
import org.springframework.security.core.Authentication;

public class LogOutPanel extends Panel {
    private static final long serialVersionUID = -6245651530312025190L;

    public LogOutPanel() {
        this("logoutpanel", Model.of(new UserData()));
        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);
        setOutputMarkupId(false);
    }

    public LogOutPanel(final String id, final IModel<UserData> model) {
        super(id, model);
        Properties webProperties = WicketApplication.get().getWebProperties();
        Authentication authentication = WicketSession.getSecurityContext().getAuthentication();
        add(new LogOutForm(authentication, webProperties));
        setVisible(authentication != null && !authentication.getPrincipal().equals(webProperties.getProperty("anonymous.user")));
    }

    public class LogOutForm extends StatelessForm<UserData> {
        private static final long serialVersionUID = -8305944845978935182L;

        protected final Properties webProperties;

        public LogOutForm(final Authentication authentication, final Properties webProperties) {
            super("logoutform", Model.of(new UserData()));
            this.webProperties = webProperties;
            Button logout = new Button("logout", new ResourceModel("logout.label"));
            add(logout.setMarkupId(logout.getId()));
            // Label usernamelabel = new Label("knownusername", authentication == null ? webProperties.getProperty("anonymous.user")
            // : authentication.getName());
            // usernamelabel.setVisible(authentication != null);
            // add(usernamelabel);
            setMarkupId(getId());
        }

        @Override
        protected CharSequence getActionUrl() {
            return getRequest().getContextPath() + webProperties.getProperty("logout");
        }
    }
}