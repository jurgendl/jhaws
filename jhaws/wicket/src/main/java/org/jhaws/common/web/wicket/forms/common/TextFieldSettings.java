package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class TextFieldSettings extends AbstractFormElementSettings<TextFieldSettings> {
	protected Boolean selectAllOnFocus;

	protected Boolean replaceAllOnDrop;

	protected Boolean autocomplete = false;

	protected String pattern;

	protected Integer minlength;

	protected Integer maxlength;

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
