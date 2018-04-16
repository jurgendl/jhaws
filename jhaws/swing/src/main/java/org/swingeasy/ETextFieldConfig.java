package org.swingeasy;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Jurgen
 */
public class ETextFieldConfig extends EComponentConfig<ETextFieldConfig> {
	protected int columns = 0;

	protected boolean enabled = true;

	protected boolean selectAllOnFocus = false;

	protected String text = null;

	protected Function<String, String> filterInput = null;

	protected Predicate<String> textValidator = null;

	public ETextFieldConfig() {
		super();
	}

	public ETextFieldConfig(boolean enabled) {
		this.enabled = enabled;
	}

	public ETextFieldConfig(boolean enabled, int columns) {
		this.enabled = enabled;
		this.columns = columns;
	}

	public ETextFieldConfig(boolean enabled, String text) {
		this.enabled = enabled;
		this.text = text;
	}

	public ETextFieldConfig(int columns) {
		this.columns = columns;
	}

	public ETextFieldConfig(String text) {
		this.text = text;
	}

	public int getColumns() {
		return columns;
	}

	public String getText() {
		return text;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isSelectAllOnFocus() {
		return selectAllOnFocus;
	}

	public ETextFieldConfig setColumns(int columns) {
		lockCheck();
		this.columns = columns;
		return this;
	}

	public ETextFieldConfig setEnabled(boolean enabled) {
		lockCheck();
		this.enabled = enabled;
		return this;
	}

	public ETextFieldConfig setSelectAllOnFocus(boolean selectAllOnFocus) {
		lockCheck();
		this.selectAllOnFocus = selectAllOnFocus;
		return this;
	}

	public ETextFieldConfig setText(String text) {
		lockCheck();
		this.text = text;
		return this;
	}

	public Function<String, String> getFilterInput() {
		return this.filterInput;
	}

	public ETextFieldConfig setFilterInput(Function<String, String> filterInput) {
		lockCheck();
		this.filterInput = filterInput;
		return this;
	}

	public Predicate<String> getTextValidator() {
		return this.textValidator;
	}

	public ETextFieldConfig setTextValidator(Predicate<String> textValidator) {
		lockCheck();
		this.textValidator = textValidator;
		return this;
	}

	public ETextFieldConfig removeRegexFromInput(String regexFilter) {
		lockCheck();
		this.filterInput = s -> s.replaceAll(regexFilter, "");
		return this;
	}

	public ETextFieldConfig regexValidateText(String regexFilter) {
		lockCheck();
		this.textValidator = s -> s == null || s.equals("") || s.matches(regexFilter);
		return this;
	}

	public ETextFieldConfig filterInput(Function<String, String> _filterInput) {
		lockCheck();
		this.filterInput = _filterInput;
		return this;
	}

	public ETextFieldConfig validateText(Predicate<String> _textValidator) {
		lockCheck();
		this.textValidator = _textValidator;
		return this;
	}
}
