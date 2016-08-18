package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.UserData;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/public/register")
public class RegisterPage extends DefaultWebPage {
    private static final long serialVersionUID = -5922276325475978620L;

    public RegisterPage(PageParameters parameters) {
        super(parameters);

        StatelessForm<UserData> form = new StatelessForm<UserData>("registerform", Model.of(new UserData()));
        add(form);

        TextField<String> username = new TextField<String>("username") {
            private static final long serialVersionUID = -3575587602375327486L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put("placeholder", getString("username.placeholder"));
            }
        };
        EmailTextField email = new EmailTextField("email") {
            private static final long serialVersionUID = 7237862473933728443L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put("placeholder", getString("email.placeholder"));
            }
        };
        PasswordTextField password = new PasswordTextField("password") {
            private static final long serialVersionUID = 9183994935565397938L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put("placeholder", getString("password.placeholder"));
            }
        };
        Button register = new Button("register", new ResourceModel("register.label"));
        Button reset = new Button("reset", new ResourceModel("reset.label"));
        form.add(new Label("username.label", new ResourceModel("username.label")));
        form.add(new Label("password.label", new ResourceModel("password.label")));
        form.add(new Label("email.label", new ResourceModel("email.label")));
        form.add(username.setMarkupId(username.getId()));
        form.add(password.setMarkupId(password.getId()));
        form.add(email.setMarkupId(email.getId()));
        form.add(register.setMarkupId(register.getId()));
        form.add(reset.setMarkupId(reset.getId()));
    }
}
