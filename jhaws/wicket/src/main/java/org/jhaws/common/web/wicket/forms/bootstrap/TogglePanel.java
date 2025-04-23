package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.CheckBoxSettings;
import org.jhaws.common.web.wicket.forms.common.FormConstants;
import org.jhaws.common.web.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class TogglePanel extends DefaultFormRowPanel<Boolean, CheckBox, CheckBoxSettings> {
    public static final String CHECKBOXLABEL = "checkboxlabel";

    public TogglePanel(IModel<?> model, LambdaPath<?, Boolean> propertyPath, FormSettings formSettings,
                       CheckBoxSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        CheckBox checkBox = new CheckBox(VALUE, model) {
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
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
    }

    @Override
    protected void setupReadOnly(ComponentTag tag) {
        tag.addBehavior(new AttributeModifier("disabled", "disabled"));
    }

    @Override
    public Label getLabel(CheckBoxSettings settings) {
        if (label == null) {
            label = new Label(LABEL, getLabelModel()) {
                @Override
                public boolean isVisible() {
                    return super.isVisible() && settings.isShowLabel()
                            && (formSettings == null || formSettings.isShowLabel());
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
    public TogglePanel addComponents(CheckBoxSettings settings) {
        CheckBox _c = getComponent();
        WebMarkupContainer container = getComponentContainer(settings);
        this.add(getLabel(settings));
        this.add(container.add(_c));
        this.add(container.add(createCheckboxLabel(_c)));
        this.add(container.add(getRequiredMarker().setVisible(false)));
        this.add(container.add(getFeedback()));
        return this;
    }

    protected org.apache.wicket.Component createCheckboxLabel(CheckBox _c) {
        return new Label(CHECKBOXLABEL, getComponentSettings().isLabelBehind() ? getLabelModel() : Model.of("")) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, _c.getMarkupId());
            }
        }/* .setVisible(getComponentSettings().isLabelBehind()) */;
    }
}
