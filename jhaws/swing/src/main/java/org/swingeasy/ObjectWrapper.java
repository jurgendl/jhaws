package org.swingeasy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
public final class ObjectWrapper {
    /**
     * ClassCache
     */
    private static final class ClassCache {
        /** type */
        private final Class type;

        /** map */
        private final Map<String, Field> fields = new HashMap<String, Field>();

        /** allFieldInititalized */
        private boolean allFieldInititalized = false;

        /**
         * Creates a new ClassCache object.
         * 
         * @param type
         */
        private ClassCache(Class type) {
            this.type = type;
        }
    }

    public static class FieldNotFoundException extends RuntimeException {
        
        private static final long serialVersionUID = 5863861684947177500L;

        /**
         * Creates a new FieldNotFoundException object.
         */
        public FieldNotFoundException() {
            super();
        }

        /**
         * Creates a new FieldNotFoundException object.
         * 
         * @param message
         */
        public FieldNotFoundException(String message) {
            super(message);
        }

        /**
         * Creates a new FieldNotFoundException object.
         * 
         * @param message
         * @param cause
         */
        public FieldNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Creates a new FieldNotFoundException object.
         * 
         * @param cause
         */
        public FieldNotFoundException(Throwable cause) {
            super(cause);
        }
    }

    /** {@link Object}.class */
    private static final Class<Object> OBJECT_CLASS = Object.class;

    /** cache */
    private static final transient Map<Class, ClassCache> cache = new HashMap<Class, ClassCache>();

    /**
     * get {@link Field} for name
     * 
     * @param cc
     * @param fieldName
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final Field cc_getField(ClassCache cc, String fieldName) throws FieldNotFoundException {
        Field field = cc.fields.get(fieldName);

        if (field == null) {
            try {
                field = cc.type.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                throw new FieldNotFoundException(ex);
            } catch (SecurityException ex) {
                throw new FieldNotFoundException(ex);
            }
        }

        return field;
    }

    /**
     * initAllFields
     * 
     * @param cc
     * 
     * @return
     */
    private static final Set<String> cc_getFieldNames(ClassCache cc) {
        return ObjectWrapper.cc_getFields(cc).keySet();
    }

    /**
     * getFields
     * 
     * @param cc
     * 
     * @return the fields
     */
    private static final Map<String, Field> cc_getFields(ClassCache cc) {
        if (!cc.allFieldInititalized) {
            for (Field field : cc.type.getDeclaredFields()) {
                cc.fields.put(field.getName(), field);
            }

            cc.allFieldInititalized = true;
        }

        return cc.fields;
    }

    /**
     * get value
     * 
     * @param cc
     * @param fieldName
     * @param wrapper
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final Object cc_getValue(ClassCache cc, String fieldName, ObjectWrapper wrapper) throws FieldNotFoundException {
        try {
            Field field = ObjectWrapper.cc_getField(cc, fieldName);
            boolean accessible = field.isAccessible();

            if (!accessible) {
                field.setAccessible(true);
            }

            Object value = field.get(wrapper.bean);

            if (!accessible) {
                field.setAccessible(false);
            }

            return value;
        } catch (RuntimeException ex) {
            throw new FieldNotFoundException(ex);
        } catch (IllegalAccessException ex) {
            throw new FieldNotFoundException(ex);
        }
    }

    /**
     * set value
     * 
     * @param cc
     * @param fieldName
     * @param wrapper
     * @param value
     * 
     * @throws FieldNotFoundException
     */
    private static final void cc_setValue(ClassCache cc, String fieldName, ObjectWrapper wrapper, Object value) throws FieldNotFoundException {
        try {
            Field field = ObjectWrapper.cc_getField(cc, fieldName);
            boolean accessible = field.isAccessible();

            if (!accessible) {
                field.setAccessible(true);
            }

            field.set(wrapper.bean, value);

            if (!accessible) {
                field.setAccessible(false);
            }
        } catch (RuntimeException ex) {
            throw new FieldNotFoundException(ex);
        } catch (IllegalAccessException ex) {
            throw new FieldNotFoundException(ex);
        }
    }

