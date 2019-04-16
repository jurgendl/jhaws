package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;

// <a class="btn btn-primary" wicket:id="submit">submit</a>
// form.add(new ConfirmationSubmitLink("submit", Model.of("confirmationmessage")).addConfirmation());
/** MOET OP EEN A HREF / BUTTON / INPUT TYPE=SUBMIT STAAN */
@SuppressWarnings("serial")
public class ConfirmationSubmitLink extends SubmitLink {
    private IModel<String> okButton;

    private IModel<String> cancelButton;

    private IModel<String> okButtonIcon;

    private IModel<String> cancelButtonIcon;

    private IModel<String> title;

    private IModel<String> message;

    public ConfirmationSubmitLink(String id, Form<?> form) {
        super(id, form);
    }

    public ConfirmationSubmitLink(String id, IModel<?> model, Form<?> form) {
        super(id, model, form);
    }

    public ConfirmationSubmitLink(String id, IModel<?> model) {
        super(id, model);
    }

    public ConfirmationSubmitLink(String id) {
        super(id);
    }

    /** MOET OPGEROEPEN WORDEN OM BIJ TOEVOEGEN VAN DEZE LINK */
    public ConfirmationSubmitLink addConfirmation() {
        BootboxJavascriptEventConfirmation bootboxJavascriptEventConfirmation = new BootboxJavascriptEventConfirmation();
        bootboxJavascriptEventConfirmation.setMessage(message);
        bootboxJavascriptEventConfirmation.setTitle(title);
        bootboxJavascriptEventConfirmation.setOkButton(okButton);
        bootboxJavascriptEventConfirmation.setOkButtonIcon(okButtonIcon);
        bootboxJavascriptEventConfirmation.setCancelButton(cancelButton);
        bootboxJavascriptEventConfirmation.setCancelButtonIcon(cancelButtonIcon);

        add(bootboxJavascriptEventConfirmation);

        return this;
    }

    public IModel<String> getOkButton() {
        return this.okButton;
    }

    public ConfirmationSubmitLink setOkButton(IModel<String> okButton) {
        this.okButton = okButton;
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.cancelButton;
    }

    public ConfirmationSubmitLink setCancelButton(IModel<String> cancelButton) {
        this.cancelButton = cancelButton;
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.okButtonIcon;
    }

    public ConfirmationSubmitLink setOkButtonIcon(IModel<String> okButtonIcon) {
        this.okButtonIcon = okButtonIcon;
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.cancelButtonIcon;
    }

    public ConfirmationSubmitLink setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.cancelButtonIcon = cancelButtonIcon;
        return this;
    }

    public IModel<String> getTitle() {
        return this.title;
    }

    public ConfirmationSubmitLink setTitle(IModel<String> title) {
        this.title = title;
        return this;
    }

    public IModel<String> getMessage() {
        return this.message;
    }

    public ConfirmationSubmitLink setMessage(IModel<String> message) {
        this.message = message;
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(BootBox.JS));
        response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
    }
}
