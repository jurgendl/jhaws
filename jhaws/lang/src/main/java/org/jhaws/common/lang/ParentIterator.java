package org.jhaws.common.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jhaws.common.lang.functions.BufferFunction;
import org.jhaws.common.lang.functions.MonoFunction;

public class ParentIterator<T> implements Iterator<T> {
    private final T initial;

    private final Function<T, T> nextItem;

    private final Predicate<T> accept;

    private T current = null;

    public ParentIterator(T initial, MonoFunction<T> nextItem) {
        this(initial, nextItem, null);
    }

    public ParentIterator(T initial, MonoFunction<T> nextItem, Predicate<T> accept) {
        Objects.nonNull(initial);
        this.initial = initial;
        this.nextItem = new BufferFunction<>(nextItem);
        this.accept = accept == null ? Collections8.<T> notNull() : Collections8.<T> notNull().and(accept);
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