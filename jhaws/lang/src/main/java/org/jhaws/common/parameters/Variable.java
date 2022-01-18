package org.jhaws.common.parameters;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.lang.StringValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Variable<T> {
	protected final Parameter parameter;

	protected final Field field;

	protected final List<T> values = new ArrayList<>();

	protected final Class<T[]> arrayType;

	protected final boolean charArray;

	protected final boolean single;

	protected final Class<T> type;

	protected final Class<? extends Collection<T>> collectionType;

	protected final String name;

	protected final List<String> options = new ArrayList<>();

	protected boolean found;

	protected boolean secure;

	protected int min;

	protected int max;

	protected transient Object defaultValue;

	@SuppressWarnings("unchecked")
	protected Variable(Field field, Parameter parameter) {
		this.field = field;
		this.parameter = parameter;
		this.name = parameter.prefix() + (StringUtils.isBlank(parameter.value()) ? field.getName() : parameter.value());
		if (field.getType().isArray()) {
			this.arrayType = (Class<T[]>) field.getType();
			this.charArray = char[].class.equals(this.arrayType) || Character[].class.equals(this.arrayType);
			this.collectionType = null;
			this.type = type(field.getType().getComponentType(), parameter.type());
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			this.arrayType = null;
			this.charArray = false;
			this.collectionType = (Class<? extends Collection<T>>) field.getType();
			this.type = (Class<T>) parameter.type();
		} else {
			this.arrayType = null;
			this.charArray = false;
			this.collectionType = null;
			this.type = type(field.getType(), parameter.type());
		}
		this.single = (this.arrayType == null && this.collectionType == null) || this.charArray;
		this.min = parameter.min() == -1 ? 0 : parameter.min();
		this.max = parameter.max() == -1 ? (this.single ? 1 : Integer.MAX_VALUE) : parameter.max();
		if (this.single) {
			if (this.min > 1) {
				throw new PVException(this.name + ":min=" + this.min + ">1");
			}
			if (this.max > 1) {
				throw new PVException(this.name + ":max=" + this.max + ">1");
			}
		}
		Arrays.stream(parameter.options()).map(ParameterOption::value).forEach(options::add);
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
				throw new PVException(value + " not in " + options);
			}
		}
		values.add((T) convert(value));
	}

	@SuppressWarnings("unchecked")
	protected Class<T> type(Class<?>... types) {
		return (Class<T>) Arrays.stream(types).filter(c -> !Object.class.equals(c)).findFirst().orElse(Object.class);
	}

	protected Object convert(String value) {
		if (value == null) {
			throw new PVException("value null not supported");
		}
		if (value.contains("${") && value.contains("}")) {
			StringValue sv = new StringValue(value);
			System.getProperties().entrySet().stream().forEach(entry -> {
				sv.operate(v -> v.replace("${" + entry.getKey() + "}",
						Optional.ofNullable(entry.getValue()).map(String::valueOf).orElse("")));
			});
			System.getenv().entrySet().stream().forEach(entry -> {
				sv.operate(v -> v.replace("${" + entry.getKey() + "}",
						Optional.ofNullable(entry.getValue()).map(String::valueOf).orElse("")));
			});
			value = sv.get();
		}
		if (Byte.class.equals(type) || Byte.TYPE.equals(type)) {
			return value.getBytes();
		} else if (Character.class.equals(type) || Character.TYPE.equals(type)) {
			return value.toCharArray();
		} else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			if ("true".equalsIgnoreCase(value) || "1".equals(value)) {
				return Boolean.TRUE;
			}
			if ("false".equalsIgnoreCase(value) || "0".equals(value)) {
				return Boolean.FALSE;
			}
			throw new PVException("value not supported for boolean: " + value);
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
		} else if (Path.class.equals(type)) {
			return Paths.get(value);
		} else if (String.class.equals(type)) {
			return value;
		} else if (Object.class.equals(type)) {
			return value;
		}
		throw new PVException("type not supported: " + type);
	}

	protected Variable<T> defaults(Object obj) {
		try {
			@SuppressWarnings("deprecation")
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			this.defaultValue = field.get(obj);
			field.setAccessible(accessible);
		} catch (Exception ex) {
			//
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public Object apply(Object obj) {
		if (found && max == 0) {
			if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
				set(obj, Boolean.TRUE);
			} else if (String.class.equals(type)) {
				set(obj, "true");
			} else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
				set(obj, 1);
			} else {
				throw new PVException("boolean cannot be set for type: " + name);
			}
		} else if (values.isEmpty() && found && min == 0 && max == 1
				&& (Boolean.class.equals(type) || Boolean.TYPE.equals(type))) {
			set(obj, Boolean.TRUE);
		} else {
			if (min == 1 && max != Integer.MAX_VALUE && values.size() != max) {
				throw new PVException("too many/not enough arguments: " + name + ": " + values);
			}
			if (!values.isEmpty()) {
				if (arrayType != null) {
					if (values.size() == 1 && (Character.class.equals(type) || Character.TYPE.equals(type))) {
						set(obj, values.get(0));
					} else {
						set(obj, values.toArray((T[]) Array.newInstance(type, values.size())));
					}
				} else if (collectionType != null) {
					try {
						Collection<T> collection = Collection.class.cast(field.get(obj));
						if (collection == null) {
							collection = Collection.class.cast(collectionType.getDeclaredConstructor().newInstance());
							set(obj, collection);
						}
						collection.addAll(values);
					} catch (InstantiationException | IllegalAccessException ex) {
						throw new IllegalArgumentException(ex);
					} catch (IllegalArgumentException ex) {
						throw new IllegalArgumentException(ex);
					} catch (InvocationTargetException ex) {
						throw new IllegalArgumentException(ex);
					} catch (NoSuchMethodException ex) {
						throw new IllegalArgumentException(ex);
					} catch (SecurityException ex) {
						throw new IllegalArgumentException(ex);
					}
				} else {
					if (values.size() == 1) {
						set(obj, values.get(0));
					} else {
						throw new PVException("too many values: " + values);
					}
				}
			}
		}
		return obj;
	}

	protected void set(Object o, Object v) {
		try {
			@SuppressWarnings("deprecation")
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

	public List<String> info() {
		List<String> info = new ArrayList<>();

		info.add(name);

		if (min == max) {
			if (min == 1) {
				info.add("[" + "required" + "]");
			} else {
				info.add("[" + min + "]");
			}
		} else {
			if (max == 1) {
				info.add("[" + "optional" + "]");
			} else {
				if (max == Integer.MAX_VALUE) {
					info.add("[" + min + "\u2026" + "*" + "]");
				} else {
					info.add("[" + min + "\u2026" + max + "]");
				}
			}
		}

		if (!options.isEmpty())
			info.add(options.toString().replace('[', '<').replace(']', '>'));
		else
			info.add("<no options>");

		String n = type.getSimpleName();
		if (arrayType != null) {
			info.add(n + "[]" + " " + field.getName());
		} else if (collectionType != null) {
			info.add(collectionType.getSimpleName() + "<" + n + ">" + " " + field.getName());
		} else {
			info.add(n + " " + field.getName());
		}

		if (secure) {
			info.add("*****");
		} else {
			if (values.isEmpty()) {
				if (found) {
					info.add("true");
				} else {
					info.add("not set");
				}
			} else {
				if (values.size() == 1) {
					info.add(values.get(0).toString());
				} else {
					info.add(values.toString());
				}
			}
		}

		if (this.defaultValue != null) {
			info.add("{" + this.defaultValue + "}");
		} else {
			info.add("{no default}");
		}

		return info;
	}

	@Override
	public String toString() {
		return info().stream().collect(Collectors.joining("\t\t"));
	}
}
