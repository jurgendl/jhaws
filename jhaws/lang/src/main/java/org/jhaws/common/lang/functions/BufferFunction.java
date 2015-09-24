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
    public R apply(T initialValue) {
        if (returnValue == null || initialValue != this.initialValue)
            returnValue = delegate.apply(this.initialValue = initialValue);
        return returnValue;
    }
}