    /**
     * buildPath
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final String[] ow_buildPath(String... path) {
        if ((path == null) || (path.length == 0)) {
            throw new FieldNotFoundException();
        }

        ArrayList<String> rebuild = new ArrayList<String>();

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
     * 
     * @param type
     * 
     * @return
     */
    private static final ClassCache ow_fromCache(Class type) {
        ClassCache fromCache = ObjectWrapper.cache.get(type);

        if (fromCache == null) {
            fromCache = new ClassCache(type);
            ObjectWrapper.cache.put(type, fromCache);
        }

        return fromCache;
    }

    /**
     * get value
     * 
     * @param ow
     * @param field
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final Object ow_get(ObjectWrapper ow, String field) throws FieldNotFoundException {
        for (ClassCache cc : ow.localcache) {
            try {
                return ObjectWrapper.cc_getValue(cc, field, ow);
            } catch (FieldNotFoundException ex) {
                //
            }
        }

        throw new FieldNotFoundException(field);
    }

    /**
     * get value
     * 
     * @param ow
     * @param path
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final Object ow_get(ObjectWrapper ow, String[] path) throws FieldNotFoundException {
        ObjectWrapper current = ow;

        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            Object owGet = ObjectWrapper.ow_get(current, path[i]);

            if (owGet == null) {
                return null;
            }

            current = new ObjectWrapper(owGet);
        }

        return ObjectWrapper.ow_get(current, path[size_1]);
    }

    /**
     * get value
     * 
     * @param ow
     * @param field
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    private static final Field ow_getField(ObjectWrapper ow, String field) throws FieldNotFoundException {
        for (ClassCache cc : ow.localcache) {
            try {
                return ObjectWrapper.cc_getField(cc, field);
            } catch (FieldNotFoundException ex) {
                //
            }
        }

        throw new FieldNotFoundException(field);
    }

    /**
     * set value
     * 
     * @param ow
     * @param field
     * @param value
     * 
     * @throws FieldNotFoundException
     */
    private static final void ow_set(ObjectWrapper ow, String field, Object value) throws FieldNotFoundException {
        for (ClassCache cc : ow.localcache) {
            try {
                ObjectWrapper.cc_setValue(cc, field, ow, value);

                return;
            } catch (FieldNotFoundException ex) {
                //
            }
        }

        throw new FieldNotFoundException(field);
    }

    /**
     * set value
     * 
     * @param ow
     * @param path
     * @param value
     * 
     * @throws FieldNotFoundException
     */
    private static final void ow_set(ObjectWrapper ow, String[] path, Object value) throws FieldNotFoundException {
        ObjectWrapper current = ow;

        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            Object owGet = ObjectWrapper.ow_get(current, path[i]);

            if (owGet == null) {
                throw new FieldNotFoundException(path[i]);
            }

            current = new ObjectWrapper(owGet);
        }

