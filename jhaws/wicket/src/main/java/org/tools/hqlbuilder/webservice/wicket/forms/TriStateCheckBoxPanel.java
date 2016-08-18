package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.converter.BooleanConverter;
import org.tools.hqlbuilder.webservice.jquery.ui.tristate.TriState;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://vanderlee.github.io/tristate/
 */
public class TriStateCheckBoxPanel extends DefaultFormRowPanel<Boolean, HiddenField<Boolean>, TriStateCheckBoxSettings> {
    private static final long serialVersionUID = 7669787482921703670L;

    public static final String INDETERMINATE = "indeterminate";

    public static final String CHECKED = "checked";

    protected CheckBox checkBox;

    public TriStateCheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, TriStateCheckBoxSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected void setupRequired(HiddenField<Boolean> component) {
        //
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    protected void setupRequired(ComponentTag tag) {
        //
    }

    @Override
    protected void setupRequiredBehavior() {
        //
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
    protected TriStateCheckBoxPanel addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(getCheckBox());
        this.add(getRequiredMarker().setVisible(false));
        this.add(getFeedback());
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(TriState.TRISTATE_FACTORY_JS));
    }

    @Override
    protected HiddenField<Boolean> createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        HiddenField<Boolean> hiddenField = new HiddenField<Boolean>(VALUE, model);
        hiddenField.setRequired(false);
        return hiddenField;
    }

    protected CheckBox createCheckBox() {
        IModel<Boolean> checkBoxModel = new IModel<Boolean>() {
            private static final long serialVersionUID = -2705845792897833793L;

            @Override
            public void detach() {
                //
            }

            @Override
            public void setObject(Boolean object) {
                //
            }

            @Override
            public Boolean getObject() {
                Object object = getValueModel().getObject();
                Boolean value = toBoolean(object);
                return value;
            }
        };
        checkBox = new CheckBox("checkbox", checkBoxModel) {
            private static final long serialVersionUID = -5957722711076496171L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                Object object = getValueModel().getObject();
                Boolean value = toBoolean(object);
                if (Boolean.TRUE.equals(value)) {
                    tag.remove(INDETERMINATE);
                    tag.put(CHECKED, CHECKED);
                } else if (Boolean.FALSE.equals(value)) {
                    tag.remove(INDETERMINATE);
                    tag.remove(CHECKED);
                } else {
                    tag.put(INDETERMINATE, INDETERMINATE);
                    tag.remove(CHECKED);
                }
            }
        };
        checkBox.add(new AttributeModifier("for", getComponent().getMarkupId()));
        checkBox.add(new CssClassNameAppender("tristate"));
        return checkBox;
    }

    public CheckBox getCheckBox() {
        if (checkBox == null) {
            checkBox = createCheckBox();
        }
        return this.checkBox;
    }

    protected BooleanConverter booleanConverter = new BooleanConverter();

    public Boolean toBoolean(Object object) {
        return toBoolean(object, booleanConverter, getLocale());
    }

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

    public BooleanConverter getBooleanConverter() {
        return this.booleanConverter;
    }

    public void setBooleanConverter(BooleanConverter booleanConverter) {
        this.booleanConverter = booleanConverter;
    }
}
