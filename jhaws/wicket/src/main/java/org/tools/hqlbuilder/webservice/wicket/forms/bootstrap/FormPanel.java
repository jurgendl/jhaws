package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.NumberFieldSettings;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id, formActions, formSettings);
        bootstrap = true;
    }

    public <PropertyType extends Serializable, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> RowPanel addDefaultRow(
            RowPanel rowpanel) {
        return this.addRow(rowpanel);
    }

    protected MarkupContainer newEmptyPanel() {
        return new EmptyFormPanel();
    }

    public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public CheckBoxPanel addCheckBox(Boolean propertyPath, CheckBoxSettings componentSettings) {
        return this.addDefaultRow(new CheckBoxPanel(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <F extends Serializable> RadioButtonsPanel<F> addRadioButtons(F propertyPath, FormElementSettings componentSettings,
            IModel<List<F>> choices, IChoiceRenderer<F> renderer) {
        return this.addDefaultRow(
                new RadioButtonsPanel<F>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings, choices, renderer));
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

    public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> NumberTextFieldPanel<N> addNumberTextField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return this.addDefaultRow(new NumberTextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }
}
