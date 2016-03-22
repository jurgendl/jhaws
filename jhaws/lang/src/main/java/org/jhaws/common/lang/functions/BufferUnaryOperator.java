package org.jhaws.common.lang.functions;

import java.util.function.UnaryOperator;

public class BufferUnaryOperator<T> implements UnaryOperator<T> {
	private final UnaryOperator<T> delegate;

	private T initialValue;

	private T returnValue;

	public BufferUnaryOperator(UnaryOperator<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public T apply(T _initialValue) {
		if (returnValue == null || _initialValue != this.initialValue) {
			returnValue = delegate.apply(this.initialValue = _initialValue);
		}
		return returnValue;
	}
}
