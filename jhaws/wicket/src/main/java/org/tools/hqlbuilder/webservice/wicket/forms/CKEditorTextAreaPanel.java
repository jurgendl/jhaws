package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.ckeditor.CKEditor;

public class CKEditorTextAreaPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextArea<T>, CKEditorTextAreaSettings> {
    private static final long serialVersionUID = -828874019955093788L;

    public CKEditorTextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
            CKEditorTextAreaSettings CKEditorTextAreaSettings) {
        super(model, propertyPath, formSettings, CKEditorTextAreaSettings);
    }

    @Override
    protected TextArea<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextArea<T> textArea = new TextArea<T>(VALUE, model) {
            private static final long serialVersionUID = -4539408353408233394L;

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
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(CKEditor.forLanguage(componentSettings.getType(), getLocale())));
        response.render(OnDomReadyHeaderItem.forScript(createLoadScript()));
    }

    protected String createLoadScript() {
        componentSettings.setLanguage(getLocale().getLanguage());
        return "$(\"#" + getComponent().getMarkupId() + "\").ckeditor(" + componentSettings.toString() + ");";
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}
