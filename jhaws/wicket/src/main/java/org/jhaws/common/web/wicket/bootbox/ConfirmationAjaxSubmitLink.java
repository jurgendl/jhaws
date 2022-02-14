package org.jhaws.common.web.wicket.bootbox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ConfirmationAjaxSubmitLink extends AjaxSubmitLink {
    private BootBoxHelper builder = new BootBoxHelper();

    protected Form<?> form;

    public ConfirmationAjaxSubmitLink(String id, Form<?> form) {
        super(id, form);
        this.form = form;
    }

    public ConfirmationAjaxSubmitLink(String id) {
        super(id);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target) {
        target.add(form);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        BootBoxAjaxCallListener handler = new BootBoxAjaxCallListener();
        handler.setMessage(builder.getMessage());
        handler.setTitle(builder.getTitle());
        handler.setOkButton(builder.getOkButton());
        handler.setOkButtonIcon(builder.getOkButtonIcon());
        handler.setCancelButton(builder.getCancelButton());
        handler.setCancelButtonIcon(builder.getCancelButtonIcon());

        attributes.getAjaxCallListeners().add(handler);
    }

    // @Override
    // public void renderHead(IHeaderResponse response) {
    // super.renderHead(response);
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS));
    // response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
    // }

    public IModel<String> getOkButton() {
        return this.builder.getOkButton();
    }

    public ConfirmationAjaxSubmitLink setOkButton(IModel<String> okButton) {
        this.builder.setOkButton(okButton);
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.builder.getCancelButton();
    }

    public ConfirmationAjaxSubmitLink setCancelButton(IModel<String> cancelButton) {
        this.builder.setCancelButton(cancelButton);
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.builder.getOkButtonIcon();
    }

    public ConfirmationAjaxSubmitLink setOkButtonIcon(IModel<String> okButtonIcon) {
        this.builder.setOkButtonIcon(okButtonIcon);
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.builder.getCancelButtonIcon();
    }

    public ConfirmationAjaxSubmitLink setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.builder.setCancelButtonIcon(cancelButtonIcon);
        return this;
    }

    public IModel<String> getTitle() {
        return this.builder.getTitle();
    }

    public ConfirmationAjaxSubmitLink setTitle(IModel<String> title) {
        this.builder.setTitle(title);
        return this;
    }

    public IModel<String> getMessage() {
        return this.builder.getMessage();
    }

    public ConfirmationAjaxSubmitLink setMessage(IModel<String> message) {
        this.builder.setMessage(message);
        return this;
    }
}
