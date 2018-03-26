package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://vanderlee.github.io/tristate/
 */
@SuppressWarnings("serial")
public class CheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox, CheckBoxSettings> {
    public static final String CHECKBOXLABEL = "checkboxlabel";

    public CheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, CheckBoxSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        CheckBox checkBox = new CheckBox(VALUE, model);
        if (getComponentSettings().isNice()) {
            if (formSettings.isPreferPrime()) {
                checkBox.add(new CssClassNameAppender(PrimeUI.puicheckbox));
            } else {
                checkBox.add(new CssClassNameAppender(JQueryUI.jquibutton));
            }
        }
        return checkBox;
    }

    @Override
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
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
                    tag.getAttributes().put(FOR, getComponent().getMarkupId() + "before");
                }
            };
        }
        return label;
    }

    @Override
    public CheckBoxPanel addComponents() {
        this.add(getLabel());
        this.add(getComponentContainer().add(getComponent()));
        this.add(getComponentContainer().add(new Label(CHECKBOXLABEL, getLabelModel()) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, getComponent().getMarkupId());
            }
        }.add(new CssClassNameAppender(PrimeUI.puibutton)).setVisible(getComponentSettings().isLabelBehind())));
        this.add(getComponentContainer().add(getRequiredMarker().setVisible(false)));
        this.add(getComponentContainer().add(getFeedback()));
        return this;
    }
}
