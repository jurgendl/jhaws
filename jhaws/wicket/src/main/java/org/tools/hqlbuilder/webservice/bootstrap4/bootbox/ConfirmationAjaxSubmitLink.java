package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ConfirmationAjaxSubmitLink extends AjaxSubmitLink {
    private IModel<String> okButton;

    private IModel<String> cancelButton;

    private IModel<String> okButtonIcon;

    private IModel<String> cancelButtonIcon;

    private IModel<String> title;

    private IModel<String> message;

    public ConfirmationAjaxSubmitLink(String id, Form<?> form) {
        super(id, form);
    }

    public ConfirmationAjaxSubmitLink(String id) {
        super(id);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        target.add(form);
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

    public ConfirmationAjaxSubmitLink setOkButton(IModel<String> okButton) {
        this.okButton = okButton;
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.cancelButton;
    }

    public ConfirmationAjaxSubmitLink setCancelButton(IModel<String> cancelButton) {
        this.cancelButton = cancelButton;
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.okButtonIcon;
    }

    public ConfirmationAjaxSubmitLink setOkButtonIcon(IModel<String> okButtonIcon) {
        this.okButtonIcon = okButtonIcon;
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.cancelButtonIcon;
    }

    public ConfirmationAjaxSubmitLink setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.cancelButtonIcon = cancelButtonIcon;
        return this;
    }

    public IModel<String> getTitle() {
        return this.title;
    }

    public ConfirmationAjaxSubmitLink setTitle(IModel<String> title) {
        this.title = title;
        return this;
    }

    public IModel<String> getMessage() {
        return this.message;
    }

    public ConfirmationAjaxSubmitLink setMessage(IModel<String> message) {
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
