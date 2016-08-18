package org.tools.hqlbuilder.webservice.wicket.forms;

public class TagItTextFieldSettings extends AbstractFormElementSettings<TagItTextFieldSettings> {
    private static final long serialVersionUID = 1098120531336315493L;

    protected boolean caseSensitive = false;

    protected char fieldDelimiter = ';';

    protected int minLength = 2;

    protected long delay = 200;

    protected boolean singleField = true;

    protected boolean allowSpaces = true;

    public TagItTextFieldSettings() {
        super();
    }

    public TagItTextFieldSettings(boolean required) {
        super(required);
    }

    public long getDelay() {
        return this.delay;
    }

    public char getFieldDelimiter() {
        return this.fieldDelimiter;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public boolean isAllowSpaces() {
        return this.allowSpaces;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public boolean isSingleField() {
        return this.singleField;
    }

    public TagItTextFieldSettings setAllowSpaces(boolean allowSpaces) {
        this.allowSpaces = allowSpaces;
        return this;
    }

    public TagItTextFieldSettings setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public TagItTextFieldSettings setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public TagItTextFieldSettings setFieldDelimiter(char fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
        return this;
    }

    public TagItTextFieldSettings setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public TagItTextFieldSettings setSingleField(boolean singleField) {
        this.singleField = singleField;
        return this;
    }
}
