package org.swingeasy;

import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * @author Jurgen
 */
public final class WeakReferencedListener<T> implements java.lang.reflect.InvocationHandler {
    public static boolean isWrapped(Object object) {
        return java.lang.reflect.Proxy.isProxyClass(object.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> WeakReferencedListener<T> unwrap(Object object) {
        return (WeakReferencedListener<T>) java.lang.reflect.Proxy.getInvocationHandler(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> T wrap(final Class<T> interfaceClass, final T delegate) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                new WeakReferencedListener<T>(interfaceClass, delegate).getInvocationHandler());
    }

    private final WeakReference<T> weakreference;

    private final Class<T> interfaceClass;

    private final int hashCode;

    private WeakReferencedListener(Class<T> interfaceClass, T delegate) {
        this.interfaceClass = interfaceClass;
        this.weakreference = new WeakReference<T>(delegate);
        this.hashCode = delegate.hashCode();
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public Class<T> getInterfaceClass() {
        return this.interfaceClass;
    }

    private java.lang.reflect.InvocationHandler getInvocationHandler() {
        return this;
    }

    public T getReference() {
        return this.weakreference.get();
    }

    public WeakReference<T> getWeakReference() {
        return this.weakreference;
    }

    /**
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PropertyChangeListener listener = (PropertyChangeListener) WeakReferencedListener.this.weakreference.get();

        if (Object.class.getMethod("equals", Object.class).equals(method)) {
            return proxy == args[0];
        }

        if (Object.class.getMethod("hashCode").equals(method)) {
            return WeakReferencedListener.this.hashCode;
        }

        if (listener == null) {
            if ((args != null) && (args.length == 1) && (args[0] instanceof EventObject)) {
                EventObject event = EventObject.class.cast(args[0]);
                Object source = event.getSource();
                source.getClass()
                        .getMethod("remove" + WeakReferencedListener.this.interfaceClass.getSimpleName(), WeakReferencedListener.this.interfaceClass)
                        .invoke(source, proxy);
            }
            return null;
        }

        return method.invoke(listener, args);
    }
}
