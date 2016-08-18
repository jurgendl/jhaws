package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://vanderlee.github.io/tristate/
 */
public class CheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox, CheckBoxSettings> {
    public static final String CHECKBOXLABEL = "checkboxlabel";

    private static final long serialVersionUID = 7669787482921703670L;

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
                private static final long serialVersionUID = 8512361193054906821L;

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
    protected CheckBoxPanel addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(new Label(CHECKBOXLABEL, getLabelModel()) {
            private static final long serialVersionUID = 7533571573957789273L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, getComponent().getMarkupId());
            }
        }.add(new CssClassNameAppender(PrimeUI.puibutton)).setVisible(getComponentSettings().isLabelBehind()));
        this.add(getRequiredMarker().setVisible(false));
        this.add(getFeedback());
        return this;
    }
}
