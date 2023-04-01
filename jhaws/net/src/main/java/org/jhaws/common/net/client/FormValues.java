package org.jhaws.common.net.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class FormValues extends HashMap<String, List<String>> {
	public FormValues() {
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FormValues(Map m) {
		super();
		Set<Map.Entry> entrySet = m.entrySet();
		for (Map.Entry entry : entrySet) {
			if (entry.getValue() instanceof Collection) {
				for (Object item : Collection.class.cast(entry.getValue())) {
					if (item != null) {
						add(entry.getKey().toString(), item.toString());
					}
				}
			} else {
				if (entry.getValue() != null) {
					add(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
	}

	public FormValues add(String key, String value) {
		List<String> values = get(key);
		if (values == null) {
			values = new ArrayList<>();
			put(key, values);
		}
		values.add(value);
		return this;
	}

	public FormValues add(String key, Collection<String> newValues) {
		List<String> values = get(key);
		if (values == null) {
			values = new ArrayList<>();
			put(key, values);
		}
		values.addAll(newValues);
		return this;
	}

	public FormValues remove(String key, String value) {
		List<String> values = get(key);
		if (values != null) {
			values.remove(value);
		}
		return this;
	}

	public FormValues remove(String key, Collection<String> newValues) {
		List<String> values = get(key);
		if (values != null) {
			values.removeAll(newValues);
		}
		return this;
	}

	public FormValues replace(String key, String value) {
		List<String> values = new ArrayList<>();
		values.add(value);
		put(key, values);
		return this;
	}

	public FormValues replace(String key, Collection<String> newValues) {
		put(key, new ArrayList<>(newValues));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[\n");
		entrySet().forEach(entry -> {
			sb.append(entry.toString()).append("\n");
		});
		return sb.append("]").toString();
	}
}
