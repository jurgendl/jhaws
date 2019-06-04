package org.tools.hqlbuilder.webservice.demo;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.BootstrapFencedFeedbackPanel;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class TestMessagesPanel extends Panel {
	public TestMessagesPanel(String id) {
		super(id);
		Form<Object> form = new Form<Object>("form");
		add(form);
		form.add(new BootstrapFencedFeedbackPanel("feedback", this));
		form.add(new AjaxSubmitLink("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> _form) {
				getFeedbackMessages().fatal(form, form.getId() + " fatal");
				getFeedbackMessages().error(getPage(), getPage().getId() + " info");
				getFeedbackMessages().warn(form, form.getId() + " fatal");
				getFeedbackMessages().success(getPage(), getPage().getId() + " info");
				getFeedbackMessages().info(form, form.getId() + " fatal");
				getFeedbackMessages().debug(getPage(), getPage().getId() + " info");
				target.add(DefaultWebPage.class.cast(getPage()).getFeedbackPanel());
			}
		});
		// getFeedbackMessages().fatal(this, id + " fatal");
		// getFeedbackMessages().error(this, id + " error");
		// getFeedbackMessages().warn(this, id + " warn");
		// getFeedbackMessages().success(this, id + " success");
		// getFeedbackMessages().info(this, id + " info");
		// getFeedbackMessages().debug(this, id + " debug");
	}
}
