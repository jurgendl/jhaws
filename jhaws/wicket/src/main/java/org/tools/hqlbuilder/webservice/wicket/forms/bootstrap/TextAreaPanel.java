package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextAreaSettings;

@SuppressWarnings("serial")
public class TextAreaPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextArea<T>, TextAreaSettings> {
    public TextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, TextAreaSettings textAreaSettings) {
        super(model, propertyPath, formSettings, textAreaSettings);
    }

    @Override
    protected void onFormComponentTag(ComponentTag tag) {
        super.onFormComponentTag(tag);
        tag(tag, "cols", getComponentSettings().getCols());
        tag(tag, "rows", getComponentSettings().getRows());
        if (!getComponentSettings().isResizable()) {
            // http://brett.batie.com/website_development/no-resize-textarea-in-chrome- safari/
            tag(tag, "style", "resize: none; height: auto;");
        }
        if (getComponentSettings().getMinlength() != null) {
            WebHelper.tag(tag, "minlength", String.valueOf(getComponentSettings().getMinlength()));
        }
        if (getComponentSettings().getMinlength() != null) {
            WebHelper.tag(tag, "maxlength", String.valueOf(getComponentSettings().getMaxlength()));
        }
    }

    @Override
    protected TextArea<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextArea<T> textArea = new TextArea<T>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return textArea;
    }
}
