package org.jhaws.common.lang;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "rawtypes" })
public class BeanWrapper {
	/**
	 * ClassCache
	 */
	private static final class ClassCache {
		/** type */
		private final Class type;

		/** map */
		private final Map<String, PropertyDescriptor> fields = new HashMap<>();

		/** allFieldInititalized */
		private boolean allFieldInititalized = false;

		/**
		 * Creates a new ClassCache object.
		 */
		private ClassCache(Class type) {
			this.type = type;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public final String toString() {
			return this.type.toString();
		}
	}

	/** {@link Object}.class */
	private static final Class<Object> OBJECT_CLASS = Object.class;

	/** cache */
	private static final transient Map<Class, ClassCache> cache = new HashMap<>();

	/**
	 * get {@link Field} for name
	 *
	 * @throws FieldNotFoundException
	 */
	private static final PropertyDescriptor cc_getField(ClassCache cc, String fieldName) throws FieldNotFoundException {
		PropertyDescriptor field = cc.fields.get(fieldName);

		if (field == null) {
			try {
				BeanInfo info = Introspector.getBeanInfo(cc.type, cc.type.getSuperclass());
				for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
					cc.fields.put(pd.getName(), pd);
				}
				field = cc.fields.get(fieldName);
			} catch (SecurityException ex) {
				throw new FieldNotFoundException(ex);
			} catch (IntrospectionException ex) {
				throw new FieldNotFoundException(ex);
			}
		}

		return field;
	}

	/**
	 * initAllFields
	 */
	private static final Set<String> cc_getFieldNames(ClassCache cc) {
		return BeanWrapper.cc_getFields(cc).keySet();
	}

	/**
	 * getFields
	 */
	private static final Map<String, PropertyDescriptor> cc_getFields(ClassCache cc) {
		if (!cc.allFieldInititalized) {
			BeanInfo beanInfo;
			try {
				beanInfo = Introspector.getBeanInfo(cc.type, cc.type.getSuperclass());
			} catch (IntrospectionException ex) {
				throw new RuntimeException(ex);
			}
			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
				cc.fields.put(pd.getName(), pd);
			}

			cc.allFieldInititalized = true;
		}

		return cc.fields;
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final Object cc_getValue(ClassCache cc, String fieldName, BeanWrapper wrapper) throws FieldNotFoundException {
		try {
			PropertyDescriptor field = BeanWrapper.cc_getField(cc, fieldName);
			Object value = field.getReadMethod().invoke(wrapper.bean);

			return value;
		} catch (RuntimeException ex) {
			throw new FieldNotFoundException(ex);
		} catch (IllegalAccessException ex) {
			throw new FieldNotFoundException(ex);
		} catch (InvocationTargetException ex) {
			throw new FieldNotFoundException(ex.getTargetException());
		}
	}

	/**
	 * set value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final void cc_setValue(ClassCache cc, String fieldName, BeanWrapper wrapper, Object value) throws FieldNotFoundException {
		try {
			PropertyDescriptor field = BeanWrapper.cc_getField(cc, fieldName);
			field.getWriteMethod().invoke(wrapper.bean, value);
		} catch (RuntimeException ex) {
			throw new FieldNotFoundException(ex);
		} catch (IllegalAccessException ex) {
			throw new FieldNotFoundException(ex);
		} catch (InvocationTargetException ex) {
			throw new FieldNotFoundException(ex.getTargetException());
		}
	}

	public final static void clear() {
		BeanWrapper.cache.clear();
	}

	public final static void clear(Class<?> bean) {
		do {
			BeanWrapper.cache.remove(bean);
			bean = bean.getSuperclass();
		} while (!BeanWrapper.OBJECT_CLASS.equals(bean));
	}

	public final static void clear(Object bean) {
		BeanWrapper.clear(bean.getClass());
	}

	/**
	 * buildPath
	 *
	 * @throws FieldNotFoundException
	 */
	private static final String[] ow_buildPath(String... path) {
		if ((path == null) || (path.length == 0)) {
			throw new FieldNotFoundException();
		}

		ArrayList<String> rebuild = new ArrayList<>();

		for (String arrayElement : path) {
			Enumeration enumer = new StringTokenizer(arrayElement, ".");

			while (enumer.hasMoreElements()) {
				rebuild.add((String) enumer.nextElement());
			}
		}

		return rebuild.toArray(new String[rebuild.size()]);
	}

