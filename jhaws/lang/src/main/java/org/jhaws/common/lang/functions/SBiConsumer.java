package org.jhaws.common.lang.functions;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {
    public static <T, U> BiConsumer<T, U> enhance(SBiConsumer<T, U> consumer) {
        return consumer::accept;
    }
}
