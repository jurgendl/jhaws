package org.swingeasy;

/**
 * @author Jurgen
 */
public class EventThreadSafeWrapper<C> {
    public static <C, I> C getSimpleThreadSafeInterface(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        try {
            return org.swingeasy.JavassistEventThreadSafeWrapper.getSimpleThreadSafeInterface(componentClass, component, interfaced);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return component;
        }
    }
}
