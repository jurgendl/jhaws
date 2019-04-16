package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

// <a class="btn btn-primary" wicket:id="submit">submit</a>
// form.add(new ConfirmationSubmitLink("submit", Model.of("confirmationmessage")).addConfirmation());
/** MOET OP EEN A HREF / BUTTON / INPUT TYPE=SUBMIT STAAN */
@SuppressWarnings("serial")
public class ConfirmationLink<T> extends Link<T> {
    private IModel<String> okButton;

    private IModel<String> cancelButton;

    private IModel<String> okButtonIcon;

    private IModel<String> cancelButtonIcon;

    private IModel<String> title;

    private IModel<String> message;

    public ConfirmationLink(String id) {
        super(id);
    }

    public ConfirmationLink(String id, IModel<T> model) {
        super(id, model);
    }

    /** MOET OPGEROEPEN WORDEN OM BIJ TOEVOEGEN VAN DEZE LINK */
    public ConfirmationLink<T> addConfirmation() {
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

    public ConfirmationLink<T> setOkButton(IModel<String> okButton) {
        this.okButton = okButton;
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.cancelButton;
    }

    public ConfirmationLink<T> setCancelButton(IModel<String> cancelButton) {
        this.cancelButton = cancelButton;
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.okButtonIcon;
    }

    public ConfirmationLink<T> setOkButtonIcon(IModel<String> okButtonIcon) {
        this.okButtonIcon = okButtonIcon;
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.cancelButtonIcon;
    }

    public ConfirmationLink<T> setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.cancelButtonIcon = cancelButtonIcon;
        return this;
    }

    public IModel<String> getTitle() {
        return this.title;
    }

    public ConfirmationLink<T> setTitle(IModel<String> title) {
        this.title = title;
        return this;
    }

    public IModel<String> getMessage() {
        return this.message;
    }

    public ConfirmationLink<T> setMessage(IModel<String> message) {
        this.message = message;
        return this;
    }

    @Override
    public void onClick() {
        //
    }

    // @Override
    // public void renderHead(IHeaderResponse response) {
    // super.renderHead(response);
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS));
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
    // }
}
