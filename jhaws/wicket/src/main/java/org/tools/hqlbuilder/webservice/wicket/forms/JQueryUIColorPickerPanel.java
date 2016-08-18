package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryuicolorpicker.JQueryUIColorPicker;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://vanderlee.github.io/colorpicker/
 */
public class JQueryUIColorPickerPanel extends DefaultFormRowPanel<String, TextField<String>, JQueryUIColorPickerSettings> {
    private static final long serialVersionUID = 6138233201045557179L;

    protected Output output;

    public JQueryUIColorPickerPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings,
            JQueryUIColorPickerSettings colorPickerSettings) {
        super(model, propertyPath, formSettings, colorPickerSettings);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    public Class<String> getPropertyType() {
        return String.class;
    }

    @Override
    protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
        TextField<String> textField = new TextField<String>(VALUE, model, valueType) {
            private static final long serialVersionUID = 56917009106322008L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        textField.add(new CssClassNameAppender(PrimeUI.puiinputtext + " " + JQueryUIColorPicker.jquicolorpicker));
        return textField;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(JQueryUIColorPicker.JQUERY_COLORPICKER_I18N_JS));
        response.render(CssHeaderItem.forReference(JQueryUIColorPicker.JQUERY_COLORPICKER_CSS));
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
        response.render(OnDomReadyHeaderItem.forScript(createLoadScript()));
    }

    protected String createLoadScript() {
        JQueryUIColorPickerSettings colorPickerSettings = JQueryUIColorPickerSettings.class.cast(getComponentSettings());
        // if (!(getValueModel() == null || StringUtils.isBlank(getValueModel().getObject()))) {
        // colorPickerSettings.setColor(getValueModel().getObject());
        // }
        return "$(\"#" + getComponent().getMarkupId() + "\").colorpicker(" + colorPickerSettings + ");";
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}
