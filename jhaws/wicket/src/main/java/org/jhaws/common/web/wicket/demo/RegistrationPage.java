package org.jhaws.common.web.wicket.demo;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;

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

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(
				";$('.toggle-password').click(function(){$(this).toggleClass('fa-eye fa-eye-slash');var input=$($(this).attr('toggle'));if(input.attr('type')=='password'){input.attr('type','text');}else{input.attr('type','password');}});"));
	}
}
