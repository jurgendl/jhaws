package org.jhaws.common.parameters;

import static org.jhaws.common.lang.Collections8.stream;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Variable<T> {
	protected final Parameter parameter;

	protected final Field field;

	protected final List<T> values = new ArrayList<T>();

	protected final Class<T[]> arrayType;

	protected final Class<T> type;

	protected final Class<? extends Collection<T>> collectionType;

	protected final String name;

	protected final List<String> options = new ArrayList<>();

	protected boolean found;

	protected boolean secure;

	@SuppressWarnings("unchecked")
	protected Variable(Field field, Parameter parameter) {
		this.field = field;
		this.parameter = parameter;
		this.name = parameter.prefix() + (StringUtils.isBlank(parameter.value()) ? field.getName() : parameter.value());
		if (field.getType().isArray()) {
			this.arrayType = (Class<T[]>) field.getType();
			this.collectionType = null;
			this.type = type(field.getType().getComponentType(), parameter.type());
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			this.arrayType = null;
			this.collectionType = (Class<? extends Collection<T>>) field.getType();
			this.type = (Class<T>) parameter.type();
		} else {
			this.arrayType = null;
			this.collectionType = null;
			this.type = type(field.getType(), parameter.type());
		}
		stream(parameter.options()).map(ParameterOption::value).forEach(options::add);
	}

	protected boolean name(String n) {
		return this.name.equals(n);
	}

	public void found() {
		this.found = true;
	}

	@SuppressWarnings("unchecked")
	public void add(String value) {
		if (!options.isEmpty()) {
			if (!options.contains(value)) {
				throw new IllegalArgumentException(value + " not in " + options);
			}
		}
		values.add((T) convert(value));
	}

	@SuppressWarnings("unchecked")
	protected Class<T> type(Class<?>... types) {
		return (Class<T>) stream(types).filter(c -> !Object.class.equals(c)).findFirst().orElse(Object.class);
	}

	protected Object convert(String value) {
		if (Byte.class.equals(type) || Byte.TYPE.equals(type)) {
			return value.getBytes();
		} else if (Character.class.equals(type) || Character.TYPE.equals(type)) {
			return value.toCharArray();
		} else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			return "true".equalsIgnoreCase(value) || "1".equals(value);
		} else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
			return Integer.parseInt(value);
		} else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
			return Long.parseLong(value);
		} else if (Short.class.equals(type) || Short.TYPE.equals(type)) {
			return Short.parseShort(value);
		} else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
			return Double.parseDouble(value);
		} else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
			return Float.parseFloat(value);
		} else if (String.class.equals(type)) {
			return value;
		} else if (Object.class.equals(type)) {
			return value;
		}
		throw new IllegalArgumentException("type not supported: " + type);
	}

	@SuppressWarnings("unchecked")
	public Object apply(Object obj) {
		if (found && parameter.size() == 0) {
			if (Boolean.class.equals(type) || Boolean.TYPE.equals(type))
				set(obj, Boolean.TRUE);
			else if (String.class.equals(type))
				set(obj, "true");
			else if (Integer.class.equals(type) || Integer.TYPE.equals(type))
				set(obj, 1);
			else
				throw new IllegalArgumentException();
		} else {
			if (parameter.required() && values.size() != parameter.size()) {
				throw new IllegalArgumentException("too many/not enough arguments: " + name + " (" + parameter.size() + ")" + ": " + values);
			}
			if (!values.isEmpty()) {
				if (arrayType != null) {
					if (values.size() == 1 && (Character.class.equals(type) || Character.TYPE.equals(type))) {
						set(obj, values.get(0));
					} else {
						T[] a = (T[]) Array.newInstance(type, values.size());
						set(obj, values.toArray(a));
					}
				} else if (collectionType != null) {
					try {
						Collection<T> collection = Collection.class.cast(field.get(obj));
						if (collection == null) {
							collection = Collection.class.cast(collectionType.newInstance());
							set(obj, collection);
						}
						collection.addAll(values);
					} catch (InstantiationException | IllegalAccessException ex) {
						throw new IllegalArgumentException(ex);
					}
				} else {
					if (values.size() == 1)
						set(obj, values.get(0));
					else
						throw new IllegalArgumentException();
				}
			}
		}
		return obj;
	}

	protected void set(Object o, Object v) {
		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(o, v);
			field.setAccessible(accessible);
		} catch (Exception ex) {
			System.err.println(field);
			System.err.println(v);
			throw new IllegalArgumentException(ex);
		}
	}

	@Override
	public String toString() {
		String st = type.getSimpleName();
		return (arrayType != null ? st + "[]" : (collectionType != null ? collectionType.getSimpleName() + "<" + st + ">" : st)) + " " + field.getName() + " (" + name + ") "
				+ (parameter.required() ? "! " : "") + "= " + (secure ? "*****" : (values.isEmpty() ? (found ? "true" : "null") : (values.size() == 1 ? values.get(0) : values)));
	}
}
