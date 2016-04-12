package org.jhaws.common.parameters;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Variables<T> {
	protected List<Variable<?>> vars;

	protected Class<T> type;

	protected String[] parameters;

	@SuppressWarnings("unchecked")
	public Variables(T o) {
		this((Class<T>) o.getClass());
	}

	public Variables(Class<T> c) {
		type = c;
		vars = Arrays.stream(c.getDeclaredFields()).filter(field -> field.getDeclaredAnnotation(Parameter.class) != null)
				.map(field -> new Variable<>(field, field.getDeclaredAnnotation(Parameter.class))).collect(Collectors.toList());
	}

	public Variables<T> parameters(String... args) {
		this.parameters = args;
		try {
			Queue<String> queue = Arrays.stream(args).collect(Collectors.toCollection(LinkedList::new));
			Variable<?> currentVariable = null;
			while (!queue.isEmpty()) {
				String value = queue.remove();
				Variable<?> variable = vars.stream().filter(var -> var.name(value)).findFirst().orElse(null);
				if (variable != null) {
					variable.found();
					currentVariable = variable;
				} else {
					if (currentVariable == null)
						throw new PVException("parameter not processed: " + value);
					currentVariable.add(value);
				}
			}
		} catch (PVException ex) {
			pverror(ex);
		}
		return this;
	}

	public void pverror(PVException ex) {
		System.out.println("error: " + ex.getMessage());
		log();
		throw ex;
	}

	public Variables<T> log() {
		IntStream.range(0, parameters.length).mapToObj(i -> (i + 1) + ": " + parameters[i]).forEach(System.out::println);
		IntStream.range(0, vars.size()).mapToObj(i -> "parameter " + (i + 1) + ": " + vars.get(i)).forEach(System.out::println);
		return this;
	}

	public Variables<T> apply(T obj) {
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
}