	/**
	 * fromCache
	 */
	private static final ClassCache ow_fromCache(Class type) {
		ClassCache fromCache = BeanWrapper.cache.get(type);

		if (fromCache == null) {
			fromCache = new ClassCache(type);
			BeanWrapper.cache.put(type, fromCache);
		}

		return fromCache;
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final Object ow_get(BeanWrapper ow, String field) throws FieldNotFoundException {
		FieldNotFoundException first = null;
		for (ClassCache cc : ow.localcache) {
			try {
				return BeanWrapper.cc_getValue(cc, field, ow);
			} catch (FieldNotFoundException ex) {
				first = ex;
			}
		}

		if (first == null) {
			first = new FieldNotFoundException(field);
		}
		throw first;
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final Object ow_get(BeanWrapper ow, String[] path) throws FieldNotFoundException {
		BeanWrapper current = ow;

		int size_1 = path.length - 1;

		for (int i = 0; i < size_1; i++) {
			Object owGet = BeanWrapper.ow_get(current, path[i]);

			if (owGet == null) {
				return null;
			}

			current = new BeanWrapper(owGet);
		}

		return BeanWrapper.ow_get(current, path[size_1]);
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final PropertyDescriptor ow_getField(BeanWrapper ow, String field) throws FieldNotFoundException {
		FieldNotFoundException first = null;
		for (ClassCache cc : ow.localcache) {
			try {
				return BeanWrapper.cc_getField(cc, field);
			} catch (FieldNotFoundException ex) {
				first = ex;
			}
		}

		if (first == null) {
			first = new FieldNotFoundException(field);
		}
		throw first;
	}

	/**
	 * set value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final void ow_set(BeanWrapper ow, String field, Object value) throws FieldNotFoundException {
		FieldNotFoundException first = null;
		for (ClassCache cc : ow.localcache) {
			try {
				BeanWrapper.cc_setValue(cc, field, ow, value);

				return;
			} catch (FieldNotFoundException ex) {
				first = ex;
			}
		}

		if (first == null) {
			first = new FieldNotFoundException(field);
		}
		throw first;
	}

	/**
	 * set value
	 *
	 * @throws FieldNotFoundException
	 */
	private static final void ow_set(BeanWrapper ow, String[] path, Object value) throws FieldNotFoundException {
		BeanWrapper current = ow;

		int size_1 = path.length - 1;

		for (int i = 0; i < size_1; i++) {
			Object owGet = BeanWrapper.ow_get(current, path[i]);

			if (owGet == null) {
				throw new FieldNotFoundException(path[i]);
			}

			current = new BeanWrapper(owGet);
		}

		BeanWrapper.ow_set(current, path[size_1], value);
	}

	/** bean */
	private final transient Class beanclass;

	/** localcache to root */
	private final transient List<ClassCache> localcache = new ArrayList<>();

	/** bean */
	private final transient Object bean;

	/** bean */
	private final transient String beanclassname;

	/**
	 * Creates a new BeanWrapper object.
	 */
	public BeanWrapper(Class<?> bean) {
		this.bean = bean;
		this.beanclass = bean;
		this.beanclassname = this.beanclass.getName();

		Class<?> current = this.beanclass;

		do {
			this.localcache.add(BeanWrapper.ow_fromCache(current));
			current = current.getSuperclass();
		} while (!BeanWrapper.OBJECT_CLASS.equals(current));
	}

	/**
	 * Creates a new BeanWrapper object.
	 */
	public BeanWrapper(Object bean) {
		this.bean = bean;
		this.beanclass = bean.getClass();
		this.beanclassname = this.beanclass.getName();

		Class<?> current = this.beanclass;

		do {
			this.localcache.add(BeanWrapper.ow_fromCache(current));
			current = current.getSuperclass();
		} while (!BeanWrapper.OBJECT_CLASS.equals(current));
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	public final <T> T get(Class<T> type, String... path) throws FieldNotFoundException {
		Object obj = this.get(path);
		try {
			return type.cast(obj);
		} catch (java.lang.ClassCastException ex) {
			throw new FieldNotFoundException(type.getName() + "<>" + obj.getClass().getName());
		}
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	public final Object get(String... path) throws FieldNotFoundException {
		return BeanWrapper.ow_get(this, BeanWrapper.ow_buildPath(path));
	}

	/**
	 * get value
	 *
	 * @throws FieldNotFoundException
	 */
	public final <T> T get(String path, Class<T> type) throws FieldNotFoundException {
		return this.get(type, path);
	}

	/**
	 * getBean
	 */
	public final Object getBean() {
		return this.bean;
	}

	/**
	 * getBeanclass
	 */
	public final Class getBeanclass() {
		return this.beanclass;
	}

	/**
	 * getBeanclassname
	 */
	public final String getBeanclassname() {
		return this.beanclassname;
	}

	/**
	 * get field
	 */
	public final PropertyDescriptor getField(String field) throws FieldNotFoundException {
		String[] path = BeanWrapper.ow_buildPath(field);
		BeanWrapper current = this;
		int size_1 = path.length - 1;

		for (int i = 0; i < size_1; i++) {
			current = new BeanWrapper(BeanWrapper.ow_get(current, path[i]));
		}

		return BeanWrapper.ow_getField(current, path[size_1]);
	}

	/**
	 * getFieldNames
	 */
	public final Set<String> getFieldNames() {
		HashSet<String> list = new HashSet<>();

		for (ClassCache element : this.localcache) {
			list.addAll(BeanWrapper.cc_getFieldNames(element));
		}

		return list;
	}

	/**
	 * get all fields
	 */
	public final Map<String, PropertyDescriptor> getFields() {
		Map<String, PropertyDescriptor> map = new HashMap<>();

		for (ClassCache element : this.localcache) {
			map.putAll(BeanWrapper.cc_getFields(element));
		}

		return map;
	}

	/**
	 * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan {@link #getFields()}
	 */
	public final Map<String, PropertyDescriptor> getFields(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
		Map<String, PropertyDescriptor> map = new HashMap<>();
		List<Class<?>> exclusiveClasses = new ArrayList<>();

		if (exclusiveClass != null) {
			Class<?> current = exclusiveClass;
			while (current != null) {
				exclusiveClasses.add(current);
				current = current.getSuperclass();
			}
		}

		for (Map.Entry<String, PropertyDescriptor> field : this.getFields().entrySet()) {
			map.put(field.getKey(), field.getValue());
		}

		return map;
	}

	/**
	 * get type for field
	 */
	public final Class<?> getType(String field) throws FieldNotFoundException {
		return this.getField(field).getPropertyType();
	}

	/**
	 * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan {@link #getFields()}
	 */
	public final Map<String, Class<?>> getTypes(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
		Map<String, Class<?>> types = new HashMap<>();

		for (Map.Entry<String, PropertyDescriptor> entry : this.getFields(removeStatic, removeFinal, exclusiveClass).entrySet()) {
			types.put(entry.getKey(), entry.getValue().getPropertyType());
		}

		return types;
	}

	/**
	 * set value
	 */
	public final BeanWrapper set(Object value, String... path) throws FieldNotFoundException {
		BeanWrapper.ow_set(this, BeanWrapper.ow_buildPath(path), value);

		return this;
	}

	/**
	 * set value
	 */
	public final BeanWrapper set(String path, Object value) throws FieldNotFoundException {
		return this.set(value, path);
	}

	/**
	 * set each value on deep path
	 */
	public final BeanWrapper set(String path, Object... values) throws FieldNotFoundException {
		String[] fullpath = BeanWrapper.ow_buildPath(path);

		if ((values == null) || (values.length == 0) || (fullpath.length != values.length)) {
			throw new IllegalArgumentException("path.length=values.length");
		}

		BeanWrapper current = this;

		for (int i = 0; i < fullpath.length; i++) {
			Object value = values[i];
			current.set(fullpath[i], value);
			current = new BeanWrapper(value);
		}

		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BeanWrapper [");

		if (this.bean != null) {
			builder.append("bean=");
			builder.append(this.bean);
		}

		builder.append("]");

		return builder.toString();
	}

	/**
	 * set value null
	 */
	public final BeanWrapper unset(String path) throws FieldNotFoundException {
		return this.set(path, (Object) null);
	}
}
