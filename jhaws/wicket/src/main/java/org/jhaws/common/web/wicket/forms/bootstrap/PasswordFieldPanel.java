package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.forms.common.FormRowPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TextFieldSettings;

@SuppressWarnings("serial")
public class PasswordFieldPanel<T extends Serializable>
		extends DefaultFormRowPanel<T, TextField<T>, TextFieldSettings> {
	public PasswordFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
			TextFieldSettings componentSettings) {
		super(model, propertyPath, formSettings, componentSettings);
	}

	@Override
	protected void onFormComponentTag(ComponentTag tag) {
		super.onFormComponentTag(tag);
		if (getComponentSettings().getMinlength() != null) {
			WebHelper.tag(tag, "minlength", String.valueOf(getComponentSettings().getMinlength()));
		}
		if (getComponentSettings().getMinlength() != null) {
			WebHelper.tag(tag, "maxlength", String.valueOf(getComponentSettings().getMaxlength()));
		}
		if (StringUtils.isNotBlank(getComponentSettings().getPattern())) {
			WebHelper.tag(tag, "pattern", getComponentSettings().getPattern());
		}
		if (Boolean.TRUE.equals(getComponentSettings().getSelectAllOnFocus())) {
			WebHelper.tag(tag, "onclick", TextFieldPanel.ONCLICK);
		}
	}

	@Override
	protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
		TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				onFormComponentTag(tag);
			}
		};
		return textField;
	}

	@Override
	public FormRowPanelParent<T, T, TextField<T>, TextFieldSettings> addComponents(TextFieldSettings settings) {
		FormRowPanelParent<T, T, TextField<T>, TextFieldSettings> components = super.addComponents(settings);
		getComponentContainer(settings).add(new WebMarkupContainer("eye") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("toggle", "#" + getComponent().getMarkupId());
			}
		});
		return components;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!isEnabledInHierarchy()) {
			return;
		}
		response.render(OnDomReadyHeaderItem.forScript(
				";$('.toggle-password').click(function(){$(this).toggleClass('fa-eye fa-eye-slash');var input=$($(this).attr('toggle'));if(input.attr('type')=='password'){input.attr('type','text');}else{input.attr('type','password');}});"));
	}
}
