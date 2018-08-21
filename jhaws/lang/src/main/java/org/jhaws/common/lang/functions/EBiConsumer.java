package org.jhaws.common.lang.functions;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface EBiConsumer<T, U> extends SBiConsumer<T, U> {
    public static <T, U> BiConsumer<T, U> enhance(EBiConsumer<T, U> consumer) {
        return consumer::accept;
    }

    @Override
    default void accept(T t, U u) {
        try {
            acceptEnhanced(t, u);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void acceptEnhanced(T t, U u) throws Exception;
}