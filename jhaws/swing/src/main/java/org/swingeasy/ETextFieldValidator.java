package org.swingeasy;

import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ETextFieldValidator extends DocumentFilter {
	protected Function<String, String> filterInput = null;

	protected Predicate<String> textValidator = null;

	public ETextFieldValidator removeRegexFromInput(String regexFilter) {
		this.filterInput = s -> s.replaceAll(regexFilter, "");
		return this;
	}

	public ETextFieldValidator regexValidateText(String regexFilter) {
		this.textValidator = s -> s == null || s.equals("") || s.matches(regexFilter);
		return this;
	}

	public ETextFieldValidator filterInput(Function<String, String> _filterInput) {
		this.filterInput = _filterInput;
		return this;
	}

	public ETextFieldValidator validateText(Predicate<String> _textValidator) {
		this.textValidator = _textValidator;
		return this;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		String replaced = filterInput == null ? string : filterInput.apply(string);
		if (textValidator != null) {
			if (!textValidator.test(new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength())).insert(offset, replaced).toString())) {
				return;
			}
		}
		fb.insertString(offset, replaced, attr);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
		String replaced = filterInput == null ? string : filterInput.apply(string);
		if (textValidator != null) {
			if (!textValidator
					.test(new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength())).delete(offset, offset + length).insert(offset, replaced).toString())) {
				return;
			}
		}
		fb.replace(offset, length, replaced, attrs);
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		if (textValidator != null) {
			if (!textValidator.test(new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength())).delete(offset, offset + length).toString())) {
				return;
			}
		}
		fb.remove(offset, length);
	}

	public Function<String, String> getFilterInput() {
		return this.filterInput;
	}

	public ETextFieldValidator setFilterInput(Function<String, String> filterInput) {
		this.filterInput = filterInput;
		return this;
	}

	public Predicate<String> getTextValidator() {
		return this.textValidator;
	}

	public ETextFieldValidator setTextValidator(Predicate<String> textValidator) {
		this.textValidator = textValidator;
		return this;
	}
}