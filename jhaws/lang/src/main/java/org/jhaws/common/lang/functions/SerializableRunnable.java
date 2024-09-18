package org.jhaws.common.lang.functions;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableRunnable extends Runnable, Serializable {
    public static Runnable enhance(SerializableRunnable runnable) {
        return runnable::run;
    }
}
