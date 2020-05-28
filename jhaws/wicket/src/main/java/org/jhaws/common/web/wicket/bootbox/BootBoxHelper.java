package org.jhaws.common.web.wicket.bootbox;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;

import com.fasterxml.jackson.databind.ObjectMapper;

//bootbox.confirm({
//    opt1:opt1,
//    opt2:opt2,
//    callback: function (result) {
//        do stuff on confirmation
//    }
//});
@SuppressWarnings("serial")
public class BootBoxHelper implements Serializable {
	public static final String FUNC = "||FUNC||";

	public static final String QFUNC = '"' + FUNC + '"';

	private static final ObjectMapper jsonMapperObj = new ObjectMapper();

	private IModel<String> okButton;

	private IModel<String> cancelButton;

	private IModel<String> okButtonIcon;

	private IModel<String> cancelButtonIcon;

	private IModel<String> title;

	private IModel<String> message;

	public String getScript() {
		// method: knoppen
		// alert: ok
		// confirm: cancel, confirm
		// prompt: cancel, confirm
		Map<String, Object> options = new LinkedHashMap<>();

		// boolean backdrop (true) // grijze achtergrond

		// className // extra wrapper css class
		options.put("className", "bootboxmodalclass");

		// size // sm lg [xl bootstrap 4]
		options.put("size", "sm");

		options.put("centerVertical", "true");

		// boolean swapButtonOrder (false)

		// boolean scrollable (false) [bootstrap 4]

		options.put("locale", "en");
		// FIXME luister naar session (maakt het eigenlijk uit?)

		options.put("onEscape", "true");

		options.put("animate", "false");

		if (message != null) {
			options.put("message", message.getObject());
		} else {
			options.put("message", "Are you sure?");
		}
		if (title != null) {
			options.put("title", title.getObject());
		}
		{
			Map<String, Object> btns = new LinkedHashMap<>();
			{
				Map<String, Object> confirm = new LinkedHashMap<>();
				StringBuilder sb = new StringBuilder();
				if (okButtonIcon != null) {
					sb.append("<i class='").append(okButtonIcon.getObject()).append("'></i> ");
				}
				if (okButton != null) {
					sb.append(okButton.getObject());
				} else {
					sb.append("Ok");
				}
				confirm.put("label", sb.toString());
				confirm.put("className", "btn-primary btn-sm");
				btns.put("confirm", confirm);
			}
			{
				Map<String, Object> cancel = new LinkedHashMap<>();
				StringBuilder sb = new StringBuilder();
				if (cancelButtonIcon != null) {
					sb.append("<i class='").append(cancelButtonIcon.getObject()).append("'></i> ");
				}
				if (cancelButton != null) {
					sb.append(cancelButton.getObject());
				} else {
					sb.append("Cancel");
				}
				cancel.put("label", sb.toString());
				cancel.put("className", "btn-secondary btn-sm");
				btns.put("cancel", cancel);
			}
			options.put("buttons", btns);
		}
		// ||FUNC|| anders is function niet callable door double quote aan het
		// begin en einde
		options.put("callback", FUNC);

		String jsonResp;
		try {
			jsonResp = jsonMapperObj.writeValueAsString(options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return jsonResp;
	}

	public IModel<String> getOkButton() {
		return this.okButton;
	}

	public BootBoxHelper setOkButton(IModel<String> okButton) {
		this.okButton = okButton;
		return this;
	}

	public IModel<String> getCancelButton() {
		return this.cancelButton;
	}

	public BootBoxHelper setCancelButton(IModel<String> cancelButton) {
		this.cancelButton = cancelButton;
		return this;
	}

	public IModel<String> getOkButtonIcon() {
		return this.okButtonIcon;
	}

	public BootBoxHelper setOkButtonIcon(IModel<String> okButtonIcon) {
		this.okButtonIcon = okButtonIcon;
		return this;
	}

	public IModel<String> getCancelButtonIcon() {
		return this.cancelButtonIcon;
	}

	public BootBoxHelper setCancelButtonIcon(IModel<String> cancelButtonIcon) {
		this.cancelButtonIcon = cancelButtonIcon;
		return this;
	}

	public IModel<String> getTitle() {
		return this.title;
	}

	public BootBoxHelper setTitle(IModel<String> title) {
		this.title = title;
		return this;
	}

	public IModel<String> getMessage() {
		return this.message;
	}

	public BootBoxHelper setMessage(IModel<String> message) {
		this.message = message;
		return this;
	}
}
