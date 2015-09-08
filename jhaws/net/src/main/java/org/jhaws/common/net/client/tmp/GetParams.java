package org.jhaws.common.net.client.tmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetParams extends Params {
    private static final long serialVersionUID = 4305650301682256528L;

    protected Map<String, List<String>> formValues = new HashMap<String, List<String>>();

    public GetParams() {
        super();
    }

    public GetParams(URI uri) {
        setUri(uri);
    }

    public GetParams(String uri) {
        setUri(uri);
    }

    public Map<String, List<String>> getFormValues() {
        return formValues;
    }

    public void setFormValues(Map<String, List<String>> formValues) {
        this.formValues = formValues;
    }

    public void addFormValue(String key, String value) {
        List<String> list = formValues.get(key);
        if (list == null) {
            list = new ArrayList<>();
            formValues.put(key, list);
        }
        list.add(value);
    }
}