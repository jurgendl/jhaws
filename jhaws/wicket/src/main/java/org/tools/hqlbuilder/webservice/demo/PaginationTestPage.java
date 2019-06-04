package org.tools.hqlbuilder.webservice.demo;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.bootstrap4.popoverx.PopoverXPanel;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class PaginationTestPage extends DefaultWebPage {
	@Override
	protected void addComponents(PageParameters parameters, MarkupContainer html) {
		html.add(new PopoverXPanel("popover-x-test"));
		html.add(new PopoverXPanel("pagination-popover") {
			@Override
			protected Model<Boolean> createHeaderVisibibleModel() {
				return Model.of(Boolean.FALSE);
			}

			@Override
			protected Component createTrigger(String id) {
				Fragment fragment = new Fragment(id, POPOVER_X_TRIGGER_FRAGMENT, PaginationTestPage.this);
				Button triggeredBy = new Button(id) {
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("data-target", getDataTargetAttribute());
					}
				};
				fragment.add(triggeredBy);
				return fragment;
			}

			@Override
			protected Component createContent(String id) {
				WebMarkupContainer content = new WebMarkupContainer(id);
				content.add(new Fragment(id, "popover-form-content-fragment", PaginationTestPage.this));
				return content;
			}

			@Override
			protected Component createFooter(String id) {
				WebMarkupContainer footer = new WebMarkupContainer(id);
				footer.add(new Fragment(id, "popover-footer-content-fragment", PaginationTestPage.this));
				return footer;
			}
		});
	}
}
