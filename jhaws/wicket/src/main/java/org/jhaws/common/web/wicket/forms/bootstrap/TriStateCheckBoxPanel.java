package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.converter.BooleanConverter;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.FormConstants;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TriStateCheckBoxSettings;

import java.util.Locale;

@SuppressWarnings("serial")
public class TriStateCheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox, TriStateCheckBoxSettings> {
    public static Boolean toBoolean(Object object, BooleanConverter booleanConverter, Locale locale) {
        if (object == null) {
            return null;
        }
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        if (object instanceof String) {
            return booleanConverter.convertToObject((String) object, locale);
        }
        throw new UnsupportedOperationException();
    }

    public static final String CHECKBOXLABEL = "checkboxlabel";

    private CheckBox checkBox;

    public TriStateCheckBoxPanel(IModel<?> model, LambdaPath<?, Boolean> propertyPath, FormSettings formSettings, TriStateCheckBoxSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        checkBox = new CheckBox(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (getComponentSettings().isReadOnly()) {
                    tag.getAttributes().put(DISABLED, null);
                }
            }
        };
        return checkBox;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(OnDomReadyHeaderItem.forScript(";$('#" + getComponent().getMarkupId() + "').prop('indeterminate', true);"));
    }

    @Override
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
    }

    @Override
    protected void setupReadOnly(ComponentTag tag) {
        //
    }

    @Override
    public Label getLabel(TriStateCheckBoxSettings settings) {
        if (label == null) {
            label = new Label(LABEL, getLabelModel()) {
                @Override
                public boolean isVisible() {
                    return super.isVisible() && settings.isShowLabel() && (formSettings == null || formSettings.isShowLabel());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    String markupId = getComponent().getMarkupId();
                    tag.getAttributes().put(FormConstants.FOR, markupId);
                    tag.getAttributes().put(FormConstants.TITLE, getTitleModel().getObject());
                }
            };
            prepareLabel(label, settings);
        }
        return label;
    }

    @Override
    public TriStateCheckBoxPanel addComponents(TriStateCheckBoxSettings settings) {
        CheckBox _c = getComponent();
        this.add(getComponentContainer(settings).add(_c));
        this.add(getLabel(settings));
        this.add(getComponentContainer(settings).add(new Label(CHECKBOXLABEL, getLabelModel()) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, _c.getMarkupId());
            }
        }.setVisible(false/* getComponentSettings().isLabelBehind() */)));
        this.add(getComponentContainer(settings).add(getRequiredMarker().setVisible(false)));
        this.add(getComponentContainer(settings).add(getFeedback()));
        return this;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }
}
