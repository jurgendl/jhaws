package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.util.Arrays;
import java.util.List;

public class TypeAheadTextFieldAltSettings extends AbstractFormElementSettings<TypeAheadTextFieldAltSettings> {
    private static final long serialVersionUID = 1098120531336315493L;

    protected boolean caseSensitive = false;

    protected int max = 10;

    protected int minLength = 2;

    protected long delay = 200;

    protected String remote;

    protected String queryParam;

    protected boolean remoteFilters = false;

    protected String local;

    protected boolean free = true;

    protected List<String> properties = Arrays.asList("value");

    protected String template = "value";

    public TypeAheadTextFieldAltSettings() {
        super();
    }

    public TypeAheadTextFieldAltSettings(boolean required) {
        super(required);
    }

    public long getDelay() {
        return this.delay;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public TypeAheadTextFieldAltSettings setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public TypeAheadTextFieldAltSettings setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public TypeAheadTextFieldAltSettings setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public String getRemote() {
        return this.remote;
    }

    public TypeAheadTextFieldAltSettings setRemote(String remote) {
        this.remote = remote;
        return this;
    }

    public boolean isFree() {
        return this.free;
    }

    public TypeAheadTextFieldAltSettings setFree(boolean free) {
        this.free = free;
        return this;
    }

    public String getLocal() {
        return this.local;
    }

    public TypeAheadTextFieldAltSettings setLocal(String local) {
        this.local = local;
        return this;
    }

    public int getMax() {
        return this.max;
    }

    public TypeAheadTextFieldAltSettings setMax(int max) {
        this.max = max;
        return this;
    }

    public boolean isRemoteFilters() {
        return this.remoteFilters;
    }

    public TypeAheadTextFieldAltSettings setRemoteFilters(boolean remoteFilters) {
        this.remoteFilters = remoteFilters;
        return this;
    }

    public List<String> getProperties() {
        return this.properties;
    }

    public TypeAheadTextFieldAltSettings setProperties(List<String> properties) {
        this.properties = properties;
        return this;
    }

    public String getTemplate() {
        return this.template;
    }

    public TypeAheadTextFieldAltSettings setTemplate(String template) {
        this.template = template;
        return this;
    }

    public String getQueryParam() {
        return this.queryParam;
    }

    public TypeAheadTextFieldAltSettings setQueryParam(String queryParam) {
        this.queryParam = queryParam;
        return this;
    }
}
