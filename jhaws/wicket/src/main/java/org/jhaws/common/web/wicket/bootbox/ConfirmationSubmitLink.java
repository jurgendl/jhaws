package org.jhaws.common.web.wicket.bootbox;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

// <a class="btn btn-primary" wicket:id="submit">submit</a>
// form.add(new ConfirmationSubmitLink("submit", Model.of("confirmationmessage")).addConfirmation());
// werkt in forms en geneste forms
// werkt op "a href", "input type=submit", "button"
@SuppressWarnings("serial")
public class ConfirmationSubmitLink extends SubmitLink {
    private BootBoxHelper builder = new BootBoxHelper();

    public ConfirmationSubmitLink(String id, Form<?> form) {
        super(id, form);
        builder.setCancelButton(new ResourceModel("confirmation.box.cancel.button.label", (IModel<String>) null));
    }

    public ConfirmationSubmitLink(String id, IModel<?> model, Form<?> form) {
        super(id, model, form);
        builder.setCancelButton(new ResourceModel("confirmation.box.cancel.button.label", (IModel<String>) null));
    }

    public ConfirmationSubmitLink(String id, IModel<?> model) {
        super(id, model);
        builder.setCancelButton(new ResourceModel("confirmation.box.cancel.button.label", (IModel<String>) null));
    }

    public ConfirmationSubmitLink(String id) {
        super(id);
        builder.setCancelButton(new ResourceModel("confirmation.box.cancel.button.label", (IModel<String>) null));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // WICKET 6/7
        // BootboxJavascriptEventConfirmation handler = new
        // BootboxJavascriptEventConfirmation();
        // handler.setMessage(builder.getMessage());
        // handler.setTitle(builder.getTitle());
        // handler.setOkButton(builder.getOkButton());
        // handler.setOkButtonIcon(builder.getOkButtonIcon());
        // handler.setCancelButton(builder.getCancelButton());
        // handler.setCancelButtonIcon(builder.getCancelButtonIcon());
        // add(handler);
    }

    @Override
    protected CharSequence getTriggerJavaScript() {
        CharSequence triggerJavaScript = super.getTriggerJavaScript();
        // WICKET 9
        if (triggerJavaScript != null) {
            triggerJavaScript = "event.preventDefault();bootbox.confirm(" + builder.getScript().replace(BootBoxHelper.QFUNC, "function(result){console.log(result);if(result){" + triggerJavaScript + "}}") + ");";
        }
        return triggerJavaScript;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(BootBox.JS));
        response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
    }

    public IModel<String> getOkButton() {
        return this.builder.getOkButton();
    }

    public ConfirmationSubmitLink setOkButton(IModel<String> okButton) {
        this.builder.setOkButton(okButton);
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.builder.getCancelButton();
    }

    public ConfirmationSubmitLink setCancelButton(IModel<String> cancelButton) {
        this.builder.setCancelButton(cancelButton);
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.builder.getOkButtonIcon();
    }

    public ConfirmationSubmitLink setOkButtonIcon(IModel<String> okButtonIcon) {
        this.builder.setOkButtonIcon(okButtonIcon);
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.builder.getCancelButtonIcon();
    }

    public ConfirmationSubmitLink setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.builder.setCancelButtonIcon(cancelButtonIcon);
        return this;
    }

    public IModel<String> getTitle() {
        return this.builder.getTitle();
    }

    public ConfirmationSubmitLink setTitle(IModel<String> title) {
        this.builder.setTitle(title);
        return this;
    }

    public IModel<String> getMessage() {
        return this.builder.getMessage();
    }

    public ConfirmationSubmitLink setMessage(IModel<String> message) {
        this.builder.setMessage(message);
        return this;
    }
}
