package org.jhaws.common.web.wicket.bootbox;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.model.IModel;

// https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Creating_and_triggering_events
@SuppressWarnings("serial")
public class BootBoxAjaxCallListener extends AjaxCallListener {
    private BootBoxHelper model;

    public BootBoxAjaxCallListener() {
        model = new BootBoxHelper();
    }

    public BootBoxAjaxCallListener setOkButton(IModel<String> okButton) {
        this.model.setOkButton(okButton);
        return this;
    }

    public BootBoxAjaxCallListener setCancelButton(IModel<String> cancelButton) {
        this.model.setCancelButton(cancelButton);
        return this;
    }

    public BootBoxAjaxCallListener setOkButtonIcon(IModel<String> okButtonIcon) {
        this.model.setOkButtonIcon(okButtonIcon);
        return this;
    }

    public BootBoxAjaxCallListener setCancelButtonIcon(IModel<String> cancelButtonIcon) {
        this.model.setCancelButtonIcon(cancelButtonIcon);
        return this;
    }

    public BootBoxAjaxCallListener setTitle(IModel<String> title) {
        this.model.setTitle(title);
        return this;
    }

    public BootBoxAjaxCallListener setMessage(IModel<String> message) {
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
    public CharSequence getPrecondition(Component component) {
        //
        String jsonResp = model.getScript();
        // {..OPTIONS..,"callback":"||FUNC||"}
        //
        jsonResp = jsonResp.replace(BootBoxHelper.QFUNC, ""//
                // --> D result kan confirmed zijn of niet
                + "function(result){"//
                // --> C als confirmed
                + "if(result){"//
                // *B* 2de maal
                + "attrs.event.originalEvent.target.dataset.doContinueConfirm='true';"//
                + "if(navigator.userAgent.toLowerCase().indexOf('firefox')!=-1||navigator.userAgent.toLowerCase().indexOf('chrome')!=-1){"// --> F
                // werkt niet in IE
                + "attrs.event.target.dispatchEvent(attrs.event.originalEvent);" // heroproepen event
                + "}else{"// -->F<--
                // https://stackoverflow.com/questions/27176983/dispatchevent-not-working-in-ie11
                + "let event;"//
                + "if(typeof(Event)==='function'){"// --> G
                + "event=new Event(attrs.event.originalEvent.type);" //
                + "}else{"// -->G<--
                + "event=document.createEvent('Event');"//
                + "event.initEvent(attrs.event.originalEvent.type,true,true);"//
                + "}" // <-- G
                + "attrs.event.target.dispatchEvent(event);"// heroproepen event
                + "}" // <-- F
                + "}"// <-- C
                + "}"// <-- D
        );
        // {..OPTIONS..,"callback":function(result){if(result){attrs.event.originalEvent.target.dataset.doContinueConfirm='true';attrs.event.target.dispatchEvent(attrs.event.originalEvent);}}}
        //
        jsonResp = "" //
                + "if(attrs.event.originalEvent.target.dataset.doContinueConfirm==='true'){" // --> A
                // *B* reset 2de maal
                + "attrs.event.originalEvent.target.dataset.doContinueConfirm='false';"//
                // <-- *B* 2de maal is al geconfimed, return true voor normale
                // afhandeling
                + "return(true);"//
                + "}"// <-- A
                + "attrs.event.preventDefault();"//
                + "bootbox.confirm(" + jsonResp + ");"//
                // de eerste maal stopt normale afhandeling en wordt een confirm gevraagd
                + "return(false);"//
        ;
        // if(attrs.event.originalEvent.target.dataset.doContinueConfirm==='true'){attrs.event.originalEvent.target.dataset.doContinueConfirm='false';return(true);}attrs.event.preventDefault();bootbox.confirm({..FUNCTIONS..,"callback":function(result){if(result){attrs.event.originalEvent.target.dataset.doContinueConfirm='true';attrs.event.target.dispatchEvent(attrs.event.originalEvent);}}});return(false);
        //
        return jsonResp;
    }
}
