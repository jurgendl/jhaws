package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.util.MissingResourceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings("serial")
public abstract class FormRowPanelParent<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>>
        extends Panel implements FormConstants {
    protected static final Logger logger = LoggerFactory.getLogger(FormRowPanelParent.class);

    protected static final Logger loggerMissingLabel = LoggerFactory.getLogger("missinglabels");

    protected Label label;

    protected IModel<String> labelModel;

    protected IModel<T> valueModel;

    /** normally a lambda path */
    protected transient P propertyPath;

    protected Class<T> propertyType;

    protected String propertyName;

    protected FeedbackPanel feedbackPanel;

    protected C component;

    protected FormSettings formSettings;

    protected ElementSettings componentSettings;

    protected FormRowPanelParent(boolean bootstrap, IModel<?> model, P propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
        super(FormConstants.FORM_ELEMENT, model);
        if (formSettings == null) {
            throw new NullPointerException("formSettings");
        }
        if (componentSettings == null) {
            throw new NullPointerException("componentSettings");
        }
        this.formSettings = formSettings;
        this.componentSettings = componentSettings;
        this.propertyPath = propertyPath;
        if (bootstrap)
            WebHelper.hide(this);
        else
            WebHelper.show(this);
    }

    public FormRowPanelParent(boolean bootstrap, P propertyPath, IModel<T> valueModel, FormSettings formSettings, ElementSettings componentSettings) {
        this(bootstrap, valueModel, propertyPath, formSettings, componentSettings);
        this.valueModel = valueModel;
    }

    @Override
    public MarkupContainer add(Component... childs) {
        if ((childs == null) || (childs.length == 0) || ((childs.length == 1) && (childs[0] == null))) {
            return this;
        }
        return super.add(childs);
    }

    public FormRowPanelParent<P, T, C, ElementSettings> addComponents(ElementSettings settings) {
        add(getLabel(settings));
        add(getComponentContainer(settings));
        C _component = getComponent();
        getComponentContainer(settings).add(_component);
        getComponentContainer(settings).add(getRequiredMarker());
        getComponentContainer(settings).add(getFeedback());
        return this;
    }

    protected WebMarkupContainer componentContainer;

    public String getLabelClass(ElementSettings settings) {
        return null;
    }

    public String getComponentClass(ElementSettings settings) {
        return null;
    }

    public WebMarkupContainer getComponentContainer(ElementSettings settings) {
        if (componentContainer == null) {
            componentContainer = new WebMarkupContainer("componentContainer");
            String componentClass = getComponentClass(settings);
            if (componentClass != null) componentContainer.add(new CssClassNameAppender(componentClass));
        }
        return componentContainer;
    }

    @SuppressWarnings("unchecked")
    public FormRowPanelParent<P, T, C, ElementSettings> afterAddComponents() {
        // jsr bean validation
        this.getComponent().add(new PropertyValidator<T>());
        this.getComponent().setLabel((IModel<String>) this.getLabel(null).getDefaultModel());
        this.setupRequiredBehavior();
        this.setupId();
        return this;
    }

    protected abstract C createComponent(IModel<T> model, Class<T> valueType);

    public C getComponent() {
        if (this.component == null) {
            this.component = this.createComponent(this.getValueModel(), this.getPropertyType());
            this.setupRequired(this.component);
        }
        return this.component;
    }

    public ElementSettings getComponentSettings() {
        return this.componentSettings;
    }

    protected String getFeedbackMessageCSSClass(final FeedbackMessage message) {
        return "feedbackPanel" + message.getLevelAsString();
    }

    protected FeedbackPanel getFeedback() {
        if (this.feedbackPanel == null) {
            this.feedbackPanel = new FeedbackPanel(FormConstants.FEEDBACK_ID, new ComponentFeedbackMessageFilter(this.component)) {
                @Override
                public boolean isVisible() {
                    return super.isVisible() && this.anyMessage();
                }

                protected String getCSSClass(final FeedbackMessage message) {
                    return getFeedbackMessageCSSClass(message);
                }
            };
            this.feedbackPanel.setOutputMarkupId(true);
        }
        return this.feedbackPanel;
    }

    public C getFormComponent() {
        return this.getComponent();
    }

    public Label getLabel(ElementSettings settings) {
        if (this.label == null) {
            this.label = new Label(FormConstants.LABEL, this.getLabelModel()) {
                @Override
                public boolean isVisible() {
                    return super.isVisible() && settings.isShowLabel()
                            && ((FormRowPanelParent.this.formSettings == null) || FormRowPanelParent.this.formSettings.isShowLabel());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    String markupId = FormRowPanelParent.this.getComponent().getMarkupId();
                    tag.getAttributes().put(FormConstants.FOR, markupId);
                    tag.getAttributes().put(FormConstants.TITLE, FormRowPanelParent.this.getLabelModel().getObject());
                }
            };
            label.setEscapeModelStrings(false);
            String labelClass = getLabelClass(settings);
            if (labelClass != null) label.add(new CssClassNameAppender(labelClass));
        }
        return this.label;
    }

    public IModel<String> getLabelModel() {
        if (this.labelModel == null) {
            this.labelModel = new LoadableDetachableModel<String>() {
                @Override
                protected String load() {
                    return FormRowPanelParent.this.getLabelText();
                }
            };
        }
        return this.labelModel;
    }

    protected String getLabelText() {
        try {
            return this.getString(this.getPropertyName());
        } catch (MissingResourceException ex) {
            loggerMissingLabel.error(this.getPropertyName() + "=" + this.getPropertyName().toLowerCase());
            return "[" + this.getPropertyName() + "_" + this.getLocale() + "]";
        }
    }

    protected String getPlaceholderText() {
        try {
            return this.getString(FormConstants.PLACEHOLDER);
        } catch (MissingResourceException ex) {
            logger.info("no translation for " + FormConstants.PLACEHOLDER);
            return null;
        }
    }

    public String getPropertyName() {
        if (this.propertyName == null) {
            try {
                this.propertyName = WebHelper.name(this.propertyPath);
            } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                this.propertyName = this.propertyPath == null ? null : this.propertyPath.toString();
            }
        }
        return this.propertyName;
    }

    public Class<T> getPropertyType() {
        if (this.propertyType == null) {
            throw new NullPointerException();
        }
        return this.propertyType;
    }

    protected WebMarkupContainer getRequiredMarker() {
        WebMarkupContainer requiredMarker = new WebMarkupContainer("requiredMarker") {
            @Override
            public boolean isVisible() {
                return super.isVisible() && FormRowPanelParent.this.componentSettings.isRequired();
            }
        };
        return requiredMarker;
    }

    public IModel<T> getValueModel() {
        if (this.valueModel == null) {
            throw new NullPointerException();
        }
        return this.valueModel;
    }

    public FormRowPanelParent<P, T, C, ElementSettings> inheritId() {
        // . is replaced because sql selectors don't work well with dot's
        this.getComponent().setMarkupId(this.getPropertyName().toString().replace('.', FormConstants.DOT_REPLACER));
        return this;
    }

    /**
     * call this in overridden method:<br>
     * org.tools.hqlbuilder.webservice.wicket.forms.[Component]Panel. createComponent().new [Component]() {...}.onComponentTag(ComponentTag)
     */
    protected void onFormComponentTag(ComponentTag tag) {
        this.setupPlaceholder(tag);
        this.setupRequired(tag);
        this.setupReadOnly(tag);
    }

    public FormRowPanelParent<P, T, C, ElementSettings> setLabelModel(IModel<String> labelModel) {
        this.labelModel = labelModel;
        return this;
    }

    public FormRowPanelParent<P, T, C, ElementSettings> setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public FormRowPanelParent<P, T, C, ElementSettings> setPropertyType(Class<T> propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    // FIXME specific
    protected Behavior setupDynamicRequiredBehavior() {
        return new AjaxFormComponentUpdatingBehavior(HtmlFormEvent.BLUR) {
            @Override
            protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
                C c = FormRowPanelParent.this.getComponent();
                c.add(new CssClassNameRemover(FormRowPanelParent.this.formSettings.getValidClass()));
                c.add(new CssClassNameAppender(FormRowPanelParent.this.formSettings.getInvalidClass()));
                ajaxRequestTarget.add(c, FormRowPanelParent.this.getFeedback());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                C c = FormRowPanelParent.this.getComponent();
                c.add(new CssClassNameRemover(FormRowPanelParent.this.formSettings.getInvalidClass()));
                c.add(new CssClassNameAppender(FormRowPanelParent.this.formSettings.getValidClass()));
                ajaxRequestTarget.add(c, FormRowPanelParent.this.getFeedback());
            }
        };
    }

    protected void setupId() {
        if (this.formSettings.isInheritId() || this.componentSettings.isInheritId()) {
            this.inheritId();
        }
    }

    protected void setupPlaceholder(ComponentTag tag) {
        if ((this.componentSettings != null) && !this.componentSettings.isShowPlaceholder()) {
            WebHelper.untag(tag, FormConstants.PLACEHOLDER);
        } else if ((this.componentSettings != null) && this.componentSettings.isShowPlaceholder()) {
            WebHelper.tag(tag, FormConstants.PLACEHOLDER, this.getPlaceholderText());
        } else if ((this.formSettings != null) && this.formSettings.isShowPlaceholder()) {
            WebHelper.tag(tag, FormConstants.PLACEHOLDER, this.getPlaceholderText());
        } else {
            WebHelper.untag(tag, FormConstants.PLACEHOLDER);
        }
    }

    protected void setupReadOnly(ComponentTag tag) {
        if ((this.componentSettings != null) && this.componentSettings.isReadOnly()) {
            WebHelper.tag(tag, FormConstants.READ_ONLY, FormConstants.READ_ONLY);
        } else {
            WebHelper.untag(tag, FormConstants.READ_ONLY);
        }
    }

    protected void setupRequired(C component) {
        try {
            component.setRequired(this.componentSettings.isRequired());
            if (StringUtils.isNotBlank(this.formSettings.getRequiredClass())) {
                if (this.componentSettings.isRequired()) {
                    component.add(new CssClassNameAppender(this.formSettings.getRequiredClass()));
                } else {
                    component.add(new CssClassNameRemover(this.formSettings.getRequiredClass()));
                }
            }
        } catch (WicketRuntimeException ex) {
            // TODO primitive
        }
    }

    protected void setupRequired(ComponentTag tag) {
        if ((this.formSettings != null) && this.formSettings.isClientsideRequiredValidation() && (this.componentSettings != null)
                && this.componentSettings.isRequired()) {
            WebHelper.tag(tag, FormConstants.REQUIRED, FormConstants.REQUIRED);
        } else {
            WebHelper.untag(tag, FormConstants.REQUIRED);
        }
    }

    protected void setupRequiredBehavior() {
        C c = this.getComponent();
        if (this.formSettings.isAjax() && this.formSettings.isLiveValidation() && !(c instanceof PasswordTextField)
                && !(c instanceof com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker)) {
            // c.add(setupDynamicRequiredBehavior());
        }
    }

    public FormRowPanelParent<P, T, C, ElementSettings> setValueModel(IModel<T> valueModel) {
        this.valueModel = valueModel;
        return this;
    }

    public boolean takesUpSpace() {
        return true;
    }
}
