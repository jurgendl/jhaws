package org.tools.hqlbuilder.webservice.wicket.forms.common;

@SuppressWarnings("serial")
public class TextFieldSettings extends AbstractFormElementSettings<TextFieldSettings> {
	protected Boolean selectAllOnFocus;

	protected Boolean replaceAllOnDrop;

	protected Boolean autocomplete = false;

	protected String pattern;

	protected Integer minlength;

	protected Integer maxlength;

	protected String placeholder;

	public String getPlaceholder() {
		return this.placeholder;
	}

	public TextFieldSettings setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		return this;
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

	public Boolean getAutocomplete() {
		return this.autocomplete;
	}

	public TextFieldSettings setAutocomplete(Boolean autocomplete) {
		this.autocomplete = autocomplete;
		return this;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Integer getMinlength() {
		return this.minlength;
	}

	public void setMinlength(Integer minlength) {
		this.minlength = minlength;
	}

	public Integer getMaxlength() {
		return this.maxlength;
	}

	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}
}