        ObjectWrapper.ow_set(current, path[size_1], value);
    }

    /** bean */
    private final transient Class beanclass;

    /** localcache to root */
    private final transient List<ClassCache> localcache = new ArrayList<ClassCache>();

    /** bean */
    private final transient Object bean;

    /** bean */
    private final transient String beanclassname;

    /**
     * Creates a new ObjectWrapper object.
     * 
     * @param bean
     */
    public ObjectWrapper(Class<?> bean) {
        this.bean = bean;
        this.beanclass = bean;
        this.beanclassname = this.beanclass.getName();

        Class<?> current = this.beanclass;

        do {
            this.localcache.add(ObjectWrapper.ow_fromCache(current));
            current = current.getSuperclass();
        } while (!ObjectWrapper.OBJECT_CLASS.equals(current));
    }

    /**
     * Creates a new ObjectWrapper object.
     * 
     * @param bean
     */
    public ObjectWrapper(Object bean) {
        this.bean = bean;
        this.beanclass = bean.getClass();
        this.beanclassname = this.beanclass.getName();

        Class<?> current = this.beanclass;

        do {
            this.localcache.add(ObjectWrapper.ow_fromCache(current));
            current = current.getSuperclass();
        } while (!ObjectWrapper.OBJECT_CLASS.equals(current));
    }

    /**
     * get value
     * 
     * @param <T>
     * @param type
     * 
     * @return
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
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final Object get(String... path) throws FieldNotFoundException {
        return ObjectWrapper.ow_get(this, ObjectWrapper.ow_buildPath(path));
    }

    /**
     * get value
     * 
     * @param <T>
     * @param path
     * @param type
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final <T> T get(String path, Class<T> type) throws FieldNotFoundException {
        return this.get(type, path);
    }

    /**
     * getBean
     * 
     * @return the bean
     */
    public final Object getBean() {
        return this.bean;
    }

    /**
     * getBeanclass
     * 
     * @return the beanclass
     */
    public final Class getBeanclass() {
        return this.beanclass;
    }

    /**
     * getBeanclassname
     * 
     * @return the beanclassname
     */
    public final String getBeanclassname() {
        return this.beanclassname;
    }

    /**
     * get field
     * 
     * @param field
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final Field getField(String field) throws FieldNotFoundException {
        String[] path = ObjectWrapper.ow_buildPath(field);
        ObjectWrapper current = this;
        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            current = new ObjectWrapper(ObjectWrapper.ow_get(current, path[i]));
        }

        return ObjectWrapper.ow_getField(current, path[size_1]);
    }

    /**
     * getFieldNames
     * 
     * @return
     */
    public final Set<String> getFieldNames() {
        HashSet<String> list = new HashSet<String>();

        for (ClassCache element : this.localcache) {
            list.addAll(ObjectWrapper.cc_getFieldNames(element));
        }

        return list;
    }

    /**
     * get all fields
     * 
     * @return
     */
    public final Map<String, Field> getFields() {
        Map<String, Field> map = new HashMap<String, Field>();

        for (ClassCache element : this.localcache) {
            map.putAll(ObjectWrapper.cc_getFields(element));
        }

        return map;
    }

    /**
     * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan
     * {@link #getFields()}
     * 
     * @return
     */
    public final Map<String, Field> getFields(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
        Map<String, Field> map = new HashMap<String, Field>();
        List<Class<?>> exclusiveClasses = new ArrayList<Class<?>>();

        if (exclusiveClass != null) {
            Class<?> current = exclusiveClass;
            while (current != null) {
                exclusiveClasses.add(current);
                current = current.getSuperclass();
            }
        }

        for (Map.Entry<String, Field> field : this.getFields().entrySet()) {
            if (removeStatic && Modifier.isStatic(field.getValue().getModifiers())) {
                continue;
            }

            if (removeFinal && Modifier.isFinal(field.getValue().getModifiers())) {
                continue;
            }

            if ((exclusiveClass != null) && exclusiveClasses.contains(field.getValue().getDeclaringClass())) {
                continue;
            }

            map.put(field.getKey(), field.getValue());
        }

        return map;
    }

    /**
     * get type for field
     * 
     * @param field
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final Class<?> getType(String field) throws FieldNotFoundException {
        return this.getField(field).getType();
    }

    /**
     * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan
     * {@link #getFields()}
     * 
     * @return
     */
    public final Map<String, Class<?>> getTypes(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
        Map<String, Class<?>> types = new HashMap<String, Class<?>>();

        for (Map.Entry<String, Field> entry : this.getFields(removeStatic, removeFinal, exclusiveClass).entrySet()) {
            types.put(entry.getKey(), entry.getValue().getType());
        }

        return types;
    }

    /**
     * set value
     * 
     * @param value
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final ObjectWrapper set(Object value, String... path) throws FieldNotFoundException {
        ObjectWrapper.ow_set(this, ObjectWrapper.ow_buildPath(path), value);

        return this;
    }

    /**
     * set value
     * 
     * @param path
     * @param value
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final ObjectWrapper set(String path, Object value) throws FieldNotFoundException {
        return this.set(value, path);
    }

    /**
     * set each value on deep path
     * 
     * @param path
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     * @throws IllegalArgumentException
     */
    public final ObjectWrapper set(String path, Object... values) throws FieldNotFoundException {
        String[] fullpath = ObjectWrapper.ow_buildPath(path);

        if ((values == null) || (values.length == 0) || (fullpath.length != values.length)) {
            throw new IllegalArgumentException("path.length=values.length");
        }

        ObjectWrapper current = this;

        for (int i = 0; i < fullpath.length; i++) {
            Object value = values[i];
            current.set(fullpath[i], value);
            current = new ObjectWrapper(value);
        }

        return this;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ObjectWrapper [");

        if (this.bean != null) {
            builder.append("bean=");
            builder.append(this.bean);
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * set value null
     * 
     * @param path
     * 
     * @return
     * 
     * @throws FieldNotFoundException
     */
    public final ObjectWrapper unset(String path) throws FieldNotFoundException {
        return this.set(path, (Object) null);
    }
}
