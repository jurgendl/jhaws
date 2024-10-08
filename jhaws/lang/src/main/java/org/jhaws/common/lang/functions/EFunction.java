package org.jhaws.common.lang.functions;

import java.util.function.Function;

@FunctionalInterface
public interface EFunction<T, R> extends SerializableFunction<T, R> {
    public static <T, R> Function<T, R> enhance(EFunction<T, R> function) {
        return function::apply;
    }

    @Override
    default R apply(T t) {
        try {
            return applyEnhanced(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    R applyEnhanced(T elem) throws Exception;
}