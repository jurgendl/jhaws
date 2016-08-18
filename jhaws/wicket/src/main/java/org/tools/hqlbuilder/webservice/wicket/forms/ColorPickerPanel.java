package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.spectrum.Spectrum;

/**
 * @see http://bgrins.github.io/spectrum
 */
public class ColorPickerPanel extends DefaultFormRowPanel<String, TextField<String>, ColorPickerSettings> {
    private static final long serialVersionUID = 3920158103962799959L;

    public ColorPickerPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, ColorPickerSettings colorPickerSettings) {
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
        return new TextField<String>(VALUE, model, valueType) {
            private static final long serialVersionUID = -989970628058227688L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (!isEnabledInHierarchy()) {
            return;
        }

        response.render(CssHeaderItem.forReference(Spectrum.SPECTRUM_CSS));
        response.render(JavaScriptHeaderItem.forReference(Spectrum.SPECTRUM_I18N_JS));
        response.render(OnDomReadyHeaderItem.forScript(createLoadScript()));
    }

    protected String createLoadScript() {
        ColorPickerSettings colorPickerSettings = ColorPickerSettings.class.cast(getComponentSettings());
        if (!(getValueModel() == null || StringUtils.isBlank(getValueModel().getObject()))) {
            colorPickerSettings.setColor(getValueModel().getObject());
        }
        return "$(\"#" + getComponent().getMarkupId() + "\").spectrum( " + getComponentSettings()
                + " ); $('.sp-replacer.sp-light').addClass('ui-corner-all');";
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}
