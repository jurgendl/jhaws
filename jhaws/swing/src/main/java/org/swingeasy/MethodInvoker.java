package org.swingeasy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Jurgen
 */
public final class MethodInvoker {
	public static final class InvocationException extends Exception {

		private static final long serialVersionUID = -4908577741649536787L;

		private InvocationException(String message) {
			super(message);
		}

		private InvocationException(Throwable cause) {
			super(cause);
		}
	}

	private static Method fuzzy(boolean static0, Object invoker, String methodName, Class<?>[] types) {
		Class<?> clazz = static0 ? (Class<?>) invoker : invoker.getClass();

		do {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName) && (types.length == method.getParameterTypes().length)) {
					for (int i = 0; i < types.length; i++) {
						if (method.getParameterTypes()[i].isAssignableFrom(types[i])) {
							return method;
						}
					}
				}
			}

			clazz = clazz.getSuperclass();
		} while (clazz != null);

		return null;
	}

	/**
	 * find method
	 *
	 * @param invoker
	 * @param methodName
	 * @param types
	 *
	 * @return
	 *
	 * @throws InvocationException
	 */
	public static final Method getMethod(Object invoker, String methodName, Class<?>[] types) throws InvocationException {
		if (invoker == null) {
			throw new InvocationException("invoker cannot be <null>");
		}

		if (methodName == null) {
			throw new InvocationException("methodName cannot be <null>");
		}

		if (types == null) {
			types = new Class[0];
		}

		boolean static0 = invoker instanceof Class<?>;
		Class<?> clazz = static0 ? (Class<?>) invoker : invoker.getClass();
		Method method = null;

		try {
			method = clazz.getMethod(methodName, types);
		} catch (Exception ex) {
			do {
				try {
					method = clazz.getDeclaredMethod(methodName, types);
				} catch (Exception ex2) {
					clazz = clazz.getSuperclass();
				}
			} while ((method == null) && (clazz != null));
		}

		if (method == null) {
			method = MethodInvoker.fuzzy(static0, invoker, methodName, types);

			if (method == null) {
				throw new InvocationException("method '" + methodName + "' not found");
			}
		}

		if (!Modifier.isStatic(method.getModifiers()) && static0) {
			throw new InvocationException("can not invoke non-static method on class");
		}

		return method;
	}

	public static final Object invoke(Object invoker, String methodName) throws InvocationException {
		return MethodInvoker.invoke(invoker, methodName, (Class[]) null, (Object[]) null, Object.class);
	}

	public static final Object invoke(Object invoker, String methodName, Class<?> type, Object parameter) throws InvocationException {
		return MethodInvoker.invoke(invoker, methodName, new Class[] { type }, new Object[] { parameter }, Object.class);
	}

	public static final <T> T invoke(Object invoker, String methodName, Class<?> type, Object parameter, Class<T> returns) throws InvocationException {
		return MethodInvoker.invoke(invoker, methodName, new Class[] { type }, new Object[] { parameter }, returns);
	}

	public static final Object invoke(Object invoker, String methodName, Class<?>[] types, Object[] parameters) throws InvocationException {
		return MethodInvoker.invoke(invoker, methodName, types, parameters, Object.class);
	}

	public static final <T> T invoke(Object invoker, String methodName, Class<?>[] types, Object[] parameters, Class<T> returns) throws InvocationException {
		Method method = MethodInvoker.getMethod(invoker, methodName, types);
		Object invoked = MethodInvoker.invoke0(method, invoker, parameters);
		return returns.cast(invoked);
	}

	public static final <T> T invoke(Object invoker, String methodName, Class<T> returns) throws InvocationException {
		return MethodInvoker.invoke(invoker, methodName, (Class[]) null, (Object[]) null, returns);
	}

	public static final Object invoke(Object invoker, String methodName, Object parameter) throws InvocationException {
		MethodInvoker.nn(parameter, "parameter cannot be <null>");
		return MethodInvoker.invoke(invoker, methodName, parameter.getClass(), parameter);
	}

	private static final Object invoke0(Method method, Object invoker, Object[] parameters) throws InvocationException {
		try {
			boolean accessible = method.isAccessible();

			if (!accessible) {
				method.setAccessible(true);
			}

			Object returns;

			if (method.getReturnType() == Void.TYPE) {
				method.invoke(invoker, parameters);
				returns = Void.TYPE;
			} else {
				returns = method.invoke(invoker, parameters);
			}

			if (!accessible) {
				method.setAccessible(accessible);
			}

			return returns;
		} catch (RuntimeException ex) {
			throw new InvocationException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof InvocationException) {
				throw (InvocationException) ex.getTargetException();
			}

			if (ex.getTargetException() instanceof RuntimeException) {
				throw new InvocationException(ex.getTargetException());
			}

			throw new InvocationException(ex.getTargetException());
		}
	}

	private static final <T> T nn(T o, String msg) throws InvocationException {
		if (o == null) {
			throw new InvocationException(msg);
		}

		return o;
	}

	/**
	 * conversie
	 *
	 * @return
	 */
	public static Class<?>[] toTypes(Object... params) {
		Class<?>[] cl = new Class[params.length];

		for (int i = 0; i < params.length; i++) {
			cl[i] = (params[i] == null) ? Object.class : params[i].getClass();
		}

		return cl;
	}

	/** method */
	private Method method;

	/** invoker */
	private final Object invoker;

	/** methodName */
	private final String methodName;

	/** returns */
	private Object returns;

	/** parameters */
	private Object[] parameters;

	/** types */
	private Class<?>[] types;

	/**
	 * Creates a new MethodInvoker object.
	 *
	 * @param invoker
	 * @param methodName
	 * @param types
	 * @param parameters
	 *
	 * @throws InvocationException
	 */
	public MethodInvoker(Object invoker, String methodName, Class<?>... types) throws InvocationException {
		this.invoker = invoker;
		this.methodName = methodName;
		this.types = types;
	}

	/**
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof MethodInvoker)) {
			return false;
		}

		MethodInvoker castOther = (MethodInvoker) other;

		return new EqualsBuilder().append(this.invoker, castOther.invoker).append(this.methodName, castOther.methodName).append(this.types, castOther.types).isEquals();
	}

	/**
	 * Getter voor invoker
	 *
	 * @return Returns the invoker.
	 */
	public final Object getInvoker() {
		return this.invoker;
	}

	/**
	 * Getter voor method
	 *
	 * @return Returns the method.
	 *
	 * @throws InvocationException
	 */
	public final Method getMethod() throws InvocationException {
		if (this.method == null) {
			this.method = MethodInvoker.getMethod(this.invoker, this.methodName, this.types);
			this.types = this.method.getParameterTypes();
		}

		return this.method;
	}

	/**
	 * Getter voor methodName
	 *
	 * @return Returns the methodName.
	 */
	public final String getMethodName() {
		return this.methodName;
	}

	/**
	 * Getter voor parameters
	 *
	 * @return Returns the parameters.
	 */
	public final Object[] getParameters() {
		return this.parameters;
	}

	/**
	 * Getter voor returns
	 *
	 * @return Returns the returns.
	 */
	public final Object getReturns() {
		return this.returns;
	}

	/**
	 * Getter voor types
	 *
	 * @return Returns the types.
	 */
	public final Class<?>[] getTypes() {
		return this.types;
	}

	/**
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(1859497571, 1345127631).append(this.invoker).append(this.methodName).append(this.types).toHashCode();
	}

	/**
	 * invoke method with parameters
	 *
	 * @return
	 *
	 * @throws InvocationException
	 */
	public final Object invoke(Object... p) throws InvocationException {
		Method method2 = this.getMethod();

		if ((p == null) || ((p.length == 0) && (this.types.length != 0))) {
			throw new InvocationException("incorrect number or types of parameters");
		}

		if (p.length != this.types.length) {
			throw new InvocationException("incorrect number of parameters");
		}

		for (int i = 0; i < p.length; i++) {
			if (!((p[i] == null) || this.types[i].isAssignableFrom(p[i].getClass()))) {
				throw new InvocationException("incorrect types of parameters");
			}
		}

		this.parameters = p;
		this.returns = MethodInvoker.invoke0(method2, this.invoker, p);

		return this.returns;
	}

	@SuppressWarnings("unused")
	private final void setMethod(Method method) {
		//
	}

	@SuppressWarnings("unused")
	private final void setParameters(Object[] parameters) {
		//
	}

	@SuppressWarnings("unused")
	private final void setReturns(Object returns) {
		//
	}

	@SuppressWarnings("unused")
	private final void setTypes(Class<?>[] types) {
		//
	}

	/**
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString()).append("invoker", this.invoker).append("methodName", this.methodName)
				.append("types", this.types).append("method", this.method).append("parameters", this.parameters).append("returns", this.returns).toString();
	}
}
