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
				// heroproepen event
				+ "attrs.event.target.dispatchEvent(attrs.event.originalEvent);"//
				+ "}"// <-- C
				+ "}"// <-- D
		);
		// {..OPTIONS..,"callback":function(result){if(result){attrs.event.originalEvent.target.dataset.doContinueConfirm='true';attrs.event.target.dispatchEvent(attrs.event.originalEvent);}}}
		//
		jsonResp = "" //
				// --> E
				+ "if(attrs.event.originalEvent.target.dataset.doContinueConfirm==='true'){" //
				// *B* reset 2de maal
				+ "attrs.event.originalEvent.target.dataset.doContinueConfirm='false';"//
				// <-- *B* 2de maal is al geconfimed, return true voor normale
				// afhandeling
				+ "return(true);"//
				+ "}"// <-- E
				+ "attrs.event.preventDefault();"//
				+ "bootbox.confirm("//
				+ jsonResp//
				+ ");"// <-- A
				// de eerste maal stopt normale afhandeling en wordt een confirm
				// gevraagd
				+ "return(false);"//
		;
		// if(attrs.event.originalEvent.target.dataset.doContinueConfirm==='true'){attrs.event.originalEvent.target.dataset.doContinueConfirm='false';return(true);}attrs.event.preventDefault();bootbox.confirm({..FUNCTIONS..,"callback":function(result){if(result){attrs.event.originalEvent.target.dataset.doContinueConfirm='true';attrs.event.target.dispatchEvent(attrs.event.originalEvent);}}});return(false);
		//
		return jsonResp;
	}
}
