package org.tools.hqlbuilder.webservice.wicket.forms;

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
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
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
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

public abstract class FormRowPanel<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>> extends Panel implements FormConstants {
	private static final long serialVersionUID = 5258950770053560483L;

	protected static final Logger logger = LoggerFactory.getLogger(FormRowPanel.class);

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

	protected FormRowPanel(IModel<?> model, P propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
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
		WebHelper.show(this);
	}

	public FormRowPanel(P propertyPath, IModel<T> valueModel, FormSettings formSettings, ElementSettings componentSettings) {
		this(valueModel, propertyPath, formSettings, componentSettings);
		this.valueModel = valueModel;
	}

	@Override
	public MarkupContainer add(Component... childs) {
		if ((childs == null) || (childs.length == 0) || ((childs.length == 1) && (childs[0] == null))) {
			return this;
		}
		return super.add(childs);
	}

	protected FormRowPanel<P, T, C, ElementSettings> addComponents() {
		this.add(this.getLabel());
		this.add(this.getComponent());
		this.add(this.getRequiredMarker());
		this.add(this.getFeedback());
		return this;
	}

	@SuppressWarnings("unchecked")
	protected FormRowPanel<P, T, C, ElementSettings> afterAddComponents() {
		this.getComponent().add(new PropertyValidator<T>()); // jsr bean validation
		this.getComponent().setLabel((IModel<String>) this.getLabel().getDefaultModel());
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

	protected FeedbackPanel getFeedback() {
		if (this.feedbackPanel == null) {
			this.feedbackPanel = new FeedbackPanel(FormConstants.FEEDBACK_ID, new ComponentFeedbackMessageFilter(this.component)) {
				private static final long serialVersionUID = 211849904711387432L;

				@Override
				public boolean isVisible() {
					return super.isVisible() && this.anyMessage();
				}
			};
			this.feedbackPanel.setOutputMarkupId(true);
		}
		return this.feedbackPanel;
	}

	public C getFormComponent() {
		return this.getComponent();
	}

	public Label getLabel() {
		if (this.label == null) {
			this.label = new Label(FormConstants.LABEL, this.getLabelModel()) {
				private static final long serialVersionUID = 8512361193054906821L;

				@Override
				public boolean isVisible() {
					return super.isVisible() && ((FormRowPanel.this.formSettings == null) || FormRowPanel.this.formSettings.isShowLabel());
				}

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.getAttributes().put(FormConstants.FOR, FormRowPanel.this.getComponent().getMarkupId());
					tag.getAttributes().put(FormConstants.TITLE, FormRowPanel.this.getLabelModel().getObject());
				}
			};
		}
		return this.label;
	}

	public IModel<String> getLabelModel() {
		if (this.labelModel == null) {
			this.labelModel = new LoadableDetachableModel<String>() {
				private static final long serialVersionUID = -6695403939068552376L;

				@Override
				protected String load() {
					return FormRowPanel.this.getLabelText();
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
			FormRowPanel.logger.info("no translation for " + FormConstants.PLACEHOLDER);
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
			private static final long serialVersionUID = -6386826366809908431L;

			@Override
			public boolean isVisible() {
				return super.isVisible() && FormRowPanel.this.componentSettings.isRequired();
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

	public FormRowPanel<P, T, C, ElementSettings> inheritId() {
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

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!this.isEnabledInHierarchy()) {
			return;
		}
		response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
	}

	public FormRowPanel<P, T, C, ElementSettings> setLabelModel(IModel<String> labelModel) {
		this.labelModel = labelModel;
		return this;
	}

	public FormRowPanel<P, T, C, ElementSettings> setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public FormRowPanel<P, T, C, ElementSettings> setPropertyType(Class<T> propertyType) {
		this.propertyType = propertyType;
		return this;
	}

	protected Behavior setupDynamicRequiredBehavior() {
		return new AjaxFormComponentUpdatingBehavior(HtmlFormEvent.BLUR) {
			private static final long serialVersionUID = -2678991525434409884L;

			@Override
			protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
				C c = FormRowPanel.this.getComponent();
				c.add(new CssClassNameRemover(FormRowPanel.this.formSettings.validClass));
				c.add(new CssClassNameAppender(FormRowPanel.this.formSettings.invalidClass));
				ajaxRequestTarget.add(c, FormRowPanel.this.getFeedback());
			}

			@Override
			protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
				C c = FormRowPanel.this.getComponent();
				c.add(new CssClassNameRemover(FormRowPanel.this.formSettings.invalidClass));
				c.add(new CssClassNameAppender(FormRowPanel.this.formSettings.validClass));
				ajaxRequestTarget.add(c, FormRowPanel.this.getFeedback());
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
		if ((this.formSettings != null) && this.formSettings.isClientsideRequiredValidation() && (this.componentSettings != null) && this.componentSettings.isRequired()) {
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

	public FormRowPanel<P, T, C, ElementSettings> setValueModel(IModel<T> valueModel) {
		this.valueModel = valueModel;
		return this;
	}

	public boolean takesUpSpace() {
		return true;
	}
}
