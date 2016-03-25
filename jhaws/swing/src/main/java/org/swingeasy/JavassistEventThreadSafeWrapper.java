package org.swingeasy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 */
public class JavassistEventThreadSafeWrapper<C> implements MethodHandler {
    public static interface EventSafe {
        //
    }

    public static <C, I> C getSimpleThreadSafeInterface(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        if (component instanceof EventSafe) {
            return component;
        }

        return new JavassistEventThreadSafeWrapper<C>(componentClass, component, interfaced).createProxy();
    }

    protected final List<String> interfacedMethods;

    protected final C component;

    protected final ProxyFactory factory;

    protected final Class<C> componentClass;

    protected C proxy;

    protected <I> JavassistEventThreadSafeWrapper(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        this.component = component;
        this.componentClass = componentClass;
        this.factory = new ProxyFactory();
        this.factory.setSuperclass(componentClass);
        this.factory.setInterfaces(new Class[] { interfaced, EventSafe.class });
        this.interfacedMethods = new ArrayList<String>();
        for (Method method : interfaced.getDeclaredMethods()) {
            String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            this.interfacedMethods.add(sig);
        }
    }

    protected C createProxy() {
        try {
            this.proxy = this.componentClass.cast(this.factory.createClass().newInstance());
            ((ProxyObject) this.proxy).setHandler(this);
            return this.proxy;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(final Object self, final Method method, final Method proceed, final Object[] args) throws Throwable {
        if ("equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType()) && (args.length == 1)) {
            return proceed.invoke(self, args);
        }
        if ("hashcode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType()) && (args.length == 0)) {
            return proceed.invoke(self, args);
        }
        if ("getOriginal".equals(method.getName()) && (args.length == 0)) {
            return method.invoke(this.component, args);
        }
        String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        boolean interfacedMethod = this.interfacedMethods.contains(sig);
        if (!interfacedMethod) {
            return method.invoke(this.component, args);
        }
        boolean edt = SwingUtilities.isEventDispatchThread();
        if (edt) {
            try {
                return method.invoke(this.component, args);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
                throw ex.getTargetException();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        final ValueHolder<Throwable> exceptionThrown = new ValueHolder<Throwable>();
        final ValueHolder<Object> returnValue = new ValueHolder<Object>(Void.TYPE);
        Runnable doRun = new Runnable() {
            @Override
            public void run() {
                try {
                    returnValue.value = method.invoke(JavassistEventThreadSafeWrapper.this.component, args);
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    exceptionThrown.value = ex.getTargetException();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    exceptionThrown.value = ex;
                }
            }
        };
        try {
            SwingUtilities.invokeAndWait(doRun);
        } catch (InterruptedException ex) {
            //
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
        if (exceptionThrown.value != null) {
            throw exceptionThrown.value;
        }
        return returnValue.value;
    }
}
