package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TextFieldSettings;

@SuppressWarnings("serial")
public class TextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, TextFieldSettings> {
    public static final String ONDROP = ";event.preventDefault();$(event.target).val(event.dataTransfer.getData('text'));";

    public static final String ONCLICK = ";this.setSelectionRange(0,this.value.length);";

    public TextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, TextFieldSettings componentSettings) {
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
        if (Boolean.FALSE.equals(getComponentSettings().getAutocomplete())) {
            WebHelper.tag(tag, "autocomplete", "off");
        }
        if (StringUtils.isNotBlank(getComponentSettings().getPattern())) {
            WebHelper.tag(tag, "pattern", getComponentSettings().getPattern());
        }
        if (Boolean.TRUE.equals(getComponentSettings().getReplaceAllOnDrop())) {
            WebHelper.tag(tag, "ondrop", ONDROP);
        }
        if (Boolean.TRUE.equals(getComponentSettings().getSelectAllOnFocus())) {
            WebHelper.tag(tag, "onclick", ONCLICK);
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
}
