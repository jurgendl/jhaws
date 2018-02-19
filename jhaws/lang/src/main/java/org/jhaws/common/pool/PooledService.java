package org.jhaws.common.pool;

import java.lang.reflect.Method;

import org.jhaws.common.lang.functions.ERunnable;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

public class PooledService {
    protected static <T> T pool(Pool<Object> pool, Class<T> controllerType, T subject) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        if (controllerType.isInterface()) {
            proxyFactory.setInterfaces(new Class[] { controllerType });
        } else {
            proxyFactory.setSuperclass(controllerType);
        }
        T p = controllerType.cast(proxyFactory.createClass().newInstance());
        Proxy.class.cast(p).setHandler(new MethodHandler() {
            @Override
            public Object invoke(Object _this, Method method, Method proceed, Object[] args) throws Exception {
                if (method.getAnnotation(Pooled.class) == null && method.getDeclaredAnnotation(Pooled.class) == null) {
                    return method.invoke(subject, args);
                }
                pool.addJob((ERunnable) () -> method.invoke(subject, args));
                return null;
            }
        });
        return p;
    }
}
