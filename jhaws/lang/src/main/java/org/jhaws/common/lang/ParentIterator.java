package org.jhaws.common.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.jhaws.common.lang.functions.BufferUnaryOperator;

public class ParentIterator<T> implements Iterator<T> {
	private final T initial;

	private final UnaryOperator<T> nextItem;

	private final Predicate<T> accept;

	private T current = null;

	public ParentIterator(T initial, UnaryOperator<T> nextItem) {
		this(initial, nextItem, null);
	}

	public ParentIterator(T initial, UnaryOperator<T> nextItem, Predicate<T> accept) {
		Objects.nonNull(initial);
		this.initial = initial;
		this.nextItem = new BufferUnaryOperator<>(nextItem);
		this.accept = accept == null ? CollectionUtils8.<T>isNotNull() : CollectionUtils8.<T>isNotNull().and(accept);
	}

	@Override
	public boolean hasNext() {
		return current == null ? accept.test(initial) : accept.test(nextItem.apply(current));
	}

	@Override
	public T next() {
		return current = (current == null ? initial : nextItem.apply(current));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}
}