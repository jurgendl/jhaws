package org.jhaws.common.parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Variables<T> {
	protected Class<T> type;

	protected transient String[] parameters;

	protected T config;

	protected transient List<Variable<?>> vars;

	public Variables() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Variables(T o) {
		this((Class<T>) o.getClass());
	}

	public Variables(Class<T> c) {
		init(c);
	}

	protected void init(Class<T> c) {
		type = c;

		List<Field> fields = new ArrayList<>();
		Class<?> cc = c;
		while (!Object.class.equals(cc)) {
			Field[] declaredFields = cc.getDeclaredFields();
			Arrays.stream(declaredFields).forEach(fields::add);
			cc = cc.getSuperclass();
		}
		vars = fields.stream().filter(field -> field.getDeclaredAnnotation(Parameter.class) != null)
				.map(field -> new Variable<>(field, field.getDeclaredAnnotation(Parameter.class)))
				.collect(Collectors.toList());
	}

	public Variables<T> parameters(String... args) {
		this.parameters = args;
		try {
			Queue<String> queue = Arrays.stream(args).collect(Collectors.toCollection(LinkedList::new));
			Variable<?> currentVariable = null;
			while (!queue.isEmpty()) {
				String q = queue.remove();
				if ((q.startsWith("--") || q.startsWith("-+") || q.startsWith("+")) && q.contains("=")) {
					throw new PVException("parameter not processed: " + q);
				}
				String key = q;
				String value = null;
				if (q.startsWith("--")) {
					key = "-" + q.substring(2);
					value = "false";
				} else if (q.startsWith("-+")) {
					key = "-" + q.substring(2);
					value = "true";
				} else if (q.startsWith("+")) {
					key = "-" + q.substring(1);
					value = "true";
				} else if (q.contains("=")) {
					String[] p = q.split("=");
					key = p[0];
					value = p[1];
				}
				final String _k = key;
				Variable<?> variable = vars.stream().filter(var -> var.name(_k)).findFirst().orElse(null);
				if (variable != null) {
					variable.found();
					currentVariable = variable;
				} else {
					if (currentVariable == null)
						throw new PVException("parameter not processed: " + key);
					currentVariable.add(key);
				}
				if (value != null) {
					currentVariable.add(value);
				}
			}
		} catch (PVException ex) {
			pverror(ex);
		}
		return this;
	}

	protected void pverror(PVException ex) {
		System.out.println("error: " + ex.getMessage());
		log();
		throw ex;
	}

	public Variables<T> log() {
		if (parameters != null)
			IntStream.range(0, parameters.length).mapToObj(i -> (i + 1) + ": " + parameters[i])
					.forEach(System.out::println);
		if (vars != null) {
			Map<Integer, Integer> ii = new HashMap<>();
			for (Variable<?> v : vars) {
				List<String> info = v.info();
				for (int j = 0; j < info.size(); j++) {
					Integer len = ii.get(j);
					if (len == null) {
						len = info.get(j).toString().length();
					} else {
						len = Math.max(len, info.get(j).toString().length());
					}
					ii.put(j, len);
				}
			}
			StringBuilder format = new StringBuilder();
			for (int j = 0; j < ii.size(); j++) {
				format.append("%-" + ii.get(j) + "s\t");
			}
			String _format = format.toString();
			IntStream.range(0, vars.size())
					.mapToObj(i -> "#" + (i < 9 ? "0" : "") + (i + 1) + ": "
							+ String.format(_format,
									vars.get(i).info().stream().map(s -> (Object) s).toArray(a -> new Object[a])))
					.forEach(System.out::println);
		}
		return this;
	}

	public Variables<T> apply(T obj) {
		this.config = obj;
		try {
			vars.stream().forEach(var -> var.defaults(obj));
		} catch (Exception ex) {
			//
		}
		try {
			vars.stream().forEach(var -> var.apply(obj));
		} catch (PVException ex) {
			pverror(ex);
		}
		return this;
	}

	public T apply() {
		try {
			return type.cast(apply(type.getDeclaredConstructor().newInstance()));
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
	}

	public Variables<T> saveConfig(File file) {
		try {
			JAXBContext.newInstance(Variables.class, type).createMarshaller().marshal(this, file);
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public static <T> Variables<T> loadConfig(Class<T> type, File file) {
		try (FileInputStream in = new FileInputStream(file)) {
			Variables<T> vars = (Variables<T>) (JAXBContext.newInstance(Variables.class, type).createUnmarshaller()
					.unmarshal(in));
			vars.init(type);
			return vars;
		} catch (JAXBException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public T getConfig() {
		return this.config;
	}
}