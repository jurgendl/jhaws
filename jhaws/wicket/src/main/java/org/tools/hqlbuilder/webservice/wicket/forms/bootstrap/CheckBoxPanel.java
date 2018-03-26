package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

@SuppressWarnings("serial")
public class CheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox, CheckBoxSettings> {
    public static final String CHECKBOXLABEL = "checkboxlabel";

    public CheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, CheckBoxSettings componentSettings) {
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
        //
    }

    @Override
    public Label getLabel() {
        if (label == null) {
            label = new Label(LABEL, getLabelModel()) {
                @Override
                public boolean isVisible() {
                    return super.isVisible() && (formSettings == null || formSettings.isShowLabel());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    String markupId = getComponent().getMarkupId();
                    tag.getAttributes().put(FormConstants.FOR, markupId);
                    tag.getAttributes().put(FormConstants.TITLE, getLabelModel().getObject());
                }
            };
            if (getLabelClass() != null) {
                label.add(new CssClassNameAppender(getLabelClass()));
            }
        }
        return label;
    }

    @Override
    public CheckBoxPanel addComponents() {
        CheckBox _c = getComponent();
        // System.out.println(_c.getMarkupId());
        this.add(getComponentContainer().add(_c));
        this.add(getLabel());
        this.add(getComponentContainer().add(new Label(CHECKBOXLABEL, getLabelModel()) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, _c.getMarkupId());
            }
        }.setVisible(getComponentSettings().isLabelBehind())));
        this.add(getComponentContainer().add(getRequiredMarker().setVisible(false)));
        this.add(getComponentContainer().add(getFeedback()));
        return this;
    }
}
