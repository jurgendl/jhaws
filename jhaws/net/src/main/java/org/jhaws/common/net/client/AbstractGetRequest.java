package org.jhaws.common.net.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetRequest<T extends AbstractGetRequest<? super T>> extends AbstractRequest<T> {
	private static final long serialVersionUID = 4305650301682256528L;

	protected Map<String, List<String>> formValues = new HashMap<>();

	public AbstractGetRequest() {
		super();
	}

	public AbstractGetRequest(URI uri) {
		setUri(uri);
	}

	public AbstractGetRequest(String uri) {
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