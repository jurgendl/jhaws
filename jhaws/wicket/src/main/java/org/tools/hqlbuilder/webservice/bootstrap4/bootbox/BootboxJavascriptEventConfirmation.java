package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

// http://bootboxjs.com/
// public void renderHead(IHeaderResponse response) {
// response.render(JavaScriptHeaderItem.forReference(BootBox.JS_LOCALE));
// }
//
// vb
// bootbox.confirm({"locale":"nl","onEscape":"true","message":"message","buttons":{"confirm":{"label":"Ok","className":"btn-default"},"cancel":{"label":"Cancel","className":"btn-primary"}},"callback":function(result){if(result){var
// e=document.getElementById('form5_hf_0'); e.name='effectContainer:content:contentpanel:form:submit1'; e.value='x';var
// f=document.getElementById('form5');var ff=document.getElementById('form7');if (typeof ff.onsubmit === 'function') { if (ff.onsubmit()==false)
// return false; }f.submit();e.value='';e.name='';return false;}}});
@SuppressWarnings("serial")
public class BootboxJavascriptEventConfirmation extends AttributeModifier {
    private BootBoxHelper model;

    public BootboxJavascriptEventConfirmation() {
        super("onclick", Model.of(""));
        model = new BootBoxHelper();
    }

    public BootboxJavascriptEventConfirmation setOkButton(IModel<String> okButton) {
        this.model.setOkButton(okButton);
        return this;
    }

    public BootboxJavascriptEventConfirmation setCancelButton(IModel<String> cancelButton) {
        this.model.setCancelButton(cancelButton);
        return this;
    }

    public BootboxJavascriptEventConfirmation setOkButtonIcon(IModel<String> okButtonIcon) {
        this.model.setOkButtonIcon(okButtonIcon);
        return this;
    }

    public BootboxJavascriptEventConfirmation setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.model.setCancelButtonIcon(cancelButtonIcon);
        return this;
    }

    public BootboxJavascriptEventConfirmation setTitle(IModel<String> title) {
        this.model.setTitle(title);
        return this;
    }

    public BootboxJavascriptEventConfirmation setMessage(IModel<String> message) {
        this.model.setMessage(message);
        return this;
    }

    public IModel<String> getOkButton() {
        return this.model.getOkButton();
    }

    public IModel<String> getCancelButton() {
        return this.model.getCancelButton();
    }

    public IModel<String> getOkButtonIcon() {
        return this.model.getOkButtonIcon();
    }

    public IModel<String> getCancelButtonIcon() {
        return this.model.getCancelButtonIcon();
    }

    public IModel<String> getTitle() {
        return this.model.getTitle();
    }

    public IModel<String> getMessage() {
        return this.model.getMessage();
    }

    @Override
    protected String newValue(final String currentValue, final String replacementValue) {
        String jsonResp = model.getScript();
        // TODO is dit ook nodig? e.stopImmediatePropagation(); // stopPropagation
        // https://medium.com/@jacobwarduk/how-to-correctly-use-preventdefault-stoppropagation-or-return-false-on-events-6c4e3f31aedb
        //
        // ||FUNC|| anders is function niet callable door double quote aan het begin en einde
        // event.preventDefault() is belangrijk voor button en input[type=submit] want anders submit deze toch zonder confirmation
        jsonResp = "event.preventDefault();bootbox.confirm("
                + jsonResp.replace(BootBoxHelper.QFUNC, "function(result){console.log(result);if(result){" + currentValue + "}}") + ");";
        // System.out.println(jsonResp);
        return jsonResp;
    }
}