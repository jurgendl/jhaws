package org.tools.hqlbuilder.webservice.wicket.forms;

public class AutoCompleteTextFieldSettings extends AbstractFormElementSettings<AutoCompleteTextFieldSettings> {
    private static final long serialVersionUID = 208227230310006451L;

    protected int maxResults = 10;

    protected boolean contains = true;

    protected int minLenght = 3;

    protected boolean normalize = true;

    public int getMaxResults() {
        return this.maxResults;
    }

    public boolean isContains() {
        return this.contains;
    }

    public int getMinLenght() {
        return this.minLenght;
    }

    public AutoCompleteTextFieldSettings setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public AutoCompleteTextFieldSettings setContains(boolean contains) {
        this.contains = contains;
        return this;
    }

    public AutoCompleteTextFieldSettings setMinLenght(int minLenght) {
        this.minLenght = minLenght;
        return this;
    }

    public boolean isNormalize() {
        return this.normalize;
    }

    public AutoCompleteTextFieldSettings setNormalize(boolean normalize) {
        this.normalize = normalize;
        return this;
    }
}
