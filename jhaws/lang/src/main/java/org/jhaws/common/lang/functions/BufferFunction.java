package org.jhaws.common.lang.functions;

import java.util.function.Function;

public class BufferFunction<T, R> implements Function<T, R> {
	private final Function<T, R> delegate;

	private T initialValue;

	private R returnValue;

	public BufferFunction(Function<T, R> delegate) {
		this.delegate = delegate;
	}

	@Override
	public R apply(T _initialValue) {
		if (returnValue == null || _initialValue != this.initialValue) {
			returnValue = delegate.apply(this.initialValue = _initialValue);
		}
		return returnValue;
	}
}