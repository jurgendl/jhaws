package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.pocketgrid.PocketGrid;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;
import org.tools.hqlbuilder.webservice.wicket.sass.SassResourceReference;

import com.googlecode.wicket.jquery.core.renderer.ITextRenderer;

public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
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

    public static final SassResourceReference FORM_CSS = new SassResourceReference(WicketCSSRoot.class, "form.css");

    protected StringBuilder css = new StringBuilder();

    protected Set<String> cssClasses = new HashSet<>();

    public FormPanel(String id, FormActions<T> formActions) {
        this(id, formActions, null);
    }

    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id, formActions, formSettings);
    }

    public <F extends Serializable> AutoCompleteTextFieldPanel<F> addAutoCompleteTextField(F propertyPath,
            AutoCompleteTextFieldSettings componentSettings, IModel<List<F>> choices, ITextRenderer<F> renderer) {
        return this.addDefaultRow(
                new AutoCompleteTextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
        return this.addDefaultRow(new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> CKEditorTextAreaPanel<F> addCKEditorTextAreaPanel(F propertyPath, CKEditorTextAreaSettings componentSettings) {
        return this.addDefaultRow(new CKEditorTextAreaPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <F extends Serializable> DatePickerPanel<F> addDatePicker(F propertyPath, FormElementSettings componentSettings,
            Converter<F, Date> dateConverter) {
        return this.addDefaultRow(
                (DatePickerPanel) new DatePickerPanel<>(this.getFormModel(), propertyPath, dateConverter, this.getFormSettings(), componentSettings));
    }

    public <PropertyType extends Serializable, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> RowPanel addDefaultRow(
            RowPanel rowpanel) {
        return this.addRow(rowpanel);
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

    public EmailTextFieldPanel addEmailTextField(String propertyPath, FormElementSettings componentSettings) {
        return this.addDefaultRow(new EmailTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    /**
     * also "form.setMultiPart(true);" and "form.setMaxSize(Bytes.megabytes(1));"
     */
    public <F> FilePickerPanel<F> addFilePicker(F propertyPath, FilePickerSettings componentSettings, FilePickerHook hook) {
        return this.addCustomRow(new FilePickerPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, hook));
    }

    public <F extends Serializable> HiddenFieldPanel<F> addHidden(F propertyPath) {
        return this.addDefaultRow(new HiddenFieldPanel<>(this.getFormModel(), propertyPath));
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

    public LocaleDropDownPanel addLocalesDropDown(Locale propertyPath, FormElementSettings componentSettings, IChoiceRenderer<Locale> renderer,
            IModel<List<Locale>> choices) {
        return this.addCustomRow(
                new LocaleDropDownPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> MultiSelectCheckBoxPanel<F> addMultiSelectCheckBox(Collection<F> propertyPath,
            FormElementSettings componentSettings, IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
        return this.addCustomRow(
                new MultiSelectCheckBoxPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable, C extends Collection<F>> MultiListPanel<F, C> addMultiSelectList(C propertyPath,
            MultiListSettings componentSettings, IOptionRenderer<F> renderer, IModel<List<F>> choices) {
        return this
                .addCustomRow(new MultiListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, renderer, choices));
    }

    public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> NumberTextFieldPanel<N> addNumberTextField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberTextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public PasswordTextFieldPanel addPasswordTextField(String propertyPath, FormElementSettings componentSettings) {
        return this.addDefaultRow(new PasswordTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> PickListPanel<F> addPickList(Collection<F> propertyPath, ListSettings componentSettings, IModel<List<F>> choices,
            IChoiceRenderer<F> renderer) {
        return this
                .addCustomRow(new PickListPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> RadioButtonsPanel<F> addRadioButtons(F propertyPath, FormElementSettings componentSettings,
            IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
        return this.addDefaultRow(
                new RadioButtonsPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
    }

    public <N extends Number & Comparable<N>> RangeFieldPanel<N> addRangeField(N propertyPath, RangeFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new RangeFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> SliderPanel<N> addSliderField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new SliderPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TagItTextFieldPanel addTagItTextFieldPanel(String propertyPath, TagItTextFieldSettings componentSettings, IModel<List<String>> choices) {
        return this.addDefaultRow(new TagItTextFieldPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices));
    }

    public <F extends Serializable> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
        return this.addDefaultRow(new TextAreaPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> TinyMCETextAreaPanel<F> addTinyMCETextArea(F propertyPath, TinyMCETextAreaSettings componentSettings) {
        return this.addDefaultRow(new TinyMCETextAreaPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public TriStateCheckBoxPanel addTriStateCheckBox(Boolean propertyPath, TriStateCheckBoxSettings componentSettings) {
        return this.addDefaultRow(new TriStateCheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    @Override
    protected String getComponentRepeaterCssClass() {
        return "cols" + this.getFormSettings().getColumns() + " " + this.renderColumnsCss(this.getFormSettings().isShowLabel(),
                this.getFormSettings().getColumns(), this.getFormSettings().getLabelWidth());
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
                    sbColumnsCss.append("calc((100% - (")
                            .append(labelWidth)
                            .append(" * ")
                            .append(columnCount)
                            .append(")) / ")
                            .append(columnCount)
                            .append(")");
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
    protected MarkupContainer newEmptyPanel() {
        return new EmptyFormPanel();
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

    @Override
    protected String getButtonCssClass() {
        return JQueryUI.jquibutton;
    }
}
