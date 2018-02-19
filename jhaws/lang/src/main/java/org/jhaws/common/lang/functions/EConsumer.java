package org.jhaws.common.lang.functions;

import java.util.function.Consumer;

@FunctionalInterface
public interface EConsumer<T> extends SConsumer<T> {
    public static <T> Consumer<T> enhance(EConsumer<T> consumer) {
        return consumer::accept;
    }

    @Override
    default void accept(T t) {
        try {
            acceptEnhanced(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void acceptEnhanced(T elem) throws Exception;
}