package org.jhaws.common.pool;

import java.lang.reflect.Method;

import org.jhaws.common.lang.RunIndefinitely;
import org.jhaws.common.lang.functions.ERunnable;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

public class PoolTest {
	public static void main(String[] args) {
		try {
			Pool<Object> pool = new Pool<>("test", 2);
			TestService t = proxy(pool, TestService.class, new TestServiceImpl());
			for (int i = 0; i < 10; i++) {
				t.doSomething(i);
				t.doSomethingElse(i);
			}
			System.out.println("added");
			new Thread((RunIndefinitely) () -> Long.MAX_VALUE).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public @interface Pooled {
		//
	}

	protected static <T> T proxy(Pool<Object> pool, Class<T> controllerType, T subject) throws Exception {
		ProxyFactory proxyFactory = new ProxyFactory();
		if (controllerType.isInterface()) {
			proxyFactory.setInterfaces(new Class[] { controllerType });
		} else {
			proxyFactory.setSuperclass(controllerType);
		}
		@SuppressWarnings("unchecked")
		T p = (T) proxyFactory.createClass().newInstance();
		Proxy.class.cast(p).setHandler(new MethodHandler() {
			@Override
			public Object invoke(Object _this, Method method, Method proceed, Object[] args) throws Exception {
				if (method.getAnnotation(Pooled.class) == null) {
					return method.invoke(subject, args);
				}
				pool.addJob((ERunnable) () -> method.invoke(subject, args));
				return null;
			}
		});
		return p;
	}

	public static interface TestService {
		@Pooled
		public void doSomething(Object o);

		public void doSomethingElse(Object o);
	}

	public static class TestServiceImpl implements TestService {
		@Override
		public void doSomething(Object o) {
			System.out.println("doSomething:start:" + o);
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException ex) {
				//
			}
			System.out.println("doSomething:end:" + o);
		}

		@Override
		public void doSomethingElse(Object o) {
			System.out.println("doSomethingElse:start:" + o);
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException ex) {
				//
			}
			System.out.println("doSomethingElse:end:" + o);
		}
	}
}
