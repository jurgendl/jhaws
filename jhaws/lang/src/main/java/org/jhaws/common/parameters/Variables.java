package org.jhaws.common.parameters;

import static org.jhaws.common.lang.Collections8.collectList;
import static org.jhaws.common.lang.Collections8.stream;
import static org.jhaws.common.lang.Collections8.toQueue;

import java.util.List;
import java.util.Queue;

public class Variables<T> {
	protected List<Variable<?>> vars;

	protected Class<T> type;

	@SuppressWarnings("unchecked")
	public Variables(T o) {
		this((Class<T>) o.getClass());
	}

	public Variables(Class<T> c) {
		type = c;
		vars = stream(c.getDeclaredFields()).map(field -> new Variable<>(field, field.getDeclaredAnnotation(Parameter.class))).filter(p -> p.parameter != null)
				.collect(collectList());
	}

	public Variables<T> parameters(String... args) {
		Queue<String> queue = toQueue(args);
		Variable<?> currentVariable = null;
		while (!queue.isEmpty()) {
			String value = queue.remove();
			Variable<?> variable = stream(vars).filter(var -> var.name(value)).findFirst().orElse(null);
			if (variable != null) {
				variable.found();
				currentVariable = variable;
			} else {
				if (currentVariable == null)
					throw new NullPointerException();
				currentVariable.add(value);
			}
		}

		return this;
	}

	public Variables<T> log() {
		stream(vars).forEach(System.out::println);
		return this;
	}

	public T apply(T obj) {
		stream(vars).forEach(var -> var.apply(obj));
		return obj;
	}

	public T apply() {
		try {
			return type.cast(apply(type.newInstance()));
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}
