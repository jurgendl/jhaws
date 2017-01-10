package org.jhaws.common.parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
		vars = Arrays.stream(c.getDeclaredFields())
				.filter(field -> field.getDeclaredAnnotation(Parameter.class) != null)
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
				String value = q.contains("=") ? q.substring(0, q.indexOf("=")) : q;
				Variable<?> variable = vars.stream().filter(var -> var.name(value)).findFirst().orElse(null);
				if (variable != null) {
					variable.found();
					currentVariable = variable;
				} else {
					if (currentVariable == null)
						throw new PVException("parameter not processed: " + value);
					currentVariable.add(value);
				}
				if (q.contains("=")) {
					currentVariable.add(q.substring(1 + q.indexOf("=")));
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
		if (vars != null)
			IntStream.range(0, vars.size()).mapToObj(i -> "parameter " + (i + 1) + ": " + vars.get(i))
					.forEach(System.out::println);
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
			return type.cast(apply(type.newInstance()));
		} catch (InstantiationException | IllegalAccessException ex) {
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
