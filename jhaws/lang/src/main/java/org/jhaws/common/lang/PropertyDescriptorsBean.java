package org.jhaws.common.lang;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.unmodifiableMap;
import static org.jhaws.common.lang.Collections8.stream;
import static org.jhaws.common.lang.Collections8.streamMaps;
import static org.jhaws.common.lang.Collections8.notNull;
import static org.jhaws.common.lang.Collections8.collectMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class PropertyDescriptorsBean {
    private static final String SPLIT = "_";

    private final Map<String, PropertyDescriptor> propertyDescriptors;

    private final Map<String, Method> get = new HashMap<>();

    private final Map<String, Method> set = new HashMap<>();

    private final Map<String, Method> is = new HashMap<>();

    public PropertyDescriptorsBean(Class<?> clazz) {
        Predicate<Method> mf = ((Predicate<Method>) m -> isPublic(m.getModifiers())).and(m -> !isStatic(m.getModifiers()))
                .and(m -> !isAbstract(m.getModifiers()));
        stream(new ParentClassIterator(clazz)).forEach(current -> {
            Method[] methods = current.getDeclaredMethods();
            get.putAll(stream(true, methods).filter(mf)
                    .filter(m -> m.getName().matches("^get.+"))
                    .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 0)
                    .collect(collectMap(this::getKey)));
            is.putAll(stream(true, methods).filter(mf)
                    .filter(m -> m.getName().matches("^is.+"))
                    .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 0)
                    .collect(collectMap(this::isKey)));
            set.putAll(stream(true, methods).filter(mf)
                    .filter(m -> m.getName().matches("^set.+"))
                    .filter(m -> Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 1)
                    .collect(collectMap(this::setKey)));
        });
        this.propertyDescriptors = unmodifiableMap(
                streamMaps(get, is).map(this::create).filter(notNull()).collect(collectMap(PropertyDescriptor::getName)));
    }

    private String getKey(Method m) {
        return m.getName().substring(3) + SPLIT + m.getReturnType().getName();
    }

    private String isKey(Method m) {
        return m.getName().substring(2) + SPLIT + m.getReturnType().getName();
    }

    private String setKey(Method m) {
        return m.getName().substring(3) + SPLIT + m.getParameterTypes()[0].getName();
    }

    private PropertyDescriptor create(Entry<String, Method> e) {
        try {
            return new PropertyDescriptor(getPropertyName(e.getKey()), e.getValue(), set.get(e.getKey()));
        } catch (Exception ignore) {
            return null;
        }
    }

    private String getPropertyName(String key) {
        String[] p = key.split(SPLIT);
        return Character.toLowerCase(p[0].charAt(0)) + p[0].substring(1);
    }

    public Map<String, PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
