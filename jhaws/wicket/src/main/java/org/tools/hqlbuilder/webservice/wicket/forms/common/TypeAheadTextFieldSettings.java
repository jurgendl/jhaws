package org.tools.hqlbuilder.webservice.wicket.forms.common;

public class TypeAheadTextFieldSettings extends AbstractFormElementSettings<TypeAheadTextFieldSettings> {
    private static final long serialVersionUID = 1098120531336315493L;

    protected boolean caseSensitive = false;

    protected int max = 10;

    protected int minLength = 2;

    protected long delay = 200;

    protected boolean allowSpaces = true;

    protected String remote;

    protected boolean remoteFilters = false;

    protected String local;

    protected boolean free = true;

    public TypeAheadTextFieldSettings() {
        super();
    }

    public TypeAheadTextFieldSettings(boolean required) {
        super(required);
    }

    public long getDelay() {
        return this.delay;
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

    public TypeAheadTextFieldSettings setAllowSpaces(boolean allowSpaces) {
        this.allowSpaces = allowSpaces;
        return this;
    }

    public TypeAheadTextFieldSettings setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public TypeAheadTextFieldSettings setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public TypeAheadTextFieldSettings setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public String getRemote() {
        return this.remote;
    }

    public TypeAheadTextFieldSettings setRemote(String remote) {
        this.remote = remote;
        return this;
    }

    public boolean isFree() {
        return this.free;
    }

    public TypeAheadTextFieldSettings setFree(boolean free) {
        this.free = free;
        return this;
    }

    public String getLocal() {
        return this.local;
    }

    public TypeAheadTextFieldSettings setLocal(String local) {
        this.local = local;
        return this;
    }

    public int getMax() {
        return this.max;
    }

    public TypeAheadTextFieldSettings setMax(int max) {
        this.max = max;
        return this;
    }

    public boolean isRemoteFilters() {
        return this.remoteFilters;
    }

    public TypeAheadTextFieldSettings setRemoteFilters(boolean remoteFilters) {
        this.remoteFilters = remoteFilters;
        return this;
    }
}
