package org.jhaws.common.net.client.latest;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetParams<T extends AbstractGetParams<? super T>> extends AbstractParams<T> {
    private static final long serialVersionUID = 4305650301682256528L;

    protected Map<String, List<String>> formValues = new HashMap<String, List<String>>();

    public AbstractGetParams() {
        super();
    }

    public AbstractGetParams(URI uri) {
        setUri(uri);
    }

    public AbstractGetParams(String uri) {
        setUri(uri);
    }

    public Map<String, List<String>> getFormValues() {
        return formValues;
    }

    public T setFormValues(Map<String, List<String>> formValues) {
        this.formValues = formValues;
        return cast();
    }

    public T addFormValue(String key, String value) {
        List<String> list = formValues.get(key);
        if (list == null) {
            list = new ArrayList<>();
            formValues.put(key, list);
        }
        list.add(value);
        return cast();
    }
}