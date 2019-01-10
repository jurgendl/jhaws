package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.bootstrap4.tinymce.BootstrapTinyMCE;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class TinyMCEPanel extends DefaultFormRowPanel<String, TextArea<String>, TinyMCESettings> {
    public TinyMCEPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, TinyMCESettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected void onFormComponentTag(ComponentTag tag) {
        super.onFormComponentTag(tag);
    }

    @Override
    protected TextArea<String> createComponent(IModel<String> model, Class<String> valueType) {
        TextArea<String> textArea = new TextArea<String>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return textArea;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(BootstrapTinyMCE.factory()));
    }
}
