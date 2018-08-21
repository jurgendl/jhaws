package org.jhaws.common.lang.functions;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.Callable;

/**
 * https://dzone.com/articles/hacking-lambda-expressions-in-java
 */
public interface SilentInvokerFactory {
    @FunctionalInterface
    static interface SilentInvoker {
        MethodType SIGNATURE = MethodType.methodType(Object.class, Callable.class);// signature of method INVOKE

        <V> V invoke(final Callable<V> callable);
    }

    static class SilentInvokerBuilder {
        static SilentInvoker createSilentInvoker() {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                final MethodHandles.Lookup caller = lookup;
                final String invokedName = "invoke";
                final MethodType invokedType = MethodType.methodType(SilentInvoker.class);
                final MethodType samMethodType = SilentInvoker.SIGNATURE;
                final MethodHandle implMethod = lookup.findVirtual(Callable.class, "call", MethodType.methodType(Object.class));
                final MethodType instantiatedMethodType = SilentInvoker.SIGNATURE;
                final CallSite site = LambdaMetafactory.metafactory(caller, invokedName, invokedType, samMethodType, implMethod,
                        instantiatedMethodType);
                return (SilentInvoker) site.getTarget().invokeExact();
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    final static SilentInvoker SILENT_INVOKER = SilentInvokerBuilder.createSilentInvoker();

    public static <V> V call(final Callable<V> callable) /* no throws */ {
        return SILENT_INVOKER.invoke(callable);
    }

    default <V> V cu(final Callable<V> callable) /* no throws */ {
        return SILENT_INVOKER.invoke(callable);
    }
}
