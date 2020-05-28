package org.jhaws.common.web.wicket.bootbox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class ConfirmationAjaxLink<T> extends AjaxLink<T> {
    private BootBoxHelper builder = new BootBoxHelper();

    public ConfirmationAjaxLink(String id) {
        super(id);
    }

    public ConfirmationAjaxLink(String id, IModel<T> model) {
        super(id, model);
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

    @Override
    public void onClick(AjaxRequestTarget target) {
        //
    }

    public IModel<String> getOkButton() {
        return this.builder.getOkButton();
    }

    public ConfirmationAjaxLink<T> setOkButton(IModel<String> okButton) {
        this.builder.setOkButton(okButton);
        return this;
    }

    public IModel<String> getCancelButton() {
        return this.builder.getCancelButton();
    }

    public ConfirmationAjaxLink<T> setCancelButton(IModel<String> cancelButton) {
        this.builder.setCancelButton(cancelButton);
        return this;
    }

    public IModel<String> getOkButtonIcon() {
        return this.builder.getOkButtonIcon();
    }

    public ConfirmationAjaxLink<T> setOkButtonIcon(IModel<String> okButtonIcon) {
        this.builder.setOkButtonIcon(okButtonIcon);
        return this;
    }

    public IModel<String> getCancelButtonIcon() {
        return this.builder.getCancelButtonIcon();
    }

    public ConfirmationAjaxLink<T> setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.builder.setCancelButtonIcon(cancelButtonIcon);
        return this;
    }

    public IModel<String> getTitle() {
        return this.builder.getTitle();
    }

    public ConfirmationAjaxLink<T> setTitle(IModel<String> title) {
        this.builder.setTitle(title);
        return this;
    }

    public IModel<String> getMessage() {
        return this.builder.getMessage();
    }

    public ConfirmationAjaxLink<T> setMessage(IModel<String> message) {
        this.builder.setMessage(message);
        return this;
    }
}
