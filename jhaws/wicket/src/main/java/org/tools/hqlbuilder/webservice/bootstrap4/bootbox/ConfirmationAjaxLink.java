package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ConfirmationAjaxLink<T> extends AjaxLink<T> {
    private IModel<String> okButton;

    private IModel<String> cancelButton;

    private IModel<String> okButtonIcon;

    private IModel<String> cancelButtonIcon;

    private IModel<String> title;

    private IModel<String> message;

    public ConfirmationAjaxLink(String id) {
        super(id);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        BootBoxAjaxCallListener bootBoxAjaxCallListener = new BootBoxAjaxCallListener();
        bootBoxAjaxCallListener.setMessage(message);
        bootBoxAjaxCallListener.setTitle(title);
        bootBoxAjaxCallListener.setOkButton(okButton);
        bootBoxAjaxCallListener.setOkButtonIcon(okButtonIcon);
        bootBoxAjaxCallListener.setCancelButton(cancelButton);
        bootBoxAjaxCallListener.setCancelButtonIcon(cancelButtonIcon);

        attributes.getAjaxCallListeners().add(bootBoxAjaxCallListener);
    }

    public IModel<String> getOkButton() {
        return this.okButton;
    }

    public ConfirmationAjaxLink<T> setOkButton(IModel<String> okButton) {
        this.okButton = okButton;
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.cancelButton;
    }

    public ConfirmationAjaxLink<T> setCancelButton(IModel<String> cancelButton) {
        this.cancelButton = cancelButton;
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.okButtonIcon;
    }

    public ConfirmationAjaxLink<T> setOkButtonIcon(IModel<String> okButtonIcon) {
        this.okButtonIcon = okButtonIcon;
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.cancelButtonIcon;
    }

    public ConfirmationAjaxLink<T> setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.cancelButtonIcon = cancelButtonIcon;
        return this;
    }

    public IModel<String> getTitle() {
        return this.title;
    }

    public ConfirmationAjaxLink<T> setTitle(IModel<String> title) {
        this.title = title;
        return this;
    }

    public IModel<String> getMessage() {
        return this.message;
    }

    public ConfirmationAjaxLink<T> setMessage(IModel<String> message) {
        this.message = message;
        return this;
    }

    // @Override
    // public void renderHead(IHeaderResponse response) {
    // super.renderHead(response);
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS));
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
    // }

    @Override
    public void onClick(AjaxRequestTarget target) {
        //
    }
}
