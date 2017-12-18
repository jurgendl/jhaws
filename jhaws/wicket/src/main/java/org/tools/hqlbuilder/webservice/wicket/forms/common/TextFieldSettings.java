package org.tools.hqlbuilder.webservice.wicket.forms.common;

@SuppressWarnings("serial")
public class TextFieldSettings extends AbstractFormElementSettings<TextFieldSettings> {
	protected Boolean selectAllOnFocus;

	protected Boolean replaceAllOnDrop;

	public TextFieldSettings() {
		super();
	}

	public Boolean getSelectAllOnFocus() {
		return this.selectAllOnFocus;
	}

	public TextFieldSettings setSelectAllOnFocus(Boolean selectAllOnFocus) {
		this.selectAllOnFocus = selectAllOnFocus;
		return this;
	}

	public Boolean getReplaceAllOnDrop() {
		return this.replaceAllOnDrop;
	}

	public TextFieldSettings setReplaceAllOnDrop(Boolean replaceAllOnDrop) {
		this.replaceAllOnDrop = replaceAllOnDrop;
		return this;
	}
}
