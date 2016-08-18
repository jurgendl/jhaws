package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/inputtextarea.html
 */
public class TextAreaPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextArea<T>, TextAreaSettings> {
    private static final long serialVersionUID = 7189330022100675150L;

    public TextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, TextAreaSettings textAreaSettings) {
        super(model, propertyPath, formSettings, textAreaSettings);
    }

    @Override
    protected TextArea<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextArea<T> textArea = new TextArea<T>(VALUE, model) {
            private static final long serialVersionUID = 4613842350545363891L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag(tag, "cols", getComponentSettings().getCols());
                tag(tag, "rows", getComponentSettings().getRows());
                /* http://brett.batie.com/website_development/no-resize-textarea-in-chrome-safari/ */
                if (!getComponentSettings().isResizable()) {
                    tag(tag, "style", "resize: none; height: auto;");
                }
                onFormComponentTag(tag);
            }
        };
        textArea.add(new CssClassNameAppender(PrimeUI.puiinputtextarea));
        return textArea;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }
}
