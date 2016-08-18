package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.pocketgrid.PocketGrid;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.sass.SassResourceReference;

import com.googlecode.wicket.jquery.core.renderer.ITextRenderer;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://wicket.apache.org/guide/guide/forms2.html#forms2_1
 */
public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
	// FIXME
	// static {
	// Lambda.registerFinalClassArgumentCreator(URL.class, seed -> {
	// try {
	// return new URL("http://www." + seed);
	// } catch (MalformedURLException ex) {
	// throw new RuntimeException(ex);
	// }
	// });
	// }

	private static final long serialVersionUID = -6387604067134639316L;

	protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

	public static final SassResourceReference FORM_CSS = new SassResourceReference(WicketCSSRoot.class, "form.css");

	protected RepeatingView rowRepeater;

	protected RepeatingView componentRepeater;

	protected FormSettings formSettings;

	protected FormActions<T> formActions;

	protected Form<T> form;

	protected int count = 0;

	protected StringBuilder css = new StringBuilder();

	protected Set<String> cssClasses = new HashSet<String>();

	public FormPanel(String id, FormActions<T> formActions) {
		this(id, formActions, null);
	}

	public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
		super(id);
		WebHelper.show(this);
		this.setFormActions(formActions != null ? formActions : new DefaultFormActions<T>() {
			private static final long serialVersionUID = -6135914559717102175L;

			@Override
			public Class<T> forObjectClass() {
				try {
					return WebHelper.<T>getImplementation(FormPanel.this, FormPanel.class);
				} catch (IllegalArgumentException ex) {
					throw new IllegalArgumentException("implement FormActions#forObjectClass or set generic type of FormActions<T>");
				}
			}

			@Override
			public String toString() {
				return "DefaultFormActions";
			}
		});
		this.setFormSettings(formSettings == null ? new FormSettings() : formSettings);
	}

	public <F extends Serializable> AutoCompleteTextFieldPanel<F> addAutoCompleteTextField(F propertyPath, AutoCompleteTextFieldSettings componentSettings, IModel<List<F>> choices,
			ITextRenderer<F> renderer) {
		return this.addDefaultRow(new AutoCompleteTextFieldPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
	}

	public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
		return this.addDefaultRow(new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> CKEditorTextAreaPanel<F> addCKEditorTextAreaPanel(F propertyPath, CKEditorTextAreaSettings componentSettings) {
		return this.addDefaultRow(new CKEditorTextAreaPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public ColorPickerPanel addColorPicker(String propertyPath, ColorPickerSettings componentSettings) {
		return this.addDefaultRow(new ColorPickerPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public JQueryUIColorPickerPanel addColorPicker(String propertyPath, JQueryUIColorPickerSettings componentSettings) {
		return this.addDefaultRow(new JQueryUIColorPickerPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType, ElementSettings>> RowPanel addCustomRow(
			RowPanel rowpanel) {
		return this.addRow(rowpanel);
	}

	public DatePickerPanel<Date> addDatePicker(Date propertyPath, FormElementSettings componentSettings) {
		return this.addDatePicker(propertyPath, componentSettings, (Converter<Date, Date>) null);
	}

	@SuppressWarnings({ "unchecked" })
	public <F extends Serializable> DatePickerPanel<F> addDatePicker(F propertyPath, FormElementSettings componentSettings, Converter<F, Date> dateConverter) {
		return this.addDefaultRow(new DatePickerPanel<F>(this.getFormModel(), propertyPath, dateConverter, this.getFormSettings(), componentSettings));
	}

	public <PropertyType extends Serializable, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> RowPanel addDefaultRow(
			RowPanel rowpanel) {
		return this.addRow(rowpanel);
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings, IOptionRenderer<F> renderer, IModel<List<F>> choices) {
		return this.addDefaultRow(new DropDownPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings, IOptionRenderer<F> renderer, IModel<List<F>>[] choices,
			IModel<String>[] groupLabels) {
		return this.addDefaultRow(new DropDownPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices, groupLabels));
	}

	public EmailTextFieldPanel addEmailTextField(String propertyPath, FormElementSettings componentSettings) {
		return this.addDefaultRow(new EmailTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	/**
	 * also "form.setMultiPart(true);" and "form.setMaxSize(Bytes.megabytes(1));"
	 */
	public <F> FilePickerPanel<F> addFilePicker(F propertyPath, FilePickerSettings componentSettings, FilePickerHook hook) {
		return this.addCustomRow(new FilePickerPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, hook));
	}

	public <F extends Serializable> HiddenFieldPanel<F> addHidden(F propertyPath) {
		return this.addDefaultRow(new HiddenFieldPanel<F>(this.getFormModel(), propertyPath));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings, IOptionRenderer<F> renderer, IModel<List<F>> choices) {
		return this.addDefaultRow(new ListPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings, IOptionRenderer<F> renderer, IModel<List<F>>[] choices,
			IModel<String>[] groupLabels) {
		return this.addDefaultRow(new ListPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices, groupLabels));
	}

	public LocaleDropDownPanel addLocalesDropDown(Locale propertyPath, FormElementSettings componentSettings, IChoiceRenderer<Locale> renderer, IModel<List<Locale>> choices) {
		return this.addCustomRow(new LocaleDropDownPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
	}

	public <F extends Serializable> MultiSelectCheckBoxPanel<F> addMultiSelectCheckBox(Collection<F> propertyPath, FormElementSettings componentSettings, IModel<List<F>> choices,
			IChoiceRenderer<F> renderer) {
		return this.addCustomRow(new MultiSelectCheckBoxPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
	}

	public <F extends Serializable, C extends Collection<F>> MultiListPanel<F, C> addMultiSelectList(C propertyPath, MultiListSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<List<F>> choices) {
		return this.addCustomRow(new MultiListPanel<F, C>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
	}

	public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
		return this.addDefaultRow(new NumberFieldPanel<N>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <N extends Number & Comparable<N>> NumberTextFieldPanel<N> addNumberTextField(N propertyPath, NumberFieldSettings<N> componentSettings) {
		return this.addDefaultRow(new NumberTextFieldPanel<N>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public PasswordTextFieldPanel addPasswordTextField(String propertyPath, FormElementSettings componentSettings) {
		return this.addDefaultRow(new PasswordTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> PickListPanel<F> addPickList(Collection<F> propertyPath, ListSettings componentSettings, IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
		return this.addCustomRow(new PickListPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
	}

	public <F extends Serializable> RadioButtonsPanel<F> addRadioButtons(F propertyPath, FormElementSettings componentSettings, IModel<List<F>> choices,
			IChoiceRenderer<F> renderer) {
		return this.addDefaultRow(new RadioButtonsPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
	}

	public <N extends Number & Comparable<N>> RangeFieldPanel<N> addRangeField(N propertyPath, RangeFieldSettings<N> componentSettings) {
		return this.addDefaultRow(new RangeFieldPanel<N>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType, ElementSettings>> RowPanel addRow(
			RowPanel rowpanel) {
		this.getForm();

		WebMarkupContainer elementContainer = WebHelper.hide(new WebMarkupContainer(this.getComponentRepeater().newChildId()));
		this.getComponentRepeater().add(elementContainer);

		// rowpanel is already created
		elementContainer.add(rowpanel);

		// components are created in "rowpanel.addComponents"
		rowpanel.addComponents();

		// some post creation stuff
		rowpanel.afterAddComponents();

		if (rowpanel.takesUpSpace()) {
			this.count++;
			if (this.count == this.formSettings.getColumns()) {
				this.count = 0; // reset count
				// so that a new one is created when needed
				this.componentRepeater = null;
			}
		}

		return rowpanel;
	}

	public <N extends Number & Comparable<N>> SliderPanel<N> addSliderField(N propertyPath, NumberFieldSettings<N> componentSettings) {
		return this.addDefaultRow(new SliderPanel<N>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public TagItTextFieldPanel addTagItTextFieldPanel(String propertyPath, TagItTextFieldSettings componentSettings, IModel<List<String>> choices) {
		return this.addDefaultRow(new TagItTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices));
	}

	public <F extends Serializable> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
		return this.addDefaultRow(new TextAreaPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
		return this.addDefaultRow(new TextFieldPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> TinyMCETextAreaPanel<F> addTinyMCETextArea(F propertyPath, TinyMCETextAreaSettings componentSettings) {
		return this.addDefaultRow(new TinyMCETextAreaPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public TriStateCheckBoxPanel addTriStateCheckBox(Boolean propertyPath, TriStateCheckBoxSettings componentSettings) {
		return this.addDefaultRow(new TriStateCheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	protected RepeatingView getComponentRepeater() {
		// only create a new row when needed
		if (this.componentRepeater == null) {
			WebMarkupContainer rowContainer = new WebMarkupContainer(this.getRowRepeater().newChildId());

			rowContainer.add(new CssClassNameAppender("cols" + this.getFormSettings().getColumns() + " "
					+ this.renderColumnsCss(this.getFormSettings().isShowLabel(), this.getFormSettings().getColumns(), this.getFormSettings().getLabelWidth())));
			this.getRowRepeater().add(rowContainer);

			RepeatingView repeater = new RepeatingView(FormConstants.FORM_ELEMENT_REPEATER);
			this.componentRepeater = WebHelper.hide(repeater);
			rowContainer.add(this.componentRepeater);
		}
		return this.componentRepeater;
	}

	public Form<T> getForm() {
		if (this.form == null) {
			this.getFormActions(); // check if exists asap
			IModel<T> formModel = new LoadableDetachableModel<T>() {
				private static final long serialVersionUID = -5489467484161698560L;

				@Override
				protected T load() {
					return FormPanel.this.getFormActions().loadObject();
				}

				@Override
				public String toString() {
					return "Form:IModel<T>";
				}
			};
			if (this.getFormSettings().isStateless()) {
				this.form = new StatelessForm<T>(FormConstants.FORM, formModel) {
					private static final long serialVersionUID = -5855525240326128645L;

					@Override
					protected String getMethod() {
						return FormPanel.this.formSettings.getMethod() != null ? FormPanel.this.formSettings.getMethod().toString() : super.getMethod();
					}

					@Override
					protected void onSubmit() {
						FormPanel.this.onBeforeSubmit();
						Serializable submitReturnValue = FormPanel.this.getFormActions().submitModel(FormPanel.this.getFormModel());
						FormPanel.this.onAfterSubmit(submitReturnValue);
					}

					@Override
					public String toString() {
						return "StatelessForm";
					}
				};
			} else {
				this.form = new Form<T>(FormConstants.FORM, formModel) {
					private static final long serialVersionUID = -5899425422548211723L;

					@Override
					protected String getMethod() {
						return FormPanel.this.formSettings.getMethod() != null ? FormPanel.this.formSettings.getMethod().toString() : super.getMethod();
					}

					@Override
					protected void onSubmit() {
						FormPanel.this.onBeforeSubmit();
						Serializable submitReturnValue = FormPanel.this.getFormActions().submitModel(FormPanel.this.getFormModel());
						FormPanel.this.onAfterSubmit(submitReturnValue);
					}

					@Override
					public String toString() {
						return "Form";
					}
				};
			}

			if (Boolean.FALSE.equals(this.formSettings.getAutocomplete())) {
				this.form.add(new AttributeModifier("autocomplete", "off"));
			} else if (Boolean.TRUE.equals(this.formSettings.getAutocomplete())) {
				this.form.add(new AttributeModifier("autocomplete", "on"));
			}

			if (this.getFormSettings().isInheritId()) {
				this.form.setMarkupId(this.form.getId());
			}

			WebHelper.show(this.form);
			this.add(this.form);

			WebMarkupContainer formHeader = new WebMarkupContainer(FormConstants.FORM_HEADER) {
				private static final long serialVersionUID = 6548216685529936996L;

				@Override
				public boolean isVisible() {
					for (int i = 0; i < this.size(); i++) {
						if (!this.get(i).isVisible()) {
							return false;
						}
					}
					return super.isVisible();
				}

				@Override
				public String toString() {
					return "WebMarkupContainer:" + FormConstants.FORM_HEADER;
				}
			};
			this.form.add(formHeader);

			WebMarkupContainer formBody = new WebMarkupContainer(FormConstants.FORM_BODY);
			this.form.add(formBody);

			WebMarkupContainer formFieldSet = new WebMarkupContainer(FormConstants.FORM_FIELDSET);
			formBody.add(formFieldSet);
			String fieldSetLegend = this.getFormSettings().getFieldSetLegend();
			Label formFieldSetLegend = new Label(FormConstants.FORM_FIELDSET_LEGEND, fieldSetLegend == null ? Model.of("") : new ResourceModel(fieldSetLegend));
			formFieldSet.add(formFieldSetLegend.setVisible(fieldSetLegend != null));
			formFieldSet.add(this.getRowRepeater());

			ResourceModel submitModel = new ResourceModel(FormConstants.SUBMIT_LABEL);
			ResourceModel resetModel = new ResourceModel(FormConstants.RESET_LABEL);
			ResourceModel cancelModel = new ResourceModel(FormConstants.CANCEL_LABEL);

			Component submit;
			if (this.getFormSettings().isAjax()) {
				submit = new AjaxSubmitLink(FormConstants.FORM_SUBMIT, this.form) {
					private static final long serialVersionUID = 1046494045754727027L;

					@SuppressWarnings("unchecked")
					@Override
					protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
						FormPanel.this.getFormActions().afterSubmit(target, (Form<T>) f, FormPanel.this.getFormModel());
					}

					@Override
					public String toString() {
						return "AjaxSubmitLink:" + FormConstants.FORM_SUBMIT;
					}
				};
				submit.setDefaultModel(submitModel);
			} else {
				submit = new Button(FormConstants.FORM_SUBMIT, submitModel);
			}
			submit.setVisible(this.formSettings.isShowSubmit());
			submit.add(new CssClassNameAppender(JQueryUI.jquibutton));

			Button reset = new Button(FormConstants.FORM_RESET, resetModel);
			reset.add(new CssClassNameAppender(JQueryUI.jquibutton));
			reset.setVisible(this.formSettings.isShowReset());

			// https://cwiki.apache.org/confluence/display/WICKET/Multiple+submit +buttons
			Component cancel;
			if (this.getFormSettings().isAjax()) {
				cancel = new AjaxSubmitLink(FormConstants.FORM_CANCEL, this.form) {
					private static final long serialVersionUID = -8816675271842238444L;

					@SuppressWarnings("unchecked")
					@Override
					protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
						FormPanel.this.getFormActions().afterCancel(target, (Form<T>) f, FormPanel.this.getFormModel());
					}

					@Override
					public String toString() {
						return "AjaxSubmitLink:" + FormConstants.FORM_CANCEL;
					}
				};
				cancel.setDefaultModel(cancelModel);
				((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
			} else {
				cancel = new Button(FormConstants.FORM_CANCEL, cancelModel);
				((Button) cancel).setDefaultFormProcessing(false);
			}
			cancel.add(new CssClassNameAppender(JQueryUI.jquibutton));
			cancel.setVisible(this.getFormSettings().isCancelable());

			if (this.getFormSettings().isInheritId()) {
				submit.setMarkupId(this.getId() + "." + FormConstants.FORM_SUBMIT);
				reset.setMarkupId(this.getId() + "." + FormConstants.FORM_RESET);
				cancel.setMarkupId(this.getId() + "." + FormConstants.FORM_CANCEL);
			}

			WebMarkupContainer formActionsContainer = new WebMarkupContainer(FormConstants.FORM_ACTIONS);
			this.form.add(formActionsContainer);
			formActionsContainer.add(submit);
			formActionsContainer.add(reset);
			formActionsContainer.add(cancel);
			formActionsContainer.setVisible(this.formSettings.isShowReset() || this.formSettings.isShowSubmit() || this.getFormSettings().isCancelable());

			formActionsContainer.add(new RepeatingView(FormConstants.FORM_ACTIONS_ADDTIONAL).setRenderBodyOnly(true).setVisible(false));

			WebMarkupContainer formFooter = new WebMarkupContainer(FormConstants.FORM_FOOTER) {
				private static final long serialVersionUID = -8111670292045284274L;

				@Override
				public boolean isVisible() {
					for (int i = 0; i < this.size(); i++) {
						if (!this.get(i).isVisible()) {
							return false;
						}
					}
					return super.isVisible();
				}

				@Override
				public String toString() {
					return "WebMarkupContainer:" + FormConstants.FORM_FOOTER;
				}
			};
			this.form.add(formFooter);

			switch (FormPanel.this.getFormSettings().getShowMessages()) {
				case bottom:
					formHeader.add(new WebMarkupContainer("allMessagesTop").setVisible(false));
					formFooter.add(new FeedbackPanel("allMessagesBottom").setEscapeModelStrings(false));
					break;
				case top:
					formHeader.add(new FeedbackPanel("allMessagesTop").setEscapeModelStrings(false));
					formFooter.add(new WebMarkupContainer("allMessagesBottom").setVisible(false));
					break;
				default:
					formHeader.add(new WebMarkupContainer("allMessagesTop").setVisible(false));
					formFooter.add(new WebMarkupContainer("allMessagesBottom").setVisible(false));
					break;
			}
			this.form.add(new RepeatingView(FormConstants.FORM_ADDITIONAL).setRenderBodyOnly(true).setVisible(false));
		}
		return this.form;
	}

	protected FormActions<T> getFormActions() {
		if (this.formActions == null) {
			throw new RuntimeException("FormActions required");
		}
		return this.formActions;
	}

	/**
	 * behind normal form buttons, default invisible, default render body only
	 */
	public RepeatingView getFormActionsAdditionalContainer() {
		return (RepeatingView) this.getForm().get(FormConstants.FORM_ACTIONS).get(FormConstants.FORM_ACTIONS_ADDTIONAL);
	}

	/**
	 * button container
	 */
	public WebMarkupContainer getFormActionsContainer() {
		return (WebMarkupContainer) this.getForm().get(FormConstants.FORM_ACTIONS);
	}

	/**
	 * behind normal form, default invisible, default render body only
	 */
	public RepeatingView getFormAdditionalContainer() {
		return (RepeatingView) this.getForm().get(FormConstants.FORM_ADDITIONAL);
	}

	public IModel<T> getFormModel() {
		return this.getForm().getModel();
	}

	public FormSettings getFormSettings() {
		if (this.formSettings == null) {
			throw new RuntimeException("FormSettings required");
		}
		return this.formSettings;
	}

	/**
	 * single lazy creation
	 */
	protected RepeatingView getRowRepeater() {
		if (this.rowRepeater == null) {
			RepeatingView repeater = new RepeatingView(FormConstants.FORM_ROW_REPEATER);
			this.rowRepeater = WebHelper.show(repeater);
		}
		return this.rowRepeater;
	}

	public void nextRow() {
		this.getForm();
		while (this.count != 0) {
			WebMarkupContainer elementContainer = new WebMarkupContainer(this.getComponentRepeater().newChildId());
			WebHelper.hide(elementContainer);
			this.getComponentRepeater().add(elementContainer);
			elementContainer.add(WebHelper.hide(new EmptyFormPanel()));
			this.count++;
			if (this.count == this.formSettings.getColumns()) {
				this.count = 0; // reset count
				// so that a new one is created when needed
				this.componentRepeater = null;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected void onAfterSubmit(final Serializable submitReturnValue) {
		this.getRowRepeater().visitChildren(FormRowPanel.class, new IVisitor<FormRowPanel, Void>() {
			@Override
			public void component(FormRowPanel object, IVisit<Void> visit) {
				if (object instanceof FormSubmitInterceptor) {
					FormSubmitInterceptor.class.cast(object).onAfterSubmit(submitReturnValue);
				}
			}

			@Override
			public String toString() {
				return "onAfterSubmit:IVisitor<FormRowPanel, Void>";
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected void onBeforeSubmit() {
		this.getRowRepeater().visitChildren(FormRowPanel.class, new IVisitor<FormRowPanel, Void>() {
			@Override
			public void component(FormRowPanel object, IVisit<Void> visit) {
				if (object instanceof FormSubmitInterceptor) {
					FormSubmitInterceptor.class.cast(object).onBeforeSubmit();
				}
			}

			@Override
			public String toString() {
				return "onBeforeSubmit:IVisitor<FormRowPanel, Void>";
			}
		});
	}

	public T proxy() {
		return WebHelper.proxy(this.getFormActions().forObjectClass());
	}

	protected String renderColumnsCss(boolean showLabel, int columnCount, String labelWidth) {
		if (!this.formSettings.isRenderPocketGrid()) {
			return "";
		}

		String cssClass = "pocketgrid_" + this.getId() + '_' + columnCount
				+ (showLabel ? '_' + new String(org.apache.commons.codec.binary.Hex.encodeHex(labelWidth.getBytes())) : "");
		if (!this.cssClasses.contains(cssClass)) {
			this.cssClasses.add(cssClass);
			StringBuilder sbColumnsCss = new StringBuilder();
			if (showLabel) {
				sbColumnsCss.append(".").append(cssClass).append(" .block:nth-child(2n+1){width:").append(labelWidth).append(";}\n");
				sbColumnsCss.append(".").append(cssClass).append(" .block:nth-child(2n+2){width:");
				if (columnCount == 1) {
					sbColumnsCss.append("calc(100% - ").append(labelWidth).append(")");
				} else {
					sbColumnsCss.append("calc((100% - (").append(labelWidth).append(" * ").append(columnCount).append(")) / ").append(columnCount).append(")");
				}
				sbColumnsCss.append(";}\n");
			} else {
				sbColumnsCss.append(".").append(cssClass).append(" .block:nth-child(n){width:");
				if (columnCount == 1) {
					sbColumnsCss.append("100%");
				} else {
					sbColumnsCss.append("calc(100% / ").append(columnCount).append(")");
				}
				sbColumnsCss.append(";}\n");
			}
			this.css.append(sbColumnsCss.toString());
		}
		return cssClass;
	}

	protected void renderColumnsCss(IHeaderResponse response) {
		response.render(CssHeaderItem.forCSS(this.css.toString(), "js_pocketgrid_" + this.getId()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!this.isEnabledInHierarchy()) {
			return;
		}
		response.render(CssHeaderItem.forReference(PocketGrid.POCKET_GRID));
		response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
		response.render(JavaScriptHeaderItem.forReference(JQueryUI.JQUERY_UI_FACTORY_JS));
		response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
		if (this.formSettings.isRenderPocketGrid()) {
			this.renderColumnsCss(response);
		}
		response.render(CssHeaderItem.forReference(FormPanel.FORM_CSS));
	}

	public void setFormActions(FormActions<T> formActions) {
		if (formActions == null) {
			throw new RuntimeException("FormActions required");
		}
		this.formActions = formActions;
	}

	public void setFormSettings(FormSettings formSettings) {
		if (formSettings == null) {
			throw new RuntimeException("FormSettings required");
		}
		this.formSettings = formSettings;
	}
}
