package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DatePickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.MultiSelectSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.NumberFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TagItTextFieldSettings;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
	public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
		super(id, formActions, formSettings);
		bootstrap = true;
	}

	protected String getFeedbackCss(org.apache.wicket.feedback.FeedbackMessage message) {
		return "alert alert-" + getFeedbackCssInternal(message) + " mb-1";
	}

	protected org.apache.wicket.markup.html.panel.FeedbackPanel newFeedbackPanel(String string) {
		org.tools.hqlbuilder.webservice.wicket.forms.bootstrap.FeedbackPanel feedbackPanel = new org.tools.hqlbuilder.webservice.wicket.forms.bootstrap.FeedbackPanel(
				string) {
			protected String getCSSClass(org.apache.wicket.feedback.FeedbackMessage message) {
				String feedbackCss = getFeedbackCss(message);
				return feedbackCss == null ? super.getCSSClass(message) : feedbackCss;
			}
		};
		feedbackPanel.setEscapeModelStrings(false);
		return feedbackPanel;
	}

	protected String getFeedbackCssInternal(org.apache.wicket.feedback.FeedbackMessage message) {
		if (FeedbackMessage.DEBUG == message.getLevel()) {
			return "debug";
		}
		if (FeedbackMessage.INFO == message.getLevel()) {
			return "info";
		}
		if (FeedbackMessage.SUCCESS == message.getLevel()) {
			return "success";
		}
		if (FeedbackMessage.WARNING == message.getLevel()) {
			return "warning";
		}
		if (FeedbackMessage.ERROR == message.getLevel()) {
			return "danger";
		}
		if (FeedbackMessage.FATAL == message.getLevel()) {
			return "error";
		}
		return "primary";
	}

	public <PropertyType, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> RowPanel addDefaultRow(
			RowPanel rowpanel) {
		return this.addRow(rowpanel);
	}

	protected MarkupContainer newEmptyPanel() {
		return new EmptyFormPanel();
	}

	public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath,
			FormElementSettings componentSettings) {
		return this.addDefaultRow(
				new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
		return this.addDefaultRow(
				new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> RadioButtonsPanel<F> addRadioButtons(F propertyPath,
			FormElementSettings componentSettings, IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
		return this.addDefaultRow(new RadioButtonsPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, choices, renderer));
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<List<F>> choices) {
		return this.addDefaultRow(new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<List<F>>[] choices, IModel<String>[] groupLabels) {
		return this.addDefaultRow(new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices, groupLabels));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<List<F>> choices) {
		return this.addDefaultRow(new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<List<F>>[] choices, IModel<String>[] groupLabels) {
		return this.addDefaultRow(new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices, groupLabels));
	}

	public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
			List<I> propertyPath, //
			MultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IModel<List<I>> choices//
	) {
		return this.addDefaultRow(new MultiSelectPanel<I>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
			List<I> propertyPath, //
			MultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IModel<List<I>>[] choices, //
			IModel<String>[] groupLabels//
	) {
		return this.addDefaultRow(new MultiSelectPanel<I>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices, groupLabels));
	}

	public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath,
			NumberFieldSettings<N> componentSettings) {
		return this.addDefaultRow(
				new NumberFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <N extends Number & Comparable<N>> NumberTextFieldPanel<N> addNumberTextField(N propertyPath,
			NumberFieldSettings<N> componentSettings) {
		return this.addDefaultRow(new NumberTextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings));
	}

	public DatePickerPanel<Date> addDatePicker(Date propertyPath, DatePickerSettings componentSettings) {
		return this.addDatePicker(propertyPath, componentSettings, (Converter<Date, Date>) null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <F extends Serializable> DatePickerPanel<F> addDatePicker(F propertyPath,
			DatePickerSettings componentSettings, Converter<F, Date> dateConverter) {
		return this.addDefaultRow((DatePickerPanel) new DatePickerPanel<>(this.getFormModel(), propertyPath,
				dateConverter, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> HiddenFieldPanel<F> addHidden(F propertyPath) {
		return this.addDefaultRow(new HiddenFieldPanel<F>(this.getFormModel(), propertyPath));
	}

	public TagItTextFieldPanel addTagItTextFieldPanel(String propertyPath, TagItTextFieldSettings componentSettings,
			IModel<List<String>> choices) {
		return this.addDefaultRow(new TagItTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, choices));
	}
}
