package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.BootstrapFencedFeedbackPanel;
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
import org.tools.hqlbuilder.webservice.wicket.forms.common.RatingFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TagItTextFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TriStateCheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TypeAheadTextFieldSettings;

// TODO validation https://developer.mozilla.org/en-US/docs/Learn/HTML/Forms/Form_validation
@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
    public static final CssResourceReference FORM_CSS = new CssResourceReference(FormPanel.class, "form.css");

    public static final JavaScriptResourceReference FORM_JS = new JavaScriptResourceReference(FormPanel.class, "form.js");

    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id, formActions, formSettings);
        bootstrap = true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        response.render(CssHeaderItem.forReference(FormPanel.FORM_CSS));
        response.render(JavaScriptHeaderItem.forReference(FormPanel.FORM_JS));
    }

    protected org.apache.wicket.markup.html.panel.FeedbackPanel newFeedbackPanel(String id) {
        BootstrapFencedFeedbackPanel feedbackPanel = new BootstrapFencedFeedbackPanel(id, this);
        return feedbackPanel;
    }

    public <PropertyType, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> //
    RowPanel addDefaultRow(RowPanel rowpanel) {
        return this.addRow(rowpanel);
    }

    protected MarkupContainer newEmptyPanel() {
        return new EmptyFormPanel();
    }

    public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, TextFieldSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TextFieldPanel<String> addPasswordField(String propertyPath, TextFieldSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<String>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings) {
            @Override
            protected void onFormComponentTag(ComponentTag tag) {
                super.onFormComponentTag(tag);
                WebHelper.tag(tag, "type", "password");
            }
        });
    }

    public TextFieldPanel<String> addEmailField(String propertyPath, TextFieldSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<String>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings) {
            @Override
            protected void onFormComponentTag(ComponentTag tag) {
                super.onFormComponentTag(tag);
                WebHelper.tag(tag, "type", "email");
            }
        });
    }

    public TextFieldPanel<String> addUrlField(String propertyPath, TextFieldSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<String>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings) {
            @Override
            protected void onFormComponentTag(ComponentTag tag) {
                super.onFormComponentTag(tag);
                WebHelper.tag(tag, "type", "url");
            }
        });
    }

    public <F extends Serializable> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
        return this.addDefaultRow(new TextAreaPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
        return this.addDefaultRow(new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TogglePanel addToggle(Boolean propertyPath, CheckBoxSettings componentSettings) {
        return this.addDefaultRow(new TogglePanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> RadioButtonsAltPanel<F> addRadioButtonsAlt(F propertyPath, FormElementSettings componentSettings,
            IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
        return this.addDefaultRow(
                new RadioButtonsAltPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings, IOptionRenderer<F> renderer,
            IModel<List<F>> choices) {
        return this
                .addDefaultRow(new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
    }

    public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, DropDownSettings componentSettings, IOptionRenderer<F> renderer,
            IModel<List<F>>[] choices, IModel<String>[] groupLabels) {
        return this.addDefaultRow(
                new DropDownPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices, groupLabels));
    }

    public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings, IOptionRenderer<F> renderer,
            IModel<List<F>> choices) {
        return this.addDefaultRow(new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
    }

    public <F extends Serializable> ListPanel<F> addList(F propertyPath, ListSettings componentSettings, IOptionRenderer<F> renderer,
            IModel<List<F>>[] choices, IModel<String>[] groupLabels) {
        return this.addDefaultRow(
                new ListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices, groupLabels));
    }

    public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
            List<I> propertyPath, //
            MultiSelectSettings componentSettings, //
            IOptionRenderer<I> renderer, //
            IModel<List<I>> choices//
    ) {
        return this.addDefaultRow(
                new MultiSelectPanel<I>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
    }

    public <I extends Serializable> MultiSelectPanel<I> addMultiSelect(//
            List<I> propertyPath, //
            MultiSelectSettings componentSettings, //
            IOptionRenderer<I> renderer, //
            IModel<List<I>>[] choices, //
            IModel<String>[] groupLabels//
    ) {
        return this.addDefaultRow(new MultiSelectPanel<I>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer,
                choices, groupLabels));
    }

    public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> NumberTextFieldPanel<N> addNumberTextField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberTextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public RatingFieldPanel addRatingField(Integer propertyPath, RatingFieldSettings componentSettings) {
        return this.addDefaultRow(new RatingFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public RatingFieldPanel2 addRatingField2(Integer propertyPath, RatingFieldSettings componentSettings) {
        return this.addDefaultRow(new RatingFieldPanel2(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public DatePickerPanel<Date> addDatePicker(Date propertyPath, DatePickerSettings componentSettings) {
        return this.addDatePicker(propertyPath, componentSettings, (Converter<Date, Date>) null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <F extends Serializable> DatePickerPanel<F> addDatePicker(F propertyPath, DatePickerSettings componentSettings,
            Converter<F, Date> dateConverter) {
        return this.addDefaultRow(
                (DatePickerPanel) new DatePickerPanel<>(this.getFormModel(), propertyPath, dateConverter, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> HiddenFieldPanel<F> addHidden(F propertyPath) {
        return this.addDefaultRow(new HiddenFieldPanel<F>(this.getFormModel(), propertyPath));
    }

    public TagItTextFieldPanel addTagItField(String propertyPath, TagItTextFieldSettings componentSettings, IModel<List<String>> choices) {
        return this.addDefaultRow(new TagItTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices));
    }

    public TriStateCheckBoxPanel addTriStateCheckBox(Boolean propertyPath, TriStateCheckBoxSettings componentSettings) {
        return this.addDefaultRow(new TriStateCheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TinyMCEPanel addTinyMCEPanel(String propertyPath, TinyMCESettings componentSettings) {
        return this.addDefaultRow(new TinyMCEPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TypeAheadTextFieldPanel addTypeAheadTextField(String propertyPath, TypeAheadTextFieldSettings componentSettings) {
        return this.addDefaultRow(new TypeAheadTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public void feedbackError(Serializable message) {
        getForm().error(message);
    }

    public void feedbackInfo(Serializable message) {
        getForm().info(message);
    }

    public void feedbackFatal(Serializable message) {
        getForm().fatal(message);
    }

    public void feedbackWarn(Serializable message) {
        getForm().warn(message);
    }

    public void feedbackSuccess(Serializable message) {
        getForm().success(message);
    }
}
