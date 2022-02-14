package org.jhaws.common.web.wicket.demo;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.web.wicket.bootbox.ConfirmationAjaxLink;
import org.jhaws.common.web.wicket.bootbox.ConfirmationAjaxSubmitLink;
import org.jhaws.common.web.wicket.bootbox.ConfirmationSubmitLink;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class TestMessagesPage extends DefaultWebPage {
    Model<String> counter;
    Model<String> innercounter;

    public TestMessagesPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        counter = Model.of("" + 0);
        innercounter = Model.of("" + 0);

        add(new TestMessagesPanel("feedback1"));
        add(new TestMessagesPanel("feedback2"));
        // getFeedbackMessages().fatal(this, "fatal");
        // getFeedbackMessages().error(this, "error");
        // getFeedbackMessages().warn(this, "warn");
        // getFeedbackMessages().success(this, "success");
        // getFeedbackMessages().info(this, "info");
        // getFeedbackMessages().debug(this, "debug");

        Form<String> form = new Form<String>("formsubmit", counter) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                counter.setObject(String.valueOf(Integer.parseInt(counter.getObject()) + 1));
            }
        };
        form.add(new TextField<>("counter", counter));
        add(form);
        form.add(new ConfirmationSubmitLink("submit1").setMessage(Model.of("confirm")));
        form.add(new ConfirmationSubmitLink("submit2").setMessage(Model.of("confirm")));
        form.add(new ConfirmationSubmitLink("submit3").setMessage(Model.of("confirm")));
        form.add(new ConfirmationAjaxSubmitLink("submit4", form).setMessage(Model.of("confirm")));
        form.add(new ConfirmationAjaxSubmitLink("submit5", form).setMessage(Model.of("confirm")));
        form.add(new ConfirmationAjaxSubmitLink("submit6", form).setMessage(Model.of("confirm")));

        Form<String> innerform = new Form<String>("innerformsubmit", innercounter) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                innercounter.setObject(String.valueOf(Integer.parseInt(innercounter.getObject()) + 1));
            }
        };
        innerform.add(new TextField<>("innercounter", innercounter));
        form.add(innerform);
        innerform.add(new ConfirmationSubmitLink("innersubmit1").setMessage(Model.of("confirm")));
        innerform.add(new ConfirmationSubmitLink("innersubmit2").setMessage(Model.of("confirm")));
        innerform.add(new ConfirmationSubmitLink("innersubmit3").setMessage(Model.of("confirm")));
        innerform.add(new ConfirmationAjaxSubmitLink("innersubmit4", innerform).setMessage(Model.of("confirm")));
        innerform.add(new ConfirmationAjaxSubmitLink("innersubmit5", innerform).setMessage(Model.of("confirm")));
        innerform.add(new ConfirmationAjaxSubmitLink("innersubmit6", innerform).setMessage(Model.of("confirm")));

        add(new ConfirmationAjaxLink<String>("link1") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(form);
                counter.setObject(String.valueOf(Integer.parseInt(counter.getObject()) + 1));
                innercounter.setObject(String.valueOf(Integer.parseInt(innercounter.getObject()) + 1));
            }
        }.setMessage(Model.of("confirm")));
        add(new ConfirmationAjaxLink<String>("link2") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(form);
                counter.setObject(String.valueOf(Integer.parseInt(counter.getObject()) + 1));
                innercounter.setObject(String.valueOf(Integer.parseInt(innercounter.getObject()) + 1));
            }
        }.setMessage(Model.of("confirm")));
    }
}
