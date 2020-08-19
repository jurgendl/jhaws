package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.bootstrap.BootstrapFencedFeedbackPanel;
import org.jhaws.common.web.wicket.converter.Converter;
import org.jhaws.common.web.wicket.forms.common.AbstractFormElementSettings;
import org.jhaws.common.web.wicket.forms.common.BootstrapMultiSelectSettings;
import org.jhaws.common.web.wicket.forms.common.BootstrapSelectSettings;
import org.jhaws.common.web.wicket.forms.common.CheckBoxSettings;
import org.jhaws.common.web.wicket.forms.common.DatePickerSettings;
import org.jhaws.common.web.wicket.forms.common.DropDownSettings;
import org.jhaws.common.web.wicket.forms.common.FormActions;
import org.jhaws.common.web.wicket.forms.common.FormElementSettings;
import org.jhaws.common.web.wicket.forms.common.FormPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.ListSettings;
import org.jhaws.common.web.wicket.forms.common.MultiSelectSettings;
import org.jhaws.common.web.wicket.forms.common.NumberFieldSettings;
import org.jhaws.common.web.wicket.forms.common.RatingFieldSettings;
import org.jhaws.common.web.wicket.forms.common.TagItTextFieldSettings;
import org.jhaws.common.web.wicket.forms.common.TextAreaSettings;
import org.jhaws.common.web.wicket.forms.common.TextFieldSettings;
import org.jhaws.common.web.wicket.forms.common.TriStateCheckBoxSettings;
import org.jhaws.common.web.wicket.forms.common.TypeAheadTextFieldAltSettings;
import org.jhaws.common.web.wicket.forms.common.TypeAheadTextFieldSettings;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
	public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
		super(id, formActions, formSettings);
	}

	@Override
	protected org.apache.wicket.markup.html.panel.FeedbackPanel newFeedbackPanel(String id) {
		BootstrapFencedFeedbackPanel feedbackPanel = new BootstrapFencedFeedbackPanel(id, this);
		return feedbackPanel;
	}

	public <PropertyType, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> //
	RowPanel addDefaultRow(RowPanel rowpanel) {
		return this.addRow(rowpanel);
	}

	@Override
	protected MarkupContainer newEmptyPanel() {
		return new EmptyFormPanel();
	}

	public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath,
			TextFieldSettings componentSettings) {
		return this.addDefaultRow(
				new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public PasswordFieldPanel<String> addPasswordField(String propertyPath, TextFieldSettings componentSettings) {
		return this.addDefaultRow(
				new PasswordFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public TextFieldPanel<String> addEmailField(String propertyPath, TextFieldSettings componentSettings) {
		return this.addDefaultRow(new TextFieldPanel<String>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings) {
			@Override
			protected void onFormComponentTag(ComponentTag tag) {
				super.onFormComponentTag(tag);
				WebHelper.tag(tag, "type", "email");
			}
		});
	}

	public TextFieldPanel<String> addUrlField(String propertyPath, TextFieldSettings componentSettings) {
		return this.addDefaultRow(new TextFieldPanel<String>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings) {
			@Override
			protected void onFormComponentTag(ComponentTag tag) {
				super.onFormComponentTag(tag);
				WebHelper.tag(tag, "type", "url");
			}
		});
	}

	public <F extends Serializable> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
		return this.addDefaultRow(
				new TextAreaPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
		return this.addDefaultRow(
				new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public TogglePanel addToggle(Boolean propertyPath, CheckBoxSettings componentSettings) {
		return this.addDefaultRow(
				new TogglePanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public <F extends Serializable> RadioButtonsAltPanel<F> addRadioButtonsAlt(F propertyPath,
			FormElementSettings componentSettings, IModel<? extends List<? extends F>> choices,
			IChoiceRenderer<F> renderer) {
		return this.addDefaultRow(new RadioButtonsAltPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, choices, renderer));
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<? extends List<? extends F>> choices) {
		return this.addDefaultRow(new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<? extends List<? extends F>>[] choices, IModel<String>[] groupLabels) {
		return this.addDefaultRow(new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices, groupLabels));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<? extends List<? extends F>> choices) {
		return this.addDefaultRow(new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings,
			IOptionRenderer<F> renderer, IModel<? extends List<? extends F>>[] choices, IModel<String>[] groupLabels) {
		return this.addDefaultRow(new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices, groupLabels));
	}

	public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
			List<I> propertyPath, //
			MultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IModel<? extends List<? extends I>> choices//
	) {
		return this.addDefaultRow(new MultiSelectPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, choices));
	}

	public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
			List<I> propertyPath, //
			MultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IModel<? extends List<? extends I>>[] choices, //
			IModel<String>[] groupLabels//
	) {
		return this.addDefaultRow(new MultiSelectPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
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

	public RatingFieldPanel addRatingField(Integer propertyPath, RatingFieldSettings componentSettings) {
		return this.addDefaultRow(
				new RatingFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public RatingFieldPanelAlt addRatingFieldAlt(Integer propertyPath, RatingFieldSettings componentSettings) {
		return this.addDefaultRow(
				new RatingFieldPanelAlt(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
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
		return this.addDefaultRow(new HiddenFieldPanel<>(this.getFormModel(), propertyPath));
	}

	public TagItTextFieldPanel addTagItField(String propertyPath, TagItTextFieldSettings componentSettings,
			IModel<? extends List<String>> choices) {
		return this.addDefaultRow(new TagItTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, choices));
	}

	public TriStateCheckBoxPanel addTriStateCheckBox(Boolean propertyPath, TriStateCheckBoxSettings componentSettings) {
		return this.addDefaultRow(new TriStateCheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings));
	}

	public TinyMCEPanel addTinyMCEPanel(String propertyPath, TinyMCESettings componentSettings) {
		return this.addDefaultRow(
				new TinyMCEPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
	}

	public TypeAheadTextFieldPanel addTypeAheadTextField(String propertyPath,
			TypeAheadTextFieldSettings componentSettings, IModel<? extends List<String>> choices) {
		return this.addDefaultRow(new TypeAheadTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, choices));
	}

	public TypeAheadTextFieldAltPanel addTypeAheadTextFieldAlt(String propertyPath,
			TypeAheadTextFieldAltSettings componentSettings, IModel<? extends List<Map<String, String>>> choices) {
		return this.addDefaultRow(new TypeAheadTextFieldAltPanel(this.getFormModel(), propertyPath,
				this.getFormSettings(), componentSettings, choices));
	}

	public static final CssResourceReference FLAGS_CSS = new CssResourceReference(FormPanel.class,
			"country/flags/css/flag-icon.css");
//	public static final JavaScriptResourceReference COUNTRY_JS = new JavaScriptResourceReference(FormPanel.class,
//			"country/countries.js");
	// org.jhaws.wicket.test.CountryExtra
	public static final JavaScriptResourceReference COUNTRY_EXTRA_JS = new JavaScriptResourceReference(FormPanel.class,
			"country/countries-extra.js");

	public TypeAheadTextFieldAltPanel addCountryTypeAhead(String propertyPath,
			TypeAheadTextFieldAltSettings componentSettings) {
		if (componentSettings.getProperties() == null || (componentSettings.getProperties().size() == 1
				&& "value".equals(componentSettings.getProperties().get(0)))) {
			componentSettings.setProperties(Arrays.asList("name", "adjectivals", "iso"));
		}
		if ("value".equals(componentSettings.getTemplate())) {
			componentSettings.setTemplate(
					"\"<span class='flag-icon flag-icon-\"+ item.iso.toLowerCase()+ \" flag-icon'></span>&nbsp;{{name}}:&nbsp;{{capital}}&nbsp;{{adjectivals}}&nbsp;[{{iso}}]&nbsp;(+{{phone}})\"");
		}
		if (componentSettings.getLocal() == null) {
			componentSettings.setLocal("countrydata");
		}
		return this.addDefaultRow(new TypeAheadTextFieldAltPanel(this.getFormModel(), propertyPath,
				this.getFormSettings(), componentSettings, null) {
			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);
				response.render(CssHeaderItem.forReference(FLAGS_CSS));
				response.render(JavaScriptHeaderItem.forReference(COUNTRY_EXTRA_JS));
			}
		});
	}

	public <I extends Serializable> BootstrapSelectPanel<I> addBootstrapSelect(//
			I propertyPath, //
			BootstrapSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IOptionRenderer<I> contentRenderer, //
			IModel<? extends List<? extends I>> choices //
	) {
		return this.addDefaultRow(new BootstrapSelectPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, contentRenderer, choices));
	}

	public <I extends Serializable> BootstrapSelectPanel<I> addBootstrapSelect(//
			I propertyPath, //
			BootstrapSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IOptionRenderer<I> contentRenderer, //
			IModel<? extends List<? extends I>>[] choices, //
			IModel<String>[] groupLabels//
	) {
		return this.addDefaultRow(new BootstrapSelectPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(),
				componentSettings, renderer, contentRenderer, choices, groupLabels));
	}

	public <I extends Serializable> BootstrapMultiSelectPanel<I> addBootstrapMultiSelect(//
			List<I> propertyPath, //
			BootstrapMultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IOptionRenderer<I> contentRenderer, //
			IModel<? extends List<? extends I>> choices //
	) {
		return this.addDefaultRow(new BootstrapMultiSelectPanel<>(this.getFormModel(), propertyPath,
				this.getFormSettings(), componentSettings, renderer, contentRenderer, choices));
	}

	public <I extends Serializable> BootstrapMultiSelectPanel<I> addBootstrapMultiSelect(//
			List<I> propertyPath, //
			BootstrapMultiSelectSettings componentSettings, //
			IOptionRenderer<I> renderer, //
			IOptionRenderer<I> contentRenderer, //
			IModel<? extends List<? extends I>>[] choices, //
			IModel<String>[] groupLabels//
	) {
		return this.addDefaultRow(new BootstrapMultiSelectPanel<>(this.getFormModel(), propertyPath,
				this.getFormSettings(), componentSettings, renderer, contentRenderer, choices, groupLabels));
	}
}
