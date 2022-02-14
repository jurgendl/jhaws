package org.jhaws.common.web.wicket.forms.bootstrap;

import java.util.List;
import java.util.Map;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.IValueMap;

@SuppressWarnings("serial")
public class BootstrapCheckBoxes<T> extends AbstractSingleSelectChoice<T> implements IRequestListener {
    protected String buttonCssClass = "btn btn-sm btn-light";

    protected boolean asButtons = false;

    public BootstrapCheckBoxes(final String id) {
        super(id);
        asButtons = true;
    }

    public BootstrapCheckBoxes(final String id, final List<? extends T> choices) {
        super(id, choices);
        asButtons = true;
    }

    public BootstrapCheckBoxes(final String id, final List<? extends T> choices, final IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        asButtons = true;
    }

    public BootstrapCheckBoxes(final String id, IModel<T> model, final List<? extends T> choices) {
        super(id, model, choices);
        asButtons = true;
    }

    public BootstrapCheckBoxes(final String id, IModel<T> model, final List<? extends T> choices, final IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        asButtons = true;
    }

    public BootstrapCheckBoxes(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
        asButtons = true;
    }

    public BootstrapCheckBoxes(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
        asButtons = true;
    }

    public BootstrapCheckBoxes(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        asButtons = true;
    }

    public BootstrapCheckBoxes(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        asButtons = true;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        // since this component cannot be attached to input tag the name
        // variable is illegal
        tag.remove("name");
    }

    @Override
    public void onRequest() {
        convertInput();
        updateModel();
        onSelectionChanged(getDefaultModelObject());
    }

    protected void onSelectionChanged(Object newSelection) {
        //
    }

    protected boolean wantOnSelectionChangedNotifications() {
        return false;
    }

    @Override
    protected boolean getStatelessHint() {
        if (wantOnSelectionChangedNotifications()) {
            return false;
        }
        return super.getStatelessHint();
    }

    @Override
    public final void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        // Iterate through choices
        final List<? extends T> choices = getChoices();

        // Buffer to hold generated body
        final AppendingStringBuffer buffer = new AppendingStringBuffer((choices.size() + 1) * 70);

        // The selected value
        final String selected = getValue();

        buffer.append("<span class='btn-group btn-group-toggle'");
        if (asButtons) buffer.append(" data-toggle='buttons'");
        buffer.append(">");

        // Loop through choices
        for (int index = 0; index < choices.size(); index++) {
            // Get next choice
            final T choice = choices.get(index);
            appendOptionHtml(buffer, choice, index, selected);
        }

        buffer.append("</span");

        // Replace body
        replaceComponentTagBody(markupStream, openTag, buffer);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void appendOptionHtml(final AppendingStringBuffer buffer, final T choice, int index, final String selected) {
        Object displayValue = getChoiceRenderer().getDisplayValue(choice);
        Class<?> objectClass = (displayValue == null ? null : displayValue.getClass());
        String label = "";
        if (objectClass != null && objectClass != String.class) {
            final IConverter converter = getConverter(objectClass);
            label = converter.convertToString(displayValue, getLocale());
        } else if (displayValue != null) {
            label = displayValue.toString();
        }

        // If there is a display value for the choice, then we know that the
        // choice is automatic in some way. If label is /null/ then we know
        // that the choice is a manually created radio tag at some random
        // location in the page markup!
        // if (label != null) {
        String id = getChoiceRenderer().getIdValue(choice, index);
        final String idAttr = getMarkupId() + "-" + id;
        boolean enabled = isEnabledInHierarchy() && !isDisabled(choice, index, selected);

        // Add label for radio button
        String display = label;
        if (localizeDisplayValues()) {
            display = getLocalizer().getString(label, this, label);
        }

        CharSequence escaped = display;
        if (getEscapeModelStrings()) {
            escaped = Strings.escapeMarkup(display);
        }

        // Allows user to add attributes to the <label..> tag
        IValueMap labelAttrs = getAdditionalAttributesForLabel(index, choice);
        StringBuilder extraLabelAttributes = new StringBuilder();
        if (labelAttrs != null) {
            for (Map.Entry<String, Object> attr : labelAttrs.entrySet()) {
                extraLabelAttributes.append(' ').append(Strings.escapeMarkup(attr.getKey())).append("=\"").append(Strings.escapeMarkup(attr.getValue().toString())).append('"');
            }
        }

        buffer.append("<label class='" + getButtonCssClass());
        if (asButtons && isSelected(choice, index, selected)) buffer.append(" active");
        buffer.append(extraLabelAttributes);
        buffer.append("'>");

        // Add radio tag
        buffer.append("<input"//
                + " type='checkbox'"//
                + " name='" + getInputName() + "'"// FIXME unique
                + " id='" + Strings.escapeMarkup(idAttr) + "'"// FIXME unique
                + " value='" + Strings.escapeMarkup(id) + "'"//
                + " autocomplete='off'"//
                + ((isSelected(choice, index, selected) ? " checked=\"checked\"" : ""))//
                + (enabled ? "" : " disabled=\"disabled\"")//
        );

        // Should a roundtrip be made (have onSelectionChanged called)
        // when the option is clicked?
        if (wantOnSelectionChangedNotifications()) {
            CharSequence url = urlForListener(new PageParameters());

            Form<?> form = findParent(Form.class);
            if (form != null) {
                buffer.append(" onclick=\"").append(form.getJsForListenerUrl(url)).append(";\"");
            } else {
                // NOTE: do not encode the url as that would give invalid
                // JavaScript
                buffer.append(" onclick=\"window.location.href='").append(url).append((url.toString().indexOf('?') > -1 ? '&' : '?') + getInputName()).append('=').append(Strings.escapeMarkup(id)).append("';\"");
            }
        }

        // Allows user to add attributes to the <input..> tag
        {
            IValueMap attrs = getAdditionalAttributes(index, choice);
            if (attrs != null) {
                for (Map.Entry<String, Object> attr : attrs.entrySet()) {
                    buffer.append(' ').append(Strings.escapeMarkup(attr.getKey())).append("=\"").append(Strings.escapeMarkup(attr.getValue().toString())).append('"');
                }
            }
        }

        // WICKET UPGRADE
        // if (getApplication().getDebugSettings().isOutputComponentPath()) {
        // CharSequence path = getPageRelativePath();
        // path = Strings.replaceAll(path, "_", "__");
        // path = Strings.replaceAll(path, ":", "_");
        // buffer.append(" wicketpath=\"").append(path).append("_input_").append(index).append('"');
        // }

        buffer.append(">");

        buffer.append("&nbsp;" + escaped + "&nbsp;</label>");
    }

    protected IValueMap getAdditionalAttributesForLabel(int index, T choice) {
        return null;
    }

    protected IValueMap getAdditionalAttributes(final int index, final T choice) {
        return null;
    }

    public String getButtonCssClass() {
        return this.buttonCssClass;
    }

    public void setButtonCssClass(String buttonCssClass) {
        this.buttonCssClass = buttonCssClass;
    }

    public BootstrapCheckBoxes<T> buttonCssClass(String buttonCssClass) {
        this.buttonCssClass = buttonCssClass;
        return this;
    }

    public boolean isAsButtons() {
        return this.asButtons;
    }

    public void setAsButtons(boolean asButtons) {
        this.asButtons = asButtons;
    }

    public BootstrapCheckBoxes<T> asButtons(boolean asButtons) {
        this.asButtons = asButtons;
        return this;
    }
}
