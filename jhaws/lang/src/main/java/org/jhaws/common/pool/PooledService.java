package org.jhaws.common.pool;

import org.jhaws.common.lang.functions.ERunnable;

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
		T p = controllerType.cast(proxyFactory.createClass().getDeclaredConstructor().newInstance());
		Proxy.class.cast(p).setHandler((_this, method, proceed, args) -> {
        	if (method.getAnnotation(Pooled.class) == null && method.getDeclaredAnnotation(Pooled.class) == null) {
        		return method.invoke(subject, args);
        	}
        	pool.addJob((ERunnable) () -> method.invoke(subject, args));
        	return null;
        });
		return p;
	}
}
